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
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeq;
import com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.SubSeqBefore;
import com.fluxtion.ext.streaming.api.stream.ListCollector;
import com.fluxtion.ext.streaming.api.stream.SerialisedFunctionHelper.LambdaFunction;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;
import com.fluxtion.ext.text.api.csv.ValidationLogSink;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.event.RegisterEventHandler;
import com.fluxtion.ext.text.api.util.EventPublsher;

public class VoterProcessor implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final Count count_8 = new Count();
  private final LambdaFunction lambdaFunction_10 =
      new LambdaFunction("lambda$buildLambda$ca54918b$1_0");
  private final ListCollector listCollector_12 = new ListCollector();
  private final SubSeqBefore subSeqBefore_3 = new SubSeqBefore(',');
  private final SubSeq subSeq_2 = new SubSeq(0, 6);
  private final VoterCsvDecoder0 voterCsvDecoder0_4 = new VoterCsvDecoder0();
  private final EventPublsher eventPublsher_5 = new EventPublsher();
  private final GroupBy_2 groupBy_2_6 = new GroupBy_2();
  private final GroupBy_5 groupBy_5_7 = new GroupBy_5();
  private final Map_Voter_By_apply0 map_Voter_By_apply0_11 = new Map_Voter_By_apply0();
  private final Map_Object_By_addItem0 map_Object_By_addItem0_13 = new Map_Object_By_addItem0();
  private final Map_Voter_By_increment0 map_Voter_By_increment0_9 = new Map_Voter_By_increment0();
  private final ResultsPrinter resultsPrinter_1 =
      new ResultsPrinter(
          groupBy_2_6, groupBy_5_7, map_Voter_By_increment0_9, map_Object_By_addItem0_13);
  private final ValidationLogger validationLogger_14 = new ValidationLogger("validationLog");
  private final ValidationLogSink validationLogSink_15 = new ValidationLogSink("validationLogSink");
  //Dirty flags
  private boolean isDirty_eventPublsher_5 = false;
  private boolean isDirty_map_Object_By_addItem0_13 = false;
  private boolean isDirty_map_Voter_By_apply0_11 = false;
  private boolean isDirty_map_Voter_By_increment0_9 = false;
  private boolean isDirty_resultsPrinter_1 = false;
  private boolean isDirty_validationLogSink_15 = false;
  private boolean isDirty_voterCsvDecoder0_4 = false;
  //Filter constants

  public VoterProcessor() {
    groupBy_2_6.voterCsvDecoder00 = voterCsvDecoder0_4;
    groupBy_5_7.voterCsvDecoder00 = voterCsvDecoder0_4;
    map_Object_By_addItem0_13.setAlwaysReset(false);
    map_Object_By_addItem0_13.setNotifyOnChangeOnly(false);
    map_Object_By_addItem0_13.setResetImmediate(true);
    map_Object_By_addItem0_13.filterSubject = map_Voter_By_apply0_11;
    map_Object_By_addItem0_13.f = listCollector_12;
    map_Voter_By_apply0_11.setAlwaysReset(false);
    map_Voter_By_apply0_11.setNotifyOnChangeOnly(false);
    map_Voter_By_apply0_11.setResetImmediate(true);
    map_Voter_By_apply0_11.filterSubject = voterCsvDecoder0_4;
    map_Voter_By_apply0_11.f = lambdaFunction_10;
    map_Voter_By_increment0_9.setAlwaysReset(false);
    map_Voter_By_increment0_9.setNotifyOnChangeOnly(false);
    map_Voter_By_increment0_9.setResetImmediate(true);
    map_Voter_By_increment0_9.filterSubject = voterCsvDecoder0_4;
    map_Voter_By_increment0_9.f = count_8;
    voterCsvDecoder0_4.errorLog = validationLogger_14;
    voterCsvDecoder0_4.subSeq_0 = subSeq_2;
    voterCsvDecoder0_4.subSeqBefore_1 = subSeqBefore_3;
    validationLogSink_15.setPublishLogImmediately(true);
    validationLogger_14.logSink = validationLogSink_15;
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
        isDirty_validationLogSink_15 = true;
        validationLogSink_15.controlLogProvider(typedEvent);
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
      groupBy_2_6.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      groupBy_5_7.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      map_Voter_By_apply0_11.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_By_increment0_9.updated_filterSubject(voterCsvDecoder0_4);
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_By_apply0_11 = map_Voter_By_apply0_11.onEvent();
      if (isDirty_map_Voter_By_apply0_11) {
        map_Object_By_addItem0_13.updated_filterSubject(map_Voter_By_apply0_11);
      }
    }
    if (isDirty_map_Voter_By_apply0_11) {
      isDirty_map_Object_By_addItem0_13 = map_Object_By_addItem0_13.onEvent();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_By_increment0_9 = map_Voter_By_increment0_9.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_voterCsvDecoder0_4 = voterCsvDecoder0_4.eof(typedEvent);
    if (isDirty_voterCsvDecoder0_4) {
      eventPublsher_5.wrapperUpdate(voterCsvDecoder0_4);
      groupBy_2_6.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      groupBy_5_7.updatevoterCsvDecoder00(voterCsvDecoder0_4);
      map_Voter_By_apply0_11.updated_filterSubject(voterCsvDecoder0_4);
      map_Voter_By_increment0_9.updated_filterSubject(voterCsvDecoder0_4);
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_By_apply0_11 = map_Voter_By_apply0_11.onEvent();
      if (isDirty_map_Voter_By_apply0_11) {
        map_Object_By_addItem0_13.updated_filterSubject(map_Voter_By_apply0_11);
      }
    }
    if (isDirty_map_Voter_By_apply0_11) {
      isDirty_map_Object_By_addItem0_13 = map_Object_By_addItem0_13.onEvent();
    }
    if (isDirty_voterCsvDecoder0_4) {
      isDirty_map_Voter_By_increment0_9 = map_Voter_By_increment0_9.onEvent();
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
    map_Voter_By_increment0_9.resetAfterEvent();
    map_Object_By_addItem0_13.resetAfterEvent();
    isDirty_eventPublsher_5 = false;
    isDirty_map_Object_By_addItem0_13 = false;
    isDirty_map_Voter_By_apply0_11 = false;
    isDirty_map_Voter_By_increment0_9 = false;
    isDirty_resultsPrinter_1 = false;
    isDirty_validationLogSink_15 = false;
    isDirty_voterCsvDecoder0_4 = false;
  }

  @Override
  public void init() {
    lambdaFunction_10.init();
    voterCsvDecoder0_4.init();
    eventPublsher_5.init();
    groupBy_2_6.init();
    groupBy_5_7.init();
    map_Voter_By_apply0_11.init();
    map_Object_By_addItem0_13.init();
    map_Voter_By_increment0_9.init();
    validationLogSink_15.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
