/* 
 * Copyright (C) 2018 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.ext.streaming.builder.stream;

import com.fluxtion.api.annotations.AfterEvent;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.api.partition.LambdaReflection.SerializableConsumer;
import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.api.partition.LambdaReflection.SerializableSupplier;
import com.fluxtion.builder.generation.GenerationContext;
import static com.fluxtion.builder.generation.GenerationContext.SINGLETON;
import com.fluxtion.ext.streaming.api.Constant;
import com.fluxtion.ext.streaming.api.FilterWrapper;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.Test;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.AbstractFilterWrapper;
import com.fluxtion.ext.streaming.api.stream.Argument;
import static com.fluxtion.ext.streaming.api.stream.Argument.arg;
import com.fluxtion.ext.streaming.builder.util.FunctionGeneratorHelper;
import com.fluxtion.ext.streaming.builder.util.FunctionInfo;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.filter;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.filterSubjectClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.functionClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.imports;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.input;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.isPrimitiveNumeric;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.newFunction;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.outputClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceMappingList;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.stateful;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.targetClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.targetMethod;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.wrappedSubject;
import com.fluxtion.ext.streaming.builder.util.ImportMap;
import com.fluxtion.ext.streaming.builder.util.SourceInfo;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.velocity.VelocityContext;

/**
 * Applies filtering logic to a node in the execution graph. The filter invokes
 * a predicate that tests a node for a valid match. The outcome of the match has
 * the following effect:
 * <ul>
 * <li>Successful match allows the event wave to propagate.
 * <li>Failed match the event wave stops at the node under test.
 * </ul>
 *
 * <p>
 * Filtering has the following characteristics:
 * <ul>
 * <li>Filter functions are either instance or static methods.
 * <li>An instance filter is a node in the SEP and can receive inputs from any
 * other nodes.
 * <li>An instance filter is stateful, the same filter instance will be used
 * across multiple event processing cycles.
 * <li>An instance filter can attach to any SEP life-cycle method such as
 * {@link AfterEvent}
 * <li>A filter inspection target can be a method reference or a node in the
 * execution graph.
 * <li>A ,target method reference can return either a primitive or reference
 * types.
 * <li>Fluxtion will cast all supplied values to the receiving type.
 * <li><b>Lambdas cannot be used as filter predicates</b> use method references.
 * </ul>
 *
 * Below is an example creating a filter on a primitive double property. The
 * filter is an int parameter, Fluxtion manages all casts.
 * <p>
 * <pre><code>
 *{@code @SepBuilder(name="FilterTest", packageName="com.fluxtion.testfilter")}
 * public void buildFilter(SEPConfig cfg) { 
 *     MyDataHandler dh1 = cfg.addNode(new MyDataHandler("dh1")); 
 *     filter(lt(34), dh1::getDoubleVal).build();
 *     filter(positive(), dh1::getIntVal).build(); 
 * } 
 * ... 
 * 
 * public class NumericValidator {
 *
 * //method reference wraps instance test 
 * public static SerializableFunction lt(int test) { 
 *     return (SerializableFunction<Integer, Boolean>)new NumericValidator(test)::lessThan; 
 * } 
 * 
 * //method reference wraps static test
 * public static SerializableFunction positive() { 
 *     return (SerializableFunction<Integer, Boolean>) NumericValidator::positiveInt; 
 * }
 * 
 * public int limit;
 *
 * public NumericValidator(int limit) { this.limit = limit; }
 *
 * public static boolean positiveInt(int d) { return d > 0; }
 *
 * public boolean greaterThan(int d) { return d > limit; }
 *
 * public boolean lessThan(int d) { return d < limit; } } 
 * </code></pre>
 *
 *
 * @author V12 Technology Ltd.
 * @param <T> The test function applied to the filter subject
 * @param <F> The filter subject under test
 */
public class StreamFunctionCompiler<T, F> {

