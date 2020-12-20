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
package com.fluxtion.ext.streaming.builder.group;

import com.fluxtion.api.SepContext;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.api.annotations.SepNode;
import com.fluxtion.api.partition.LambdaReflection.SerializableBiConsumer;
import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.builder.generation.GenerationContext;
import com.fluxtion.ext.streaming.api.ArrayListWrappedCollection;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.WrappedCollection;
import com.fluxtion.ext.streaming.api.WrappedList;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import com.fluxtion.ext.streaming.api.group.GroupByIniitialiser;
import com.fluxtion.ext.streaming.api.group.GroupByTargetMap;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.builder.Templates;
import com.fluxtion.ext.streaming.builder.util.FunctionGeneratorHelper;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.functionClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.imports;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.keyClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceMappingList;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.targetClass;
import com.fluxtion.ext.streaming.builder.util.ImportMap;
import com.fluxtion.ext.streaming.builder.util.SourceInfo;
import static com.fluxtion.generator.targets.JavaGenHelper.mapPrimitiveToWrapper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;

/**
 * Builds a group by set of functions, each mapPrimitive built will push its calculated value into a target type, using
 * a mutator method on the target type to accept the value.
 *
 * @param <K> key provider
 * @param <T> the target class for the result of aggregate operations
 * @author Greg Higgins
 */
public class GroupByContext<K, T> {

    private final List<SourceContext> contexts;
    private final Class<K> keyClazz;
    private final Class<T> targetClazz;
    private final Group<?, T> primaryGroup;
    private boolean initialiserRequired = false;
    private int count;
    private static final String TEMPLATE = Templates.PACKAGE + "/GroupByTemplate.vsl";
    private static final String TEMPLATE_CALC_STATE = Templates.PACKAGE + "/GroupByCalculationState.vsl";
    private final SourceContext<K, T> primaryContext;
    final ImportMap importMap = ImportMap.newMap(Initialise.class, OnEvent.class,
        Wrapper.class, OnParentUpdate.class, OnEventComplete.class,
        Map.class, BitSet.class, GroupBy.class, EventHandler.class,
        GroupByIniitialiser.class, GroupByTargetMap.class, NoEventReference.class,
        WrappedCollection.class, Collection.class, Stateful.class, MutableNumber.class,
        ArrayListWrappedCollection.class, SepContext.class, Collections.class, SerializableBiConsumer.class,
        Comparator.class, WrappedList.class, SepNode.class, PushReference.class, lombok.extern.slf4j.Slf4j.class
    );
    private String genClassName;
    private String calcStateClass;
    private String eventCompleteMethod;
    private String eventMethod;
    private SerializableBiConsumer<T, T> initCopy;

    public static <K, T> GroupByBuilder<K, T> builder(Group<K, T> group) {
        GroupByContext<K, T> ctxt = new GroupByContext<>(group);
        GroupByBuilder<K, T> builder = new GroupByBuilder<>(ctxt, ctxt.primaryContext);
        return builder;
    }

    public <S> GroupByBuilder<S, T> join(Wrapper<S> k, SerializableFunction<S, ?> f) {
        Group<S, T> joinedGroup = primaryGroup.join(k, f);
        SourceContext<S, T> secondaryContext = new SourceContext<>(joinedGroup);
        contexts.add(secondaryContext);
        GroupByBuilder<S, T> builder = new GroupByBuilder(this, secondaryContext);
        return builder;
    }

    public <K> GroupByBuilder<K, T> join(Class<K> k, SerializableFunction<K, ?> f) {
        Group<K, T> joinedGroup = primaryGroup.join(k, f);
        SourceContext secondaryContext = new SourceContext(joinedGroup);
        contexts.add(secondaryContext);
        GroupByBuilder builder = new GroupByBuilder(this, secondaryContext);
        return builder;
    }

    public <K> GroupByBuilder<K, T> join(Class<K> k, SerializableFunction<K, ?>... f) {
        Group<K, T> joinedGroup = primaryGroup.join(k, f);
        SourceContext secondaryContext = new SourceContext(joinedGroup);
        contexts.add(secondaryContext);
        GroupByBuilder builder = new GroupByBuilder(this, secondaryContext);
        return builder;
    }

    public <K> GroupByBuilder<K, T> join(Wrapper<K> k, SerializableFunction<K, ?>... f) {
        Group<K, T> joinedGroup = primaryGroup.join(k, f);
        SourceContext secondaryContext = new SourceContext(joinedGroup);
        contexts.add(secondaryContext);
        GroupByBuilder builder = new GroupByBuilder(this, secondaryContext);
        return builder;
    }

