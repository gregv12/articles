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

import com.fluxtion.ext.text.api.util.StringDriver;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class FlightDelayTest {
        String sampleData = "Year,Month,DayofMonth,DayOfWeek,DepTime,CRSDepTime,ArrTime,CRSArrTime,UniqueCarrier,FlightNum,TailNum,ActualElapsedTime,CRSElapsedTime,AirTime,ArrDelay,DepDelay,Origin,Dest,Distance,TaxiIn,TaxiOut,Cancelled,CancellationCode,Diverted,CarrierDelay,WeatherDelay,NASDelay,SecurityDelay,LateAircraftDelay\n"
            + "2008,1,3,4,NA,700,NA,830,WN,126,,NA,90,NA,NA,NA,LAS,OAK,407,NA,NA,1,A,0,NA,NA,NA,NA,NA\n"
            + "2008,1,3,4,NA,700,NA,830,WN,126,,NA,90,NA,,NA,LAS,OAK,407,NA,NA,1,A,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,NA,700,NA,830,WN,126,,NA,90,NA,0,-200000,LAS,OAK,407,NA,NA,1,A,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,2003,1955,2211,2225,WN,335,N712SW,128,150,116,-14,8,IAD,TPA,810,4,8,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,754,735,1002,1000,WN,3231,N772SW,128,145,113,2,19,IAD,TPA,810,5,10,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,628,620,804,750,WN,448,N428WN,96,90,76,14,8,IND,BWI,515,3,17,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,926,930,1054,1100,WN,1746,N612SW,88,90,78,-6,-4,IND,BWI,515,3,7,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1829,1755,1959,1925,WN,3920,N464WN,90,90,77,34,34,IND,BWI,515,3,10,0,,0,2,0,0,0,32\n"
//            + "2008,1,3,4,1940,1915,2121,2110,WN,378,N726SW,101,115,87,11,25,IND,JAX,688,4,10,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1937,1830,2037,1940,WN,509,N763SW,240,250,230,57,67,IND,LAS,1591,3,7,0,,0,10,0,0,0,47\n"
//            + "2008,1,3,4,1039,1040,1132,1150,WN,535,N428WN,233,250,219,-18,-1,IND,LAS,1591,7,7,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,617,615,652,650,WN,11,N689SW,95,95,70,2,2,IND,MCI,451,6,19,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1620,1620,1639,1655,WN,810,N648SW,79,95,70,-16,0,IND,MCI,451,3,6,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,706,700,916,915,WN,100,N690SW,130,135,106,1,6,IND,MCO,828,5,19,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1644,1510,1845,1725,WN,1333,N334SW,121,135,107,80,94,IND,MCO,828,6,8,0,,0,8,0,0,0,72\n"
//            + "2008,1,3,4,1426,1430,1426,1425,WN,829,N476WN,60,55,39,1,-4,IND,MDW,162,9,12,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,715,715,720,710,WN,1016,N765SW,65,55,37,10,0,IND,MDW,162,7,21,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1702,1700,1651,1655,WN,1827,N420WN,49,55,35,-4,2,IND,MDW,162,4,10,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1029,1020,1021,1010,WN,2272,N263WN,52,50,37,11,9,IND,MDW,162,6,9,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1452,1425,1640,1625,WN,675,N286WN,228,240,213,15,27,IND,PHX,1489,7,8,0,,0,3,0,0,0,12\n"
//            + "2008,1,3,4,754,745,940,955,WN,1144,N778SW,226,250,205,-15,9,IND,PHX,1489,5,16,0,,0,NA,NA,NA,NA,NA\n"
//            + "2008,1,3,4,1323,1255,1526,1510,WN,4,N674AA,123,135,110,16,28,IND,TPA,838,4,9,0,,0,0,0,0,0,16\n"
            + "2008,1,3,4,1416,1325,1512,1435,WN,54,N643SW,56,70,49,37,51,ISP,BWI,220,2,5,0,,0,12,0,0,0,25";

    
    
    @Test
    public void testInput(){
        StringDriver.streamChars(sampleData, new FlightDelayAnalyser());
    }
}
