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
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.log.AsciiConsoleLogger;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.log.MsgBuilder;
import com.fluxtion.ext.streaming.api.stream.NodeWrapper;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Delta;

public class PositionReconciliator implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Delta delta_10 = new Delta();
  private final Delta delta_13 = new Delta();
  private final ReusableEventHandler handlerPosition =
      new ReusableEventHandler(2147483647, Position.class);
  private final LambdaFunction lambdaFunction_17 =
      new LambdaFunction("lambda$buildCalc$5054700f$1_0");
  private final MsgBuilder1 msgBuilder1_4 = new MsgBuilder1();
  private final String string_0 =
      new String(new byte[] {(byte) 99, (byte) 111, (byte) 114, (byte) 101}, (byte) 0);
  public final Filter_getSource_By_equals0 corePosition = new Filter_getSource_By_equals0();
  public final Map_getBase_With_value0 baseQuantiy = new Map_getBase_With_value0();
  private final Push_Number_To_setBase0 push_Number_To_setBase0_12 = new Push_Number_To_setBase0();
  private final Push_getBaseCcy_To_setBaseCcy0 push_getBaseCcy_To_setBaseCcy0_7 =
      new Push_getBaseCcy_To_setBaseCcy0();
  private final Push_getTermsCcy_To_setTermsCcy0 push_getTermsCcy_To_setTermsCcy0_8 =
      new Push_getTermsCcy_To_setTermsCcy0();
  private final Push_getTradeType_To_setTradeType0 push_getTradeType_To_setTradeType0_9 =
      new Push_getTradeType_To_setTradeType0();
  private final String string_2 =
      new String(new byte[] {(byte) 99, (byte) 111, (byte) 108, (byte) 111}, (byte) 0);
  public final Filter_getSource_By_equals0 coloPosition = new Filter_getSource_By_equals0();
  public final Map_getTerms_With_value0 termsQuantiy = new Map_getTerms_With_value0();
  private final Push_Number_To_setTerms0 push_Number_To_setTerms0_15 =
      new Push_Number_To_setTerms0();
  private final Trade trade_6 = new Trade();
  private final NodeWrapper nodeWrapper_16 = new NodeWrapper(trade_6);
  public final Filter_getTradeType_By_apply0 clientTrades = new Filter_getTradeType_By_apply0();
  private final MsgBuilder3 msgBuilder3_21 = new MsgBuilder3();
  private final AsciiConsoleLogger asciiConsoleLogger_5 = new AsciiConsoleLogger();
  private final Push_Trade_To_publishHouseTrade0 push_Trade_To_publishHouseTrade0_20 =
      new Push_Trade_To_publishHouseTrade0();
  private final HouseTradePublisher houseTradePublisher_19 = new HouseTradePublisher();
  //Dirty flags
  private boolean isDirty_baseQuantiy = false;
  private boolean isDirty_clientTrades = false;
  private boolean isDirty_corePosition = false;
  private boolean isDirty_handlerPosition = false;
  private boolean isDirty_msgBuilder1_4 = false;
  private boolean isDirty_msgBuilder3_21 = false;
  private boolean isDirty_nodeWrapper_16 = false;
  private boolean isDirty_push_Number_To_setBase0_12 = false;
  private boolean isDirty_push_Number_To_setTerms0_15 = false;
  private boolean isDirty_push_Trade_To_publishHouseTrade0_20 = false;
  private boolean isDirty_push_getBaseCcy_To_setBaseCcy0_7 = false;
  private boolean isDirty_push_getTermsCcy_To_setTermsCcy0_8 = false;
  private boolean isDirty_push_getTradeType_To_setTradeType0_9 = false;
  private boolean isDirty_termsQuantiy = false;
  //Filter constants

  public PositionReconciliator() {
    trade_6.setBase(0.0);
    trade_6.setTerms(0.0);
    coloPosition.setAlwaysReset(false);
    coloPosition.setNotifyOnChangeOnly(false);
    coloPosition.setResetImmediate(true);
    coloPosition.filterSubject = handlerPosition;
    coloPosition.source_0 = handlerPosition;
    coloPosition.f = string_2;
    corePosition.setAlwaysReset(false);
    corePosition.setNotifyOnChangeOnly(false);
    corePosition.setResetImmediate(true);
    corePosition.filterSubject = handlerPosition;
    corePosition.source_0 = handlerPosition;
    corePosition.f = string_0;
    clientTrades.setAlwaysReset(false);
    clientTrades.setNotifyOnChangeOnly(false);
    clientTrades.setResetImmediate(true);
    clientTrades.filterSubject = nodeWrapper_16;
    clientTrades.source_0 = nodeWrapper_16;
    clientTrades.f = lambdaFunction_17;
    baseQuantiy.setAlwaysReset(false);
    baseQuantiy.setNotifyOnChangeOnly(false);
    baseQuantiy.setResetImmediate(true);
    baseQuantiy.filterSubject = corePosition;
    baseQuantiy.f = delta_10;
    termsQuantiy.setAlwaysReset(false);
    termsQuantiy.setNotifyOnChangeOnly(false);
    termsQuantiy.setResetImmediate(true);
    termsQuantiy.filterSubject = corePosition;
    termsQuantiy.f = delta_13;
    msgBuilder1_4.source_ReusableEventHandler_0 = handlerPosition;
    msgBuilder1_4.logLevel = (int) 3;
    msgBuilder1_4.initCapacity = (int) 256;
    msgBuilder3_21.source_Filter_getTradeType_By_apply0_2 = clientTrades;
    msgBuilder3_21.logLevel = (int) 3;
    msgBuilder3_21.initCapacity = (int) 256;
    push_Number_To_setBase0_12.filterSubject = baseQuantiy;
    push_Number_To_setBase0_12.f = trade_6;
    push_Number_To_setTerms0_15.filterSubject = termsQuantiy;
    push_Number_To_setTerms0_15.f = trade_6;
    push_Trade_To_publishHouseTrade0_20.filterSubject = clientTrades;
    push_Trade_To_publishHouseTrade0_20.f = houseTradePublisher_19;
    push_getBaseCcy_To_setBaseCcy0_7.filterSubject = corePosition;
    push_getBaseCcy_To_setBaseCcy0_7.f = trade_6;
    push_getTermsCcy_To_setTermsCcy0_8.filterSubject = corePosition;
    push_getTermsCcy_To_setTermsCcy0_8.f = trade_6;
    push_getTradeType_To_setTradeType0_9.filterSubject = corePosition;
    push_getTradeType_To_setTradeType0_9.f = trade_6;
    asciiConsoleLogger_5.initCapacity = (int) 512;
    asciiConsoleLogger_5.msgBuilders = new MsgBuilder[2];
    asciiConsoleLogger_5.msgBuilders[0] = msgBuilder1_4;
    asciiConsoleLogger_5.msgBuilders[1] = msgBuilder3_21;
    delta_10.previous = (double) 0.0;
    delta_13.previous = (double) 0.0;
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
      isDirty_msgBuilder1_4 = msgBuilder1_4.buildMessage();
      if (isDirty_msgBuilder1_4) {
        asciiConsoleLogger_5.publishMessage(msgBuilder1_4);
      }
    }
    if (isDirty_handlerPosition) {
      isDirty_corePosition = corePosition.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_baseQuantiy = baseQuantiy.onEvent();
    }
    if (isDirty_baseQuantiy) {
      isDirty_push_Number_To_setBase0_12 = push_Number_To_setBase0_12.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getBaseCcy_To_setBaseCcy0_7 = push_getBaseCcy_To_setBaseCcy0_7.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getTermsCcy_To_setTermsCcy0_8 = push_getTermsCcy_To_setTermsCcy0_8.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_push_getTradeType_To_setTradeType0_9 = push_getTradeType_To_setTradeType0_9.onEvent();
    }
    if (isDirty_handlerPosition) {
      coloPosition.onEvent();
    }
    if (isDirty_corePosition) {
      isDirty_termsQuantiy = termsQuantiy.onEvent();
    }
    if (isDirty_termsQuantiy) {
      isDirty_push_Number_To_setTerms0_15 = push_Number_To_setTerms0_15.onEvent();
    }
    if (isDirty_push_Number_To_setBase0_12
        | isDirty_push_Number_To_setTerms0_15
        | isDirty_push_getBaseCcy_To_setBaseCcy0_7
        | isDirty_push_getTermsCcy_To_setTermsCcy0_8
        | isDirty_push_getTradeType_To_setTradeType0_9) {
      isDirty_nodeWrapper_16 = nodeWrapper_16.onEvent();
    }
    if (isDirty_nodeWrapper_16) {
      isDirty_clientTrades = clientTrades.onEvent();
    }
    if (isDirty_clientTrades) {
      isDirty_msgBuilder3_21 = msgBuilder3_21.buildMessage();
      if (isDirty_msgBuilder3_21) {
        asciiConsoleLogger_5.publishMessage(msgBuilder3_21);
      }
    }
    if (isDirty_clientTrades) {
      isDirty_push_Trade_To_publishHouseTrade0_20 = push_Trade_To_publishHouseTrade0_20.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        isDirty_msgBuilder1_4 = msgBuilder1_4.controlLogIdFilter(typedEvent);
        isDirty_msgBuilder3_21 = msgBuilder3_21.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        isDirty_msgBuilder1_4 = msgBuilder1_4.controlLogLevelFilter(typedEvent);
        isDirty_msgBuilder3_21 = msgBuilder3_21.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_LEVEL]
      case ("RECORD_LEVEL"):
        asciiConsoleLogger_5.controlLevelLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_NAME]
      case ("RECORD_NAME"):
        asciiConsoleLogger_5.controlIdLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_TIME]
      case ("RECORD_TIME"):
        asciiConsoleLogger_5.controlTimeLogging(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  @Override
  public void afterEvent() {
    msgBuilder3_21.afterEvent();
    clientTrades.resetAfterEvent();
    termsQuantiy.resetAfterEvent();
    coloPosition.resetAfterEvent();
    baseQuantiy.resetAfterEvent();
    corePosition.resetAfterEvent();
    msgBuilder1_4.afterEvent();
    isDirty_baseQuantiy = false;
    isDirty_clientTrades = false;
    isDirty_corePosition = false;
    isDirty_handlerPosition = false;
    isDirty_msgBuilder1_4 = false;
    isDirty_msgBuilder3_21 = false;
    isDirty_nodeWrapper_16 = false;
    isDirty_push_Number_To_setBase0_12 = false;
    isDirty_push_Number_To_setTerms0_15 = false;
    isDirty_push_Trade_To_publishHouseTrade0_20 = false;
    isDirty_push_getBaseCcy_To_setBaseCcy0_7 = false;
    isDirty_push_getTermsCcy_To_setTermsCcy0_8 = false;
    isDirty_push_getTradeType_To_setTradeType0_9 = false;
    isDirty_termsQuantiy = false;
  }

  @Override
  public void init() {
    lambdaFunction_17.init();
    msgBuilder1_4.init();
    corePosition.init();
    baseQuantiy.init();
    coloPosition.init();
    termsQuantiy.init();
    clientTrades.init();
    msgBuilder3_21.init();
    asciiConsoleLogger_5.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
