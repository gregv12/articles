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

import com.fluxtion.api.partition.LambdaReflection.SerializableBiConsumer;
import com.fluxtion.api.partition.LambdaReflection.SerializableBiFunction;
import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.api.partition.LambdaReflection.SerializableTriFunction;
import com.fluxtion.builder.generation.GenerationContext;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.group.AggregateFunctions;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import com.fluxtion.ext.streaming.builder.group.GroupByContext.SourceContext;
import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;

/**
 * The main instance a user interacts with while building a group by function.
 *
 * @author Greg Higgins
 * @param <K> Source type
 * @param <T> Target type
 */
public class GroupByBuilder<K, T> {

    private final GroupByContext<K, T> groupBy;
    private final GroupByContext<K, T>.SourceContext<K, T> sourceContext;

    public GroupByBuilder(GroupByContext<K, T> context, SourceContext sourceContext) {
        this.groupBy = context;
        this.sourceContext = sourceContext;
    }

    public <K> GroupByBuilder<K, T> join(K k, SerializableFunction<K, ?> f) {
        return groupBy.join(k, f);
    }

    public <K> GroupByBuilder<K, T> join(Class<K> k, SerializableFunction<K, ?> f) {
        return groupBy.join(k, f);
    }

    public <K> GroupByBuilder<K, T> join(Class<K> k, SerializableFunction<K, ?>... f) {
        return groupBy.join(k, f);
    }

    public <S> GroupByBuilder<S, T> join(Wrapper<S> k, SerializableFunction<S, ?> f) {
        return groupBy.join(k, f);
    }

    public <S> GroupByBuilder<S, T> join(Wrapper<S> k, SerializableFunction<S, ?>... f) {
        return groupBy.join(k, f);
    }

    public <V> GroupByBuilder<K, T> init(SerializableFunction<K, V> valueFunction, SerializableBiConsumer<T, V> tragetFunction) {
        try {
            Method sourceMethod = valueFunction.method();
            Method targetMethod = tragetFunction.method();
            GroupByInitialiserInfo info = new GroupByInitialiserInfo(groupBy.getImportMap());
            sourceContext.setInitialiserId("initialiser" + sourceContext.sourceInfo.id);
            if (sourceContext.isWrapped()) {
                info.setSource(((Wrapper) sourceContext.keyProvider).eventClass(), sourceMethod, "source");
            } else {
                info.setSource(sourceContext.keyProvider.getClass(), sourceMethod, "source");
            }
            info.setTarget(sourceContext.targetClass, targetMethod, "target");
            sourceContext.addInitialiserFunction(info);
        } catch (Exception e) {
            //maybe numeric?
            initPrimitive((SerializableFunction<K, ? super Number>) valueFunction, (SerializableBiConsumer<T, ? super Byte>) tragetFunction);
        }
        return this;
    }

    public GroupByBuilder<K, T> initPrimitive(SerializableFunction<K, ? super Number> valueFunction, SerializableBiConsumer<T, ? super Byte> tragetFunction) {
        Method sourceMethod = valueFunction.method();
        Method targetMethod = tragetFunction.method();
        GroupByInitialiserInfo info = new GroupByInitialiserInfo(groupBy.getImportMap());
        sourceContext.setInitialiserId("initialiser" + sourceContext.sourceInfo.id);
        if (sourceContext.isWrapped()) {
            info.setSource(((Wrapper) sourceContext.keyProvider).eventClass(), sourceMethod, "source");
        } else {
            info.setSource(sourceContext.keyProvider.getClass(), sourceMethod, "source");
        }
        info.setTarget(sourceContext.targetClass, targetMethod, "target");
        sourceContext.addInitialiserFunction(info);
        return this;
    }

    public GroupByBuilder<K, T>
        avg(SerializableFunction<K, ? extends Number> sourceFunction, SerializableBiConsumer<T, ? super Byte> target) {
        return function(sourceFunction, target, AggregateFunctions.AggregateAverage::calcAverage);
    }

    public GroupByBuilder<K, T>
        count(SerializableBiConsumer<T, ? super Byte> target) {
        return function( target, AggregateFunctions.AggregateCount::increment);
    }

    public GroupByBuilder<K, T>
        set(SerializableFunction<K, ? extends Number> sourceFunction, SerializableBiConsumer<T, ? super Byte> target) {
        return function(sourceFunction, target, AggregateFunctions.AggregatePassThrough::set);
    }

    public GroupByBuilder<K, T>
        min(SerializableFunction<K, ? extends Number> sourceFunction, SerializableBiConsumer<T, ? super Byte> target) {
        return function(sourceFunction, target, AggregateFunctions.AggregateMin::minimum);
    }

