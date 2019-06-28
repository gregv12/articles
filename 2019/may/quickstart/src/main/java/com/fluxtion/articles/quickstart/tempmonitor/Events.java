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
package com.fluxtion.articles.quickstart.tempmonitor;

import com.fluxtion.api.event.Event;
import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * Events for the temperature monitoring example.
 *
 * @author V12 Technology Ltd.
 */
public class Events {

    @Data
    @AllArgsConstructor
    public static class TempEvent extends Event {
        private int temp;
    }

    @Data
    @AllArgsConstructor
    public static class StartOfDay extends Event {
        private final String day;
    }

    public static class EndOfDay extends Event {}
}
