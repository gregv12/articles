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
package com.fluxtion.articles.lombok.flight;

import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.positive;
import static com.fluxtion.ext.streaming.api.util.GroupByPrint.printValues;
import static com.fluxtion.ext.streaming.builder.group.Group.groupBy;
import com.fluxtion.ext.text.api.csv.Converters;
import static com.fluxtion.ext.text.api.csv.Converters.defaultInt;
import static com.fluxtion.ext.text.api.event.EofEvent.eofTrigger;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FlightAnalyser {

    @SepBuilder(
            name = "FlightDelayAnalyser",
            packageName = "com.fluxtion.articles.lombok.flight.generated"
    )
    public void buildFlightProcessor(SEPConfig cfg) {
        var flightDetails = csvMarshaller(FlightDetails.class, 1)
                .map(14, FlightDetails::setDelay).converter(14, defaultInt(-1))
                .map(8, FlightDetails::setCarrier).converter(8, Converters::intern).build();
        //filter and group by
        var delayedFlight = flightDetails.filter(FlightDetails::getDelay, positive());
        var carrierDelay = groupBy(delayedFlight, FlightDetails::getCarrier, CarrierDelay.class);
        //derived values for a group
        carrierDelay.init(FlightDetails::getCarrier, CarrierDelay::setCarrierId);
        carrierDelay.avg(FlightDetails::getDelay, CarrierDelay::setAvgDelay);
        carrierDelay.count(CarrierDelay::setTotalFlights);
        carrierDelay.sum(FlightDetails::getDelay, CarrierDelay::setTotalDelayMins);
        //make public for testing
        var delayByGroup = cfg.addPublicNode(carrierDelay.build(), "delayMap");
        //dump to console, triggers on EofEvent
        printValues("\nFlight delay analysis\n========================",
                delayByGroup, eofTrigger());
    }

    @Data //input data from CSV
    public static class FlightDetails {

        private String carrier;
        private int delay;
    }

    @Data //derived data
    public static class CarrierDelay {

        private String carrierId;
        private int avgDelay;
        private int totalFlights;
        private int totalDelayMins;
    }

}
