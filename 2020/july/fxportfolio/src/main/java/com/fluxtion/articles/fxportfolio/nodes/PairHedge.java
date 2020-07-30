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
package com.fluxtion.articles.fxportfolio.nodes;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.articles.fxportfolio.shared.Order;

/**
 *
 * @author V12 Technology Ltd.
 */
public class PairHedge extends EventLogNode implements PairPosition {

    private final String ccyPairStr;
    private final transient CcyPair ccyPair;
    @Inject(singleton = true, singletonName = "orderManager")
    @NoEventReference
    public OrderManager orderManager;
    private Order order;
    private double rate = 1.5;

    public PairHedge(String ccyPair) {
        this.ccyPairStr = ccyPair;
        this.ccyPair = new CcyPair(ccyPair);
    }

    @Override
    public CcyPair ccyPair() {
        return ccyPair;
    }

    public double getPosForCcy(Ccy ccy) {
        if (order == null || order.isComplete()) {
            return 0;
        }
        return order.getOpenPosForCcy(ccy, rate);
    }

    /**
     * Place a new hedge if the current hedge does not match the
     *
     * @param ccy
     * @param amount
     * @return status indicating a new hedge
     */
    public boolean hedge(Ccy ccy, double amount) {
        log.info("hedgeCcy", ccy.name());
        log.info("hedgeAmount", -1 *amount);
        if (amount == 0) {
            orderManager.cancelOrder(order);
        } else if (order == null) {
            order = orderManager.placeorder(ccyPair, ccy, -1 * amount, rate);
            log.info("hedgeOrderId", order.getOrderId());
        } else {
            double openPosForCcy = order.getOpenPosForCcy(ccy, rate);
            if (openPosForCcy != amount) {
                orderManager.cancelOrder(order);
            }
            order = orderManager.placeorder(ccyPair, ccy, -1 * amount, rate);
            log.info("hedgeOrderId", order.getOrderId());
        }
        return true;
    }

    @EventHandler(filterVariable = "ccyPairStr")
    public boolean rate(Rate rate) {
        log.info("rateUpdate", rate);
        this.rate = rate.value;
        return false;
    }

}
