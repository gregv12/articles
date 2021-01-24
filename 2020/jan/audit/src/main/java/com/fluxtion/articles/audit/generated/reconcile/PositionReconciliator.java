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
import com.fluxtion.api.audit.Auditor;
import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.audit.EventLogManager;
import com.fluxtion.api.event.GenericEvent;
import com.fluxtion.api.time.Clock;
import com.fluxtion.articles.audit.HouseTradePublisher;
import com.fluxtion.articles.audit.MyClockHolder;
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
 * generation time   : 2020-02-09T12:45:33.475676600
 * generator version : 1.8.7-SNAPSHOT
 * api version       : 1.8.7-SNAPSHOT
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked"})
public class PositionReconciliator implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  public final Clock clock = new Clock();
  private final Delta delta_12 = new Delta();
  private final Delta delta_15 = new Delta();
  public final EventLogManager eventLogger = new EventLogManager();
  private final IntFilterEventHandler handlerPosition =
      new IntFilterEventHandler(2147483647, Position.class);
  private final LambdaFunction lambdaFunction_19 =
      new LambdaFunction("lambda$buildCalc$5054700f$1_0");
  private final MsgBuilder1 msgBuilder1_6 = new MsgBuilder1();
  private final MyClockHolder myClockHolder_3 = new MyClockHolder(clock);
  private final String string_4 = "core";
  public final Filter_getSource_By_equals0 corePosition = new Filter_getSource_By_equals0();
  public final Map_getBase_With_value0 baseQuantiy = new Map_getBase_With_value0();
  private final Push_Number_To_setBase0 push_Number_To_setBase0_14 = new Push_Number_To_setBase0();
  private final Push_getBaseCcy_To_setBaseCcy0 push_getBaseCcy_To_setBaseCcy0_9 =
      new Push_getBaseCcy_To_setBaseCcy0();
  private final Push_getTermsCcy_To_setTermsCcy0 push_getTermsCcy_To_setTermsCcy0_10 =
      new Push_getTermsCcy_To_setTermsCcy0();
  private final Push_getTradeType_To_setTradeType0 push_getTradeType_To_setTradeType0_11 =
      new Push_getTradeType_To_setTradeType0();
  public final Map_getTerms_With_value0 termsQuantiy = new Map_getTerms_With_value0();
  private final Push_Number_To_setTerms0 push_Number_To_setTerms0_17 =
      new Push_Number_To_setTerms0();
  private final Trade trade_8 = new Trade();
  private final NodeWrapper nodeWrapper_18 = new NodeWrapper(trade_8);
  public final Filter_getTradeType_By_apply0 clientTrades = new Filter_getTradeType_By_apply0();
  private final MsgBuilder3 msgBuilder3_23 = new MsgBuilder3();
  private final AsciiConsoleLogger asciiConsoleLogger_7 = new AsciiConsoleLogger();
  private final Push_Trade_To_publishHouseTrade0 push_Trade_To_publishHouseTrade0_22 =
      new Push_Trade_To_publishHouseTrade0();
  private final HouseTradePublisher houseTradePublisher_21 = new HouseTradePublisher();
  //Dirty flags
  private boolean isDirty_baseQuantiy = false;
  private boolean isDirty_clientTrades = false;
  private boolean isDirty_clock = false;
  private boolean isDirty_corePosition = false;
  private boolean isDirty_handlerPosition = false;
  private boolean isDirty_msgBuilder1_6 = false;
  private boolean isDirty_msgBuilder3_23 = false;
  private boolean isDirty_nodeWrapper_18 = false;
  private boolean isDirty_push_Number_To_setBase0_14 = false;
  private boolean isDirty_push_Number_To_setTerms0_17 = false;
  private boolean isDirty_push_Trade_To_publishHouseTrade0_22 = false;
  private boolean isDirty_push_getBaseCcy_To_setBaseCcy0_9 = false;
  private boolean isDirty_push_getTermsCcy_To_setTermsCcy0_10 = false;
  private boolean isDirty_push_getTradeType_To_setTradeType0_11 = false;
  private boolean isDirty_termsQuantiy = false;
  //Filter constants

  public PositionReconciliator() {
    eventLogger.trace = (boolean) true;
    eventLogger.traceLevel = com.fluxtion.api.audit.EventLogControlEvent.LogLevel.INFO;
    eventLogger.clock = clock;
    trade_8.setBase(0.0);
    trade_8.setTerms(0.0);
    corePosition.setAlwaysReset(false);
    corePosition.setNotifyOnChangeOnly(false);
    corePosition.setResetImmediate(true);
    corePosition.filterSubject = handlerPosition;
    corePosition.source_0 = handlerPosition;
    corePosition.f = string_4;
    clientTrades.setAlwaysReset(false);
    clientTrades.setNotifyOnChangeOnly(false);
    clientTrades.setResetImmediate(true);
    clientTrades.filterSubject = nodeWrapper_18;
    clientTrades.source_0 = nodeWrapper_18;
    clientTrades.f = lambdaFunction_19;
    baseQuantiy.setAlwaysReset(false);
    baseQuantiy.setNotifyOnChangeOnly(false);
    baseQuantiy.setResetImmediate(true);
    baseQuantiy.filterSubject = corePosition;
    baseQuantiy.f = delta_12;
    termsQuantiy.setAlwaysReset(false);
    termsQuantiy.setNotifyOnChangeOnly(false);
    termsQuantiy.setResetImmediate(true);
    termsQuantiy.filterSubject = corePosition;
    termsQuantiy.f = delta_15;
    msgBuilder1_6.source_IntFilterEventHandler_0 = handlerPosition;
    msgBuilder1_6.logLevel = (int) 3;
    msgBuilder1_6.initCapacity = (int) 256;
    msgBuilder3_23.source_Filter_getTradeType_By_apply0_2 = clientTrades;
    msgBuilder3_23.logLevel = (int) 3;
    msgBuilder3_23.initCapacity = (int) 256;
    push_Number_To_setBase0_14.filterSubject = baseQuantiy;
    push_Number_To_setBase0_14.f = trade_8;
    push_Number_To_setTerms0_17.filterSubject = termsQuantiy;
    push_Number_To_setTerms0_17.f = trade_8;
    push_Trade_To_publishHouseTrade0_22.filterSubject = clientTrades;
    push_Trade_To_publishHouseTrade0_22.f = houseTradePublisher_21;
    push_getBaseCcy_To_setBaseCcy0_9.filterSubject = corePosition;
    push_getBaseCcy_To_setBaseCcy0_9.f = trade_8;
    push_getTermsCcy_To_setTermsCcy0_10.filterSubject = corePosition;
    push_getTermsCcy_To_setTermsCcy0_10.f = trade_8;
    push_getTradeType_To_setTradeType0_11.filterSubject = corePosition;
    push_getTradeType_To_setTradeType0_11.f = trade_8;
    asciiConsoleLogger_7.initCapacity = (int) 512;
    asciiConsoleLogger_7.msgBuilders = new MsgBuilder[2];
    asciiConsoleLogger_7.msgBuilders[0] = msgBuilder1_6;
    asciiConsoleLogger_7.msgBuilders[1] = msgBuilder3_23;
    delta_12.previous = (double) 0.0;
    delta_15.previous = (double) 0.0;
    //node auditors
    initialiseAuditor(eventLogger);
    initialiseAuditor(clock);
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
      case ("com.fluxtion.api.audit.EventLogControlEvent"):
        {
          EventLogControlEvent typedEvent = (EventLogControlEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.api.event.GenericEvent"):
        {
          GenericEvent typedEvent = (GenericEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.articles.audit.Position"):
        {
          Position typedEvent = (Position) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(EventLogControlEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    auditInvocation(eventLogger, "eventLogger", "calculationLogConfig", typedEvent);
    eventLogger.calculationLogConfig(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(GenericEvent typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.api.event.GenericEvent] filterString:[com.fluxtion.api.time.ClockStrategy]
      case ("com.fluxtion.api.time.ClockStrategy"):
        auditInvocation(clock, "clock", "setClockStrategy", typedEvent);
        isDirty_clock = true;
        clock.setClockStrategy(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(Position typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    auditInvocation(handlerPosition, "handlerPosition", "onEvent", typedEvent);
    isDirty_handlerPosition = true;
    handlerPosition.onEvent(typedEvent);
    if (isDirty_handlerPosition) {
      auditInvocation(msgBuilder1_6, "msgBuilder1_6", "buildMessage", typedEvent);
      isDirty_msgBuilder1_6 = msgBuilder1_6.buildMessage();
      if (isDirty_msgBuilder1_6) {
        asciiConsoleLogger_7.publishMessage(msgBuilder1_6);
      }
    }
    if (isDirty_handlerPosition) {
      auditInvocation(corePosition, "corePosition", "onEvent", typedEvent);
      isDirty_corePosition = corePosition.onEvent();
    }
    if (isDirty_corePosition) {
      auditInvocation(baseQuantiy, "baseQuantiy", "onEvent", typedEvent);
      isDirty_baseQuantiy = baseQuantiy.onEvent();
    }
    if (isDirty_baseQuantiy) {
      auditInvocation(
          push_Number_To_setBase0_14, "push_Number_To_setBase0_14", "onEvent", typedEvent);
      isDirty_push_Number_To_setBase0_14 = push_Number_To_setBase0_14.onEvent();
    }
    if (isDirty_corePosition) {
      auditInvocation(
          push_getBaseCcy_To_setBaseCcy0_9,
          "push_getBaseCcy_To_setBaseCcy0_9",
          "onEvent",
          typedEvent);
      isDirty_push_getBaseCcy_To_setBaseCcy0_9 = push_getBaseCcy_To_setBaseCcy0_9.onEvent();
    }
    if (isDirty_corePosition) {
      auditInvocation(
          push_getTermsCcy_To_setTermsCcy0_10,
          "push_getTermsCcy_To_setTermsCcy0_10",
          "onEvent",
          typedEvent);
      isDirty_push_getTermsCcy_To_setTermsCcy0_10 = push_getTermsCcy_To_setTermsCcy0_10.onEvent();
    }
    if (isDirty_corePosition) {
      auditInvocation(
          push_getTradeType_To_setTradeType0_11,
          "push_getTradeType_To_setTradeType0_11",
          "onEvent",
          typedEvent);
      isDirty_push_getTradeType_To_setTradeType0_11 =
          push_getTradeType_To_setTradeType0_11.onEvent();
    }
    if (isDirty_corePosition) {
      auditInvocation(termsQuantiy, "termsQuantiy", "onEvent", typedEvent);
      isDirty_termsQuantiy = termsQuantiy.onEvent();
    }
    if (isDirty_termsQuantiy) {
      auditInvocation(
          push_Number_To_setTerms0_17, "push_Number_To_setTerms0_17", "onEvent", typedEvent);
      isDirty_push_Number_To_setTerms0_17 = push_Number_To_setTerms0_17.onEvent();
    }
    if (isDirty_push_Number_To_setBase0_14
        | isDirty_push_Number_To_setTerms0_17
        | isDirty_push_getBaseCcy_To_setBaseCcy0_9
        | isDirty_push_getTermsCcy_To_setTermsCcy0_10
        | isDirty_push_getTradeType_To_setTradeType0_11) {
      auditInvocation(nodeWrapper_18, "nodeWrapper_18", "onEvent", typedEvent);
      isDirty_nodeWrapper_18 = nodeWrapper_18.onEvent();
    }
    if (isDirty_nodeWrapper_18) {
      auditInvocation(clientTrades, "clientTrades", "onEvent", typedEvent);
      isDirty_clientTrades = clientTrades.onEvent();
    }
    if (isDirty_clientTrades) {
      auditInvocation(msgBuilder3_23, "msgBuilder3_23", "buildMessage", typedEvent);
      isDirty_msgBuilder3_23 = msgBuilder3_23.buildMessage();
      if (isDirty_msgBuilder3_23) {
        asciiConsoleLogger_7.publishMessage(msgBuilder3_23);
      }
    }
    if (isDirty_clientTrades) {
      auditInvocation(
          push_Trade_To_publishHouseTrade0_22,
          "push_Trade_To_publishHouseTrade0_22",
          "onEvent",
          typedEvent);
      isDirty_push_Trade_To_publishHouseTrade0_22 = push_Trade_To_publishHouseTrade0_22.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(LogControlEvent typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_FILTER]
      case ("CHANGE_FILTER"):
        auditInvocation(msgBuilder1_6, "msgBuilder1_6", "controlLogIdFilter", typedEvent);
        isDirty_msgBuilder1_6 = msgBuilder1_6.controlLogIdFilter(typedEvent);
        auditInvocation(msgBuilder3_23, "msgBuilder3_23", "controlLogIdFilter", typedEvent);
        isDirty_msgBuilder3_23 = msgBuilder3_23.controlLogIdFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LEVEL]
      case ("CHANGE_LEVEL"):
        auditInvocation(msgBuilder1_6, "msgBuilder1_6", "controlLogLevelFilter", typedEvent);
        isDirty_msgBuilder1_6 = msgBuilder1_6.controlLogLevelFilter(typedEvent);
        auditInvocation(msgBuilder3_23, "msgBuilder3_23", "controlLogLevelFilter", typedEvent);
        isDirty_msgBuilder3_23 = msgBuilder3_23.controlLogLevelFilter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_LEVEL]
      case ("RECORD_LEVEL"):
        auditInvocation(
            asciiConsoleLogger_7, "asciiConsoleLogger_7", "controlLevelLogging", typedEvent);
        asciiConsoleLogger_7.controlLevelLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_NAME]
      case ("RECORD_NAME"):
        auditInvocation(
            asciiConsoleLogger_7, "asciiConsoleLogger_7", "controlIdLogging", typedEvent);
        asciiConsoleLogger_7.controlIdLogging(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[RECORD_TIME]
      case ("RECORD_TIME"):
        auditInvocation(
            asciiConsoleLogger_7, "asciiConsoleLogger_7", "controlTimeLogging", typedEvent);
        asciiConsoleLogger_7.controlTimeLogging(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    eventLogger.eventReceived(typedEvent);
    clock.eventReceived(typedEvent);
  }

  private void auditInvocation(Object node, String nodeName, String methodName, Object typedEvent) {
    eventLogger.nodeInvoked(node, nodeName, methodName, typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(houseTradePublisher_21, "houseTradePublisher_21");
    auditor.nodeRegistered(myClockHolder_3, "myClockHolder_3");
    auditor.nodeRegistered(trade_8, "trade_8");
    auditor.nodeRegistered(corePosition, "corePosition");
    auditor.nodeRegistered(clientTrades, "clientTrades");
    auditor.nodeRegistered(baseQuantiy, "baseQuantiy");
    auditor.nodeRegistered(termsQuantiy, "termsQuantiy");
    auditor.nodeRegistered(msgBuilder1_6, "msgBuilder1_6");
    auditor.nodeRegistered(msgBuilder3_23, "msgBuilder3_23");
    auditor.nodeRegistered(push_Number_To_setBase0_14, "push_Number_To_setBase0_14");
    auditor.nodeRegistered(push_Number_To_setTerms0_17, "push_Number_To_setTerms0_17");
    auditor.nodeRegistered(
        push_Trade_To_publishHouseTrade0_22, "push_Trade_To_publishHouseTrade0_22");
    auditor.nodeRegistered(push_getBaseCcy_To_setBaseCcy0_9, "push_getBaseCcy_To_setBaseCcy0_9");
    auditor.nodeRegistered(
        push_getTermsCcy_To_setTermsCcy0_10, "push_getTermsCcy_To_setTermsCcy0_10");
    auditor.nodeRegistered(
        push_getTradeType_To_setTradeType0_11, "push_getTradeType_To_setTradeType0_11");
    auditor.nodeRegistered(handlerPosition, "handlerPosition");
    auditor.nodeRegistered(asciiConsoleLogger_7, "asciiConsoleLogger_7");
    auditor.nodeRegistered(nodeWrapper_18, "nodeWrapper_18");
    auditor.nodeRegistered(lambdaFunction_19, "lambdaFunction_19");
    auditor.nodeRegistered(delta_12, "delta_12");
    auditor.nodeRegistered(delta_15, "delta_15");
    auditor.nodeRegistered(string_4, "string_4");
  }

  @Override
  public void afterEvent() {
    msgBuilder3_23.afterEvent();
    clientTrades.resetAfterEvent();
    termsQuantiy.resetAfterEvent();
    baseQuantiy.resetAfterEvent();
    corePosition.resetAfterEvent();
    msgBuilder1_6.afterEvent();
    eventLogger.processingComplete();
    clock.processingComplete();
    isDirty_baseQuantiy = false;
    isDirty_clientTrades = false;
    isDirty_clock = false;
    isDirty_corePosition = false;
    isDirty_handlerPosition = false;
    isDirty_msgBuilder1_6 = false;
    isDirty_msgBuilder3_23 = false;
    isDirty_nodeWrapper_18 = false;
    isDirty_push_Number_To_setBase0_14 = false;
    isDirty_push_Number_To_setTerms0_17 = false;
    isDirty_push_Trade_To_publishHouseTrade0_22 = false;
    isDirty_push_getBaseCcy_To_setBaseCcy0_9 = false;
    isDirty_push_getTermsCcy_To_setTermsCcy0_10 = false;
    isDirty_push_getTradeType_To_setTradeType0_11 = false;
    isDirty_termsQuantiy = false;
  }

  @Override
  public void init() {
    clock.init();
    lambdaFunction_19.init();
    msgBuilder1_6.init();
    corePosition.init();
    baseQuantiy.init();
    termsQuantiy.init();
    clientTrades.init();
    msgBuilder3_23.init();
    asciiConsoleLogger_7.init();
  }

  @Override
  public void tearDown() {
    clock.tearDown();
    eventLogger.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
