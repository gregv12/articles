/*
 * Copyright (c) 2019, V12 Technology Ltd.
 * All rights reserved.
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
package com.fluxtion.ext.streaming.api.stream;

import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.ext.streaming.api.Stateful;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;

/**
 * Predicates for numeric change in a value.
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class PercentPredicates implements Stateful {

    
    public static SerializableFunction<Number, Boolean> ltPercent(double barrier){
        return new PercentPredicates(barrier)::lt;
    }
    
    public static SerializableFunction<Number, Boolean> gtPercent(double barrier){
        return new PercentPredicates(barrier)::gt;
    }
    
    public double previous = Double.NaN;
    private final double changeBarrier;
    private MutableNumber result = new MutableNumber();

    public PercentPredicates(double changeBarrier) {
        this.changeBarrier = changeBarrier;
    }

    private MutableNumber delta(Number newVal) {
        result.set(newVal.doubleValue() / previous);
        previous = newVal.doubleValue();
        return result;
    }

    public boolean lt(Number newVal) {
        return delta(newVal).doubleValue < changeBarrier;
    }

    public boolean gt(Number newVal) {
        return delta(newVal).doubleValue > changeBarrier;
    }

    @Override
    public void reset() {
        previous = Double.NaN;
    }

}