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
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.articles.fxportfolio.shared.Order;

/**
 *
 * @author V12 Technology Ltd.
 */
public class PairHedge extends EventLogNode implements PairPosition{
    
    private final String ccyPairStr;
    private final transient CcyPair ccyPair;
    @Inject(singleton = true, singletonName = "idGenerator")
    @NoEventReference
    public IdGenerator idGenerator;
    @Inject(singleton = true, singletonName = "orderManager")
    @NoEventReference
    public OrderManager orderManager;
    private Order order;
    private boolean newOrderPlaced;
    private double rate = 1.5; 

    public PairHedge(String ccyPair) {
        this.ccyPairStr = ccyPair;
        this.ccyPair = new CcyPair(ccyPair);
        idGenerator = null;
    }

    @Override
    public CcyPair ccyPair() {
        return ccyPair;
    }

//    @EventHandler
//    @EventHandler(filterVariable = "ccyPairStr")
    public boolean trade(Trade trade) {
        boolean matches = trade.getCcyPair().equals(ccyPair);
        
        log.debug("tradeCcyPair", trade.getCcyPair().name);
        log.debug("matchCcyPair", matches);
        log.debug("myCcyPair", ccyPair.name);
        return matches;
    }
    
    public void hedge(Ccy ccy, double amount){
        log.info("hedgeCcy", ccy.name());
        log.info("hedgeAmount", amount);
        order = Order.buildOrder(ccyPair, ccy, -1*amount, rate, idGenerator);
        newOrderPlaced = true;
        log.info("hedgeOrder", order);
    }
    
    @EventHandler
    public boolean rate(Rate trade) {
        return false;
    }
    
    @OnEvent
    public boolean recalculateOpenPositions(){
        boolean update = newOrderPlaced;
        newOrderPlaced = false;
        log.info("updated", update);
        return update;
    }
    

    @Initialise
    public void init(){
        newOrderPlaced = false;
    }
}
