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

import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.annotations.OnParentUpdate;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.CLOSE_POSITIONS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author V12 Technology Ltd.
 */
public class ManagedAsset extends EventLogNode {

    @PushReference
    public PairHedge hedger1;
    @PushReference
    public PairHedge hedger2;
    @PushReference
    public PairHedge hedger3;
    @PushReference
    public PairHedge hedger4;
    public List<PairPosition> openPositions = new ArrayList<>();
    public final Ccy currency;
    private double position;

    public ManagedAsset(Ccy currency) {
        this.currency = currency;
    }

    public double position() {
        return position;
    }

    @EventHandler
//    public void trade(Trade trade) {
    public boolean handleTrade(Trade trade) {
        boolean matches = trade.getCcyPair().containsCcy(currency);
        position += trade.amountForCcy(currency);
        log.debug("tradeCcyPair", trade.getCcyPair().name);
        log.debug("matchPosCcy", matches);
        if (matches) {
            log.info("tradeUpdate", true);
            log.info("position", position);
            if(hedger1!=null){
                hedger1.hedge(currency, position);
            }
        }
        //calculate open positions
        return matches;
    }

//    @OnParentUpdate("openPositions")
    public void openPositionChange(PairPosition position) {
        log.info("newOpenPosition" + currency, 0);
        log.info("hedger", position.ccyPair());
    }
    
    @EventHandler(filterVariable = "currency")
    public boolean updateHedgeLimits(LimitConfig cfg){
        return false;
    } 
    
    @EventHandler(filterVariable = "currency")
    public boolean updateHedgeRouteConfig(HedgeRouteConfig cfg){
        return false;
    } 
    
    @EventHandler(filterString = CLOSE_POSITIONS)
    public boolean closePosition(Signal signal){
        return false;
    }

    @OnEvent
    public boolean recalculateHedges() {
        log.info("positionRecalc", true);
        log.info("position", position);
        return true;
    }

}
