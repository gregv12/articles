/*
 * Copyright (C) 2018 V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio.generated;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.Lifecycle;

import com.fluxtion.api.audit.Auditor;
import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.api.event.Event;
import com.fluxtion.api.event.Signal;
import com.fluxtion.api.time.Clock;
import com.fluxtion.api.time.ClockStrategy.ClockStrategyEvent;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.nodes.IdGenerator;
import com.fluxtion.articles.fxportfolio.nodes.ManagedAsset;
import com.fluxtion.articles.fxportfolio.nodes.OrderManager;
import com.fluxtion.articles.fxportfolio.nodes.PairHedge;
import com.fluxtion.articles.fxportfolio.nodes.PortfolioPosition;
import com.fluxtion.articles.fxportfolio.shared.Ccy;

/*
 * <pre>
 * generation time   : 2021-01-23T17:23:24.197950600
 * generator version : 2.10.14
 * api version       : 2.10.14
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class PortfolioCalc implements StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  public final Clock clock = new Clock();
  public final EventLogManager eventLogger = new EventLogManager();
  private final IdGenerator idGenerator = new IdGenerator();
  private final OrderManager orderManager = new OrderManager();
  public final ManagedAsset positionCHF = new ManagedAsset(Ccy.CHF);
  public final PairHedge hedgeEURCHF = new PairHedge("EURCHF");
  public final PairHedge hedgeUSDCHF = new PairHedge("USDCHF");
  public final ManagedAsset positionJPY = new ManagedAsset(Ccy.JPY);
  public final PairHedge hedgeEURJPY = new PairHedge("EURJPY");
  public final PairHedge hedgeUSDJPY = new PairHedge("USDJPY");
  public final ManagedAsset positionEUR = new ManagedAsset(Ccy.EUR);
  public final PairHedge hedgeEURGBP = new PairHedge("EURGBP");
  public final PairHedge hedgeEURUSD = new PairHedge("EURUSD");
  public final ManagedAsset positionUSD = new ManagedAsset(Ccy.USD);
  public final PairHedge hedgeGBPUSD = new PairHedge("GBPUSD");
  public final ManagedAsset positionGBP = new ManagedAsset(Ccy.GBP);
  public final PortfolioPosition portfolioPosition = new PortfolioPosition();
  //Dirty flags
  private boolean isDirty_clock = false;
  private boolean isDirty_hedgeEURCHF = false;
  private boolean isDirty_hedgeEURGBP = false;
  private boolean isDirty_hedgeEURJPY = false;
  private boolean isDirty_hedgeEURUSD = false;
  private boolean isDirty_hedgeGBPUSD = false;
  private boolean isDirty_hedgeUSDCHF = false;
  private boolean isDirty_hedgeUSDJPY = false;
  private boolean isDirty_positionCHF = false;
  private boolean isDirty_positionEUR = false;
  private boolean isDirty_positionGBP = false;
  private boolean isDirty_positionJPY = false;
  private boolean isDirty_positionUSD = false;
  //Filter constants

  public PortfolioCalc() {
    eventLogger.trace = (boolean) false;
    eventLogger.printEventToString = (boolean) true;
    eventLogger.clock = clock;
    positionCHF.hedgeList.add(hedgeUSDCHF);
    positionCHF.hedgeList.add(hedgeEURCHF);
    positionCHF.orderManager = orderManager;
    positionEUR.hedgeList.add(hedgeEURGBP);
    positionEUR.hedgeList.add(hedgeEURUSD);
    positionEUR.openPositions.add(hedgeEURCHF);
    positionEUR.openPositions.add(hedgeEURJPY);
    positionEUR.orderManager = orderManager;
    positionGBP.openPositions.add(hedgeEURGBP);
    positionGBP.openPositions.add(hedgeGBPUSD);
    positionGBP.orderManager = orderManager;
    positionJPY.hedgeList.add(hedgeUSDJPY);
    positionJPY.hedgeList.add(hedgeEURJPY);
    positionJPY.orderManager = orderManager;
    positionUSD.hedgeList.add(hedgeGBPUSD);
    positionUSD.openPositions.add(hedgeUSDCHF);
    positionUSD.openPositions.add(hedgeUSDJPY);
    positionUSD.openPositions.add(hedgeEURUSD);
    positionUSD.orderManager = orderManager;
    orderManager.idGenerator = idGenerator;
    hedgeEURCHF.orderManager = orderManager;
    hedgeEURGBP.orderManager = orderManager;
    hedgeEURJPY.orderManager = orderManager;
    hedgeEURUSD.orderManager = orderManager;
    hedgeGBPUSD.orderManager = orderManager;
    hedgeUSDCHF.orderManager = orderManager;
    hedgeUSDJPY.orderManager = orderManager;
    portfolioPosition.assetList.add(positionCHF);
    portfolioPosition.assetList.add(positionUSD);
    portfolioPosition.assetList.add(positionEUR);
    portfolioPosition.assetList.add(positionGBP);
    portfolioPosition.assetList.add(positionJPY);
    //node auditors
    initialiseAuditor(eventLogger);
    initialiseAuditor(clock);
  }

  @Override
  public void onEvent(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.api.audit.EventLogControlEvent"):
        {
          EventLogControlEvent typedEvent = (EventLogControlEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.api.event.Signal"):
        {
          Signal typedEvent = (Signal) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.api.time.ClockStrategy$ClockStrategyEvent"):
        {
          ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig"):
        {
          HedgeRouteConfig typedEvent = (HedgeRouteConfig) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.fxportfolio.event.LimitConfig"):
        {
          LimitConfig typedEvent = (LimitConfig) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.fxportfolio.event.Rate"):
        {
          Rate typedEvent = (Rate) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.fxportfolio.event.Trade"):
        {
          Trade typedEvent = (Trade) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(EventLogControlEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    eventLogger.calculationLogConfig(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Signal typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[CLOSE_POSITIONS]
      case ("CLOSE_POSITIONS"):
        isDirty_positionCHF = positionCHF.closePosition(typedEvent);
        isDirty_positionJPY = positionJPY.closePosition(typedEvent);
        isDirty_positionEUR = positionEUR.closePosition(typedEvent);
        isDirty_positionUSD = positionUSD.closePosition(typedEvent);
        isDirty_positionGBP = positionGBP.closePosition(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[ORDER_ID]
      case ("ORDER_ID"):
        idGenerator.registerIdSource(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[PUBLISH_POSITIONS]
      case ("PUBLISH_POSITIONS"):
        portfolioPosition.publishPositions(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[PUBLISH_POSITIONS_STOP]
      case ("PUBLISH_POSITIONS_STOP"):
        portfolioPosition.publishPositionsStop(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[REGISTER_ORDER_PUBLISHER]
      case ("REGISTER_ORDER_PUBLISHER"):
        orderManager.registerPublisher(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_clock = true;
    clock.setClockStrategy(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(HedgeRouteConfig typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig] filterString:[CHF]
      case ("CHF"):
        isDirty_positionCHF = positionCHF.updateHedgeRouteConfig(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig] filterString:[EUR]
      case ("EUR"):
        isDirty_positionEUR = positionEUR.updateHedgeRouteConfig(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig] filterString:[GBP]
      case ("GBP"):
        isDirty_positionGBP = positionGBP.updateHedgeRouteConfig(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig] filterString:[JPY]
      case ("JPY"):
        isDirty_positionJPY = positionJPY.updateHedgeRouteConfig(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig] filterString:[USD]
      case ("USD"):
        isDirty_positionUSD = positionUSD.updateHedgeRouteConfig(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(LimitConfig typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_positionCHF = positionCHF.updateHedgeLimits(typedEvent);
    isDirty_positionJPY = positionJPY.updateHedgeLimits(typedEvent);
    isDirty_positionEUR = positionEUR.updateHedgeLimits(typedEvent);
    isDirty_positionUSD = positionUSD.updateHedgeLimits(typedEvent);
    isDirty_positionGBP = positionGBP.updateHedgeLimits(typedEvent);
    if (isDirty_positionCHF
        | isDirty_positionEUR
        | isDirty_positionGBP
        | isDirty_positionJPY
        | isDirty_positionUSD) {
      portfolioPosition.publish();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Rate typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[EURCHF]
      case ("EURCHF"):
        isDirty_hedgeEURCHF = hedgeEURCHF.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[EURGBP]
      case ("EURGBP"):
        isDirty_hedgeEURGBP = hedgeEURGBP.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[EURJPY]
      case ("EURJPY"):
        isDirty_hedgeEURJPY = hedgeEURJPY.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[EURUSD]
      case ("EURUSD"):
        isDirty_hedgeEURUSD = hedgeEURUSD.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[GBPUSD]
      case ("GBPUSD"):
        isDirty_hedgeGBPUSD = hedgeGBPUSD.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[USDCHF]
      case ("USDCHF"):
        isDirty_hedgeUSDCHF = hedgeUSDCHF.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.articles.fxportfolio.event.Rate] filterString:[USDJPY]
      case ("USDJPY"):
        isDirty_hedgeUSDJPY = hedgeUSDJPY.rate(typedEvent);
        if (isDirty_positionCHF
            | isDirty_positionEUR
            | isDirty_positionGBP
            | isDirty_positionJPY
            | isDirty_positionUSD) {
          portfolioPosition.publish();
        }
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(Trade typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    orderManager.trade(typedEvent);
    isDirty_positionCHF = positionCHF.handleTrade(typedEvent);
    isDirty_positionJPY = positionJPY.handleTrade(typedEvent);
    isDirty_positionEUR = positionEUR.handleTrade(typedEvent);
    isDirty_positionUSD = positionUSD.handleTrade(typedEvent);
    isDirty_positionGBP = positionGBP.handleTrade(typedEvent);
    if (isDirty_positionCHF
        | isDirty_positionEUR
        | isDirty_positionGBP
        | isDirty_positionJPY
        | isDirty_positionUSD) {
      portfolioPosition.publish();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    eventLogger.eventReceived(typedEvent);
    clock.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    eventLogger.eventReceived(typedEvent);
    clock.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(idGenerator, "idGenerator");
    auditor.nodeRegistered(positionCHF, "positionCHF");
    auditor.nodeRegistered(positionEUR, "positionEUR");
    auditor.nodeRegistered(positionGBP, "positionGBP");
    auditor.nodeRegistered(positionJPY, "positionJPY");
    auditor.nodeRegistered(positionUSD, "positionUSD");
    auditor.nodeRegistered(orderManager, "orderManager");
    auditor.nodeRegistered(hedgeEURCHF, "hedgeEURCHF");
    auditor.nodeRegistered(hedgeEURGBP, "hedgeEURGBP");
    auditor.nodeRegistered(hedgeEURJPY, "hedgeEURJPY");
    auditor.nodeRegistered(hedgeEURUSD, "hedgeEURUSD");
    auditor.nodeRegistered(hedgeGBPUSD, "hedgeGBPUSD");
    auditor.nodeRegistered(hedgeUSDCHF, "hedgeUSDCHF");
    auditor.nodeRegistered(hedgeUSDJPY, "hedgeUSDJPY");
    auditor.nodeRegistered(portfolioPosition, "portfolioPosition");
  }

  private void afterEvent() {
    eventLogger.processingComplete();
    clock.processingComplete();
    isDirty_clock = false;
    isDirty_hedgeEURCHF = false;
    isDirty_hedgeEURGBP = false;
    isDirty_hedgeEURJPY = false;
    isDirty_hedgeEURUSD = false;
    isDirty_hedgeGBPUSD = false;
    isDirty_hedgeUSDCHF = false;
    isDirty_hedgeUSDJPY = false;
    isDirty_positionCHF = false;
    isDirty_positionEUR = false;
    isDirty_positionGBP = false;
    isDirty_positionJPY = false;
    isDirty_positionUSD = false;
  }

  @Override
  public void init() {
    clock.init();
    orderManager.init();
    positionCHF.init();
    positionJPY.init();
    positionEUR.init();
    positionUSD.init();
    positionGBP.init();
    portfolioPosition.init();
  }

  @Override
  public void tearDown() {
    clock.tearDown();
    eventLogger.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
