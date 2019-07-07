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
package com.fluxtion.articles.lombok.flight.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.util.GroupByPrint.PrintGroupByValues;
import com.fluxtion.ext.text.api.csv.Converters.IntConverter;
import com.fluxtion.ext.text.api.csv.ValidationLogSink;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;

public class FlightDelayAnalyser implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final ReusableEventHandler handlerEofEvent =
      new ReusableEventHandler(2147483647, EofEvent.class);
  private final IntConverter intConverter_0 = new IntConverter(-1);
  private final FlightDetailsCsvDecoder0 flightDetailsCsvDecoder0_1 =
      new FlightDetailsCsvDecoder0();
  private final Filter_getDelay_By_positiveInt0 filter_getDelay_By_positiveInt0_2 =
      new Filter_getDelay_By_positiveInt0();
  public final GroupBy_4 delayMap = new GroupBy_4();
  private final PrintGroupByValues printGroupByValues_4 =
      new PrintGroupByValues(
          "\nFlight delay analysis\n========================", delayMap, handlerEofEvent);
  private final ValidationLogger validationLogger_5 = new ValidationLogger("validationLog");
  private final ValidationLogSink validationLogSink_6 = new ValidationLogSink("validationLogSink");
  //Dirty flags
  private boolean isDirty_filter_getDelay_By_positiveInt0_2 = false;
  private boolean isDirty_flightDetailsCsvDecoder0_1 = false;
  private boolean isDirty_handlerEofEvent = false;
  //Filter constants

  public FlightDelayAnalyser() {
    filter_getDelay_By_positiveInt0_2.setAlwaysReset(false);
    filter_getDelay_By_positiveInt0_2.setNotifyOnChangeOnly(false);
    filter_getDelay_By_positiveInt0_2.setResetImmediate(true);
    filter_getDelay_By_positiveInt0_2.filterSubject = flightDetailsCsvDecoder0_1;
    filter_getDelay_By_positiveInt0_2.source_0 = flightDetailsCsvDecoder0_1;
    flightDetailsCsvDecoder0_1.errorLog = validationLogger_5;
    flightDetailsCsvDecoder0_1.intConverter_0 = intConverter_0;
    delayMap.filter_getDelay_By_positiveInt00 = filter_getDelay_By_positiveInt0_2;
    validationLogSink_6.setPublishLogImmediately(true);
    validationLogger_5.logSink = validationLogSink_6;
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.eventId()) {
      case (LogControlEvent.ID):
        {
          LogControlEvent typedEvent = (LogControlEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case (CharEvent.ID):
        {
          CharEvent typedEvent = (CharEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case (EofEvent.ID):
        {
          EofEvent typedEvent = (EofEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LOG_PROVIDER]
      case ("CHANGE_LOG_PROVIDER"):
        validationLogSink_6.controlLogProvider(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(CharEvent typedEvent) {
    //Default, no filter methods
    isDirty_flightDetailsCsvDecoder0_1 = flightDetailsCsvDecoder0_1.charEvent(typedEvent);
    if (isDirty_flightDetailsCsvDecoder0_1) {
      isDirty_filter_getDelay_By_positiveInt0_2 = filter_getDelay_By_positiveInt0_2.onEvent();
      if (isDirty_filter_getDelay_By_positiveInt0_2) {
        delayMap.updatefilter_getDelay_By_positiveInt00(filter_getDelay_By_positiveInt0_2);
      }
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerEofEvent = true;
    handlerEofEvent.onEvent(typedEvent);
    isDirty_flightDetailsCsvDecoder0_1 = flightDetailsCsvDecoder0_1.eof(typedEvent);
    if (isDirty_flightDetailsCsvDecoder0_1) {
      isDirty_filter_getDelay_By_positiveInt0_2 = filter_getDelay_By_positiveInt0_2.onEvent();
      if (isDirty_filter_getDelay_By_positiveInt0_2) {
        delayMap.updatefilter_getDelay_By_positiveInt00(filter_getDelay_By_positiveInt0_2);
      }
    }
    if (isDirty_handlerEofEvent) {
      printGroupByValues_4.printValues();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    filter_getDelay_By_positiveInt0_2.resetAfterEvent();
    isDirty_filter_getDelay_By_positiveInt0_2 = false;
    isDirty_flightDetailsCsvDecoder0_1 = false;
    isDirty_handlerEofEvent = false;
  }

  @Override
  public void init() {
    flightDetailsCsvDecoder0_1.init();
    filter_getDelay_By_positiveInt0_2.init();
    delayMap.init();
    validationLogSink_6.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
