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
package com.fluxtion.articles.sales;

import com.fluxtion.ext.declarative.builder.stream.StreamInprocessTest;
import static com.fluxtion.ext.streaming.builder.factory.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.cumSum;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.multiply;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class SimpleTest extends StreamInprocessTest {

    @Test
    public void testForHannah() {
        sep((c) -> {
            var sold = cumSum(Shop.Sale::getAmountSold)
                .defaultVal(0)
                .log("my sum is: {}", Number::intValue);
            
            var delivered = select(Shop.Delivery.class)
                .map(SimpleTest::multiplyBy10, Shop.Delivery::getAmountDelivered)
                .defaultVal(10.0)
                .log("x10 result: {}", Number::intValue);
            
            multiply(delivered, sold)
                .log("crazy multiply: {}", Number::intValue);
        });
        
        onEvent(new Shop.Sale(100));
        onEvent(new Shop.Sale(200));
        onEvent(new Shop.Delivery(200));
        onEvent(new Shop.Delivery(200));
        onEvent(new Shop.Sale(200));

    }
    
    public static double multiplyBy10(int in){
        return 10 * in;
    }

}
