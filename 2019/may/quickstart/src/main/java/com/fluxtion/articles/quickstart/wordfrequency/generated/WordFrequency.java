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
package com.fluxtion.articles.quickstart.wordfrequency.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.quickstart.wordfrequency.StatsPrinter;
import com.fluxtion.ext.text.api.ascii.ByteBufferDelimiter;
import com.fluxtion.ext.text.api.event.CharEvent;

public class WordFrequency implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final AsciiAnyCharMatcher_0 asciiAnyCharMatcher_0_5 = new AsciiAnyCharMatcher_0();
  private final ByteBufferDelimiter byteBufferDelimiter_4 =
      new ByteBufferDelimiter(" \n", ",.;-_:[]{}()?|&*'\\\"\r");
  private final GroupBy_2 groupBy_2_1 = new GroupBy_2();
  private final StatsPrinter statsPrinter_3 = new StatsPrinter(groupBy_2_1);
  //Dirty flags
  private boolean isDirty_asciiAnyCharMatcher_0_5 = false;
  private boolean isDirty_byteBufferDelimiter_4 = false;
  //Filter constants

  public WordFrequency() {
    groupBy_2_1.byteBufferDelimiter0 = byteBufferDelimiter_4;
    byteBufferDelimiter_4.delimiterNotifier = asciiAnyCharMatcher_0_5;
  }

  @Override
  public void onEvent(com.fluxtion.api.event.Event event) {
    switch (event.eventId()) {
      case (CharEvent.ID):
        {
          CharEvent typedEvent = (CharEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(CharEvent typedEvent) {
    switch (typedEvent.filterId()) {
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[10]
      case (10):
        isDirty_asciiAnyCharMatcher_0_5 = asciiAnyCharMatcher_0_5.onChar_newLine(typedEvent);
        if (isDirty_asciiAnyCharMatcher_0_5) {
          byteBufferDelimiter_4.onDelimiter(asciiAnyCharMatcher_0_5);
        }
        isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.appendToBuffer(typedEvent);
        if (isDirty_byteBufferDelimiter_4) {
          groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
        }
        if (isDirty_asciiAnyCharMatcher_0_5) {
          isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.onEvent();
          if (isDirty_byteBufferDelimiter_4) {
            groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
          }
        }
        //event stack unwind callbacks
        if (isDirty_asciiAnyCharMatcher_0_5) {
          byteBufferDelimiter_4.onEventComplete();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[32]
      case (32):
        isDirty_asciiAnyCharMatcher_0_5 = asciiAnyCharMatcher_0_5.onChar_space(typedEvent);
        if (isDirty_asciiAnyCharMatcher_0_5) {
          byteBufferDelimiter_4.onDelimiter(asciiAnyCharMatcher_0_5);
        }
        isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.appendToBuffer(typedEvent);
        if (isDirty_byteBufferDelimiter_4) {
          groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
        }
        if (isDirty_asciiAnyCharMatcher_0_5) {
          isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.onEvent();
          if (isDirty_byteBufferDelimiter_4) {
            groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
          }
        }
        //event stack unwind callbacks
        if (isDirty_asciiAnyCharMatcher_0_5) {
          byteBufferDelimiter_4.onEventComplete();
        }
        afterEvent();
        return;
    }
    //Default, no filter methods
    isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.appendToBuffer(typedEvent);
    if (isDirty_byteBufferDelimiter_4) {
      groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
    }
    if (isDirty_asciiAnyCharMatcher_0_5) {
      isDirty_byteBufferDelimiter_4 = byteBufferDelimiter_4.onEvent();
      if (isDirty_byteBufferDelimiter_4) {
        groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_4);
      }
    }
    //event stack unwind callbacks
    if (isDirty_asciiAnyCharMatcher_0_5) {
      byteBufferDelimiter_4.onEventComplete();
    }
    afterEvent();
  }

  @Override
  public void afterEvent() {

    isDirty_asciiAnyCharMatcher_0_5 = false;
    isDirty_byteBufferDelimiter_4 = false;
  }

  @Override
  public void init() {
    byteBufferDelimiter_4.init();
    groupBy_2_1.init();
  }

  @Override
  public void tearDown() {
    statsPrinter_3.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
