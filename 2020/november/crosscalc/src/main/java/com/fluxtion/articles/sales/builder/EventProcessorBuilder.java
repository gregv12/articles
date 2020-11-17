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
import static com.fluxtion.ext.streaming.api.stream.Argument.arg;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.lt;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.cumSum;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.multiply;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.subtract;
import static com.fluxtion.ext.streaming.builder.log.LogBuilder.log;
import static com.fluxtion.ext.streaming.builder.factory.DefaultNumberBuilder.defaultNum;

/**
 *
 * @author V12 Technology Ltd.
 */
public class EventProcessorBuilder {

    @SepBuilder(name = "StockProcessor", packageName = "com.fluxtion.articles.sales.generated.fluxtion_annotated")
    public void buildGraphAnnotated(SEPConfig cfg) {
        var amountSold = defaultNum(0, cumSum(Shop.Sale::getAmountSold));
        var amountDelivered = defaultNum(0, cumSum(Shop.Delivery::getAmountDelivered));
        var stockLevel = subtract(amountDelivered, amountSold);
        //calculate sales receipt, default price is 25 - on;y calculate when a sale event is received
        var saleReceipt = multiply(Shop.Sale::getAmountSold, defaultNum(25, Shop.Price::getCustomerPrice))
                .notifierOverride(amountSold);
        var salesTurnover = defaultNum(0, cumSum(saleReceipt));
        //calculate stock cost
        var deliveryBill = multiply(Shop.Delivery::getAmountDelivered, defaultNum(15, Shop.ItemCost::getAmount))
                .notifierOverride(amountDelivered);
        var stockCost = defaultNum(0, cumSum(deliveryBill));
        //profit
        var profit = subtract(salesTurnover, stockCost);
        //logging
        log("amount sold:{} amount deilvered:{} stockLevel:{} sales turnover:{} stock cost:{} profit:{}",
                arg(amountSold, Number::intValue),
                arg(amountDelivered, Number::intValue),
                arg(stockLevel, Number::intValue),
                arg(salesTurnover, Number::intValue),
                arg(stockCost, Number::intValue),
                arg(profit, Number::intValue)
        );
        //warning log, the filter is a trigger to log a warning
        log("Warning low stock level order more items, current level:", stockLevel.filter(lt(10)),
                arg(stockLevel, Number::intValue)
        );
    }

}
