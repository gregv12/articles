/*
 * Copyright (C) 2020 V12 Technology Ltd.
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
package com.fluxtion.articles.crosscalc;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.api.event.Signal;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
@Data
public class CompoundBreachCalc {

    private final String directInstrument;
    private final String cross1Instrument;
    private final String cross2Instrument;
    private double breachThreshold = 0.5;
    private transient double directMid;
    private transient double cross1Mid;
    private transient double cross2Mid;
    private transient double crossDelta;

    @EventHandler
    public boolean marketUpdate(MarketTick tic) {
        if (tic.getInstrument().equalsIgnoreCase(directInstrument)) {
            directMid = tic.mid();
        } else if (tic.getInstrument().equalsIgnoreCase(cross1Instrument)) {
            cross1Mid = tic.mid();
        } else if (tic.getInstrument().equalsIgnoreCase(cross2Instrument)) {
            cross2Mid = tic.mid();
        }
        return breachCheck();
    }

    @EventHandler(filterString = "SIGNAL_SAMPLE_CONFIG")
    public boolean updateConfig(Signal<Double> doubleConfig) {
        return breachCheck();
    }

    @OnEvent
    public boolean breachCheck() {
        crossDelta = Math.abs(directMid - (cross1Mid * cross2Mid));
        return crossDelta > breachThreshold;
    }

    @Initialise
    public void init() {
        directMid = Double.NaN;
        cross1Mid = Double.NaN;
        cross2Mid = Double.NaN;
        crossDelta = Double.NaN;
    }

}
