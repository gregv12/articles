/*
 * Copyright (c) 2020, V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.generated.PortfolioCalc;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.EventSink;
import com.fluxtion.articles.fxportfolio.shared.SignalKeys;
import com.fluxtion.integration.eventflow.EventFlow;
import com.fluxtion.integration.eventflow.filters.Log4j2Filter;
import com.fluxtion.integration.eventflow.filters.SepEventPublisher;
import com.fluxtion.integration.eventflow.sources.ManualEventSource;
import com.fluxtion.integration.log4j2.Log4j2AuditLogger;
import com.fluxtion.integration.log4j2.Log4j2CsvJournaller;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class FlowTest {

    @Test
    public void flowTest() {
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        EventFlow.flow(eventInjector)
                .pipeline(new Log4j2Filter())
                .next(new Log4j2CsvJournaller())
//                .next(new TestingCsvJournaller())
                .next(SepEventPublisher.of(new PortfolioCalc()))
                .start();
        //deterministic ids for orders
        List<String> idList = IntStream.rangeClosed(1, 1000).boxed().map(i -> "" + i).collect(Collectors.toList());
        eventInjector.publishToFlow(new Signal<Queue<String>>(SignalKeys.ORDER_ID, new ConcurrentLinkedQueue<>(idList)));
        //fluxtion.eventLog
        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //configure output
        eventInjector.publishToFlow(new Signal<EventSink>(SignalKeys.REGISTER_ORDER_PUBLISHER, System.out::println));
        eventInjector.publishToFlow(new Signal<>(SignalKeys.PUBLISH_POSITIONS));
        //hedging limits for some ccy 
        eventInjector.publishToFlow(new LimitConfig(Ccy.JPY, 30000, 5000));
        eventInjector.publishToFlow(new LimitConfig(Ccy.USD, 80, 10));
        //finally add some trades
        eventInjector.publishToFlow(new Trade("USDCHF", 100, -70));
        eventInjector.publishToFlow(new Trade("EURCHF", 350, -390));
        eventInjector.publishToFlow(new Trade("USDJPY", 350, -36000));
        //exeute some orders on the exchange
    }
}
