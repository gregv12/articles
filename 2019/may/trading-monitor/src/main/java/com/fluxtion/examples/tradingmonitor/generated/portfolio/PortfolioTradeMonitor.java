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
package com.fluxtion.examples.tradingmonitor.generated.portfolio;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.examples.tradingmonitor.AssetTradePos;
import com.fluxtion.examples.tradingmonitor.PortfolioTradePos;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.stream.NodeWrapper;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;
import com.fluxtion.ext.streaming.api.stream.StreamOperator.ConsoleLog;
import com.fluxtion.ext.streaming.api.test.BooleanFilter;
import com.fluxtion.ext.text.api.event.EofEvent;

public class PortfolioTradeMonitor implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Count count_4 = new Count();
  private final ReusableEventHandler handlerEofEvent =
      new ReusableEventHandler(2147483647, EofEvent.class);
  private final NumericPredicates numericPredicates_2 = new NumericPredicates();
  public final PortfolioTradePos portfolio = new PortfolioTradePos();
  private final NodeWrapper nodeWrapper_1 = new NodeWrapper(portfolio);
  private final Filter_getPnl_By_lessThan0 filter_getPnl_By_lessThan0_3 =
      new Filter_getPnl_By_lessThan0();
  private final Map_PortfolioTradePos_With_increment0 map_PortfolioTradePos_With_increment0_5 =
      new Map_PortfolioTradePos_With_increment0();
  private final BooleanFilter booleanFilter_6 =
      new BooleanFilter(map_PortfolioTradePos_With_increment0_5, handlerEofEvent);
  public final ConsoleLog consoleMsg_1 =
      new ConsoleLog(booleanFilter_6, "portfolio loss gt 1,000 breach count:");
  //Dirty flags
  private boolean isDirty_booleanFilter_6 = false;
  private boolean isDirty_filter_getPnl_By_lessThan0_3 = false;
  private boolean isDirty_handlerEofEvent = false;
  private boolean isDirty_nodeWrapper_1 = false;
  private boolean isDirty_portfolio = false;
  //Filter constants

  public PortfolioTradeMonitor() {
    filter_getPnl_By_lessThan0_3.setAlwaysReset(false);
    filter_getPnl_By_lessThan0_3.setNotifyOnChangeOnly(false);
    filter_getPnl_By_lessThan0_3.setResetImmediate(true);
    filter_getPnl_By_lessThan0_3.filterSubject = nodeWrapper_1;
    filter_getPnl_By_lessThan0_3.source_0 = nodeWrapper_1;
    filter_getPnl_By_lessThan0_3.f = numericPredicates_2;
    map_PortfolioTradePos_With_increment0_5.setAlwaysReset(false);
    map_PortfolioTradePos_With_increment0_5.setNotifyOnChangeOnly(false);
    map_PortfolioTradePos_With_increment0_5.setResetImmediate(true);
    map_PortfolioTradePos_With_increment0_5.filterSubject = filter_getPnl_By_lessThan0_3;
    map_PortfolioTradePos_With_increment0_5.f = count_4;
    numericPredicates_2.doubleLimit_0 = (double) -1000.0;
    numericPredicates_2.doubleLimit_1 = (double) Double.NaN;
    consoleMsg_1.setMethodSupplier("doubleValue");
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.eventId()) {
      case (EofEvent.ID):
        {
          EofEvent typedEvent = (EofEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
    switch (event.getClass().getName()) {
      case ("com.fluxtion.examples.tradingmonitor.AssetTradePos"):
        {
          AssetTradePos typedEvent = (AssetTradePos) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(AssetTradePos typedEvent) {
    //Default, no filter methods
    isDirty_portfolio = true;
    portfolio.positionUpdate(typedEvent);
    if (isDirty_portfolio) {
      isDirty_nodeWrapper_1 = nodeWrapper_1.onEvent();
    }
    if (isDirty_nodeWrapper_1) {
      isDirty_filter_getPnl_By_lessThan0_3 = filter_getPnl_By_lessThan0_3.onEvent();
    }
    if (isDirty_filter_getPnl_By_lessThan0_3) {
      map_PortfolioTradePos_With_increment0_5.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerEofEvent = true;
    handlerEofEvent.onEvent(typedEvent);
    if (isDirty_handlerEofEvent) {
      isDirty_booleanFilter_6 = booleanFilter_6.updated();
    }
    if (isDirty_booleanFilter_6) {
      consoleMsg_1.log();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    map_PortfolioTradePos_With_increment0_5.resetAfterEvent();
    filter_getPnl_By_lessThan0_3.resetAfterEvent();
    isDirty_booleanFilter_6 = false;
    isDirty_filter_getPnl_By_lessThan0_3 = false;
    isDirty_handlerEofEvent = false;
    isDirty_nodeWrapper_1 = false;
    isDirty_portfolio = false;
  }

  @Override
  public void init() {
    portfolio.init();
    filter_getPnl_By_lessThan0_3.init();
    map_PortfolioTradePos_With_increment0_5.init();
    consoleMsg_1.init();
  }

  @Override
  public void tearDown() {
    portfolio.publishReport();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