    public GroupByBuilder<K, T>
        max(SerializableFunction<K, ? extends Number> sourceFunction, SerializableBiConsumer<T, ? super Byte> target) {
        return function(sourceFunction, target, AggregateFunctions.AggregateMax::maximum);
    }

    public GroupByBuilder<K, T>
        sum(SerializableFunction<K, ? extends Number> sourceFunction, SerializableBiConsumer<T, ? super Byte> target) {
        return function(sourceFunction, target, AggregateFunctions.AggregateSum::calcSum);
    }

    public void optional(boolean optional) {
        sourceContext.setOptional(optional);
    }

    public <S extends Number, G extends Number, R> GroupByBuilder function(
        SerializableFunction<K, ? extends S> supplier,
        SerializableBiConsumer<T, ? super Byte> target,
        SerializableBiFunction<? super G, ? super G, ? extends G> func) {
        Class calcFunctionClass = func.getContainingClass();
        //set function
        GroupByFunctionInfo info = new GroupByFunctionInfo(groupBy.getImportMap());
        String id = StringUtils.uncapitalize(calcFunctionClass.getSimpleName() + GenerationContext.nextId());
        info.setFunction(calcFunctionClass, func.method(), id);
        //set source
        Class<K> sourceClass = null;
        if (sourceContext.isWrapped()) {
            sourceClass = (Class<K>) ((Wrapper) sourceContext.keyProvider).eventClass();
        } else {
            sourceClass = (Class<K>) sourceContext.group.getInputSource().getClass();
        }
        Method sourceMethod = supplier.method();//numericGetMethod(sourceClass, sourceFunction);
        info.setSource(sourceClass, sourceMethod, "event");
        //set source
        info.setTarget(sourceContext.targetClass, target.method(), "target");
        //add to context
        sourceContext.addGroupByFunctionInfo(info);
        return this;
    }

    public <S extends Number, G extends Number, R, F> GroupByBuilder function(
        SerializableFunction<K, ? extends S> supplier,
        SerializableBiConsumer<T, ? super Byte> target,
        SerializableTriFunction<F, ? super G, ? super G, ? extends G> func) {
        Class calcFunctionClass = func.getContainingClass();
        //set function
        GroupByFunctionInfo info = new GroupByFunctionInfo(groupBy.getImportMap());
        String id = StringUtils.uncapitalize(calcFunctionClass.getSimpleName() + GenerationContext.nextId());
        info.setFunction(calcFunctionClass, func.method(), id);
        //set source
        Class<K> sourceClass = null;
        if (sourceContext.isWrapped()) {
            sourceClass = (Class<K>) ((Wrapper) sourceContext.keyProvider).eventClass();
        } else {
            sourceClass = (Class<K>) sourceContext.group.getInputSource().getClass();
        }
        Method sourceMethod = supplier.method();//numericGetMethod(sourceClass, sourceFunction);
        info.setSource(sourceClass, sourceMethod, "event");
        //set source
        info.setTarget(sourceContext.targetClass, target.method(), "target");
        //add to context
        sourceContext.addGroupByFunctionInfo(info);
        return this;
    }

    public <G extends Number> GroupByBuilder
        function(
            SerializableBiConsumer<T, ? super Byte> target,
            SerializableBiFunction<? super G, ? super G, ? extends G> func) {
        Class calcFunctionClass = func.getContainingClass();
        //set function
        GroupByFunctionInfo info = new GroupByFunctionInfo(groupBy.getImportMap());
        String id = StringUtils.uncapitalize(calcFunctionClass.getSimpleName() + GenerationContext.nextId());
        info.setFunction(calcFunctionClass, func.method(), id);
        //set target
        info.setTarget(sourceContext.targetClass, target.method(), "target");
        //add to context
        sourceContext.addGroupByFunctionInfo(info);
        return this;
    }

    public <G extends Number, F> GroupByBuilder
        function(
            SerializableBiConsumer<T, ? super Byte> target,
            SerializableTriFunction<F, ? super G, ? super G, ? extends G> func) {
        Class calcFunctionClass = func.getContainingClass();
        //set function
        GroupByFunctionInfo info = new GroupByFunctionInfo(groupBy.getImportMap());
        String id = StringUtils.uncapitalize(calcFunctionClass.getSimpleName() + GenerationContext.nextId());
        info.setFunction(calcFunctionClass, func.method(), id);
        //set target
        info.setTarget(sourceContext.targetClass, target.method(), "target");
        //add to context
        sourceContext.addGroupByFunctionInfo(info);
        return this;
    }

    public GroupBy<T> build() {
        return groupBy.build();
    }

    public GroupBy<T> build(String publicName) {
        return groupBy.build();
    }

}
