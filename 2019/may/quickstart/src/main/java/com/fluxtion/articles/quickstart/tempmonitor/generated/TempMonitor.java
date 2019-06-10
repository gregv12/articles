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
package com.fluxtion.articles.quickstart.tempmonitor.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.quickstart.tempmonitor.EnvironmentalController;
import com.fluxtion.articles.quickstart.tempmonitor.Events.EndOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.StartOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.log.AsciiConsoleLogger;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.log.MsgBuilder;
import com.fluxtion.ext.streaming.api.stream.NumericPredicates;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Average;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Max;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Min;
import com.fluxtion.ext.streaming.api.test.BooleanFilter;

public class TempMonitor implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Average average_4 = new Average();
  private final ReusableEventHandler handlerEndOfDay =
      new ReusableEventHandler(2147483647, EndOfDay.class);
  private final ReusableEventHandler handlerStartOfDay =
      new ReusableEventHandler(2147483647, StartOfDay.class);
  private final ReusableEventHandler handlerTempEvent =
      new ReusableEventHandler(2147483647, TempEvent.class);
  private final Map_temp_By_addValue0 map_temp_By_addValue0_5 = new Map_temp_By_addValue0();
  private final BooleanFilter booleanFilter_6 =
      new BooleanFilter(map_temp_By_addValue0_5, handlerEndOfDay);
  private final Max max_0 = new Max();
  private final Map_temp_By_max0 map_temp_By_max0_1 = new Map_temp_By_max0();
  private final Min min_2 = new Min();
  private final Map_temp_By_min0 map_temp_By_min0_3 = new Map_temp_By_min0();
  private final MsgBuilder1 msgBuilder1_7 = new MsgBuilder1();
  private final MsgBuilder3 msgBuilder3_9 = new MsgBuilder3();
  private final MsgBuilder5 msgBuilder5_10 = new MsgBuilder5();
  private final MsgBuilder7 msgBuilder7_11 = new MsgBuilder7();
  private final AsciiConsoleLogger asciiConsoleLogger_8 = new AsciiConsoleLogger();
  private final NumericPredicates numericPredicates_12 = new NumericPredicates();
  private final Filter_temp_By_greaterThan0 filter_temp_By_greaterThan0_13 =
      new Filter_temp_By_greaterThan0();
  private final NumericPredicates numericPredicates_16 = new NumericPredicates();
  private final Filter_temp_By_lessThan0 filter_temp_By_lessThan0_17 =
      new Filter_temp_By_lessThan0();
  private final NumericPredicates numericPredicates_19 = new NumericPredicates();
  private final Filter_temp_By_inRange0 filter_temp_By_inRange0_20 = new Filter_temp_By_inRange0();
  private final Push_TempEvent_To_airConAndHeatingOff0 push_TempEvent_To_airConAndHeatingOff0_21 =
      new Push_TempEvent_To_airConAndHeatingOff0();
  private final Push_TempEvent_To_airConOn0 push_TempEvent_To_airConOn0_15 =
      new Push_TempEvent_To_airConOn0();
  private final Push_TempEvent_To_heatingOn0 push_TempEvent_To_heatingOn0_18 =
      new Push_TempEvent_To_heatingOn0();
  private final EnvironmentalController environmentalController_14 = new EnvironmentalController();
  //Dirty flags
  private boolean isDirty_asciiConsoleLogger_8 = false;
  private boolean isDirty_booleanFilter_6 = false;
  private boolean isDirty_filter_temp_By_greaterThan0_13 = false;
  private boolean isDirty_filter_temp_By_inRange0_20 = false;
  private boolean isDirty_filter_temp_By_lessThan0_17 = false;
  private boolean isDirty_handlerEndOfDay = false;
  private boolean isDirty_handlerStartOfDay = false;
  private boolean isDirty_handlerTempEvent = false;
  private boolean isDirty_map_temp_By_addValue0_5 = false;
  private boolean isDirty_map_temp_By_max0_1 = false;
  private boolean isDirty_map_temp_By_min0_3 = false;
  private boolean isDirty_msgBuilder1_7 = false;
  private boolean isDirty_msgBuilder3_9 = false;
  private boolean isDirty_msgBuilder5_10 = false;
  private boolean isDirty_msgBuilder7_11 = false;
  private boolean isDirty_push_TempEvent_To_airConAndHeatingOff0_21 = false;
  private boolean isDirty_push_TempEvent_To_airConOn0_15 = false;
  private boolean isDirty_push_TempEvent_To_heatingOn0_18 = false;
  //Filter constants

  public TempMonitor() {
    filter_temp_By_greaterThan0_13.setAlwaysReset(false);
    filter_temp_By_greaterThan0_13.setNotifyOnChangeOnly(true);
    filter_temp_By_greaterThan0_13.setResetImmediate(true);
    filter_temp_By_greaterThan0_13.filterSubject = handlerTempEvent;
    filter_temp_By_greaterThan0_13.source_0 = handlerTempEvent;
    filter_temp_By_greaterThan0_13.f = numericPredicates_12;
    filter_temp_By_inRange0_20.setAlwaysReset(false);
    filter_temp_By_inRange0_20.setNotifyOnChangeOnly(true);
    filter_temp_By_inRange0_20.setResetImmediate(true);
    filter_temp_By_inRange0_20.filterSubject = handlerTempEvent;
    filter_temp_By_inRange0_20.source_0 = handlerTempEvent;
    filter_temp_By_inRange0_20.f = numericPredicates_19;
    filter_temp_By_lessThan0_17.setAlwaysReset(false);
    filter_temp_By_lessThan0_17.setNotifyOnChangeOnly(true);
    filter_temp_By_lessThan0_17.setResetImmediate(true);
    filter_temp_By_lessThan0_17.filterSubject = handlerTempEvent;
    filter_temp_By_lessThan0_17.source_0 = handlerTempEvent;
    filter_temp_By_lessThan0_17.f = numericPredicates_16;
    map_temp_By_addValue0_5.setAlwaysReset(false);
    map_temp_By_addValue0_5.setNotifyOnChangeOnly(false);
    map_temp_By_addValue0_5.setResetImmediate(false);
    map_temp_By_addValue0_5.filterSubject = handlerTempEvent;
    map_temp_By_addValue0_5.f = average_4;
    map_temp_By_addValue0_5.resetNotifier = handlerEndOfDay;
    map_temp_By_max0_1.setAlwaysReset(false);
    map_temp_By_max0_1.setNotifyOnChangeOnly(true);
    map_temp_By_max0_1.setResetImmediate(true);
    map_temp_By_max0_1.filterSubject = handlerTempEvent;
    map_temp_By_max0_1.f = max_0;
    map_temp_By_max0_1.resetNotifier = handlerStartOfDay;
    map_temp_By_min0_3.setAlwaysReset(false);
    map_temp_By_min0_3.setNotifyOnChangeOnly(true);
    map_temp_By_min0_3.setResetImmediate(true);
    map_temp_By_min0_3.filterSubject = handlerTempEvent;
    map_temp_By_min0_3.f = min_2;
    map_temp_By_min0_3.resetNotifier = handlerStartOfDay;
    msgBuilder1_7.source_ReusableEventHandler_0 = handlerStartOfDay;
    msgBuilder1_7.logLevel = (int) 3;
    msgBuilder1_7.initCapacity = (int) 256;
    msgBuilder3_9.source_Map_temp_By_max0_2 = map_temp_By_max0_1;
    msgBuilder3_9.logLevel = (int) 3;
    msgBuilder3_9.initCapacity = (int) 256;
    msgBuilder5_10.source_Map_temp_By_min0_4 = map_temp_By_min0_3;
    msgBuilder5_10.logLevel = (int) 3;
    msgBuilder5_10.initCapacity = (int) 256;
    msgBuilder7_11.source_BooleanFilter_6 = booleanFilter_6;
    msgBuilder7_11.logLevel = (int) 3;
    msgBuilder7_11.initCapacity = (int) 256;
    push_TempEvent_To_airConAndHeatingOff0_21.filterSubject = filter_temp_By_inRange0_20;
    push_TempEvent_To_airConAndHeatingOff0_21.f = environmentalController_14;
    push_TempEvent_To_airConOn0_15.filterSubject = filter_temp_By_greaterThan0_13;
    push_TempEvent_To_airConOn0_15.f = environmentalController_14;
    push_TempEvent_To_heatingOn0_18.filterSubject = filter_temp_By_lessThan0_17;
    push_TempEvent_To_heatingOn0_18.f = environmentalController_14;
    asciiConsoleLogger_8.initCapacity = (int) 512;
    asciiConsoleLogger_8.msgBuilders = new MsgBuilder[4];
    asciiConsoleLogger_8.msgBuilders[0] = msgBuilder1_7;
    asciiConsoleLogger_8.msgBuilders[1] = msgBuilder3_9;
    asciiConsoleLogger_8.msgBuilders[2] = msgBuilder5_10;
    asciiConsoleLogger_8.msgBuilders[3] = msgBuilder7_11;
    numericPredicates_12.doubleLimit_0 = (double) 25.0;
    numericPredicates_12.doubleLimit_1 = (double) Double.NaN;
    numericPredicates_16.doubleLimit_0 = (double) 12.0;
    numericPredicates_16.doubleLimit_1 = (double) Double.NaN;
    numericPredicates_19.doubleLimit_0 = (double) 12.0;
    numericPredicates_19.doubleLimit_1 = (double) 25.0;
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
    }
    switch (event.getClass().getName()) {
      case ("com.fluxtion.articles.quickstart.tempmonitor.Events$EndOfDay"):
        {
          EndOfDay typedEvent = (EndOfDay) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.quickstart.tempmonitor.Events$StartOfDay"):
        {
          StartOfDay typedEvent = (StartOfDay) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.quickstart.tempmonitor.Events$TempEvent"):
        {
          TempEvent typedEvent = (TempEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(EndOfDay typedEvent) {
    //Default, no filter methods
    isDirty_handlerEndOfDay = true;
    handlerEndOfDay.onEvent(typedEvent);
    if (isDirty_handlerEndOfDay) {
      map_temp_By_addValue0_5.resetNotification(handlerEndOfDay);
    }
    if (isDirty_handlerEndOfDay) {
      isDirty_booleanFilter_6 = booleanFilter_6.updated();
    }
    if (isDirty_booleanFilter_6) {
      isDirty_msgBuilder7_11 = msgBuilder7_11.buildMessage();
      if (isDirty_msgBuilder7_11) {
        asciiConsoleLogger_8.publishMessage(msgBuilder7_11);
      }
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(StartOfDay typedEvent) {
    //Default, no filter methods
    isDirty_handlerStartOfDay = true;
    handlerStartOfDay.onEvent(typedEvent);
    if (isDirty_handlerStartOfDay) {
      map_temp_By_max0_1.resetNotification(handlerStartOfDay);
      map_temp_By_min0_3.resetNotification(handlerStartOfDay);
    }
    if (isDirty_handlerStartOfDay) {
      isDirty_msgBuilder1_7 = msgBuilder1_7.buildMessage();
      if (isDirty_msgBuilder1_7) {
        asciiConsoleLogger_8.publishMessage(msgBuilder1_7);
      }
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(TempEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerTempEvent = true;
    handlerTempEvent.onEvent(typedEvent);
    if (isDirty_handlerTempEvent) {
      map_temp_By_addValue0_5.updated_filterSubject(handlerTempEvent);
      map_temp_By_max0_1.updated_filterSubject(handlerTempEvent);
      map_temp_By_min0_3.updated_filterSubject(handlerTempEvent);
    }
    if (isDirty_handlerTempEvent) {
      isDirty_map_temp_By_addValue0_5 = map_temp_By_addValue0_5.onEvent();
    }
    if (isDirty_handlerTempEvent) {
      isDirty_map_temp_By_max0_1 = map_temp_By_max0_1.onEvent();
    }
    if (isDirty_handlerTempEvent) {
      isDirty_map_temp_By_min0_3 = map_temp_By_min0_3.onEvent();
    }
    if (isDirty_map_temp_By_max0_1) {
      isDirty_msgBuilder3_9 = msgBuilder3_9.buildMessage();
      if (isDirty_msgBuilder3_9) {
        asciiConsoleLogger_8.publishMessage(msgBuilder3_9);
      }
    }
    if (isDirty_map_temp_By_min0_3) {
      isDirty_msgBuilder5_10 = msgBuilder5_10.buildMessage();
      if (isDirty_msgBuilder5_10) {
        asciiConsoleLogger_8.publishMessage(msgBuilder5_10);
      }
    }
    if (isDirty_handlerTempEvent) {
      isDirty_filter_temp_By_greaterThan0_13 = filter_temp_By_greaterThan0_13.onEvent();
    }
    if (isDirty_handlerTempEvent) {
      isDirty_filter_temp_By_lessThan0_17 = filter_temp_By_lessThan0_17.onEvent();
    }
    if (isDirty_handlerTempEvent) {
      isDirty_filter_temp_By_inRange0_20 = filter_temp_By_inRange0_20.onEvent();
    }
    if (isDirty_filter_temp_By_inRange0_20) {
      isDirty_push_TempEvent_To_airConAndHeatingOff0_21 =
          push_TempEvent_To_airConAndHeatingOff0_21.onEvent();
    }
    if (isDirty_filter_temp_By_greaterThan0_13) {
      isDirty_push_TempEvent_To_airConOn0_15 = push_TempEvent_To_airConOn0_15.onEvent();
    }
    if (isDirty_filter_temp_By_lessThan0_17) {
      isDirty_push_TempEvent_To_heatingOn0_18 = push_TempEvent_To_heatingOn0_18.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        isDirty_msgBuilder1_7 = msgBuilder1_7.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder3_9 = msgBuilder3_9.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder5_10 = msgBuilder5_10.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder7_11 = msgBuilder7_11.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        isDirty_msgBuilder1_7 = msgBuilder1_7.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder3_9 = msgBuilder3_9.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder5_10 = msgBuilder5_10.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder7_11 = msgBuilder7_11.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_LEVEL]
      case ("RECORD_LEVEL"):
        isDirty_asciiConsoleLogger_8 = true;
        asciiConsoleLogger_8.controlLevelLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_NAME]
      case ("RECORD_NAME"):
        isDirty_asciiConsoleLogger_8 = true;
        asciiConsoleLogger_8.controlIdLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_TIME]
      case ("RECORD_TIME"):
        isDirty_asciiConsoleLogger_8 = true;
        asciiConsoleLogger_8.controlTimeLogging(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  @Override
  public void afterEvent() {
    filter_temp_By_inRange0_20.resetAfterEvent();
    filter_temp_By_lessThan0_17.resetAfterEvent();
    filter_temp_By_greaterThan0_13.resetAfterEvent();
    msgBuilder7_11.afterEvent();
    msgBuilder5_10.afterEvent();
    msgBuilder3_9.afterEvent();
    msgBuilder1_7.afterEvent();
    map_temp_By_min0_3.resetAfterEvent();
    map_temp_By_max0_1.resetAfterEvent();
    map_temp_By_addValue0_5.resetAfterEvent();
    isDirty_asciiConsoleLogger_8 = false;
    isDirty_booleanFilter_6 = false;
    isDirty_filter_temp_By_greaterThan0_13 = false;
    isDirty_filter_temp_By_inRange0_20 = false;
    isDirty_filter_temp_By_lessThan0_17 = false;
    isDirty_handlerEndOfDay = false;
    isDirty_handlerStartOfDay = false;
    isDirty_handlerTempEvent = false;
    isDirty_map_temp_By_addValue0_5 = false;
    isDirty_map_temp_By_max0_1 = false;
    isDirty_map_temp_By_min0_3 = false;
    isDirty_msgBuilder1_7 = false;
    isDirty_msgBuilder3_9 = false;
    isDirty_msgBuilder5_10 = false;
    isDirty_msgBuilder7_11 = false;
    isDirty_push_TempEvent_To_airConAndHeatingOff0_21 = false;
    isDirty_push_TempEvent_To_airConOn0_15 = false;
    isDirty_push_TempEvent_To_heatingOn0_18 = false;
  }

  @Override
  public void init() {
    map_temp_By_addValue0_5.init();
    map_temp_By_max0_1.init();
    map_temp_By_min0_3.init();
    msgBuilder1_7.init();
    msgBuilder3_9.init();
    msgBuilder5_10.init();
    msgBuilder7_11.init();
    asciiConsoleLogger_8.init();
    filter_temp_By_greaterThan0_13.init();
    filter_temp_By_lessThan0_17.init();
    filter_temp_By_inRange0_20.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
