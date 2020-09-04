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
package com.fluxtion.ext.text.builder.csv;

import com.fluxtion.api.annotations.Config;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnEventComplete;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.api.partition.LambdaReflection;
import com.fluxtion.builder.generation.GenerationContext;
import static com.fluxtion.builder.generation.GenerationContext.SINGLETON;
import com.fluxtion.ext.streaming.api.util.CharArrayCharSequence;
import com.fluxtion.ext.streaming.builder.util.FunctionGeneratorHelper;
import com.fluxtion.ext.streaming.builder.util.FunctionKeys;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.functionClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceMappingList;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.targetClass;
import com.fluxtion.ext.streaming.builder.util.ImportMap;
import static com.fluxtion.ext.text.api.csv.Converters.defaultCharSequence;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.util.EventPublsher;
import static com.fluxtion.ext.text.builder.Templates.CSV_MARSHALLER;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author gregp
 * @param <P>
 * @param <T>
 */
public class RecordParserBuilder<P extends RecordParserBuilder<P, T>, T> {

    protected Class<T> targetClazz;
    protected String targetId;
    protected final ImportMap importMap;
    protected int headerLines;
    protected int mappingRow;
    protected String eventMethod;
    protected String eventCompleteMethod;
    protected boolean skipEmptyLines;
    protected boolean skipCommentLines;
    protected boolean processEscapeSequences;
    protected boolean acceptPartials;
    protected boolean addEventPublisher;
    protected final ArrayList<CsvPushFunctionInfo> srcMappingList;
    protected final ArrayList<CsvOutInfo> outSrcList;
    protected int converterCount;
    protected Map<Object, String> converterMap;
    protected Map<Object, Pair> defaultValueMap;
    protected CharTokenConfig tokenCfg;
    protected boolean reuseTarget;
    protected final boolean fixedLen;
    private String id;

