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
import static com.fluxtion.ext.streaming.builder.group.Group.groupBy;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.count;
import static com.fluxtion.ext.text.api.csv.Converters.defaultInt;
import com.fluxtion.ext.text.builder.csv.CharTokenConfig;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;
import lombok.Data;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FlightAnalyser {

    @SepBuilder(
            name = "FlightDelayAnalyser",
            packageName = "com.fluxtion.articles.lombok.flight.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
    public void buildFlightProcessor(SEPConfig cfg) {
        var flightDetails = csvMarshaller(FlightDetails.class, 1)
                .addEventPublisher(false)
                .map(14, FlightDetails::setDelay).converter(14, defaultInt(-1))
                .map(8, FlightDetails::setCarrier).tokenConfig(CharTokenConfig.WINDOWS).build();
        //filter and group by
        var delayedFlight = flightDetails.filter(FlightDetails::getDelay, positive());
        var carrierDelay = groupBy(delayedFlight, FlightDetails::getCarrier, CarrierDelay.class);
        //derived values for a group
        carrierDelay.init(FlightDetails::getCarrier, CarrierDelay::setCarrierId);
        carrierDelay.avg(FlightDetails::getDelay, CarrierDelay::setAvgDelay);
        carrierDelay.count(CarrierDelay::setTotalFlights);
        carrierDelay.sum(FlightDetails::getDelay, CarrierDelay::setTotalDelayMins);
        //public access to nodes
        cfg.addPublicNode(carrierDelay.build(), "carrierDelayMap");
        cfg.addPublicNode(count(flightDetails), "totalFlights");
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