    public <K> GroupByBuilder<K, T> join(K k, SerializableFunction<K, ?> f) {
        Group<K, T> joinedGroup = primaryGroup.join(k, f);
        SourceContext secondaryContext = new SourceContext(joinedGroup);
        contexts.add(secondaryContext);
        GroupByBuilder builder = new GroupByBuilder(this, secondaryContext);
        return builder;
    }

    public void setInitCopy(SerializableBiConsumer<T, T> initCopy) {
        this.initCopy = initCopy;
    }

    public ImportMap getImportMap() {
        return importMap;
    }

    GroupByContext(Group<K, T> group) {
        this.primaryGroup = group;
        this.keyClazz = group.getInputClass();
        this.targetClazz = group.getTargetClass();
        Method[] methods = this.targetClazz.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(OnEventComplete.class) != null) {
                this.eventCompleteMethod = method.getName();
            }
            if (method.getAnnotation(OnEvent.class) != null) {
                this.eventMethod = method.getName();
            }
        }

        primaryContext = new SourceContext(primaryGroup);
        contexts = new ArrayList<>();
        contexts.add(primaryContext);
    }

    public GroupBy<T> build() {
        try {
            genClassName = "GroupBy_" + GenerationContext.nextId();
            buildCalculationState();
            VelocityContext ctx = new VelocityContext();
            ctx.put("currentTemplate", TEMPLATE);
            ctx.put(functionClass.name(), genClassName);
            Set<GroupByFunctionInfo> functionSet = new HashSet<>();
            contexts.stream().forEach((context) -> {
                functionSet.addAll(context.getFunctionSet());
            });
            if (mapPrimitiveToWrapper(primaryContext.getKeyMethodActual().getReturnType()) == void.class) {
                if (primaryContext.isMultiKey()) {
                    ctx.put(keyClass.name(), primaryContext.getMultiKeyClassName());
                } else {
                    ctx.put(keyClass.name(), importMap.addImport(primaryContext.getKeyMethodActual().getReturnType()));
                }
            } else {
                ctx.put(keyClass.name(), importMap.addImport(mapPrimitiveToWrapper(primaryContext.getKeyMethodActual().getReturnType())));
            }
            ctx.put(targetClass.name(), importMap.addImport(targetClazz));
            ctx.put("primaryContext", primaryContext);
            ctx.put("calcStateClass", calcStateClass);
            ctx.put("initialiserRequired", initialiserRequired);
            ctx.put("isMultiKey", primaryContext.isMultiKey());
            ctx.put("functionSet", functionSet);
            if (primaryContext.isMultiKey()) {
                ctx.put("multiKeyFunctionSet", primaryGroup.getMultiKeySourceMap());
                ctx.put("multiKeyClassName", primaryContext.getMultiKeyClassName());
            }
            if (eventCompleteMethod != null) {
                ctx.put("eventCompleteMethod", eventCompleteMethod);
            }
            if (eventMethod != null) {
                ctx.put("eventMethod", eventMethod);
            }
            if(initCopy!=null){
                Method method = initCopy.method();
                String clazzName = importMap.addImport(method.getDeclaringClass());
                ctx.put("initCopy", clazzName + "::" + method.getName());
            }else{
                ctx.put("initCopy", "(a, b) ->{}");
            }
            ctx.put(imports.name(), importMap.asString());
            ctx.put(sourceMappingList.name(), contexts);
            Class<GroupBy<T>> aggClass = FunctionGeneratorHelper.generateAndCompile(null, TEMPLATE, GenerationContext.SINGLETON, ctx);
            GroupBy<T> result = aggClass.newInstance();

            for (SourceContext context : contexts) {

                Group group = context.getGroup();
                if (!group.isEventClass()) {
                    Object source = GenerationContext.SINGLETON.addOrUseExistingNode(group.getInputSource());
                    aggClass.getField(context.getSourceInfo().id).set(result, source);
                }
            }
            GenerationContext.SINGLETON.getNodeList().add(result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("could not build function " + e.getMessage(), e);
        }
    }

    private void buildCalculationState() {
        Set<GroupByFunctionInfo> functionSet = new HashSet<>();
        contexts.stream().forEach((context) -> {
            functionSet.addAll(context.getFunctionSet());
        });
        List<Integer> optionals = contexts.stream().filter(SourceContext::isOptional)
            .mapToInt(contexts::indexOf)
            .map(i -> ++i)
            .boxed()
            .collect(Collectors.toList());
        try {
            VelocityContext ctx = new VelocityContext();
            calcStateClass = "CalculationState" + genClassName;
            ctx.put("currentTemplate", TEMPLATE_CALC_STATE);
            ctx.put(functionClass.name(), calcStateClass);
            ctx.put(targetClass.name(), importMap.addImport(targetClazz));
            ctx.put(sourceMappingList.name(), functionSet);
            ctx.put("optionals", optionals);
            ctx.put("sourceCount", contexts.size());
            ctx.put("targetInstanceId", "target");
            ctx.put(imports.name(), importMap.asString());
            Class<?> stateClass = FunctionGeneratorHelper.generateAndCompile(null, TEMPLATE_CALC_STATE, GenerationContext.SINGLETON, ctx);
            Object stateInstance = stateClass.newInstance();
            for (SourceContext context : contexts) {
                context.generatedInstance = stateInstance;
            }
        } catch (Exception e) {
            throw new RuntimeException("could not build function " + e.getMessage(), e);
        }
    }

    /**
     * holds the context for a source provider of data to the grouping container
     *
     * @param <K> Source type
     * @param <T> Traget type
     */
    @Data
    @ToString(of = "sourceInfo")
    public class SourceContext<K, T> {

        final String calcStateClassName;
        String multiKeyId;
        final String calcStateInstanceId;
        String initialiserId;
        Object generatedInstance;
        private boolean optional;
        private Group<K, T> group;
        private K keyProvider;
        private Class<T> targetClass;
        /**
         * the lookup key provider for the group context, this specifies where in the group map data will be created or
         * updated.
         */
        private Method keyMethod;
        /**
         * Identification for the data provider.
         */
        private SourceInfo sourceInfo;
        /**
         * The set of functions that bridge incoming data to the target instance
         */
        private Set<GroupByFunctionInfo> functionSet;
        private Set<GroupByInitialiserInfo> initialiserSet;

        public SourceContext(Group<K, T> group) {
            this.group = group;
            keyProvider = group.getInputSource();
            targetClass = group.getTargetClass();
            //TODO add multikey support here
            if (group.isMultiKey()) {
                keyMethod = Arrays.stream(group.getMultiKey().getClass().getMethods()).filter(m -> m.getName().equals("setKey")).findFirst().get();
                multiKeyId = group.getMultiKey().getClass().getSimpleName() + GenerationContext.nextId();
                multiKeyId = StringUtils.uncapitalize(multiKeyId);
            } else if (group.isWrapped()) {
                keyMethod = group.getKeyMethod();
            } else {
                keyMethod = group.getKeyMethod();
            }
            String id = StringUtils.uncapitalize(keyProvider.getClass().getSimpleName() + (count++));

            if (group.isWrapped()) {
                sourceInfo = new SourceInfo(importMap.addImport(((Wrapper) getKeyProvider()).eventClass()), id);
                sourceInfo.setWrapperType(importMap.addImport(keyProvider.getClass()));
            } else {
                sourceInfo = new SourceInfo(importMap.addImport(getKeyProvider().getClass()), id);
            }
            functionSet = new HashSet<>();
            initialiserSet = new HashSet<>();
            calcStateClassName = "CalculationState" + keyProvider.getClass().getSimpleName() + GenerationContext.nextId();
            calcStateInstanceId = StringUtils.uncapitalize(calcStateClassName);
        }

        public Group<K, T> getGroup() {
            return group;
        }

        public void addGroupByFunctionInfo(GroupByFunctionInfo info) {
            getFunctionSet().add(info);
        }

        public void addInitialiserFunction(GroupByInitialiserInfo initialiser) {
            initialiserRequired = true;
            getInitialiserSet().add(initialiser);
        }

        public boolean isEventClass() {
            return getGroup().isEventClass();
        }

        public boolean isWrapped() {
            return getGroup().isWrapped();
        }

        public boolean isInitialiserRequired() {
            return getInitialiserSet().size() > 0;
        }

        public boolean isMultiKey() {
            return getGroup().isMultiKey();
        }

        public String getMultiKeyClassName() {
            return getGroup().getMultiKeyClassName();
        }

        public Method getKeyMethodActual() {
            return keyMethod;
        }

        public String getKeyMethod() {
            return keyMethod.getName();
        }

        public K getKeyProvider() {
            return keyProvider;
        }

        public Class<T> getTargetClass() {
            return targetClass;
        }

        public SourceInfo getSourceInfo() {
            return sourceInfo;
        }

        public Set<GroupByFunctionInfo> getFunctionSet() {
            return functionSet;
        }

        public Set<GroupByInitialiserInfo> getInitialiserSet() {
            return initialiserSet;
        }
    }
}
