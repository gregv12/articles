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
package com.fluxtion.articles.quickstart.tempFilter.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamOperator.ConsoleLog;

public class TempFilter implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final ReusableEventHandler handlerTempEvent =
      new ReusableEventHandler(2147483647, TempEvent.class);
  private final LambdaFunction lambdaFunction_0 =
      new LambdaFunction("lambda$buildLambda$ca54918b$1_0");
  private final Filter_TempEvent_By_apply0 filter_TempEvent_By_apply0_1 =
      new Filter_TempEvent_By_apply0();
  public final ConsoleLog consoleMsg_1 = new ConsoleLog(filter_TempEvent_By_apply0_1, "Too hot!! ");
  //Dirty flags
  private boolean isDirty_consoleMsg_1 = false;
  private boolean isDirty_filter_TempEvent_By_apply0_1 = false;
  private boolean isDirty_handlerTempEvent = false;
  //Filter constants

  public TempFilter() {
    filter_TempEvent_By_apply0_1.setAlwaysReset(false);
    filter_TempEvent_By_apply0_1.setNotifyOnChangeOnly(false);
    filter_TempEvent_By_apply0_1.setResetImmediate(true);
    filter_TempEvent_By_apply0_1.filterSubject = handlerTempEvent;
    filter_TempEvent_By_apply0_1.source_0 = handlerTempEvent;
    filter_TempEvent_By_apply0_1.f = lambdaFunction_0;
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.articles.quickstart.tempmonitor.Events$TempEvent"):
        {
          TempEvent typedEvent = (TempEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(TempEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerTempEvent = true;
    handlerTempEvent.onEvent(typedEvent);
    if (isDirty_handlerTempEvent) {
      isDirty_filter_TempEvent_By_apply0_1 = filter_TempEvent_By_apply0_1.onEvent();
    }
    if (isDirty_filter_TempEvent_By_apply0_1) {
      isDirty_consoleMsg_1 = consoleMsg_1.log();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    filter_TempEvent_By_apply0_1.resetAfterEvent();
    isDirty_consoleMsg_1 = false;
    isDirty_filter_TempEvent_By_apply0_1 = false;
    isDirty_handlerTempEvent = false;
  }

  @Override
  public void init() {
    lambdaFunction_0.init();
    filter_TempEvent_By_apply0_1.init();
    consoleMsg_1.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
