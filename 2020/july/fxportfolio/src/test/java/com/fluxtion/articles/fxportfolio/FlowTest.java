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
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.ext.text.builder.csv.CsvToBeanBuilder;
import com.fluxtion.integration.eventflow.EventFlow;
import com.fluxtion.integration.eventflow.filters.ConsoleFilter;
import com.fluxtion.integration.eventflow.sources.ManualEventSource;
import com.fluxtion.integration.log4j2.Log4j2CsvJournaller;
import com.fluxtion.integration.log4j2.Log4j2SnakeYamlJournaller;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FlowTest {
    
    @Test
    @Ignore
    public void genCsv(){
        CsvToBeanBuilder.buildRowProcessor(HedgeRouteConfig.class);
    }
    
    @Test
    public void auditTest(){
        ManualEventSource eventInjector = new ManualEventSource("manulaEventSrc");
        EventFlow.flow(eventInjector)
                .pipeline(new Log4j2CsvJournaller())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new ConsoleFilter())
                .start();
        
//        eventInjector.publishToFlow("hello world");
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.CHF));
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.EUR));
//        eventInjector.publishToFlow(new Rate(CcyPair.from("EURUSD"),1.15));
//        eventInjector.publishToFlow(new Rate(CcyPair.from("EURGBP"),0.97));
    }
    
}
