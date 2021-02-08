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
package com.fluxtion.articles.aot_csv.generated;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.Lifecycle;

import com.fluxtion.articles.aot_csv.StatsCollector;
import com.fluxtion.ext.streaming.api.log.LogControlEvent;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Count;
import com.fluxtion.ext.text.api.csv.ValidationLogSink;
import com.fluxtion.ext.text.api.csv.ValidationLogger;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.util.marshaller.CharProcessor;

/*
 * <pre>
 * generation time   : 2021-02-08T08:47:47.730717300
 * generator version : 2.10.34
 * api version       : 2.10.34
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class CsvCityProcessor
    implements StaticEventProcessor, BatchHandler, Lifecycle, CharProcessor {

  //Node declarations
  private final CityCsvDecoder0 cityCsvDecoder0_0 = new CityCsvDecoder0();
  private final Count count_1 = new Count();
  private final Map_City_With_increment0 map_City_With_increment0_2 =
      new Map_City_With_increment0();
  private final Push_Number_To_rowProcessed0 push_Number_To_rowProcessed0_4 =
      new Push_Number_To_rowProcessed0();
  private final StatsCollector statsCollector_3 = new StatsCollector();
  private final ValidationLogger validationLogger_5 = new ValidationLogger("validationLog");
  private final ValidationLogSink validationLogSink_6 = new ValidationLogSink("validationLogSink");
  //Dirty flags
  private boolean isDirty_cityCsvDecoder0_0 = false;
  private boolean isDirty_map_City_With_increment0_2 = false;
  private boolean isDirty_push_Number_To_rowProcessed0_4 = false;
  //Filter constants

  public CsvCityProcessor() {
    cityCsvDecoder0_0.errorLog = validationLogger_5;
    map_City_With_increment0_2.setNotifyOnChangeOnly(false);
    map_City_With_increment0_2.setValidOnStart(false);
    map_City_With_increment0_2.filterSubject = cityCsvDecoder0_0;
    map_City_With_increment0_2.f = count_1;
    push_Number_To_rowProcessed0_4.filterSubject = map_City_With_increment0_2;
    push_Number_To_rowProcessed0_4.f = statsCollector_3;
    validationLogSink_6.setPublishLogImmediately(true);
    validationLogger_5.logSink = validationLogSink_6;
  }

  @Override
  public void onEvent(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.ext.streaming.api.log.LogControlEvent"):
        {
          LogControlEvent typedEvent = (LogControlEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.ext.text.api.event.CharEvent"):
        {
          CharEvent typedEvent = (CharEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.ext.text.api.event.EofEvent"):
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
    isDirty_cityCsvDecoder0_0 = cityCsvDecoder0_0.charEvent(typedEvent);
    if (isDirty_cityCsvDecoder0_0) {
      isDirty_map_City_With_increment0_2 = map_City_With_increment0_2.onEvent();
    }
    if (isDirty_map_City_With_increment0_2) {
      isDirty_push_Number_To_rowProcessed0_4 = push_Number_To_rowProcessed0_4.onEvent();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_cityCsvDecoder0_0 = cityCsvDecoder0_0.eof(typedEvent);
    if (isDirty_cityCsvDecoder0_0) {
      isDirty_map_City_With_increment0_2 = map_City_With_increment0_2.onEvent();
    }
    if (isDirty_map_City_With_increment0_2) {
      isDirty_push_Number_To_rowProcessed0_4 = push_Number_To_rowProcessed0_4.onEvent();
    }
    statsCollector_3.eof(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  private void afterEvent() {
    map_City_With_increment0_2.resetAfterEvent();
    isDirty_cityCsvDecoder0_0 = false;
    isDirty_map_City_With_increment0_2 = false;
    isDirty_push_Number_To_rowProcessed0_4 = false;
  }

  @Override
  public void init() {
    cityCsvDecoder0_0.init();
    cityCsvDecoder0_0.reset();
    count_1.reset();
    map_City_With_increment0_2.reset();
    push_Number_To_rowProcessed0_4.reset();
    statsCollector_3.startTimer();
    validationLogSink_6.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
