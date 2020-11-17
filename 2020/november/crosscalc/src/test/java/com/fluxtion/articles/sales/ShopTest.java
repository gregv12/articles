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

import com.fluxtion.articles.sales.generated.fluxtion_annotated.StockProcessor;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class ShopTest {
    
    
    @Test
    public void simpleTest(){
        StockProcessor stockProcessor = new StockProcessor();
        stockProcessor.init();
        //send some stock
        System.out.println("ADDING 30 ITEMS");
        stockProcessor.onEvent(new Shop.Delivery(30));
        //sell
        System.out.println("SELLING 10");
        stockProcessor.onEvent(new Shop.Sale(10));
        System.out.println("SELLING 15");
        stockProcessor.onEvent(new Shop.Sale(15));
        //
        System.out.println("NEW PRICE 100");
        stockProcessor.onEvent(new Shop.Price(100));
        System.out.println("SELLING 5");
        stockProcessor.onEvent(new Shop.Sale(5));
        //
        System.out.println("NEW COST 75");
        stockProcessor.onEvent(new Shop.ItemCost(75));
        System.out.println("RE-STOCKING 12");
        stockProcessor.onEvent(new Shop.Delivery(12));
        
    }
}
