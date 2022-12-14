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
package com.fluxtion.articles.crosscalc;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.event.Signal;
import lombok.Value;

/**
 *
 * @author V12 Technology Ltd.
 */
@Value
public class MyDependency {
    double maxChange;
    
    @EventHandler(filterString = "SIGNAL_SAMPLE_CONFIG")
    public boolean updateConfig(Signal<Double> doubleConfig){
        return true;
    }
    
    @OnEvent
    public boolean update(){
        return true;
    }
}