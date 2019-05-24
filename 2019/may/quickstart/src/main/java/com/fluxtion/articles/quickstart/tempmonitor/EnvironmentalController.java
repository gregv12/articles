/*
 * Copyright (C) 2019 V12 Technology Ltd.
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
package com.fluxtion.articles.quickstart.tempmonitor;

import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;

/**
 * Controls air conditioning and heating systems. Receives notifications from an
 * external monitor/rules engine and pushes control signals to individual
 * systems.
 *
 * @author V12 Technology Ltd.
 */
public class EnvironmentalController {

    public void airConOn(TempEvent temp) {
        System.out.println("CONTROLLER::ON -> AIRCON temp:" + temp.temp());
    }

    public void heatingOn(TempEvent temp) {
        System.out.println("CONTROLLER::ON -> HEATING temp:" + temp.temp());
    }

    public void airConAndHeatingOff(TempEvent temp) {
        System.out.println("CONTROLLER::OFF -> AIRCON HEATING temp:" + temp.temp());

    }

}
