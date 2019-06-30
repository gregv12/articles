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
package com.fluxtion.articles.lombok.generated.nolombok;

import com.fluxtion.api.lifecycle.BatchHandler;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.api.lifecycle.Lifecycle;
import com.fluxtion.articles.lombok.Main.MyTempProcessor;
import com.fluxtion.articles.lombok.Main.TempEvent;
import com.fluxtion.ext.streaming.api.ReusableEventHandler;
import com.fluxtion.ext.streaming.api.stream.StreamFunctions.Max;

public class TempMonitor implements EventHandler, BatchHandler, Lifecycle {

//Node declarations
    private final ReusableEventHandler handlerTempEvent = new ReusableEventHandler(2147483647, TempEvent.class);
    private final Map_getTemp_With_asDouble0 map_getTemp_With_asDouble0_0 = new Map_getTemp_With_asDouble0();
    private final Max max_1 = new Max();
    private final Map_Number_With_max0 map_Number_With_max0_2 = new Map_Number_With_max0();
    private final Push_Number_To_setMaxTemp0 push_Number_To_setMaxTemp0_4 = new Push_Number_To_setMaxTemp0();
    private final MyTempProcessor myTempProcessor_3 = new MyTempProcessor();
//Dirty flags
    private boolean isDirty_handlerTempEvent = false;
    private boolean isDirty_map_Number_With_max0_2 = false;
    private boolean isDirty_map_getTemp_With_asDouble0_0 = false;
    private boolean isDirty_push_Number_To_setMaxTemp0_4 = false;
//Filter constants

    public TempMonitor() {
        myTempProcessor_3.setMaxTemp(0.0);
        map_Number_With_max0_2.setAlwaysReset(false);
        map_Number_With_max0_2.setNotifyOnChangeOnly(false);
        map_Number_With_max0_2.setResetImmediate(true);
        map_Number_With_max0_2.filterSubject = map_getTemp_With_asDouble0_0;
        map_Number_With_max0_2.f = max_1;
        map_getTemp_With_asDouble0_0.setAlwaysReset(false);
        map_getTemp_With_asDouble0_0.setNotifyOnChangeOnly(false);
        map_getTemp_With_asDouble0_0.setResetImmediate(true);
        map_getTemp_With_asDouble0_0.filterSubject = handlerTempEvent;
        push_Number_To_setMaxTemp0_4.filterSubject = map_Number_With_max0_2;
        push_Number_To_setMaxTemp0_4.f = myTempProcessor_3;
    }

    @Override
    public void onEvent(com.fluxtion.api.event.Event event) {
        switch (event.getClass().getName()) {
            case ("com.fluxtion.articles.lombok.Main$TempEvent"): {
                TempEvent typedEvent = (TempEvent) event;
                handleEvent(typedEvent);
                break;
            }
        }

    }

    public void handleEvent(TempEvent typedEvent) {
        //Default, no filter methods
        isDirty_handlerTempEvent = true;
        handlerTempEvent.onEvent(typedEvent);
        if (isDirty_handlerTempEvent) {
            isDirty_map_getTemp_With_asDouble0_0 = map_getTemp_With_asDouble0_0.onEvent();
        }
        if (isDirty_map_getTemp_With_asDouble0_0) {
            isDirty_map_Number_With_max0_2 = map_Number_With_max0_2.onEvent();
        }
        if (isDirty_map_Number_With_max0_2) {
            isDirty_push_Number_To_setMaxTemp0_4 = push_Number_To_setMaxTemp0_4.onEvent();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    @Override
    public void afterEvent() {
        map_Number_With_max0_2.resetAfterEvent();
        isDirty_handlerTempEvent = false;
        isDirty_map_Number_With_max0_2 = false;
        isDirty_map_getTemp_With_asDouble0_0 = false;
        isDirty_push_Number_To_setMaxTemp0_4 = false;
    }

    @Override
    public void init() {
        map_getTemp_With_asDouble0_0.init();
        map_Number_With_max0_2.init();
    }

    @Override
    public void tearDown() {

    }

    @Override
    public void batchPause() {

    }

    @Override
    public void batchEnd() {

    }

}
