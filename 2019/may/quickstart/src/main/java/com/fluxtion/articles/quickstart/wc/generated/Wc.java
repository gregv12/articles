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
package com.fluxtion.articles.quickstart.wc.generated;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.quickstart.wc.WordCounter;
import com.fluxtion.ext.text.api.event.CharEvent;

public class Wc implements EventHandler, BatchHandler, Lifecycle {

  //Node declarations
  public final WordCounter processor = new WordCounter();
  //Dirty flags

  //Filter constants

  public Wc() {}

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
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[9]
      case (9):
        processor.onAnyChar(typedEvent);
        processor.onTabDelimiter(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[10]
      case (10):
        processor.onAnyChar(typedEvent);
        processor.onEol(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[13]
      case (13):
        processor.onAnyChar(typedEvent);
        processor.onCarriageReturn(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.ext.text.api.event.CharEvent] filterId:[32]
      case (32):
        processor.onAnyChar(typedEvent);
        processor.onSpaceDelimiter(typedEvent);
        afterEvent();
        return;
    }
    //Default, no filter methods
    processor.onAnyChar(typedEvent);
    processor.onUnmatchedChar(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  @Override
  public void afterEvent() {}

  @Override
  public void init() {}

  @Override
  public void tearDown() {
    processor.printReport();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
