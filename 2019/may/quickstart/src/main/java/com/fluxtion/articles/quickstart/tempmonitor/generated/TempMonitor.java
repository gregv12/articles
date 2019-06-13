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
  private final Average average_5 = new Average();
  private final ReusableEventHandler handlerEndOfDay =
      new ReusableEventHandler(2147483647, EndOfDay.class);
  private final ReusableEventHandler handlerStartOfDay =
      new ReusableEventHandler(2147483647, StartOfDay.class);
  private final ReusableEventHandler handlerTempEvent =
      new ReusableEventHandler(2147483647, TempEvent.class);
  private final Map_getTemp_By_asDouble0 map_getTemp_By_asDouble0_0 =
      new Map_getTemp_By_asDouble0();
  private final Map_Number_By_addValue0 map_Number_By_addValue0_6 = new Map_Number_By_addValue0();
  private final BooleanFilter booleanFilter_7 =
      new BooleanFilter(map_Number_By_addValue0_6, handlerEndOfDay);
  private final Max max_1 = new Max();
  private final Map_Number_By_max0 map_Number_By_max0_2 = new Map_Number_By_max0();
  private final Min min_3 = new Min();
  private final Map_Number_By_min0 map_Number_By_min0_4 = new Map_Number_By_min0();
  private final MsgBuilder4 msgBuilder4_8 = new MsgBuilder4();
  private final MsgBuilder6 msgBuilder6_10 = new MsgBuilder6();
  private final MsgBuilder8 msgBuilder8_11 = new MsgBuilder8();
  private final MsgBuilder10 msgBuilder10_12 = new MsgBuilder10();
  private final AsciiConsoleLogger asciiConsoleLogger_9 = new AsciiConsoleLogger();
  private final NumericPredicates numericPredicates_13 = new NumericPredicates();
  private final Filter_Number_By_greaterThan0 filter_Number_By_greaterThan0_14 =
      new Filter_Number_By_greaterThan0();
  private final NumericPredicates numericPredicates_17 = new NumericPredicates();
  private final Filter_Number_By_lessThan0 filter_Number_By_lessThan0_18 =
      new Filter_Number_By_lessThan0();
  private final NumericPredicates numericPredicates_20 = new NumericPredicates();
  private final Filter_Number_By_inRange0 filter_Number_By_inRange0_21 =
      new Filter_Number_By_inRange0();
  private final Push_Number_To_airConAndHeatingOff0 push_Number_To_airConAndHeatingOff0_22 =
      new Push_Number_To_airConAndHeatingOff0();
  private final Push_Number_To_airConOn0 push_Number_To_airConOn0_16 =
      new Push_Number_To_airConOn0();
  private final Push_Number_To_heatingOn0 push_Number_To_heatingOn0_19 =
      new Push_Number_To_heatingOn0();
  private final EnvironmentalController environmentalController_15 = new EnvironmentalController();
  //Dirty flags
  private boolean isDirty_asciiConsoleLogger_9 = false;
  private boolean isDirty_booleanFilter_7 = false;
  private boolean isDirty_filter_Number_By_greaterThan0_14 = false;
  private boolean isDirty_filter_Number_By_inRange0_21 = false;
  private boolean isDirty_filter_Number_By_lessThan0_18 = false;
  private boolean isDirty_handlerEndOfDay = false;
  private boolean isDirty_handlerStartOfDay = false;
  private boolean isDirty_handlerTempEvent = false;
  private boolean isDirty_map_Number_By_addValue0_6 = false;
  private boolean isDirty_map_Number_By_max0_2 = false;
  private boolean isDirty_map_Number_By_min0_4 = false;
  private boolean isDirty_map_getTemp_By_asDouble0_0 = false;
  private boolean isDirty_msgBuilder4_8 = false;
  private boolean isDirty_msgBuilder6_10 = false;
  private boolean isDirty_msgBuilder8_11 = false;
  private boolean isDirty_msgBuilder10_12 = false;
  private boolean isDirty_push_Number_To_airConAndHeatingOff0_22 = false;
  private boolean isDirty_push_Number_To_airConOn0_16 = false;
  private boolean isDirty_push_Number_To_heatingOn0_19 = false;
  //Filter constants

  public TempMonitor() {
    filter_Number_By_greaterThan0_14.setAlwaysReset(false);
    filter_Number_By_greaterThan0_14.setNotifyOnChangeOnly(true);
    filter_Number_By_greaterThan0_14.setResetImmediate(true);
    filter_Number_By_greaterThan0_14.filterSubject = map_getTemp_By_asDouble0_0;
    filter_Number_By_greaterThan0_14.source_0 = map_getTemp_By_asDouble0_0;
    filter_Number_By_greaterThan0_14.f = numericPredicates_13;
    filter_Number_By_inRange0_21.setAlwaysReset(false);
    filter_Number_By_inRange0_21.setNotifyOnChangeOnly(true);
    filter_Number_By_inRange0_21.setResetImmediate(true);
    filter_Number_By_inRange0_21.filterSubject = map_getTemp_By_asDouble0_0;
    filter_Number_By_inRange0_21.source_0 = map_getTemp_By_asDouble0_0;
    filter_Number_By_inRange0_21.f = numericPredicates_20;
    filter_Number_By_lessThan0_18.setAlwaysReset(false);
    filter_Number_By_lessThan0_18.setNotifyOnChangeOnly(true);
    filter_Number_By_lessThan0_18.setResetImmediate(true);
    filter_Number_By_lessThan0_18.filterSubject = map_getTemp_By_asDouble0_0;
    filter_Number_By_lessThan0_18.source_0 = map_getTemp_By_asDouble0_0;
    filter_Number_By_lessThan0_18.f = numericPredicates_17;
    map_Number_By_addValue0_6.setAlwaysReset(false);
    map_Number_By_addValue0_6.setNotifyOnChangeOnly(false);
    map_Number_By_addValue0_6.setResetImmediate(false);
    map_Number_By_addValue0_6.filterSubject = map_getTemp_By_asDouble0_0;
    map_Number_By_addValue0_6.f = average_5;
    map_Number_By_addValue0_6.resetNotifier = handlerEndOfDay;
    map_Number_By_max0_2.setAlwaysReset(false);
    map_Number_By_max0_2.setNotifyOnChangeOnly(true);
    map_Number_By_max0_2.setResetImmediate(true);
    map_Number_By_max0_2.filterSubject = map_getTemp_By_asDouble0_0;
    map_Number_By_max0_2.f = max_1;
    map_Number_By_max0_2.resetNotifier = handlerStartOfDay;
    map_Number_By_min0_4.setAlwaysReset(false);
    map_Number_By_min0_4.setNotifyOnChangeOnly(true);
    map_Number_By_min0_4.setResetImmediate(true);
    map_Number_By_min0_4.filterSubject = map_getTemp_By_asDouble0_0;
    map_Number_By_min0_4.f = min_3;
    map_Number_By_min0_4.resetNotifier = handlerStartOfDay;
    map_getTemp_By_asDouble0_0.setAlwaysReset(false);
    map_getTemp_By_asDouble0_0.setNotifyOnChangeOnly(false);
    map_getTemp_By_asDouble0_0.setResetImmediate(true);
    map_getTemp_By_asDouble0_0.filterSubject = handlerTempEvent;
    msgBuilder4_8.source_ReusableEventHandler_3 = handlerStartOfDay;
    msgBuilder4_8.logLevel = (int) 3;
    msgBuilder4_8.initCapacity = (int) 256;
    msgBuilder6_10.source_Map_Number_By_max0_5 = map_Number_By_max0_2;
    msgBuilder6_10.logLevel = (int) 3;
    msgBuilder6_10.initCapacity = (int) 256;
    msgBuilder8_11.source_Map_Number_By_min0_7 = map_Number_By_min0_4;
    msgBuilder8_11.logLevel = (int) 3;
    msgBuilder8_11.initCapacity = (int) 256;
    msgBuilder10_12.source_BooleanFilter_9 = booleanFilter_7;
    msgBuilder10_12.logLevel = (int) 3;
    msgBuilder10_12.initCapacity = (int) 256;
    push_Number_To_airConAndHeatingOff0_22.filterSubject = filter_Number_By_inRange0_21;
    push_Number_To_airConAndHeatingOff0_22.f = environmentalController_15;
    push_Number_To_airConOn0_16.filterSubject = filter_Number_By_greaterThan0_14;
    push_Number_To_airConOn0_16.f = environmentalController_15;
    push_Number_To_heatingOn0_19.filterSubject = filter_Number_By_lessThan0_18;
    push_Number_To_heatingOn0_19.f = environmentalController_15;
    asciiConsoleLogger_9.initCapacity = (int) 512;
    asciiConsoleLogger_9.msgBuilders = new MsgBuilder[4];
    asciiConsoleLogger_9.msgBuilders[0] = msgBuilder4_8;
    asciiConsoleLogger_9.msgBuilders[1] = msgBuilder6_10;
    asciiConsoleLogger_9.msgBuilders[2] = msgBuilder8_11;
    asciiConsoleLogger_9.msgBuilders[3] = msgBuilder10_12;
    numericPredicates_13.doubleLimit_0 = (double) 25.0;
    numericPredicates_13.doubleLimit_1 = (double) Double.NaN;
    numericPredicates_17.doubleLimit_0 = (double) 12.0;
    numericPredicates_17.doubleLimit_1 = (double) Double.NaN;
    numericPredicates_20.doubleLimit_0 = (double) 12.0;
    numericPredicates_20.doubleLimit_1 = (double) 25.0;
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
      map_Number_By_addValue0_6.resetNotification(handlerEndOfDay);
    }
    if (isDirty_handlerEndOfDay) {
      isDirty_booleanFilter_7 = booleanFilter_7.updated();
    }
    if (isDirty_booleanFilter_7) {
      isDirty_msgBuilder10_12 = msgBuilder10_12.buildMessage();
      if (isDirty_msgBuilder10_12) {
        asciiConsoleLogger_9.publishMessage(msgBuilder10_12);
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
      map_Number_By_max0_2.resetNotification(handlerStartOfDay);
      map_Number_By_min0_4.resetNotification(handlerStartOfDay);
    }
    if (isDirty_handlerStartOfDay) {
      isDirty_msgBuilder4_8 = msgBuilder4_8.buildMessage();
      if (isDirty_msgBuilder4_8) {
        asciiConsoleLogger_9.publishMessage(msgBuilder4_8);
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
      isDirty_map_getTemp_By_asDouble0_0 = map_getTemp_By_asDouble0_0.onEvent();
      if (isDirty_map_getTemp_By_asDouble0_0) {
        map_Number_By_addValue0_6.updated_filterSubject(map_getTemp_By_asDouble0_0);
        map_Number_By_max0_2.updated_filterSubject(map_getTemp_By_asDouble0_0);
        map_Number_By_min0_4.updated_filterSubject(map_getTemp_By_asDouble0_0);
      }
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_map_Number_By_addValue0_6 = map_Number_By_addValue0_6.onEvent();
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_map_Number_By_max0_2 = map_Number_By_max0_2.onEvent();
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_map_Number_By_min0_4 = map_Number_By_min0_4.onEvent();
    }
    if (isDirty_map_Number_By_max0_2) {
      isDirty_msgBuilder6_10 = msgBuilder6_10.buildMessage();
      if (isDirty_msgBuilder6_10) {
        asciiConsoleLogger_9.publishMessage(msgBuilder6_10);
      }
    }
    if (isDirty_map_Number_By_min0_4) {
      isDirty_msgBuilder8_11 = msgBuilder8_11.buildMessage();
      if (isDirty_msgBuilder8_11) {
        asciiConsoleLogger_9.publishMessage(msgBuilder8_11);
      }
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_filter_Number_By_greaterThan0_14 = filter_Number_By_greaterThan0_14.onEvent();
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_filter_Number_By_lessThan0_18 = filter_Number_By_lessThan0_18.onEvent();
    }
    if (isDirty_map_getTemp_By_asDouble0_0) {
      isDirty_filter_Number_By_inRange0_21 = filter_Number_By_inRange0_21.onEvent();
    }
    if (isDirty_filter_Number_By_inRange0_21) {
      isDirty_push_Number_To_airConAndHeatingOff0_22 =
          push_Number_To_airConAndHeatingOff0_22.onEvent();
    }
    if (isDirty_filter_Number_By_greaterThan0_14) {
      isDirty_push_Number_To_airConOn0_16 = push_Number_To_airConOn0_16.onEvent();
    }
    if (isDirty_filter_Number_By_lessThan0_18) {
      isDirty_push_Number_To_heatingOn0_19 = push_Number_To_heatingOn0_19.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        isDirty_msgBuilder4_8 = msgBuilder4_8.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder6_10 = msgBuilder6_10.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder8_11 = msgBuilder8_11.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder10_12 = msgBuilder10_12.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        isDirty_msgBuilder4_8 = msgBuilder4_8.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder6_10 = msgBuilder6_10.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder8_11 = msgBuilder8_11.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder10_12 = msgBuilder10_12.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_LEVEL]
      case ("RECORD_LEVEL"):
        isDirty_asciiConsoleLogger_9 = true;
        asciiConsoleLogger_9.controlLevelLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_NAME]
      case ("RECORD_NAME"):
        isDirty_asciiConsoleLogger_9 = true;
        asciiConsoleLogger_9.controlIdLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_TIME]
      case ("RECORD_TIME"):
        isDirty_asciiConsoleLogger_9 = true;
        asciiConsoleLogger_9.controlTimeLogging(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  @Override
  public void afterEvent() {
    filter_Number_By_inRange0_21.resetAfterEvent();
    filter_Number_By_lessThan0_18.resetAfterEvent();
    filter_Number_By_greaterThan0_14.resetAfterEvent();
    msgBuilder10_12.afterEvent();
    msgBuilder8_11.afterEvent();
    msgBuilder6_10.afterEvent();
    msgBuilder4_8.afterEvent();
    map_Number_By_min0_4.resetAfterEvent();
    map_Number_By_max0_2.resetAfterEvent();
    map_Number_By_addValue0_6.resetAfterEvent();
    isDirty_asciiConsoleLogger_9 = false;
    isDirty_booleanFilter_7 = false;
    isDirty_filter_Number_By_greaterThan0_14 = false;
    isDirty_filter_Number_By_inRange0_21 = false;
    isDirty_filter_Number_By_lessThan0_18 = false;
    isDirty_handlerEndOfDay = false;
    isDirty_handlerStartOfDay = false;
    isDirty_handlerTempEvent = false;
    isDirty_map_Number_By_addValue0_6 = false;
    isDirty_map_Number_By_max0_2 = false;
    isDirty_map_Number_By_min0_4 = false;
    isDirty_map_getTemp_By_asDouble0_0 = false;
    isDirty_msgBuilder4_8 = false;
    isDirty_msgBuilder6_10 = false;
    isDirty_msgBuilder8_11 = false;
    isDirty_msgBuilder10_12 = false;
    isDirty_push_Number_To_airConAndHeatingOff0_22 = false;
    isDirty_push_Number_To_airConOn0_16 = false;
    isDirty_push_Number_To_heatingOn0_19 = false;
  }

  @Override
  public void init() {
    map_getTemp_By_asDouble0_0.init();
    map_Number_By_addValue0_6.init();
    map_Number_By_max0_2.init();
    map_Number_By_min0_4.init();
    msgBuilder4_8.init();
    msgBuilder6_10.init();
    msgBuilder8_11.init();
    msgBuilder10_12.init();
    asciiConsoleLogger_9.init();
    filter_Number_By_greaterThan0_14.init();
    filter_Number_By_lessThan0_18.init();
    filter_Number_By_inRange0_21.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
