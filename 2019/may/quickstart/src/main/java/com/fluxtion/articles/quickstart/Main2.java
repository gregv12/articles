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
package com.fluxtion.articles.quickstart;

import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import static com.fluxtion.ext.streaming.api.stream.TimerFilter.throttle;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.sepInstance;

/**
 *
 * @author V12 Technology Ltd.
 */
public class Main2 {

    public static void main(String[] args) throws Exception {
        EventHandler handler = sepInstance(c -> {
            select(TempEvent.class).filter(t -> t.temp() > 20).console("Too hot!! ")
                    .filter(throttle(3))
                    .filter(t -> t.temp() > 20)
                    .console("throttled temp");
        }, "com.fluxtion.articles.quickstart.inline", "MyHandler");
        handler.onEvent(new TempEvent(0));
        handler.onEvent(new TempEvent(15));
        handler.onEvent(new TempEvent(19));
        handler.onEvent(new TempEvent(23));
        handler.onEvent(new TempEvent(26));
        handler.onEvent(new TempEvent(16));
    }
}
