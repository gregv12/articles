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
import static com.fluxtion.articles.fxportfolio.shared.UtilFunctions.round;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Order {

    private CcyPair ccyPair;
    private double termsAmount;
    private double executed;
    private double baseAmount;
    private Side side;
    private String orderId;
    private boolean cancelled = false;

    public static Order buildOrder(CcyPair ccyPair, Ccy dealtCcy, double amount, double rate, IdGenerator idGenerator) {
        Order order = new Order();
        amount = round(amount,2);
        order.ccyPair = ccyPair;
        order.orderId = idGenerator.getNextId();
        if (dealtCcy == ccyPair.terms) {
            order.termsAmount = Math.abs(amount);
            order.baseAmount = round(order.termsAmount * rate, 2);
            order.side = amount > 0 ? Side.BUY : Side.SELL;
        } else if (dealtCcy == ccyPair.base) {
            order.baseAmount = Math.abs(amount);
            order.termsAmount = round(order.baseAmount / rate, 2);
            order.side = amount < 0 ? Side.BUY : Side.SELL;
        } else {
            throw new IllegalArgumentException("Cannot convert to Ccypair order, ccy:" + dealtCcy + " is neither base nor terms in:" + ccyPair);
        }
        
        return order;
    }
    
    /**
     * always in terms adds to the amount executed
     * @param executed 
     */
    public void executed(double executed){
        this.executed += Math.abs(executed);
    }
    
    public boolean isComplete(){
        return cancelled || (executed >= termsAmount);
    }
    
    public double getOpenPosForCcy(Ccy ccy, double rate){
        if(isComplete()){
            return 0;
        }
        double pos = termsAmount - executed;
        if (ccy == ccyPair.terms) {
            int multiplier = side==Side.BUY? 1 : -1;
            pos *= multiplier;

        } else if (ccy == ccyPair.base) {
            pos *= rate;
            int multiplier = side==Side.SELL? 1 : -1;
            pos *= multiplier;
        }
        return round(pos, 2);
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

    public String getOrderId() {
        return orderId;
    }
    
    public double getRemaining(){
        return isComplete()?0:round(termsAmount - executed, 2);
    }
    
    public void cancel(){
        cancelled = true;
    }
    
    public CharSequence toYaml(){
        return "{" + toString() + "}";
    }
    
    @Override
    public String toString() {
        return "Order: {" 
                + "orderId:" + orderId 
                + ", ccyPair:" + ccyPair.name 
                + ", cancelled:" + cancelled 
                + ", isComplete:" + isComplete()
                + ", termsAmount:" + termsAmount 
                + ", baseAmount:" + baseAmount 
                + ", side:" + side 
                + '}';
    }
    
}
