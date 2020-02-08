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
package com.fluxtion.articles.audit.generated.reconcile;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.audit.HouseTradePublisher;
import com.fluxtion.articles.audit.Position;
import com.fluxtion.articles.audit.Trade;
import com.fluxtion.ext.streaming.api.IntFilterEventHandler;
import com.fluxtion.ext.streaming.api.log.AsciiConsoleLogger;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.log.MsgBuilder;
import com.fluxtion.ext.streaming.api.stream.NodeWrapper;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Delta;

/*
 * <pre>
 * generation time   : 2020-02-08T15:15:15.111219400
 * generator version : 1.8.3-SNAPSHOT
 * api version       : 1.8.3-SNAPSHOT
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class PositionReconciliator implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Delta delta_8 = new Delta();
  private final Delta delta_11 = new Delta();
  private final IntFilterEventHandler handlerPosition =
      new IntFilterEventHandler(2147483647, Position.class);
  private final LambdaFunction lambdaFunction_15 =
      new LambdaFunction("lambda$buildCalc$5054700f$1_0");
  private final MsgBuilder1 msgBuilder1_2 = new MsgBuilder1();
  private final String string_0 =
      new String(new byte[] {(byte) 99, (byte) 111, (byte) 114, (byte) 101}, (byte) 0);
  public final Filter_getSource_By_equals0 corePosition = new Filter_getSource_By_equals0();
  public final Map_getBase_With_value0 baseQuantiy = new Map_getBase_With_value0();
  private final Push_Number_To_setBase0 push_Number_To_setBase0_10 = new Push_Number_To_setBase0();
  private final Push_getBaseCcy_To_setBaseCcy0 push_getBaseCcy_To_setBaseCcy0_5 =
      new Push_getBaseCcy_To_setBaseCcy0();
  private final Push_getTermsCcy_To_setTermsCcy0 push_getTermsCcy_To_setTermsCcy0_6 =
      new Push_getTermsCcy_To_setTermsCcy0();
  private final Push_getTradeType_To_setTradeType0 push_getTradeType_To_setTradeType0_7 =
      new Push_getTradeType_To_setTradeType0();
  public final Map_getTerms_With_value0 termsQuantiy = new Map_getTerms_With_value0();
  private final Push_Number_To_setTerms0 push_Number_To_setTerms0_13 =
      new Push_Number_To_setTerms0();
  private final Trade trade_4 = new Trade();
  private final NodeWrapper nodeWrapper_14 = new NodeWrapper(trade_4);
  public final Filter_getTradeType_By_apply0 clientTrades = new Filter_getTradeType_By_apply0();
  private final MsgBuilder3 msgBuilder3_19 = new MsgBuilder3();
  private final AsciiConsoleLogger asciiConsoleLogger_3 = new AsciiConsoleLogger();
  private final Push_Trade_To_publishHouseTrade0 push_Trade_To_publishHouseTrade0_18 =
      new Push_Trade_To_publishHouseTrade0();
  private final HouseTradePublisher houseTradePublisher_17 = new HouseTradePublisher();
  //Dirty flags
  private boolean isDirty_baseQuantiy = false;
  private boolean isDirty_clientTrades = false;
  private boolean isDirty_corePosition = false;
  private boolean isDirty_handlerPosition = false;
  private boolean isDirty_msgBuilder1_2 = false;
  private boolean isDirty_msgBuilder3_19 = false;
  private boolean isDirty_nodeWrapper_14 = false;
  private boolean isDirty_push_Number_To_setBase0_10 = false;
  private boolean isDirty_push_Number_To_setTerms0_13 = false;
  private boolean isDirty_push_Trade_To_publishHouseTrade0_18 = false;
  private boolean isDirty_push_getBaseCcy_To_setBaseCcy0_5 = false;
  private boolean isDirty_push_getTermsCcy_To_setTermsCcy0_6 = false;
  private boolean isDirty_push_getTradeType_To_setTradeType0_7 = false;
  private boolean isDirty_termsQuantiy = false;
  //Filter constants

  public PositionReconciliator() {
    trade_4.setBase(0.0);
    trade_4.setTerms(0.0);
    corePosition.setAlwaysReset(false);
    corePosition.setNotifyOnChangeOnly(false);
    corePosition.setResetImmediate(true);
    corePosition.filterSubject = handlerPosition;
    corePosition.source_0 = handlerPosition;
    corePosition.f = string_0;
    clientTrades.setAlwaysReset(false);
    clientTrades.setNotifyOnChangeOnly(false);
    clientTrades.setResetImmediate(true);
    clientTrades.filterSubject = nodeWrapper_14;
    clientTrades.source_0 = nodeWrapper_14;
    clientTrades.f = lambdaFunction_15;
    baseQuantiy.setAlwaysReset(false);
    baseQuantiy.setNotifyOnChangeOnly(false);
    baseQuantiy.setResetImmediate(true);
    baseQuantiy.filterSubject = corePosition;
    baseQuantiy.f = delta_8;
    termsQuantiy.setAlwaysReset(false);
    termsQuantiy.setNotifyOnChangeOnly(false);
    termsQuantiy.setResetImmediate(true);
    termsQuantiy.filterSubject = corePosition;
    termsQuantiy.f = delta_11;
    msgBuilder1_2.source_IntFilterEventHandler_0 = handlerPosition;
    msgBuilder1_2.logLevel = (int) 3;
    msgBuilder1_2.initCapacity = (int) 256;
    msgBuilder3_19.source_Filter_getTradeType_By_apply0_2 = clientTrades;
    msgBuilder3_19.logLevel = (int) 3;
    msgBuilder3_19.initCapacity = (int) 256;
    push_Number_To_setBase0_10.filterSubject = baseQuantiy;
    push_Number_To_setBase0_10.f = trade_4;
    push_Number_To_setTerms0_13.filterSubject = termsQuantiy;
    push_Number_To_setTerms0_13.f = trade_4;
    push_Trade_To_publishHouseTrade0_18.filterSubject = clientTrades;
    push_Trade_To_publishHouseTrade0_18.f = houseTradePublisher_17;
    push_getBaseCcy_To_setBaseCcy0_5.filterSubject = corePosition;
    push_getBaseCcy_To_setBaseCcy0_5.f = trade_4;
    push_getTermsCcy_To_setTermsCcy0_6.filterSubject = corePosition;
    push_getTermsCcy_To_setTermsCcy0_6.f = trade_4;
    push_getTradeType_To_setTradeType0_7.filterSubject = corePosition;
    push_getTradeType_To_setTradeType0_7.f = trade_4;
    asciiConsoleLogger_3.initCapacity = (int) 512;
    asciiConsoleLogger_3.msgBuilders = new MsgBuilder[2];
    asciiConsoleLogger_3.msgBuilders[0] = msgBuilder1_2;
    asciiConsoleLogger_3.msgBuilders[1] = msgBuilder3_19;
    delta_8.previous = (double) 0.0;
    delta_11.previous = (double) 0.0;
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
      case ("com.fluxtion.articles.audit.Position"):
        {
          Position typedEvent = (Position) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(Position typedEvent) {
    //Default, no filter methods
    isDirty_handlerPosition = true;
    handlerPosition.onEvent(typedEvent);
    if (isDirty_handlerPosition) {
      isDirty_msgBuilder1_2 = msgBuilder1_2.buildMessage();
      if (isDirty_msgBuilder1_2) {
        asciiConsoleLogger_3.publishMessage(msgBuilder1_2);
      }
    }
    if (isDirty_handlerPosition) {
      isDirty_corePosition = corePosition.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_baseQuantiy = baseQuantiy.onEvent();
    }
    if (isDirty_baseQuantiy) {
      isDirty_push_Number_To_setBase0_10 = push_Number_To_setBase0_10.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getBaseCcy_To_setBaseCcy0_5 = push_getBaseCcy_To_setBaseCcy0_5.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getTermsCcy_To_setTermsCcy0_6 = push_getTermsCcy_To_setTermsCcy0_6.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getTradeType_To_setTradeType0_7 = push_getTradeType_To_setTradeType0_7.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_termsQuantiy = termsQuantiy.onEvent();
    }
    if (isDirty_termsQuantiy) {
      isDirty_push_Number_To_setTerms0_13 = push_Number_To_setTerms0_13.onEvent();
    }
    if (isDirty_push_Number_To_setBase0_10
        | isDirty_push_Number_To_setTerms0_13
        | isDirty_push_getBaseCcy_To_setBaseCcy0_5
        | isDirty_push_getTermsCcy_To_setTermsCcy0_6
        | isDirty_push_getTradeType_To_setTradeType0_7) {
      isDirty_nodeWrapper_14 = nodeWrapper_14.onEvent();
    }
    if (isDirty_nodeWrapper_14) {
      isDirty_clientTrades = clientTrades.onEvent();
    }
    if (isDirty_clientTrades) {
      isDirty_msgBuilder3_19 = msgBuilder3_19.buildMessage();
      if (isDirty_msgBuilder3_19) {
        asciiConsoleLogger_3.publishMessage(msgBuilder3_19);
      }
    }
    if (isDirty_clientTrades) {
      isDirty_push_Trade_To_publishHouseTrade0_18 = push_Trade_To_publishHouseTrade0_18.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        isDirty_msgBuilder1_2 = msgBuilder1_2.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder3_19 = msgBuilder3_19.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        isDirty_msgBuilder1_2 = msgBuilder1_2.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder3_19 = msgBuilder3_19.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_LEVEL]
      case ("RECORD_LEVEL"):
        asciiConsoleLogger_3.controlLevelLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_NAME]
      case ("RECORD_NAME"):
        asciiConsoleLogger_3.controlIdLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_TIME]
      case ("RECORD_TIME"):
        asciiConsoleLogger_3.controlTimeLogging(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  @Override
  public void afterEvent() {
    msgBuilder3_19.afterEvent();
    clientTrades.resetAfterEvent();
    termsQuantiy.resetAfterEvent();
    baseQuantiy.resetAfterEvent();
    corePosition.resetAfterEvent();
    msgBuilder1_2.afterEvent();
    isDirty_baseQuantiy = false;
    isDirty_clientTrades = false;
    isDirty_corePosition = false;
    isDirty_handlerPosition = false;
    isDirty_msgBuilder1_2 = false;
    isDirty_msgBuilder3_19 = false;
    isDirty_nodeWrapper_14 = false;
    isDirty_push_Number_To_setBase0_10 = false;
    isDirty_push_Number_To_setTerms0_13 = false;
    isDirty_push_Trade_To_publishHouseTrade0_18 = false;
    isDirty_push_getBaseCcy_To_setBaseCcy0_5 = false;
    isDirty_push_getTermsCcy_To_setTermsCcy0_6 = false;
    isDirty_push_getTradeType_To_setTradeType0_7 = false;
    isDirty_termsQuantiy = false;
  }

  @Override
  public void init() {
    lambdaFunction_15.init();
    msgBuilder1_2.init();
    corePosition.init();
    baseQuantiy.init();
    termsQuantiy.init();
    clientTrades.init();
    msgBuilder3_19.init();
    asciiConsoleLogger_3.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
