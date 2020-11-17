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
package com.fluxtion.articles.crosscalc.generated.fluxtion_compound;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.crosscalc.CompoundBreachCalc;
import com.fluxtion.articles.crosscalc.MarketTick;

/*
 * <pre>
 * generation time   : 2020-11-09T14:49:54.578906800
 * generator version : 2.7.3
 * api version       : 2.7.3
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class TradeExecutor implements StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  public final CompoundBreachCalc crossBreaachEURUSD =
      new CompoundBreachCalc("EURUSD", "EURCHF", "USDCHF");
  //Dirty flags

  //Filter constants

  public TradeExecutor() {
    crossBreaachEURUSD.setBreachThreshold(0.5);
  }

  @Override
  public void onEvent(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.api.event.Signal"):
        {
          Signal typedEvent = (Signal) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.crosscalc.MarketTick"):
        {
          MarketTick typedEvent = (MarketTick) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(Signal typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.api.event.Signal] filterString:[SIGNAL_SAMPLE_CONFIG]
      case ("SIGNAL_SAMPLE_CONFIG"):
        crossBreaachEURUSD.updateConfig(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(MarketTick typedEvent) {
    //Default, no filter methods
    crossBreaachEURUSD.marketUpdate(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  private void afterEvent() {}

  @Override
  public void init() {
    crossBreaachEURUSD.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
