/*
 * Copyright (C) 2019 V12 Technology Ltd.
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
package com.fluxtion.articles.quickstart.timer;

import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.OnEvent;

/**
 *
 * @author V12 Technology Ltd.
 */
public class TimeSignal {
    
    @Inject
    private final Clock clock;

    public TimeSignal(Clock clock) {
        this.clock = clock;
    }
    
    @OnEvent
    public boolean alarmNotify(){
        return true;
    }
    
}
