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
package com.fluxtion.articles.lambdaserialiser.fluxtion.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.lambdaserialiser.fluxtion.ProcessBuilder.TrafficCount;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;

public class TrafficProcessor implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Count count_2 = new Count();
  private final IntFilterEventHandler handlerTrafficCount =
      new IntFilterEventHandler(2147483647, TrafficCount.class);
  private final LambdaFunction lambdaFunction_0 = new LambdaFunction("lambda$build$ca54918b$1_0");
  private final Filter_TrafficCount_By_apply0 filter_TrafficCount_By_apply0_1 =
      new Filter_TrafficCount_By_apply0();
  private final Map_TrafficCount_With_increment0 map_TrafficCount_With_increment0_3 =
      new Map_TrafficCount_With_increment0();
  //Dirty flags
  private boolean isDirty_filter_TrafficCount_By_apply0_1 = false;
  private boolean isDirty_handlerTrafficCount = false;
  //Filter constants

  public TrafficProcessor() {
    filter_TrafficCount_By_apply0_1.setAlwaysReset(false);
    filter_TrafficCount_By_apply0_1.setNotifyOnChangeOnly(false);
    filter_TrafficCount_By_apply0_1.setResetImmediate(true);
    filter_TrafficCount_By_apply0_1.filterSubject = handlerTrafficCount;
    filter_TrafficCount_By_apply0_1.source_0 = handlerTrafficCount;
    filter_TrafficCount_By_apply0_1.f = lambdaFunction_0;
    map_TrafficCount_With_increment0_3.setAlwaysReset(false);
    map_TrafficCount_With_increment0_3.setNotifyOnChangeOnly(false);
    map_TrafficCount_With_increment0_3.setResetImmediate(true);
    map_TrafficCount_With_increment0_3.filterSubject = filter_TrafficCount_By_apply0_1;
    map_TrafficCount_With_increment0_3.f = count_2;
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.articles.lambdaserialiser.fluxtion.ProcessBuilder$TrafficCount"):
        {
          TrafficCount typedEvent = (TrafficCount) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(TrafficCount typedEvent) {
    //Default, no filter methods
    isDirty_handlerTrafficCount = true;
    handlerTrafficCount.onEvent(typedEvent);
    if (isDirty_handlerTrafficCount) {
      isDirty_filter_TrafficCount_By_apply0_1 = filter_TrafficCount_By_apply0_1.onEvent();
    }
    if (isDirty_filter_TrafficCount_By_apply0_1) {
      map_TrafficCount_With_increment0_3.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    map_TrafficCount_With_increment0_3.resetAfterEvent();
    filter_TrafficCount_By_apply0_1.resetAfterEvent();
    isDirty_filter_TrafficCount_By_apply0_1 = false;
    isDirty_handlerTrafficCount = false;
  }

  @Override
  public void init() {
    lambdaFunction_0.init();
    filter_TrafficCount_By_apply0_1.init();
    map_TrafficCount_With_increment0_3.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