    private static final String TEMPLATE = "template/FilterTemplate.vsl";
    private static final String MAPPER_TEMPLATE = "template/MapperTemplate.vsl";
    private static final String PUSH_TEMPLATE = "template/PushTemplate.vsl";
    private static final String MAPPER_PRIMITIVE_TEMPLATE = "template/MapperPrimitiveTemplate.vsl";
    private static final String MAPPER_BOOLEAN_TEMPLATE = "template/MapperBooleanTemplate.vsl";
    private static final String MAPPER_TEST_TEMPLATE = "template/TestTemplate.vsl";
    private static final String CONSUMER_TEMPLATE = "template/ConsumerTemplate.vsl";
    private static final String MAPFIELD_TEMPLATE = "template/MapFieldTemplate.vsl";
    private FunctionClassKey key;
    private String currentTemplate = TEMPLATE;
    private String genClassSuffix = "Filter_";

    private final HashMap<Object, SourceInfo> inst2SourceInfo = new HashMap<>();
    private FunctionInfo functionInfo;
    private final List<FunctionInfo> sourceFunctions;
    private final Class<T> testFunctionClass;
    //only used for filtering functionality
    private F filterSubject;
    private Wrapper filterSubjectWrapper;
    //for sources id's
    private int sourceCount;
    private boolean multiArgFunc = false;
    private boolean alwaysReset = false;
    private static boolean buildTest = false;
    //To be used for rationalising imports
    private final ImportMap importMap = ImportMap.newMap();
    private T testFunction;

    private StreamFunctionCompiler(Class<T> testFunctionClass) {
        this.testFunctionClass = testFunctionClass;
        Method[] methods = testFunctionClass.getDeclaredMethods();
        functionInfo = new FunctionInfo(methods[0], importMap);
        sourceFunctions = new ArrayList<>();
        standardImports();
    }

    private StreamFunctionCompiler(T testInstance) {
        this.testFunctionClass = (Class<T>) testInstance.getClass();
        this.testFunction = GenerationContext.SINGLETON.addOrUseExistingNode(testInstance);
        sourceFunctions = new ArrayList<>();
        standardImports();
    }

