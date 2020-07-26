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
package com.fluxtion.articles.fxportfolio.event;

import com.fluxtion.api.event.Event;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
@Data
public class Trade implements Event{
    
    private final CcyPair ccyPair;
    private double termsAmout;
    private double baseAmout;
    private String orderId;
    
    public Trade(String ccyPair, double terms, double base){
        this.ccyPair = CcyPair.from(ccyPair);
        this.termsAmout = terms;
        this.baseAmout = base;
    }
    
    public Trade(String ccyPair, double terms, double base, String orderId){
        this.ccyPair = CcyPair.from(ccyPair);
        this.termsAmout = terms;
        this.baseAmout = base;
        this.orderId = orderId;
    }
    
    public Trade(CcyPair ccyPair, double terms, double base){
        this.ccyPair = ccyPair;
        this.termsAmout = terms;
        this.baseAmout = base;
    }
    
    public double amountForCcy(Ccy ccy){
        double amount = 0;
        if(ccyPair.terms == ccy){
            amount = termsAmout;
        }else if(ccyPair.base == ccy){
            amount = baseAmout;
        }
        return amount;
    }

    @Override
    public String filterString() {
        return ccyPair.name;
    }
    
    @Override
    public String toString() {
        return "Trade: {" + "ccyPair: " + ccyPair.name + ", termsAmout: " + termsAmout + ", baseAmout: " + baseAmout + '}';
    }
    
}
