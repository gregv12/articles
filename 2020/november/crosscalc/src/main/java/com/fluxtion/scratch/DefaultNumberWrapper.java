/*
 * Copyright (C) 2020 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.scratch;

import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;

/**
 *
 * @author V12 Technology Ltd.
 */
public class DefaultNumberWrapper implements Wrapper<Number>{
   
    private final Wrapper<Number> parent;
    private final int defaultVal;
    private final MutableNumber result = new MutableNumber();
    
    public DefaultNumberWrapper(Wrapper<Number> parent, int defaultVal) {
        this.parent = parent;
        this.defaultVal = defaultVal;
    }

    @OnEvent
    public boolean onEvent() {
        Number parentNumber = parent.event();
        boolean updated = !parentNumber.equals(result);
        result.set(parentNumber);
        return updated;
    }

    @Override
    public Number event() {
        return result;
    }

    @Override
    public Class<Number> eventClass() {
        return Number.class;
    }

    @Override
    public boolean isValidOnStart() {
        return true;
    }
    
    @Initialise
    public void init(){
        result.set(defaultVal);
    }
    
}
