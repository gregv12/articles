
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
package com.fluxtion.articles.sales.builder;

import com.fluxtion.articles.sales.Shop;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.api.stream.Argument.argInt;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.lt;
import static com.fluxtion.ext.streaming.builder.factory.DefaultNumberBuilder.defaultVal;
import static com.fluxtion.ext.streaming.builder.factory.FilterBuilder.filter;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.cumSum;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.multiply;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.subtract;
import static com.fluxtion.ext.streaming.builder.log.LogBuilder.log;

/**
 *
 * @author V12 Technology Ltd.
 */
public class EventProcessorBuilder {

    @SepBuilder(name = "StockProcessor", packageName = "com.fluxtion.articles.sales.fluxtion_generated")
//    @Disabled
    public static void buildGraphAnnotated(SEPConfig cfg) {
        //stock level calculation set default values for amoutnSold and amountDelivered
        //otherwise we need both a sale and a delivery event before calculating stock level
        var amountSold = cumSum(Shop.Sale::getAmountSold).defaultVal(0);
//        var amountSola = select(Shop.Sale::getAmountSold).map(cumSum()).defaultVal(0);
//        var amountSolb = select(Shop.Sale.class).get(Shop.Sale::getAmountSold).map(cumSum()).defaultVal(0);
        var amountDelivered = cumSum(Shop.Delivery::getAmountDelivered).defaultVal(0);
        var stockLevel = subtract(amountDelivered, amountSold);
        
        //calculate sales receipt, default price is 25 - notifierOverride forces a 
        //calculate/notify when a sale event is received, price changes do not trigger child nodes
        var saleReceipt = multiply(Shop.Sale::getAmountSold, defaultVal(25, Shop.Price::getCustomerPrice))
            .notifierOverride(amountSold);
        var salesTurnover = cumSum(saleReceipt).defaultVal(0).log("sales receipt:", Number::intValue);
        
        //calculate stock cost, default cost is 15 - only calculate/notify when there is a delivery, price changes only affect new deliveries
        var deliveryBill = multiply(Shop.Delivery::getAmountDelivered, defaultVal(15, Shop.ItemCost::getAmount))
            .notifierOverride(amountDelivered);
        var stockCost = cumSum(deliveryBill).defaultVal(0).log("stock cost:", Number::intValue);
        
        //profit
        var profit = subtract(salesTurnover, stockCost);
        
        //add a reset for amountSold and amountDelivered - just match a string
        var resetSignal = filter("reset"::equalsIgnoreCase);
        amountSold.resetNoPublish(resetSignal);
        amountDelivered.resetNoPublish(resetSignal);
        
        //logging
        log("amount sold:{} amount deilvered:{} stockLevel:{} sales turnover:{} stock cost:{} profit:{}",
            argInt(amountSold),
            argInt(amountDelivered),
            argInt(stockLevel),
            argInt(salesTurnover),
            argInt(stockCost),
            argInt(profit)
        );
        
        //warning log, the filter is a trigger to log a warning when stock level is < 10
        log("Warning low stock level order more items, current level:", stockLevel.filter(lt(10)),
            argInt(stockLevel)
        );
    }

}
