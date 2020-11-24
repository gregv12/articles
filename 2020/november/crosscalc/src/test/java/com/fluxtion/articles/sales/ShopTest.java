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
package com.fluxtion.articles.sales;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.articles.sales.builder.EventProcessorBuilder;
import com.fluxtion.articles.sales.generated.fluxtion_annotated.StockProcessor;
import com.fluxtion.ext.declarative.builder.stream.StreamInprocessTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class ShopTest extends StreamInprocessTest {
    
    
    @Test
//    @Ignore
    public void simpleTest() {
        StockProcessor processor = new StockProcessor();
        processor.init();
        sendTestEvents(processor);
    }

    @Test
    @Ignore
    public void generateAndTest() {
        sendTestEvents(sep(EventProcessorBuilder::buildGraphAnnotated));
    }

    @Test
    @Ignore
    public void developProcessorTest() {
        sendTestEvents(
            sep(EventProcessorBuilder::buildGraphAnnotated,
                 "com.fluxtion.articles.sales.generated.sales.ShopProcessor")
        );
    }

    private static void sendTestEvents(StaticEventProcessor processor) {
        //send some stock
        System.out.println("ADDING 30 ITEMS");
        processor.onEvent(new Shop.Delivery(30));
        //sell
        System.out.println("SELLING 10");
        processor.onEvent(new Shop.Sale(10));
        System.out.println("SELLING 15");
        processor.onEvent(new Shop.Sale(15));
        //
        System.out.println("NEW PRICE 100");
        processor.onEvent(new Shop.Price(100));
        System.out.println("SELLING 5");
        processor.onEvent(new Shop.Sale(5));
        //
        System.out.println("NEW COST 75");
        processor.onEvent(new Shop.ItemCost(75));
        System.out.println("RE-STOCKING 12");
        processor.onEvent(new Shop.Delivery(12));
        //
        System.out.println("CLEAR STOCK AND DELIVERY COUNTS");
        processor.onEvent("reset");
        System.out.println("RE-STOCKING 4");
        processor.onEvent(new Shop.Delivery(4));
    }
}
