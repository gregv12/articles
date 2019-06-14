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
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeq;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeqBefore;
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
  private final Count count_7 = new Count();
  private final ReusableEventHandler handlerEofEvent =
      new ReusableEventHandler(2147483647, EofEvent.class);
  private final LambdaFunction lambdaFunction_4 =
      new LambdaFunction("lambda$buildLambda$ca54918b$1_0");
  private final Map_EofEvent_With_apply0 map_EofEvent_With_apply0_5 =
      new Map_EofEvent_With_apply0();
  public final ConsoleLog consoleMsg_1 =
      new ConsoleLog(map_EofEvent_With_apply0_5, "Results\n--------------");
  private final SubSeqBefore subSeqBefore_1 = new SubSeqBefore(',');
  private final SubSeq subSeq_0 = new SubSeq(0, 6);
  private final VoterCsvDecoder0 voterCsvDecoder0_2 = new VoterCsvDecoder0();
  private final EventPublsher eventPublsher_3 = new EventPublsher();
  private final Map_Voter_With_increment0 map_Voter_With_increment0_8 =
      new Map_Voter_With_increment0();
  private final BooleanFilter booleanFilter_9 =
      new BooleanFilter(map_Voter_With_increment0_8, handlerEofEvent);
  public final ConsoleLog consoleMsg_2 = new ConsoleLog(booleanFilter_9, "count:");
  private final ValidationLogger validationLogger_11 = new ValidationLogger("validationLog");
  private final ValidationLogSink validationLogSink_12 = new ValidationLogSink("validationLogSink");
  //Dirty flags
  private boolean isDirty_booleanFilter_9 = false;
  private boolean isDirty_consoleMsg_1 = false;
  private boolean isDirty_consoleMsg_2 = false;
  private boolean isDirty_eventPublsher_3 = false;
  private boolean isDirty_handlerEofEvent = false;
  private boolean isDirty_map_EofEvent_With_apply0_5 = false;
  private boolean isDirty_map_Voter_With_increment0_8 = false;
  private boolean isDirty_validationLogSink_12 = false;
  private boolean isDirty_voterCsvDecoder0_2 = false;
  //Filter constants

  public VoterProcessor() {
    map_EofEvent_With_apply0_5.setAlwaysReset(false);
    map_EofEvent_With_apply0_5.setNotifyOnChangeOnly(false);
    map_EofEvent_With_apply0_5.setResetImmediate(true);
    map_EofEvent_With_apply0_5.filterSubject = handlerEofEvent;
    map_EofEvent_With_apply0_5.f = lambdaFunction_4;
    map_Voter_With_increment0_8.setAlwaysReset(false);
    map_Voter_With_increment0_8.setNotifyOnChangeOnly(false);
    map_Voter_With_increment0_8.setResetImmediate(true);
    map_Voter_With_increment0_8.filterSubject = voterCsvDecoder0_2;
    map_Voter_With_increment0_8.f = count_7;
    voterCsvDecoder0_2.errorLog = validationLogger_11;
    voterCsvDecoder0_2.subSeq_0 = subSeq_0;
    voterCsvDecoder0_2.subSeqBefore_1 = subSeqBefore_1;
    consoleMsg_2.setMethodSupplier("intValue");
    validationLogSink_12.setPublishLogImmediately(true);
    validationLogger_11.logSink = validationLogSink_12;
    eventPublsher_3.publishOnValidate = (boolean) false;
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.eventId()) {
//      case (LogControlEvent.ID):
//        {
//          LogControlEvent typedEvent = (LogControlEvent) event;
//          handleEvent(typedEvent);
//          break;
//        }
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
//      case (RegisterEventHandler.ID):
//        {
//          RegisterEventHandler typedEvent = (RegisterEventHandler) event;
//          handleEvent(typedEvent);
//          break;
//        }
    }
  }

  public void handleEvent(LogControlEvent typedEvent) {
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.ext.streaming.api.log.LogControlEvent] filterString:[CHANGE_LOG_PROVIDER]
      case ("CHANGE_LOG_PROVIDER"):
        isDirty_validationLogSink_12 = true;
        validationLogSink_12.controlLogProvider(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(CharEvent typedEvent) {
    //Default, no filter methods
    isDirty_voterCsvDecoder0_2 = voterCsvDecoder0_2.charEvent(typedEvent);
    if (isDirty_voterCsvDecoder0_2) {
      eventPublsher_3.wrapperUpdate(voterCsvDecoder0_2);
      map_Voter_With_increment0_8.updated_filterSubject(voterCsvDecoder0_2);
    }
    if (isDirty_voterCsvDecoder0_2) {
      isDirty_map_Voter_With_increment0_8 = map_Voter_With_increment0_8.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerEofEvent = true;
    handlerEofEvent.onEvent(typedEvent);
    if (isDirty_handlerEofEvent) {
      map_EofEvent_With_apply0_5.updated_filterSubject(handlerEofEvent);
    }
    if (isDirty_handlerEofEvent) {
      isDirty_map_EofEvent_With_apply0_5 = map_EofEvent_With_apply0_5.onEvent();
    }
    if (isDirty_map_EofEvent_With_apply0_5) {
      isDirty_consoleMsg_1 = consoleMsg_1.log();
    }
    isDirty_voterCsvDecoder0_2 = voterCsvDecoder0_2.eof(typedEvent);
    if (isDirty_voterCsvDecoder0_2) {
      eventPublsher_3.wrapperUpdate(voterCsvDecoder0_2);
      map_Voter_With_increment0_8.updated_filterSubject(voterCsvDecoder0_2);
    }
    if (isDirty_voterCsvDecoder0_2) {
      isDirty_map_Voter_With_increment0_8 = map_Voter_With_increment0_8.onEvent();
    }
    if (isDirty_handlerEofEvent) {
      isDirty_booleanFilter_9 = booleanFilter_9.updated();
    }
    if (isDirty_booleanFilter_9) {
      isDirty_consoleMsg_2 = consoleMsg_2.log();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(RegisterEventHandler typedEvent) {
    //Default, no filter methods
    isDirty_eventPublsher_3 = true;
    eventPublsher_3.registerEventHandler(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {
    map_Voter_With_increment0_8.resetAfterEvent();
    isDirty_booleanFilter_9 = false;
    isDirty_consoleMsg_1 = false;
    isDirty_consoleMsg_2 = false;
    isDirty_eventPublsher_3 = false;
    isDirty_handlerEofEvent = false;
    isDirty_map_EofEvent_With_apply0_5 = false;
    isDirty_map_Voter_With_increment0_8 = false;
    isDirty_validationLogSink_12 = false;
    isDirty_voterCsvDecoder0_2 = false;
  }

  @Override
  public void init() {
    lambdaFunction_4.init();
    map_EofEvent_With_apply0_5.init();
    consoleMsg_1.init();
    voterCsvDecoder0_2.init();
    eventPublsher_3.init();
    map_Voter_With_increment0_8.init();
    consoleMsg_2.init();
    validationLogSink_12.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
