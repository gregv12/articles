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
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.Inject;
import com.fluxtion.api.annotations.NoEventReference;
import com.fluxtion.api.annotations.PushReference;
import com.fluxtion.api.audit.EventLogNode;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import static com.fluxtion.articles.fxportfolio.shared.SignalKeys.CLOSE_POSITIONS;
import static com.fluxtion.articles.fxportfolio.shared.UtilFunctions.round;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author V12 Technology Ltd.
 */
public class ManagedAsset extends EventLogNode {

    public PairHedge currentHedger;
    @PushReference
    public List<PairHedge> hedgeList = new ArrayList<>();
    public List<PairPosition> openPositions = new ArrayList<>();
    public final Ccy currency;
    private double position;
    //above this value start hedging
    private int hedgingThreshold = 400;
    //threshold
    private int hedgingTarget = 50;
    @Inject(singleton = true, singletonName = "orderManager")
    @NoEventReference
    public OrderManager orderManager;

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
        position = round(position, 2);
        log.debug("tradeCcyPair", trade.getCcyPair().name);
        log.debug("matchPosCcy", matches);
        log.info("tradeUpdate", matches);
        hedgeCalc();
        return matches;
    }

    private boolean hedgeCalc() {
        boolean placedHedge = false;
        double openPos = round(openPositions.stream().mapToDouble(p -> p.getPosForCcy(currency)).sum(), 2);
        double hedgeOpenPos = round(hedgeList.stream().mapToDouble(p -> p.getPosForCcy(currency)).sum(), 2);
        double totalOpen = round(openPos + hedgeOpenPos, 2);
        boolean hedge = Math.abs((int) (totalOpen + position)) > hedgingThreshold;
        double hedgeAmount = round(openPos + position, 2);
        if (Math.abs(hedgeAmount) < hedgingTarget) {
            hedgeAmount = 0;
        }else{
            hedgeAmount += hedgeAmount < 1 ? hedgingTarget : -hedgingTarget;
        }
        log.info("position", position);
        log.info("openPosition", openPos);
        log.info("hedgeOpenPos", hedgeOpenPos);
        log.info("totalOpen", totalOpen);
        log.info("hedgingThreshold", hedgingThreshold);
        log.info("hedgingTarget", hedgingTarget);
        log.info("hedgeAmount", hedgeAmount);
        log.info("newHedge", hedge);
        log.info("hedgerAvailable", currentHedger != null);
        if (currentHedger != null && hedge) {
            currentHedger.hedge(currency, hedgeAmount);
            placedHedge = true;
        }
        return placedHedge;
    }

    @EventHandler
//    @EventHandler(filterVariable = "currency")
    public boolean updateHedgeLimits(LimitConfig cfg) {
        if(cfg.getCcy() == currency){
            log.info("limitUpdate", cfg.getLimit());
            hedgingThreshold = cfg.getLimit();
            hedgingTarget = cfg.getTarget();
            return hedgeCalc();
        }
        return false;
    }

    @EventHandler(filterVariable = "currency")
    public boolean updateHedgeRouteConfig(HedgeRouteConfig cfg) {
        return false;
    }

    @EventHandler(filterString = CLOSE_POSITIONS)
    public boolean closePosition(Signal signal) {
        return false;
    }


    @Initialise
    public void init() {
        if (hedgeList.size() > 0) {
            currentHedger = hedgeList.get(0);
        }
    }

}
