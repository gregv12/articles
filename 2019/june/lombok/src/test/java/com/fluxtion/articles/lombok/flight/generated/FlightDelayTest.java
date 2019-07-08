/*
 * Copyright (c) 2019, V12 Technology Ltd.
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.articles.lombok.flight.generated;

import com.fluxtion.articles.lombok.flight.FlightAnalyser;
import com.fluxtion.articles.lombok.flight.FlightAnalyser.CarrierDelay;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.text.api.util.StringDriver;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class FlightDelayTest {

    String sampleData = "Year,Month,DayofMonth,DayOfWeek,DepTime,CRSDepTime,ArrTime,CRSArrTime,UniqueCarrier,FlightNum,TailNum,ActualElapsedTime,CRSElapsedTime,AirTime,ArrDelay,DepDelay,Origin,Dest,Distance,TaxiIn,TaxiOut,Cancelled,CancellationCode,Diverted,CarrierDelay,WeatherDelay,NASDelay,SecurityDelay,LateAircraftDelay\n"
            + "2008,1,3,4,NA,700,NA,830,WN,126,,NA,90,NA,NA,NA,LAS,OAK,407,NA,NA,1,A,0,NA,NA,NA,NA,NA\n"
            + "2008,1,3,4,NA,700,NA,830,WN,126,,NA,90,NA,,NA,LAS,OAK,407,NA,NA,1,A,0,NA,NA,NA,NA,NA\n"
            + "2008,1,3,4,1416,1325,1512,1435,WN,54,N643SW,56,70,49,-5,51,ISP,BWI,220,2,5,0,,0,12,0,0,0,25\n"
            + "2008,1,3,4,1416,1325,1512,1435,WN,54,N643SW,56,70,49,53,51,ISP,BWI,220,2,5,0,,0,12,0,0,0,25\n"
            + "2008,1,3,4,1416,1325,1512,1435,WN,54,N643SW,56,70,49,37,51,ISP,BWI,220,2,5,0,,0,12,0,0,0,25";

    @Test
    public void testInput() {
        final FlightDelayAnalyser flightDelayAnalyser = new FlightDelayAnalyser();
        StringDriver.streamChars(sampleData, flightDelayAnalyser);
        Map<?, Wrapper<FlightAnalyser.CarrierDelay>> map = flightDelayAnalyser.delayMap.getMap();
        CarrierDelay wnDelay = map.get("WN").event();
        Assert.assertEquals(2, wnDelay.getTotalFlights());
        Assert.assertEquals(90, wnDelay.getTotalDelayMins());
        Assert.assertEquals(45, wnDelay.getAvgDelay());
        
        
    }
}
