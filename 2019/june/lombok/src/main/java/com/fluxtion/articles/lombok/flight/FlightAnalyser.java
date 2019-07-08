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
 * @see
 * <a href=https://blog.redelastic.com/diving-into-akka-streams-2770b3aeabb0#.lt2w5bntb>Inspired
 * by: Diving into Akka Streams</a>
 * <h2>Overview</h2>
 *
 * Process a year's worth of all US flight landing records stored in CSV
 * format, approximately 7 million flights. The solution demonstrates the use of GroupBy with aggregate functions
 * to calculate delay average, count and sum.
 *
 * <h2>Application requirements</h2>
 * For a full year's data calculate summary delay statistics for each carrier
 * that has a record in the CSV source. Among other things the record contains
 * the carrier name and the delay if any on arrival. A negative delay is an
 * early arrival and a positive value is the number of minutes late the plane
 * landed. the summary should:
 *
 * <ul>
 * <li>Group the carriers by name, filtering records that are delayed
 * <li>Carrier name:column 8, delay: column 14
 * <li>For a carrier grouping calculate:
 * <ul>
 * <li>Cumulative sum of total delay
 * <li>Total number of delayed flights
 * <li>Average delay for a flight if it is late
 * </ul>
 * </ul>
 * <p>
 * The yearly data is stored in CVS format
 * @see
 * <a href="http://stat-computing.org/dataexpo/2009/the-data.html">http://stat-computing.org/dataexpo/2009/the-data.html</a>.
 *
 * <h2>Fluxtion implementation</h2>
 * A programmatic definition is used by Fluxtion event stream compiler at
 * compile time to generate a static event processor(SEP) that meets the
 * application requirements. At run time the SEP will process a stream of
 * {@link CharEvent}'s that are fed from a CSV source. Description of
 * configuration to build the flight analyser SEP:
 * <ul>
 * <li>Describe CSV marshalling using {@link CsvMarshallerBuilder#csvMarshaller(java.lang.Class)
 * } creating a {@link FlightDetails} bean for each csv record
 * <li>Filter records for delays &gt 0. Using {@link GreaterThanHelper#greaterThanFilter(Wrapper, Function, )
 * }
 * <li>Define grouping using {@link CarrierDelay} as the target "table" for
 * aggregation using {@link Group#groupBy(Wrapper, Function, java.lang.Class) }
 * <li>With the {@link GroupByBuilder} set the following:
 * <ul>
 * <li>Initialiser for a new group record, setting carrier name
 * <li>aggregate calculation for delay minutes average
 * <li>aggregate calculation for delay minutes count
 * <li>aggregate calculation for delay minutes sum
 * </ul>
 * <li>Make the GorupBy&lt CarrierDelay &gt available as public member
 * "delayMap"
 * </ul>
 *
 * Additional helper classes are generated as necessary by Fluxtion compiler
 * located in the same package as the SEP. All dispatch, calculation logic and
 * state management are encapsulated in the generated SEP,
 * "FlightDelayAnalyser".
 * <p>
 *
 * @author Greg Higgins (greg.higgins@V12technology.com)
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
