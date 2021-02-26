/*
 * Copyright (C) 2021 V12 Technology Ltd.
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
package com.fluxtion.generator.constructor;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.generator.util.BaseSepInprocessTest;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class ConstructorComplexTest extends BaseSepInprocessTest{
    
    @Test
    public void testArgs(){
    
        fixedPkg = true;
        sep((c) -> {
//            final MyThing thing = c.addNode(new MyThing());
            c.addPublicNode(new Handler(new MyThing()), "handler");
        });
        
        Handler handler = getField("handler");
        Assert.assertNotNull(handler.getName());
    }
    
    
    @Data
    public static class Handler{

        private final MyThing name;

        public Handler(MyThing name) {
            this.name = name;
        }
        
    
        @EventHandler
        public void stringUpdate(String in){
        }
    }
    
    public static class MyThing{}
}

