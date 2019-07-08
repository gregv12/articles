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
package com.fluxtion.articles.lombok.temperature;

import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class TempMonitorTest {
    
    @Test
    public void testTemp() throws Exception{
        EventHandler handler = new InlineLombok().handler();
        ((Lifecycle)handler).init();
        handler.onEvent(new InlineLombok.TempEvent(10));
        handler.onEvent(new InlineLombok.TempEvent(9));
        handler.onEvent(new InlineLombok.TempEvent(17));
        handler.onEvent(new InlineLombok.TempEvent(16));
        handler.onEvent(new InlineLombok.TempEvent(14));
        handler.onEvent(new InlineLombok.TempEvent(24));
        Assert.assertEquals(3, MyTempProcessor.count);
    }
    
}
