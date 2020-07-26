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
package com.fluxtion.articles.fxportfolio.shared;

import com.fluxtion.articles.fxportfolio.nodes.IdGenerator;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Order {

    private CcyPair ccyPair;
    private double termsAmount;
    private double baseAmount;
    private Side side;
    private String orderId;

    public static Order buildOrder(CcyPair ccyPair, Ccy dealtCcy, double amount, double rate, IdGenerator idGenerator) {
        Order order = new Order();
        order.ccyPair = ccyPair;
        order.orderId = idGenerator.getNextId();
        if (dealtCcy == ccyPair.terms) {
            order.termsAmount = Math.abs(amount);
            order.baseAmount = order.termsAmount * rate;
            order.side = amount > 0 ? Side.BUY : Side.SELL;
        } else if (dealtCcy == ccyPair.base) {
            order.baseAmount = Math.abs(amount);
            order.termsAmount = order.baseAmount / rate;
            order.side = amount < 0 ? Side.BUY : Side.SELL;
        } else {
            throw new IllegalArgumentException("Cannot convert to Ccypair order, ccy:" + dealtCcy + " is neither base nor terms in:" + ccyPair);
        }

        return order;
    }
    
    public double getPosForCcy(Ccy ccy){
        double pos = 0;
        if (ccy == ccyPair.terms) {
            pos = termsAmount;
            int multiplier = side==Side.BUY? 1 : -1;
            pos *= multiplier;

        } else if (ccy == ccyPair.base) {
            pos = baseAmount;
            int multiplier = side==Side.SELL? 1 : -1;
            pos *= multiplier;
        }
        return pos;
    }

    public CcyPair getCcyPair() {
        return ccyPair;
    }

    public double getTermsAmount() {
        return termsAmount;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "Order: {" 
                + "orderId:" + orderId 
                + ", ccyPair:" + ccyPair.name 
                + ", termsAmount:" + termsAmount 
                + ", baseAmount:" + baseAmount 
                + ", side:" + side 
                + '}';
    }
    
}