    protected RecordParserBuilder(Class<T> target, int headerLines, boolean fixedLen) {
        importMap = ImportMap.newMap();
        importMap.addImport(com.fluxtion.api.annotations.EventHandler.class);
        importMap.addImport(Initialise.class);
        importMap.addImport(CharEvent.class);
        importMap.addImport(HashMap.class);
        importMap.addImport(RowProcessor.class);
        importMap.addImport(EofEvent.class);
        importMap.addImport(Config.class);
        importMap.addImport(ValidationLogger.class);
        importMap.addImport(Inject.class);
        importMap.addImport(PushReference.class);
        importMap.addImport(CharArrayCharSequence.class);
        importMap.addImport(CharArrayCharSequence.CharSequenceView.class);
        importMap.addImport(IOException.class);
        importMap.addImport(Arrays.class);
        importMap.addImport(target);
        srcMappingList = new ArrayList<>();
        outSrcList = new ArrayList<>();
        this.headerLines = headerLines;
        this.converterMap = new HashMap<>();
        this.defaultValueMap = new HashMap<>();
        this.tokenCfg = CharTokenConfig.WINDOWS;
        this.reuseTarget = true;
        this.addEventPublisher = false;
        this.fixedLen = fixedLen;
        this.id = "validationLog";
        //
        targetClazz = target;
        targetId = "target";
        Method[] methods = target.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(OnEvent.class) != null) {
                eventMethod = method.getName();
            }
            if (method.getAnnotation(OnEventComplete.class) != null) {
                importMap.addImport(OnEventComplete.class);
                eventCompleteMethod = method.getName();
            }
        }
    }

    public <S extends CharSequence, U> P converter(int colIndex, LambdaReflection.SerializableFunction<S, U> converterFunction) {
        importMap.addImport(NoEventReference.class);
        Method method = converterFunction.method(SINGLETON.getClassLoader());
        Class<?> declaringClass = method.getDeclaringClass();
        if (Modifier.isStatic(method.getModifiers())) {
            importMap.addStaticImport(declaringClass);
            colInfo(colIndex).setConverter(method);
        } else {
            Object captured = converterFunction.captured()[0];
            String localName = converterMap.computeIfAbsent(captured, (t) -> {
                String name = declaringClass.getSimpleName();
                name = name.toLowerCase().charAt(0) + name.substring(1) + "_" + converterCount++;
                GenerationContext.SINGLETON.getNodeList().add(captured);
                return name;
            });
            colInfo(colIndex).setConverter(localName, method, captured);
        }
        return (P) this;
    }

    public <S extends CharSequence, U> P converter(String colName, LambdaReflection.SerializableFunction<S, U> converterFunction) {
        importMap.addImport(NoEventReference.class);
        Method method = converterFunction.method(SINGLETON.getClassLoader());
        Class<?> declaringClass = method.getDeclaringClass();
        if (Modifier.isStatic(method.getModifiers())) {
            importMap.addStaticImport(declaringClass);
            colInfo(colName).setConverter(method);
        } else {
            Object captured = converterFunction.captured()[0];
            String localName = converterMap.computeIfAbsent(captured, (t) -> {
                String name = declaringClass.getSimpleName();
                name = name.toLowerCase().charAt(0) + name.substring(1) + "_" + colName;
                GenerationContext.SINGLETON.getNodeList().add(captured);
                return name;
            });
            colInfo(colName).setConverter(localName, method, captured);
        }
        return (P) this;
    }

    public P defaultValue(String colName, String defaultValue) {
        importMap.addImport(NoEventReference.class);
        LambdaReflection.SerializableFunction<CharSequence, CharSequence> converterFunction = defaultCharSequence(defaultValue);
        Method method = converterFunction.method(SINGLETON.getClassLoader());
        Class<?> declaringClass = method.getDeclaringClass();
        Object captured = converterFunction.captured()[0];
        Pair localName = defaultValueMap.computeIfAbsent(captured, (t) -> {
            String name = declaringClass.getSimpleName();
            name = name.toLowerCase().charAt(0) + name.substring(1) + "_" + colName;
            //GenerationContext.SINGLETON.getNodeList().add(captured);
            return new Pair(name, defaultValue);
        });
        colInfo(colName).setDefaultValue(localName.getId(), method, captured);  
        return (P) this;
    }

    public P defaultValue(int colIndex, String defaultValue) {
        importMap.addImport(NoEventReference.class);
        LambdaReflection.SerializableFunction<CharSequence, CharSequence> converterFunction = defaultCharSequence(defaultValue);
        Method method = converterFunction.method(SINGLETON.getClassLoader());
        Class<?> declaringClass = method.getDeclaringClass();
        Object captured = converterFunction.captured()[0];
        Pair localName = defaultValueMap.computeIfAbsent(captured, (t) -> {
            String name = declaringClass.getSimpleName();
            name = name.toLowerCase().charAt(0) + name.substring(1) + "_Column" + colIndex;
            return new Pair(name, defaultValue);
        });
        colInfo(colIndex).setDefaultValue(localName.getId(), method, captured);  
        return (P) this;
    }

    <S extends CharSequence, U> P converterMethod(String colName, Method method) {
        importMap.addStaticImport(method.getDeclaringClass());
        colInfo(colName).setConverter(method);
        return (P) this;
    }
    
    <S extends CharSequence, U> P converterMethod(String colName, Method method, String instanceId) {
        if (Modifier.isStatic(method.getModifiers())) {
            importMap.addStaticImport(method.getDeclaringClass());
        }else{
            importMap.addImport(method.getDeclaringClass());
        }
        colInfo(colName).setConverter(instanceId, method, null);
        return (P) this;
    }

    public P headerLines(int lines) {
        this.headerLines = lines;
        return (P) this;
    }

    public P trim(int colIndex) {
        colInfo(colIndex).setTrim(true);
        return (P) this;
    }

    public P skipEmptyLines(boolean skipLines) {
        this.skipEmptyLines = skipLines;
        return (P) this;
    }

    public P skipCommentLines(boolean skipCommentLines) {
        this.skipCommentLines = skipCommentLines;
        return (P) this;
    }

    public P processEscapeSequences(boolean processEscapeSequences) {
        this.processEscapeSequences = processEscapeSequences;
        return (P) this;
    }

    public P tokenConfig(CharTokenConfig tokenCfg) {
        this.tokenCfg = tokenCfg;
        return (P) this;
    }

    public P acceptPartials(boolean acceptParials) {
        this.acceptPartials = acceptParials;
        return (P) this;
    }

    public P includeEventPublisher(boolean eventPublisher) {
        this.addEventPublisher = eventPublisher;
        return (P) this;
    }

    public P addEventPublisher() {
        this.addEventPublisher = true;
        return (P) this;
    }

    public P removeEventPublisher() {
        this.addEventPublisher = true;
        return (P) this;
    }

    public P reuseTarget(boolean reuseTarget) {
        this.reuseTarget = reuseTarget;
        return (P) this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RowProcessor<T> build() {
        try {
            VelocityContext ctx = new VelocityContext();
            updateContext(ctx);
            HashMap<String, String> convereters = new HashMap<>();
            HashMap<String, String> validators = new HashMap<>();
            converterMap.entrySet().stream().forEach((e) -> {
                convereters.put(e.getValue(), importMap.addImport(e.getKey().getClass()));
            });
            if (mappingRow > 0) {
                importMap.addImport(ArrayList.class);
            }
            String genClassSuffix = targetClazz.getSimpleName() + "CsvDecoder";
            String genClassName = genClassSuffix + GenerationContext.SINGLETON.nextId(genClassSuffix);
            sortSrcMappingList();
            ctx.put(functionClass.name(), genClassName);
            ctx.put("imports", importMap.asString());
            ctx.put(targetClass.name(), targetClazz.getSimpleName());
            ctx.put(sourceMappingList.name(), srcMappingList);
            ctx.put("fieldCount", maxFieldCount());
            ctx.put("outSrcList", outSrcList);
            ctx.put("headerPresent", headerLines > 0);
            ctx.put("headerLines", headerLines);
            ctx.put("mappingRowPresent", mappingRow > 0);
            ctx.put("mappingRow", mappingRow);
            ctx.put("skipEmptyLines", skipEmptyLines);
            ctx.put("skipCommentLines", skipCommentLines);
            ctx.put("processEscapeSequences", processEscapeSequences);
            ctx.put("acceptPartials", acceptPartials);
            ctx.put("fixedLen", fixedLen);
            ctx.put("newTarget", !reuseTarget);
            ctx.put("convereters", convereters);
            ctx.put("defaultValues", defaultValueMap);
            ctx.put("validators", validators);
            ctx.put(FunctionKeys.id.name(), id);
            ctx.put("delimiter", StringEscapeUtils.escapeJava("" + tokenCfg.getFieldSeparator()));
            ctx.put("eol", StringEscapeUtils.escapeJava("" + tokenCfg.getLineEnding()));
            if (tokenCfg.getIgnoredChars() != Character.MIN_VALUE) {
                ctx.put("ignore", StringEscapeUtils.escapeJava("" + tokenCfg.getIgnoredChars()));
            }
            if (eventMethod != null) {
                ctx.put("eventMethod", eventMethod);
            }
            if (eventCompleteMethod != null) {
                ctx.put("eventCompleteMethod", eventCompleteMethod);
            }
            final Class<RowProcessor> aggClass = FunctionGeneratorHelper.generateAndCompile(null, CSV_MARSHALLER, GenerationContext.SINGLETON, ctx);
            final RowProcessor result = aggClass.newInstance();

            converterMap.entrySet().stream().forEach((e) -> {
                try {
                    Object source = e.getKey();
                    String fieldName = e.getValue();
                    aggClass.getField(fieldName).set(result, source);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException("could not build function " + ex.getMessage(), ex);
                }
            });
            GenerationContext.SINGLETON.getNodeList().add(result);
            if (addEventPublisher) {
                EventPublsher publisher = new EventPublsher();
                GenerationContext.SINGLETON.addOrUseExistingNode(publisher);
                publisher.addEventSource(result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("could not build function " + e.getMessage(), e);
        }
    }

    //helper methods
    protected CsvPushFunctionInfo colInfo(int index) {
        return srcMappingList.stream().filter(c -> c.getFieldIndex() == index).findFirst().get();
    }

    protected CsvPushFunctionInfo colInfo(String name) {
        return srcMappingList.stream().filter(c -> c.getFieldName() != null && c.getFieldName().equals(name)).findFirst().get();
    }

    protected CsvPushFunctionInfo colInfo(Method accessor) {
        return srcMappingList.stream().filter(c -> c.getTargetMethod().equals(accessor)).findFirst().get();
    }

    public Class<T> getTargetClazz() {
        return targetClazz;
    }
    
    protected final void sortSrcMappingList(){
        srcMappingList.sort(new Comparator<CsvPushFunctionInfo>() {
            @Override
            public int compare(CsvPushFunctionInfo o1, CsvPushFunctionInfo o2) {
                boolean o1Derived = o1.getConverterMethod()!=null;
                boolean o2Derived = o2.getConverterMethod()!=null;
                if(o1Derived==o2Derived){
                    return 0;
                }else if(o1Derived){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
    
    } 

    protected void updateContext(VelocityContext ctx) {

    }
    
    private int maxFieldCount(){
        int max = srcMappingList.size();
        for (CsvPushFunctionInfo csvPushFunctionInfo : srcMappingList) {
            max = Math.max(max, csvPushFunctionInfo.getFieldIndex());
        }
        return max;
    }
    
    @Data
    public static class Pair{
        private final String id;
        private final String value;
    }

}
