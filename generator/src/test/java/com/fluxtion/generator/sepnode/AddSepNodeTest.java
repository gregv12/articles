/*
 * Copyright (c) 2020, V12 Technology Ltd.
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
package com.fluxtion.generator.sepnode;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.SepNode;
import com.fluxtion.generator.util.BaseSepInprocessTest;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class AddSepNodeTest extends BaseSepInprocessTest {
    
    @Test
    public void testAddNodeByAnnotation() {
        sep(cfg -> {
            cfg.addPublicNode(new Counter(new Stringhandler()), "counter");
        });
        Counter processor = getField("counter");
        onEvent("test");
        onEvent("test");
        assertThat(processor.count, is(2));
    }
    
    public static class Stringhandler{
    
        @EventHandler
        public void stringUpdate(String s){
        
        }
    }
    
    
    public static class Counter{

        private int count;
        @SepNode
        private final Stringhandler myHandler;

        public Counter(Stringhandler myHandler) {
            this.myHandler = myHandler;
        }
        
        @OnEvent
        public void increment(){
            count++;
        }
    
    }
    
}
