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
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.util.GroupByPrint.PrintFrequencyMap;
import com.fluxtion.ext.text.api.ascii.ByteBufferDelimiter;
import com.fluxtion.ext.text.api.event.CharEvent;
import com.fluxtion.ext.text.api.event.EofEvent;

public class WordFrequency implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  private final AsciiAnyCharMatcher_0 asciiAnyCharMatcher_0_3 = new AsciiAnyCharMatcher_0();
  private final ByteBufferDelimiter byteBufferDelimiter_0 =
      new ByteBufferDelimiter(" \n", ",.;-_:[]{}()?|&*'\\\"\r");
  private final GroupBy_2 groupBy_2_1 = new GroupBy_2();
  private final ReusableEventHandler handlerEofEvent =
      new ReusableEventHandler(2147483647, EofEvent.class);
  private final PrintFrequencyMap printFrequencyMap_2 =
      new PrintFrequencyMap("Word frequency statistics", groupBy_2_1, handlerEofEvent);
  //Dirty flags
  private boolean isDirty_asciiAnyCharMatcher_0_3 = false;
  private boolean isDirty_byteBufferDelimiter_0 = false;
  private boolean isDirty_handlerEofEvent = false;
  //Filter constants

  public WordFrequency() {
    groupBy_2_1.byteBufferDelimiter0 = byteBufferDelimiter_0;
    byteBufferDelimiter_0.delimiterNotifier = asciiAnyCharMatcher_0_3;
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
      case (EofEvent.ID):
        {
          EofEvent typedEvent = (EofEvent) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(CharEvent typedEvent) {
    switch (typedEvent.filterId()) {
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[10]
      case (10):
        isDirty_asciiAnyCharMatcher_0_3 = asciiAnyCharMatcher_0_3.onChar_newLine(typedEvent);
        if (isDirty_asciiAnyCharMatcher_0_3) {
          byteBufferDelimiter_0.onDelimiter(asciiAnyCharMatcher_0_3);
        }
        isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.appendToBuffer(typedEvent);
        if (isDirty_byteBufferDelimiter_0) {
          groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
        }
        if (isDirty_asciiAnyCharMatcher_0_3) {
          isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.onEvent();
          if (isDirty_byteBufferDelimiter_0) {
            groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
          }
        }
        //event stack unwind callbacks
        if (isDirty_asciiAnyCharMatcher_0_3) {
          byteBufferDelimiter_0.onEventComplete();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[32]
      case (32):
        isDirty_asciiAnyCharMatcher_0_3 = asciiAnyCharMatcher_0_3.onChar_space(typedEvent);
        if (isDirty_asciiAnyCharMatcher_0_3) {
          byteBufferDelimiter_0.onDelimiter(asciiAnyCharMatcher_0_3);
        }
        isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.appendToBuffer(typedEvent);
        if (isDirty_byteBufferDelimiter_0) {
          groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
        }
        if (isDirty_asciiAnyCharMatcher_0_3) {
          isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.onEvent();
          if (isDirty_byteBufferDelimiter_0) {
            groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
          }
        }
        //event stack unwind callbacks
        if (isDirty_asciiAnyCharMatcher_0_3) {
          byteBufferDelimiter_0.onEventComplete();
        }
        afterEvent();
        return;
    }
    //Default, no filter methods
    isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.appendToBuffer(typedEvent);
    if (isDirty_byteBufferDelimiter_0) {
      groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
    }
    if (isDirty_asciiAnyCharMatcher_0_3) {
      isDirty_byteBufferDelimiter_0 = byteBufferDelimiter_0.onEvent();
      if (isDirty_byteBufferDelimiter_0) {
        groupBy_2_1.updatebyteBufferDelimiter0(byteBufferDelimiter_0);
      }
    }
    //event stack unwind callbacks
    if (isDirty_asciiAnyCharMatcher_0_3) {
      byteBufferDelimiter_0.onEventComplete();
    }
    afterEvent();
  }

  public void handleEvent(EofEvent typedEvent) {
    //Default, no filter methods
    isDirty_handlerEofEvent = true;
    handlerEofEvent.onEvent(typedEvent);
    if (isDirty_handlerEofEvent) {
      printFrequencyMap_2.printFreqMap();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {

    isDirty_asciiAnyCharMatcher_0_3 = false;
    isDirty_byteBufferDelimiter_0 = false;
    isDirty_handlerEofEvent = false;
  }

  @Override
  public void init() {
    byteBufferDelimiter_0.init();
    groupBy_2_1.init();
  }

  @Override
  public void tearDown() {}

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
