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
package com.fluxtion.articles.fxportfolio.nodes;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.articles.fxportfolio.shared.EventSink;
import com.fluxtion.articles.fxportfolio.shared.Order;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.REGISTER_ORDER_PUBLISHER;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class OrderManager extends EventLogNode {

    @Inject(singleton = true, singletonName = "idGenerator")
    @NoEventReference
    public IdGenerator idGenerator;
    public Map<String, Order> openOrders = new HashMap<>();
    private EventSink orderSink;

    @EventHandler(propagate = false)
    public void trade(Trade trade) {
        log.info();
        Order order = openOrders.get(trade.getOrderId());
        if (order != null) {
            order.executed(trade.getTermsAmout());
            if (order.isComplete()) {
                log.info("action", "removeOrder");
                log.info("orderId", order.getOrderId());
                openOrders.remove(trade.getOrderId());
            }
        } else {
            log.info("action", "none");
        }
    }
    
    @EventHandler(filterString = REGISTER_ORDER_PUBLISHER, propagate = false)
    public void registerPublisher(Signal<EventSink> sink){
        if(sink.getValue()!=null){
            orderSink = sink.getValue();
        }
    }

    public Order placeorder(CcyPair ccyPair, Ccy dealtCcy, double amount, double rate) {
        final Order newOrder = Order.buildOrder(ccyPair, dealtCcy, amount, rate, idGenerator);
        log.info("newOrder", true);
        log.info("order", newOrder.toYaml());
        openOrders.put(newOrder.getOrderId(), newOrder);
        orderSink.handleEvent(newOrder);
        return newOrder;
    }


    public void cancelOrder(Order order) {
        if (order != null) {
            order.cancel();
            String orderId = order.getOrderId();
            log.info("cancelOrder", orderId);
            openOrders.remove(orderId);
            orderSink.handleEvent(order);
        }
    }
    
    @Initialise
    public void init(){
        orderSink = (o) -> {};
    }
}