    /**
     * static filter generation method
     *
     * @param <T>
     * @param <R>
     * @param <S>
     * @param <F>
     * @param filterMethod
     * @param source
     * @param accessor
     * @param cast
     * @return
     */
    public static <T, R extends Boolean, S, F> StreamFunctionCompiler filter(Method filterMethod, S source, Method accessor, boolean cast) {
        StreamFunctionCompiler filterBuilder = new StreamFunctionCompiler(filterMethod.getDeclaringClass());
        String sourceString = (accessor == null ? source.getClass().getSimpleName() : accessor.getName());
        filterBuilder.functionInfo = new FunctionInfo(filterMethod, filterBuilder.importMap);
        filterBuilder.filterSubject = source;
        SourceInfo sourceInfo = filterBuilder.addSource(source);
        filterBuilder.key = new FunctionClassKey(null, filterMethod, source, accessor, cast, "filter");
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = accessor == null ? filterBuilder.filterSubjectWrapper.eventClass().getSimpleName() : accessor.getName();
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, cast);
            } else {
                filterBuilder.functionInfo.appendParamSource(accessor, sourceInfo, (Wrapper) source, cast);
            }
        } else {
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamValue("filterSubject", cast, true);
            } else {
                filterBuilder.functionInfo.appendParamSource(accessor, sourceInfo, cast);
            }
        }
        filterBuilder.genClassSuffix = "Filter_" + sourceString + "_By_" + filterMethod.getName();
        return filterBuilder;
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler mapSet(F mapper, Method mappingMethod, Argument... args) {
        //call map then add sources
        //update the key
        //filterBuilder.key = new FunctionClassKey(mapper, mappingMethod, source, accessor, cast, "mapper");
        Argument arg = args[0];
        StreamFunctionCompiler builder = map(mapper, mappingMethod, arg.getSource(), arg.getAccessor(), arg.isCast());
        builder.alwaysReset = true;
        for (int i = 1; i < args.length; i++) {
            arg = args[i];
            builder.key.multiArg = false;
            builder.key.argsList.add(arg);
            SourceInfo srcInfo = builder.addSource(arg.getSource());
            FunctionInfo srcFuncInfo = new FunctionInfo(mappingMethod, builder.importMap);
            srcFuncInfo.sourceInfo = srcInfo;
            Object source = arg.getSource();
            Method accessor = arg.getAccessor();
            boolean cast = arg.isCast();
            String srcId = srcInfo.getId();
            if (source instanceof Wrapper) {
                if (accessor == null) {
                    srcFuncInfo.appendParamLocal(srcId, (Wrapper) source, cast);
                } else {
                    srcFuncInfo.appendParamLocal(accessor, srcId, (Wrapper) source, cast);
                }
            } else {
                if (accessor == null) {
                    srcFuncInfo.appendParamValue(srcId, cast, true);
                } else {
                    srcFuncInfo.appendParamLocal(accessor, srcId, cast);
                }
            }
            builder.sourceFunctions.add(srcFuncInfo);
        }
        return builder;
    }

    /**
     * Build a {@link Test} from n-ary arguments
     * 
     * @param <T>
     * @param <R>
     * @param <S>
     * @param <F>
     * @param mapper
     * @param mappingMethod
     * @param args
     * @return 
     */
    public static <T, R extends Boolean, S, F> StreamFunctionCompiler test(F mapper, Method mappingMethod, Argument... args) {
        try {
            buildTest = true;
            return map(mapper, mappingMethod, args);
        } finally {
            buildTest = false;
        }
    } 
    
    /**
     * Build a mapping function from n-ary arguments
     *
     * @param <T>
     * @param <R>
     * @param <S>
     * @param <F>
     * @param mapper
     * @param mappingMethod
     * @param args
     * @return
     */
    public static <T, R extends Boolean, S, F> StreamFunctionCompiler map(F mapper, Method mappingMethod, Argument... args) {
        Argument arg = args[0];
        StreamFunctionCompiler builder = map(mapper, mappingMethod, arg.getSource(), arg.getAccessor(), arg.isCast());
        for (int i = 1; i < args.length; i++) {
            arg = args[i];
            builder.key.multiArg = true;
            builder.key.argsList.add(arg);
            Object source = arg.getSource();
            source = GenerationContext.SINGLETON.addOrUseExistingNode(source);
            SourceInfo srcInfo = builder.addSource(source);
            Method accessor = arg.getAccessor();
            boolean cast = arg.isCast();
            String srcId = srcInfo.getId();
            if (source instanceof Wrapper) {
                if (accessor == null) {
                    builder.functionInfo.appendParamLocal(srcId, (Wrapper) source, cast);
                } else {
                    builder.functionInfo.appendParamLocal(accessor, srcId, (Wrapper) source, cast);
                }
            } else {
                if (accessor == null) {
                    builder.functionInfo.appendParamValue(srcId, cast, true);
                } else {
                    builder.functionInfo.appendParamLocal(accessor, srcId, cast);
                }
            }
            builder.multiArgFunc = true;
        }
        return builder;
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler map(F mapper, Method mappingMethod, S source, Method accessor, boolean cast) {
        StreamFunctionCompiler filterBuilder;
        boolean staticMeth = Modifier.isStatic(mappingMethod.getModifiers());
        GenerationContext.SINGLETON.addOrUseExistingNode(source);
        if (mapper == null && staticMeth) {
            filterBuilder = new StreamFunctionCompiler(mappingMethod.getDeclaringClass());
        } else if (mapper == null && !staticMeth) {
            try {
                mapper = (F) mappingMethod.getDeclaringClass().newInstance();
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException("unable to create mapper instance must have public default consturctor", ex);
            }
            filterBuilder = new StreamFunctionCompiler(mapper);
        } else {
            filterBuilder = new StreamFunctionCompiler(mapper);
        }
        String sourceString = (accessor == null ? source.getClass().getSimpleName() : accessor.getName());
        filterBuilder.currentTemplate = MAPPER_TEMPLATE;
        filterBuilder.functionInfo = new FunctionInfo(mappingMethod, filterBuilder.importMap);
        filterBuilder.key = new FunctionClassKey(mapper, mappingMethod, source, accessor, cast, "mapper");
        final Class<?> returnType = mappingMethod.getReturnType();
        if ((returnType.isPrimitive() || Number.class.isAssignableFrom(returnType)) && mappingMethod.getReturnType() != boolean.class) {
            filterBuilder.currentTemplate = MAPPER_PRIMITIVE_TEMPLATE;
            filterBuilder.importMap.addImport(Number.class);
            filterBuilder.importMap.addImport(MutableNumber.class);
        } else if (returnType == boolean.class) {
            if(buildTest){
                filterBuilder.currentTemplate = MAPPER_TEST_TEMPLATE;
            }else{
                filterBuilder.currentTemplate = MAPPER_BOOLEAN_TEMPLATE;
            }
            filterBuilder.importMap.addImport(Boolean.class);
        }
        filterBuilder.filterSubject = source;
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = accessor == null ? filterBuilder.filterSubjectWrapper.eventClass().getSimpleName() : accessor.getName();
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, cast);
            } else {
                filterBuilder.functionInfo.appendParamLocal(accessor, "filterSubject", (Wrapper) source, cast);
            }
        } else {
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamValue("filterSubject", cast, true);
            } else {
                filterBuilder.functionInfo.appendParamLocal(accessor, "filterSubject", cast);
            }
        }
            if(buildTest){
                filterBuilder.genClassSuffix = "Test_" + sourceString + "_With_" + mappingMethod.getName();
            }else{
                filterBuilder.genClassSuffix = "Map_" + sourceString + "_With_" + mappingMethod.getName();
            }
        return filterBuilder;
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler push(F mapper, Method mappingMethod, S source, Method accessor, boolean cast) {
        StreamFunctionCompiler filterBuilder;
        boolean staticMeth = Modifier.isStatic(mappingMethod.getModifiers());
        if (mapper == null && staticMeth) {
            filterBuilder = new StreamFunctionCompiler(mappingMethod.getDeclaringClass());
        } else if (mapper == null && !staticMeth) {
            try {
                mapper = (F) mappingMethod.getDeclaringClass().newInstance();
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException("unable to create mapper instance must have public default consturctor", ex);
            }
            filterBuilder = new StreamFunctionCompiler(mapper);
        } else {
            filterBuilder = new StreamFunctionCompiler(mapper);
        }
        String sourceString = (accessor == null ? source.getClass().getSimpleName() : accessor.getName());
        filterBuilder.currentTemplate = PUSH_TEMPLATE;
        filterBuilder.importMap.addImport(PushReference.class);
        filterBuilder.functionInfo = new FunctionInfo(mappingMethod, filterBuilder.importMap);
        filterBuilder.key = new FunctionClassKey(mapper, mappingMethod, source, accessor, cast, "mapper");
        filterBuilder.filterSubject = source;
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = accessor == null ? filterBuilder.filterSubjectWrapper.eventClass().getSimpleName() : accessor.getName();
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, cast);
            } else {
                filterBuilder.functionInfo.appendParamLocal(accessor, "filterSubject", (Wrapper) source, cast);
            }
        } else {
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamValue("filterSubject", cast, true);
            } else {
                filterBuilder.functionInfo.appendParamLocal(accessor, "filterSubject", cast);
            }
        }
        filterBuilder.genClassSuffix = "Push_" + sourceString + "_To_" + mappingMethod.getName();
        return filterBuilder;
    }

    
    public static<S> Wrapper<S> get(SerializableSupplier<S> source){
        Argument<S> arg = arg(source);
        return get(arg.getAccessor(), arg.source);
    }
    
    public static<S> Wrapper get(Method accessor, S source){
        StreamFunctionCompiler filterBuilder = new StreamFunctionCompiler(source);
        filterBuilder.testFunction = null;
        String sourceString = source.getClass().getSimpleName();
        filterBuilder.currentTemplate = MAPFIELD_TEMPLATE;
        filterBuilder.functionInfo = new FunctionInfo(accessor, filterBuilder.importMap);
        filterBuilder.key = new FunctionClassKey(null, null, source, accessor, false, "mapField");
        filterBuilder.filterSubject = source;
        final Class<?> returnType = accessor.getReturnType();
        if ((returnType.isPrimitive() || Number.class.isAssignableFrom(returnType)) && accessor.getReturnType() != boolean.class) {
            FunctionInfo funcInfo = filterBuilder.functionInfo;
            funcInfo.returnTypeClass = MutableNumber.class;
            funcInfo.returnType = filterBuilder.importMap.addImport(Number.class);
            filterBuilder.importMap.addImport(MutableNumber.class);
        } 
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = filterBuilder.filterSubjectWrapper.eventClass().getSimpleName();
            filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, false);
        } else {
            filterBuilder.functionInfo.appendParamValue("filterSubject", false, false);
        }
        filterBuilder.genClassSuffix = "GetField_" + sourceString + "_" + accessor.getReturnType().getSimpleName();    
        return filterBuilder.build();
        
    }
    
    public static <S, C> StreamFunctionCompiler consume(C consumer, Method mappingMethod, S source) {
        if (consumer == System.out) {
            consumer = null;
            mappingMethod = ((SerializableConsumer) System.out::println).method();
        }
        StreamFunctionCompiler filterBuilder;
        if (consumer == null) {
            filterBuilder = new StreamFunctionCompiler(mappingMethod.getDeclaringClass());
        } else {
            filterBuilder = new StreamFunctionCompiler(consumer);
        }
        String sourceString = source.getClass().getSimpleName();
        filterBuilder.currentTemplate = CONSUMER_TEMPLATE;
        filterBuilder.functionInfo = new FunctionInfo(mappingMethod, filterBuilder.importMap);
        filterBuilder.functionInfo.returnTypeClass = source.getClass();
        filterBuilder.functionInfo.returnType = source.getClass().getName();
        filterBuilder.key = new FunctionClassKey(consumer, mappingMethod, source, null, false, "consumer");
        filterBuilder.filterSubject = source;
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = filterBuilder.filterSubjectWrapper.eventClass().getSimpleName();
            filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, false);
        } else {
            filterBuilder.functionInfo.appendParamValue("filterSubject", false, false);
        }
        filterBuilder.genClassSuffix = "Consume_" + sourceString;
        return filterBuilder;
    }

    /**
     * instance filter generation method
     *
     * @param <T>
     * @param <R>
     * @param <S>
     * @param <F>
     * @param filter
     * @param filterMethod
     * @param source
     * @param accessor
     * @param cast
     * @return
     */
    //TODO update this method to handle a Number 
    public static <T, R extends Boolean, S, F> StreamFunctionCompiler filter(F filter, Method filterMethod, S source, Method accessor, boolean cast) {
        GenerationContext.SINGLETON.addOrUseExistingNode(filter);
        StreamFunctionCompiler filterBuilder = new StreamFunctionCompiler(filter);
        String sourceString = accessor == null ? source.getClass().getSimpleName() : accessor.getName();
        filterBuilder.functionInfo = new FunctionInfo(filterMethod, filterBuilder.importMap);
        filterBuilder.filterSubject = source;
        SourceInfo sourceInfo = filterBuilder.addSource(source);
        filterBuilder.key = new FunctionClassKey(filter, filterMethod, source, accessor, cast, "filter");
        if (source instanceof Wrapper) {
            filterBuilder.filterSubjectWrapper = (Wrapper) source;
            sourceString = accessor == null ? filterBuilder.filterSubjectWrapper.eventClass().getSimpleName() : accessor.getName();
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamLocal("filterSubject", (Wrapper) source, cast);
            } else {
                filterBuilder.functionInfo.appendParamSource(accessor, sourceInfo, (Wrapper) source, cast);
            }
        } else {
            if (accessor == null) {
                filterBuilder.functionInfo.appendParamValue("filterSubject", cast, true);
            } else {
                filterBuilder.functionInfo.appendParamSource(accessor, sourceInfo, cast);
            }
        }
        filterBuilder.genClassSuffix = "Filter_" + sourceString + "_By_" + filterMethod.getName();
        return filterBuilder;
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler filter(F filter, Method filterMethod, S source, Method accessor) {
        return filter(filter, filterMethod, source, accessor, true);
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler filter(F filter, Method filterMethod, S source) {
        return filter(filter, filterMethod, source, null, true);
    }

    public static <T, R extends Boolean, S, F> StreamFunctionCompiler filter(Method filterMethod, S source) {
        return filter(filterMethod, source, null, true);
    }

    public static <T, R extends Boolean, S> StreamFunctionCompiler filter(SerializableFunction<T, R> filter, S source, Method accessor) {
        if (Modifier.isStatic(filter.method(SINGLETON.getClassLoader()).getModifiers())) {
            return filter(filter.method(SINGLETON.getClassLoader()), source, accessor, true);
        }
        return filter(filter.captured()[0], filter.method(SINGLETON.getClassLoader()), source, accessor);
    }

    public static <T, R extends Boolean, S> StreamFunctionCompiler filter(SerializableFunction<T, R> filter, S source) {
        if (Modifier.isStatic(filter.method(SINGLETON.getClassLoader()).getModifiers())) {
            return filter(filter.method(SINGLETON.getClassLoader()), source, null, true);
        }
        return filter(filter.captured()[0], filter.method(SINGLETON.getClassLoader()), source, null);
    }

    public static <T, R extends Boolean> StreamFunctionCompiler filter(SerializableFunction<T, R> filter, SerializableSupplier<T> supplier) {
        if (Modifier.isStatic(filter.method(SINGLETON.getClassLoader()).getModifiers())) {
            return filter(filter.method(SINGLETON.getClassLoader()), supplier.captured()[0], supplier.method(SINGLETON.getClassLoader()), true);
        }
        return filter(filter.captured()[0], filter.method(SINGLETON.getClassLoader()), supplier.captured()[0], supplier.method(SINGLETON.getClassLoader()));
    }

    public Wrapper<F> build() {
        try {
            VelocityContext ctx = new VelocityContext();
            String genClassName = genClassSuffix + GenerationContext.SINGLETON.nextId(genClassSuffix);
            ctx.put(functionClass.name(), genClassName);
            ctx.put(outputClass.name(), functionInfo.returnType);
            ctx.put(targetClass.name(), functionInfo.calculateClass);
            ctx.put(targetMethod.name(), functionInfo.calculateMethod);
            ctx.put(isPrimitiveNumeric.name(), functionInfo.isPrimitiveNumber());
            ctx.put(input.name(), functionInfo.paramString);
            ctx.put(filter.name(), true);
            ctx.put("multiArgFunc", multiArgFunc);
            ctx.put("alwaysReset", alwaysReset);
            ctx.put(stateful.name(), isStateful(functionInfo.getFunctionMethod()));
            if (filterSubjectWrapper != null) {
                ctx.put(wrappedSubject.name(), true);
                ctx.put(filterSubjectClass.name(), importMap.addImport(filterSubjectWrapper.eventClass()));//.getSimpleName());
                importMap.addImport(filterSubjectWrapper.eventClass());
                ctx.put(sourceClass.name(), importMap.addImport(filterSubjectWrapper.getClass()));//.getSimpleName());
                ctx.put("sourceConstant", Constant.class.isAssignableFrom(filterSubjectWrapper.eventClass()));
                ctx.put("validOnStart", true);
            } else {
                ctx.put(filterSubjectClass.name(), importMap.addImport(filterSubject.getClass()));//.getSimpleName());
                ctx.put(sourceClass.name(), importMap.addImport(filterSubject.getClass()));//.getSimpleName());
                importMap.addImport(filterSubject.getClass());
                ctx.put("sourceConstant", Constant.class.isAssignableFrom(filterSubject.getClass()));
                ctx.put("validOnStart", Constant.class.isAssignableFrom(filterSubject.getClass()));
            }
            ctx.put(sourceMappingList.name(), new ArrayList(inst2SourceInfo.values()));
            ctx.put("sourceFunctions", sourceFunctions);
            ctx.put(imports.name(), importMap.asString());
            ctx.put(newFunction.name(), testFunction == null);
            Class<Wrapper<F>> aggClass = compileIfAbsent(ctx);
            Wrapper<F> result = aggClass.newInstance();
            //set function instance
            if (testFunction != null) {
                aggClass.getField("f").set(result, testFunction);
            }
            //set sources via reflection
            Set<Map.Entry<Object, SourceInfo>> entrySet = inst2SourceInfo.entrySet();
            for (Map.Entry<Object, SourceInfo> entry : entrySet) {
                Object source = entry.getKey();
                String fieldName = entry.getValue().id;
                aggClass.getField(fieldName).set(result, source);
            }
            if (filterSubjectWrapper != null) {
                aggClass.getField("filterSubject").set(result, filterSubjectWrapper);
            } else {
                aggClass.getField("filterSubject").set(result, filterSubject);
            }
            GenerationContext.SINGLETON.getNodeList().add(result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("could not build function " + toString(), e);
        }
    }

    private Class<Wrapper<F>> compileIfAbsent(VelocityContext ctx) throws Exception {
        Map<FunctionClassKey, Class<Wrapper<F>>> cache = GenerationContext.SINGLETON.getCache(StreamFunctionCompiler.class);
        Class<Wrapper<F>> clazz = cache.get(key);
        ctx.put("currentTemplate", currentTemplate);
        if (clazz == null) {
            clazz = FunctionGeneratorHelper.generateAndCompile(null, currentTemplate, GenerationContext.SINGLETON, ctx);
            cache.put(key, clazz);
        }
        return clazz;
    }

    private boolean isStateful(Method method) {
        if (!Modifier.isStatic(method.getModifiers()) && Stateful.class.isAssignableFrom(method.getDeclaringClass())) {
            importMap.addImport(Stateful.class);
            return true;
        }
        return false;
    }

    private void standardImports() {
        importMap.addImport(OnEvent.class);
        importMap.addImport(Wrapper.class);
        importMap.addImport(Initialise.class);
        importMap.addImport(NoEventReference.class);
        importMap.addImport(OnParentUpdate.class);
        importMap.addImport(Wrapper.class);
        importMap.addImport(FilterWrapper.class);
        importMap.addImport(Test.class);
        importMap.addImport(AbstractFilterWrapper.class);
        importMap.addImport(AfterEvent.class);
    }

    private SourceInfo addSource(Object input) {

        return inst2SourceInfo.computeIfAbsent(input, (in) -> {
            final SourceInfo srcInfo = new SourceInfo(
                    importMap.addImport(input.getClass()),
                    "source_" + sourceCount++);
            srcInfo.setWrapper(Wrapper.class.isAssignableFrom(input.getClass()));
            srcInfo.setConstant(Constant.class.isAssignableFrom(input.getClass()));
            return srcInfo;
        });

    }
}
