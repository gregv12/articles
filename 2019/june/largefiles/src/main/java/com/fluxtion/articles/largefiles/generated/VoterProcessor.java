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
package com.fluxtion.articles.largefiles.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.largefiles.Main.ResultsPrinter;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeq;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeqBefore;
import com.fluxtion.ext.streaming.api.stream.ListCollector;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;
import com.fluxtion.ext.streaming.api.stream.StreamOperator.ConsoleLog;
import com.fluxtion.ext.streaming.api.test.BooleanFilter;
import com.fluxtion.ext.text.api.csv.ValidationLogSink;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.event.RegisterEventHandler;
import com.fluxtion.ext.text.api.util.EventPublsher;

public class VoterProcessor implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Count count_9 = new Count();
  private final Count count_15 = new Count();
  private final ReusableEventHandler handlerEofEvent =
      new ReusableEventHandler(2147483647, EofEvent.class);
  private final LambdaFunction lambdaFunction_6 =
      new LambdaFunction("lambda$buildLambda$20142c36$1_0");
  private final LambdaFunction lambdaFunction_17 =
      new LambdaFunction("lambda$buildLambda$ca54918b$1_1");
  private final ListCollector listCollector_19 = new ListCollector();
  private final Map_EofEvent_With_apply0 map_EofEvent_With_apply0_7 =
      new Map_EofEvent_With_apply0();
  public final ConsoleLog consoleMsg_1 =
      new ConsoleLog(map_EofEvent_With_apply0_7, "Results\n--------------");
  private final SubSeqBefore subSeqBefore_3 = new SubSeqBefore(',');
  private final SubSeq subSeq_2 = new SubSeq(0, 6);
  private final VoterCsvDecoder0 voterCsvDecoder0_4 = new VoterCsvDecoder0();
  private final EventPublsher eventPublsher_5 = new EventPublsher();
  private final GroupBy_2 groupBy_2_13 = new GroupBy_2();
  private final GroupBy_5 groupBy_5_14 = new GroupBy_5();
  private final Map_Voter_With_apply0 map_Voter_With_apply0_18 = new Map_Voter_With_apply0();
  private final Map_Object_With_addItem0 map_Object_With_addItem0_20 =
      new Map_Object_With_addItem0();
  private final Map_Voter_With_increment0 map_Voter_With_increment0_10 =
      new Map_Voter_With_increment0();
  private final BooleanFilter booleanFilter_11 =
      new BooleanFilter(map_Voter_With_increment0_10, map_EofEvent_With_apply0_7);
  public final ConsoleLog consoleMsg_2 = new ConsoleLog(booleanFilter_11, "count:");
  private final Map_Voter_With_increment0 map_Voter_With_increment0_16 =
      new Map_Voter_With_increment0();
  private final ResultsPrinter resultsPrinter_1 =
      new ResultsPrinter(
          groupBy_2_13, groupBy_5_14, map_Voter_With_increment0_16, map_Object_With_addItem0_20);
  private final ValidationLogger validationLogger_21 = new ValidationLogger("validationLog");
  private final ValidationLogSink validationLogSink_22 = new ValidationLogSink("validationLogSink");
  //Dirty flags
  private boolean isDirty_booleanFilter_11 = false;
  private boolean isDirty_consoleMsg_1 = false;
  private boolean isDirty_consoleMsg_2 = false;
  private boolean isDirty_eventPublsher_5 = false;
  private boolean isDirty_handlerEofEvent = false;
  private boolean isDirty_map_EofEvent_With_apply0_7 = false;
  private boolean isDirty_map_Object_With_addItem0_20 = false;
  private boolean isDirty_map_Voter_With_apply0_18 = false;
  private boolean isDirty_map_Voter_With_increment0_10 = false;
  private boolean isDirty_map_Voter_With_increment0_16 = false;
  private boolean isDirty_resultsPrinter_1 = false;
  private boolean isDirty_validationLogSink_22 = false;
  private boolean isDirty_voterCsvDecoder0_4 = false;
  //Filter constants

  public VoterProcessor() {
    groupBy_2_13.voterCsvDecoder00 = voterCsvDecoder0_4;
    groupBy_5_14.voterCsvDecoder00 = voterCsvDecoder0_4;
    map_EofEvent_With_apply0_7.setAlwaysReset(false);
    map_EofEvent_With_apply0_7.setNotifyOnChangeOnly(false);
    map_EofEvent_With_apply0_7.setResetImmediate(true);
    map_EofEvent_With_apply0_7.filterSubject = handlerEofEvent;
    map_EofEvent_With_apply0_7.f = lambdaFunction_6;
    map_Object_With_addItem0_20.setAlwaysReset(false);
    map_Object_With_addItem0_20.setNotifyOnChangeOnly(false);
    map_Object_With_addItem0_20.setResetImmediate(true);
    map_Object_With_addItem0_20.filterSubject = map_Voter_With_apply0_18;
    map_Object_With_addItem0_20.f = listCollector_19;
    map_Voter_With_apply0_18.setAlwaysReset(false);
    map_Voter_With_apply0_18.setNotifyOnChangeOnly(false);
    map_Voter_With_apply0_18.setResetImmediate(true);
    map_Voter_With_apply0_18.filterSubject = voterCsvDecoder0_4;
    map_Voter_With_apply0_18.f = lambdaFunction_17;
    map_Voter_With_increment0_10.setAlwaysReset(false);
    map_Voter_With_increment0_10.setNotifyOnChangeOnly(false);
    map_Voter_With_increment0_10.setResetImmediate(true);
    map_Voter_With_increment0_10.filterSubject = voterCsvDecoder0_4;
    map_Voter_With_increment0_10.f = count_9;
    map_Voter_With_increment0_16.setAlwaysReset(false);
    map_Voter_With_increment0_16.setNotifyOnChangeOnly(false);
    map_Voter_With_increment0_16.setResetImmediate(true);
    map_Voter_With_increment0_16.filterSubject = voterCsvDecoder0_4;
    map_Voter_With_increment0_16.f = count_15;
    voterCsvDecoder0_4.errorLog = validationLogger_21;
    voterCsvDecoder0_4.subSeq_0 = subSeq_2;
    voterCsvDecoder0_4.subSeqBefore_1 = subSeqBefore_3;
    consoleMsg_2.setMethodSupplier("intValue");
    validationLogSink_22.setPublishLogImmediately(true);
    validationLogger_21.logSink = validationLogSink_22;
    eventPublsher_5.publishOnValidate = (boolean) false;
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
      case (RegisterEventHandler.ID):
        {
          RegisterEventHandler typedEvent = (RegisterEventHandler) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LOG_PROVIDER]
      case ("CHANGE_LOG_PROVIDER"):
        isDirty_validationLogSink_22 = true;
        validationLogSink_22.controlLogProvider(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(CharEvent typedEvent) {
    //Default, no filter methods
    isDirty_voterCsvDecoder0_4 = voterCsvDecoder0_4.charEvent(typedEvent);
    if (isDirty_voterCsvDecoder0_4) {
      eventPublsher_5.wrapperUpdate(voterCsvDecoder0_4);
      groupBy_2_13.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      groupBy_5_14.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      map_Voter_With_apply0_18.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_With_increment0_10.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_With_increment0_16.updated_filterSubject(voterCsvDecoder0_4);
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_apply0_18 = map_Voter_With_apply0_18.onEvent();
      if (isDirty_map_Voter_With_apply0_18) {
        map_Object_With_addItem0_20.updated_filterSubject(map_Voter_With_apply0_18);
      }
    }
    if (isDirty_map_Voter_With_apply0_18) {
      isDirty_map_Object_With_addItem0_20 = map_Object_With_addItem0_20.onEvent();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_increment0_10 = map_Voter_With_increment0_10.onEvent();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_increment0_16 = map_Voter_With_increment0_16.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerEofEvent = true;
    handlerEofEvent.onEvent(typedEvent);
    if (isDirty_handlerEofEvent) {
      map_EofEvent_With_apply0_7.updated_filterSubject(handlerEofEvent);
    }
    if (isDirty_handlerEofEvent) {
      isDirty_map_EofEvent_With_apply0_7 = map_EofEvent_With_apply0_7.onEvent();
    }
    if (isDirty_map_EofEvent_With_apply0_7) {
      isDirty_consoleMsg_1 = consoleMsg_1.log();
    }
    isDirty_voterCsvDecoder0_4 = voterCsvDecoder0_4.eof(typedEvent);
    if (isDirty_voterCsvDecoder0_4) {
      eventPublsher_5.wrapperUpdate(voterCsvDecoder0_4);
      groupBy_2_13.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      groupBy_5_14.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      map_Voter_With_apply0_18.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_With_increment0_10.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_With_increment0_16.updated_filterSubject(voterCsvDecoder0_4);
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_apply0_18 = map_Voter_With_apply0_18.onEvent();
      if (isDirty_map_Voter_With_apply0_18) {
        map_Object_With_addItem0_20.updated_filterSubject(map_Voter_With_apply0_18);
      }
    }
    if (isDirty_map_Voter_With_apply0_18) {
      isDirty_map_Object_With_addItem0_20 = map_Object_With_addItem0_20.onEvent();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_increment0_10 = map_Voter_With_increment0_10.onEvent();
    }
    if (isDirty_map_EofEvent_With_apply0_7) {
      isDirty_booleanFilter_11 = booleanFilter_11.updated();
    }
    if (isDirty_booleanFilter_11) {
      isDirty_consoleMsg_2 = consoleMsg_2.log();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_With_increment0_16 = map_Voter_With_increment0_16.onEvent();
    }
    isDirty_resultsPrinter_1 = true;
    resultsPrinter_1.eof(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(RegisterEventHandler typedEvent) {
    //Default, no filter methods
    isDirty_eventPublsher_5 = true;
    eventPublsher_5.registerEventHandler(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    map_Voter_With_increment0_16.resetAfterEvent();
    map_Voter_With_increment0_10.resetAfterEvent();
    map_Object_With_addItem0_20.resetAfterEvent();
    isDirty_booleanFilter_11 = false;
    isDirty_consoleMsg_1 = false;
    isDirty_consoleMsg_2 = false;
    isDirty_eventPublsher_5 = false;
    isDirty_handlerEofEvent = false;
    isDirty_map_EofEvent_With_apply0_7 = false;
    isDirty_map_Object_With_addItem0_20 = false;
    isDirty_map_Voter_With_apply0_18 = false;
    isDirty_map_Voter_With_increment0_10 = false;
    isDirty_map_Voter_With_increment0_16 = false;
    isDirty_resultsPrinter_1 = false;
    isDirty_validationLogSink_22 = false;
    isDirty_voterCsvDecoder0_4 = false;
  }

  @Override
  public void init() {
    lambdaFunction_6.init();
    lambdaFunction_17.init();
    map_EofEvent_With_apply0_7.init();
    consoleMsg_1.init();
    voterCsvDecoder0_4.init();
    eventPublsher_5.init();
    groupBy_2_13.init();
    groupBy_5_14.init();
    map_Voter_With_apply0_18.init();
    map_Object_With_addItem0_20.init();
    map_Voter_With_increment0_10.init();
    consoleMsg_2.init();
    map_Voter_With_increment0_16.init();
    validationLogSink_22.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
