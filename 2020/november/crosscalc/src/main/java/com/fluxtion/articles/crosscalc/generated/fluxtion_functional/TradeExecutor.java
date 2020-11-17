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
package com.fluxtion.articles.crosscalc.generated.fluxtion_functional;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.crosscalc.MarketTick;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;

/*
 * <pre>
 * generation time   : 2020-11-09T14:49:52.855908800
 * generator version : 2.7.3
 * api version       : 2.7.3
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class TradeExecutor implements StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  private final IntFilterEventHandler handlerMarketTick_ =
      new IntFilterEventHandler(2147483647, MarketTick.class);
  private final NumericPredicates numericPredicates_12 = new NumericPredicates();
  private final String string_0 = "EURUSD";
  private final Filter_getInstrument_By_equalsIgnoreCase0
      filter_getInstrument_By_equalsIgnoreCase0_1 = new Filter_getInstrument_By_equalsIgnoreCase0();
  public final GetField_MarketTick_double0 midEURUSD = new GetField_MarketTick_double0();
  private final String string_3 = "EURCHF";
  private final Filter_getInstrument_By_equalsIgnoreCase0
      filter_getInstrument_By_equalsIgnoreCase0_4 = new Filter_getInstrument_By_equalsIgnoreCase0();
  public final GetField_MarketTick_double0 midEURCHF = new GetField_MarketTick_double0();
  private final String string_6 = "USDCHF";
  private final Filter_getInstrument_By_equalsIgnoreCase0
      filter_getInstrument_By_equalsIgnoreCase0_7 = new Filter_getInstrument_By_equalsIgnoreCase0();
  public final GetField_MarketTick_double0 midUSDCHF = new GetField_MarketTick_double0();
  public final Map_doubleValue_With_multiply0 crossEURUSD = new Map_doubleValue_With_multiply0();
  public final Map_doubleValue_With_subtract0 crossDeltaEURUSD =
      new Map_doubleValue_With_subtract0();
  private final GetField_Number_double0 getField_Number_double0_11 = new GetField_Number_double0();
  public final Filter_Number_By_greaterThan0 crossBreaachEURUSD =
      new Filter_Number_By_greaterThan0();
  //Dirty flags
  private boolean isDirty_crossDeltaEURUSD = false;
  private boolean isDirty_crossEURUSD = false;
  private boolean isDirty_filter_getInstrument_By_equalsIgnoreCase0_1 = false;
  private boolean isDirty_filter_getInstrument_By_equalsIgnoreCase0_4 = false;
  private boolean isDirty_filter_getInstrument_By_equalsIgnoreCase0_7 = false;
  private boolean isDirty_getField_Number_double0_11 = false;
  private boolean isDirty_handlerMarketTick_ = false;
  private boolean isDirty_midEURCHF = false;
  private boolean isDirty_midEURUSD = false;
  private boolean isDirty_midUSDCHF = false;
  //Filter constants

  public TradeExecutor() {
    crossBreaachEURUSD.setAlwaysReset(false);
    crossBreaachEURUSD.setNotifyOnChangeOnly(false);
    crossBreaachEURUSD.setResetImmediate(true);
    crossBreaachEURUSD.setValidOnStart(false);
    crossBreaachEURUSD.filterSubject = getField_Number_double0_11;
    crossBreaachEURUSD.source_0 = getField_Number_double0_11;
    crossBreaachEURUSD.f = numericPredicates_12;
    filter_getInstrument_By_equalsIgnoreCase0_1.setAlwaysReset(false);
    filter_getInstrument_By_equalsIgnoreCase0_1.setNotifyOnChangeOnly(false);
    filter_getInstrument_By_equalsIgnoreCase0_1.setResetImmediate(true);
    filter_getInstrument_By_equalsIgnoreCase0_1.setValidOnStart(false);
    filter_getInstrument_By_equalsIgnoreCase0_1.filterSubject = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_1.source_0 = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_1.f = string_0;
    filter_getInstrument_By_equalsIgnoreCase0_4.setAlwaysReset(false);
    filter_getInstrument_By_equalsIgnoreCase0_4.setNotifyOnChangeOnly(false);
    filter_getInstrument_By_equalsIgnoreCase0_4.setResetImmediate(true);
    filter_getInstrument_By_equalsIgnoreCase0_4.setValidOnStart(false);
    filter_getInstrument_By_equalsIgnoreCase0_4.filterSubject = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_4.source_0 = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_4.f = string_3;
    filter_getInstrument_By_equalsIgnoreCase0_7.setAlwaysReset(false);
    filter_getInstrument_By_equalsIgnoreCase0_7.setNotifyOnChangeOnly(false);
    filter_getInstrument_By_equalsIgnoreCase0_7.setResetImmediate(true);
    filter_getInstrument_By_equalsIgnoreCase0_7.setValidOnStart(false);
    filter_getInstrument_By_equalsIgnoreCase0_7.filterSubject = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_7.source_0 = handlerMarketTick_;
    filter_getInstrument_By_equalsIgnoreCase0_7.f = string_6;
    midEURCHF.setAlwaysReset(false);
    midEURCHF.setNotifyOnChangeOnly(false);
    midEURCHF.setResetImmediate(true);
    midEURCHF.setValidOnStart(false);
    midEURCHF.filterSubject = filter_getInstrument_By_equalsIgnoreCase0_4;
    midEURUSD.setAlwaysReset(false);
    midEURUSD.setNotifyOnChangeOnly(false);
    midEURUSD.setResetImmediate(true);
    midEURUSD.setValidOnStart(false);
    midEURUSD.filterSubject = filter_getInstrument_By_equalsIgnoreCase0_1;
    midUSDCHF.setAlwaysReset(false);
    midUSDCHF.setNotifyOnChangeOnly(false);
    midUSDCHF.setResetImmediate(true);
    midUSDCHF.setValidOnStart(false);
    midUSDCHF.filterSubject = filter_getInstrument_By_equalsIgnoreCase0_7;
    getField_Number_double0_11.setAlwaysReset(false);
    getField_Number_double0_11.setNotifyOnChangeOnly(false);
    getField_Number_double0_11.setResetImmediate(true);
    getField_Number_double0_11.setValidOnStart(false);
    getField_Number_double0_11.filterSubject = crossDeltaEURUSD;
    crossEURUSD.setAlwaysReset(false);
    crossEURUSD.setNotifyOnChangeOnly(false);
    crossEURUSD.setResetImmediate(true);
    crossEURUSD.setValidOnStart(false);
    crossEURUSD.filterSubject = midEURCHF;
    crossEURUSD.source_0 = midUSDCHF;
    crossDeltaEURUSD.setAlwaysReset(false);
    crossDeltaEURUSD.setNotifyOnChangeOnly(false);
    crossDeltaEURUSD.setResetImmediate(true);
    crossDeltaEURUSD.setValidOnStart(false);
    crossDeltaEURUSD.filterSubject = midEURUSD;
    crossDeltaEURUSD.source_0 = crossEURUSD;
    numericPredicates_12.doubleLimit_0 = (double) 0.5;
    numericPredicates_12.doubleLimit_1 = (double) Double.NaN;
  }

  @Override
  public void onEvent(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.articles.crosscalc.MarketTick"):
        {
          MarketTick typedEvent = (MarketTick) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(MarketTick typedEvent) {
    //Default, no filter methods
    isDirty_handlerMarketTick_ = true;
    handlerMarketTick_.onEvent(typedEvent);
    if (isDirty_handlerMarketTick_) {
      isDirty_filter_getInstrument_By_equalsIgnoreCase0_1 =
          filter_getInstrument_By_equalsIgnoreCase0_1.onEvent();
    }
    if (isDirty_filter_getInstrument_By_equalsIgnoreCase0_1) {
      isDirty_midEURUSD = midEURUSD.onEvent();
      if (isDirty_midEURUSD) {
        crossDeltaEURUSD.updated_filterSubject(midEURUSD);
      }
    }
    if (isDirty_handlerMarketTick_) {
      isDirty_filter_getInstrument_By_equalsIgnoreCase0_4 =
          filter_getInstrument_By_equalsIgnoreCase0_4.onEvent();
    }
    if (isDirty_filter_getInstrument_By_equalsIgnoreCase0_4) {
      isDirty_midEURCHF = midEURCHF.onEvent();
      if (isDirty_midEURCHF) {
        crossEURUSD.updated_filterSubject(midEURCHF);
      }
    }
    if (isDirty_handlerMarketTick_) {
      isDirty_filter_getInstrument_By_equalsIgnoreCase0_7 =
          filter_getInstrument_By_equalsIgnoreCase0_7.onEvent();
    }
    if (isDirty_filter_getInstrument_By_equalsIgnoreCase0_7) {
      isDirty_midUSDCHF = midUSDCHF.onEvent();
      if (isDirty_midUSDCHF) {
        crossEURUSD.updated_source_0(midUSDCHF);
      }
    }
    if (isDirty_midEURCHF | isDirty_midUSDCHF) {
      isDirty_crossEURUSD = crossEURUSD.onEvent();
      if (isDirty_crossEURUSD) {
        crossDeltaEURUSD.updated_source_0(crossEURUSD);
      }
    }
    if (isDirty_crossEURUSD | isDirty_midEURUSD) {
      isDirty_crossDeltaEURUSD = crossDeltaEURUSD.onEvent();
    }
    if (isDirty_crossDeltaEURUSD) {
      isDirty_getField_Number_double0_11 = getField_Number_double0_11.onEvent();
    }
    if (isDirty_getField_Number_double0_11) {
      crossBreaachEURUSD.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  private void afterEvent() {
    crossBreaachEURUSD.resetAfterEvent();
    filter_getInstrument_By_equalsIgnoreCase0_7.resetAfterEvent();
    filter_getInstrument_By_equalsIgnoreCase0_4.resetAfterEvent();
    filter_getInstrument_By_equalsIgnoreCase0_1.resetAfterEvent();
    isDirty_crossDeltaEURUSD = false;
    isDirty_crossEURUSD = false;
    isDirty_filter_getInstrument_By_equalsIgnoreCase0_1 = false;
    isDirty_filter_getInstrument_By_equalsIgnoreCase0_4 = false;
    isDirty_filter_getInstrument_By_equalsIgnoreCase0_7 = false;
    isDirty_getField_Number_double0_11 = false;
    isDirty_handlerMarketTick_ = false;
    isDirty_midEURCHF = false;
    isDirty_midEURUSD = false;
    isDirty_midUSDCHF = false;
  }

  @Override
  public void init() {
    filter_getInstrument_By_equalsIgnoreCase0_1.init();
    midEURUSD.init();
    filter_getInstrument_By_equalsIgnoreCase0_4.init();
    midEURCHF.init();
    filter_getInstrument_By_equalsIgnoreCase0_7.init();
    midUSDCHF.init();
    crossEURUSD.init();
    crossDeltaEURUSD.init();
    getField_Number_double0_11.init();
    crossBreaachEURUSD.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
