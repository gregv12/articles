/*
<<<<<<< HEAD
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

import com.fluxtion.api.audit.EventLogControlEvent;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.generated.PortfolioCalc;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.EventSink;
import com.fluxtion.articles.fxportfolio.shared.SignalKeys;
import com.fluxtion.integration.eventflow.filters.Log4j2Filter;
import com.fluxtion.integration.eventflow.filters.SepEventPublisher;
import com.fluxtion.integration.log4j2.Log4j2AuditLogger;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.ext.text.builder.csv.CsvToBeanBuilder;
import com.fluxtion.integration.eventflow.EventFlow;
import com.fluxtion.integration.eventflow.filters.ConsoleFilter;
import com.fluxtion.integration.eventflow.sources.ManualEventSource;
import com.fluxtion.integration.log4j2.JournalRecord;
import com.fluxtion.integration.log4j2.Log4j2CsvJournaller;
import com.fluxtion.integration.log4j2.Log4j2SnakeYamlJournaller;
import org.junit.Ignore;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FlowTest {

    @Test
    @Ignore
    public void genCsv() {
        CsvToBeanBuilder.buildRowProcessor(HedgeRouteConfig.class);
    }

    @Test
    public void auditTest() {
        ManualEventSource eventInjector = new ManualEventSource("manulaEventSrc");
        EventFlow.flow(eventInjector)
                .pipeline(new Log4j2CsvJournaller())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new ConsoleFilter())
                .start();

//        eventInjector.publishToFlow("hello world");
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.CHF));
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.EUR));
        eventInjector.publishToFlow(new Rate(CcyPair.ccyPairFromCharSeq("EURUSD"), 1.15));
        eventInjector.publishToFlow(new Rate(CcyPair.ccyPairFromCharSeq("EURGBP"), 0.97));
    }

    @Test
    public void readYamlLog() {
        String s = "event: !!com.fluxtion.articles.fxportfolio.event.Rate\n"
                + "  ccypair: {base: USD, name: EURUSD, terms: EUR}\n"
                + "  value: 1.15\n";
//                + "---";

        Yaml yaml = new Yaml();
        JournalRecord record = yaml.loadAs(s, JournalRecord.class);
        System.out.println("out:" + record.toString());
    }

    @Test
    public void readYamlList() {
        String s = "---\n"
                + "!!com.fluxtion.integration.log4j2.JournalRecord\n"
                + "event: !!com.fluxtion.articles.fxportfolio.event.Rate\n"
                + "  ccypair: {base: USD, name: EURUSD, terms: EUR}\n"
                + "  value: 1.15\n"
                + "---\n"
                + "!!com.fluxtion.integration.log4j2.JournalRecord\n"
                + "event: !!com.fluxtion.articles.fxportfolio.event.Rate\n"
                + "  ccypair: {base: GBP, name: EURGBP, terms: EUR}\n"
                + "  value: 0.97\n"
                + "---\n";

        Yaml yaml = new Yaml();
        Iterable records = yaml.loadAll(s);
        
        records.forEach(System.out::println);
        
//        System.out.println("out:" + record.toString());
    }
    @Test
    public void readYamlListNoTopTag() {
        String s = "---\n"
                + "event: !!com.fluxtion.articles.fxportfolio.event.Rate\n"
                + "  ccypair: {base: USD, name: EURUSD, terms: EUR}\n"
                + "  value: 2.15\n"
                + "---\n"
                + "event: !!com.fluxtion.articles.fxportfolio.event.Rate\n"
                + "  ccypair: {base: GBP, name: EURGBP, terms: EUR}\n"
                + "  value: 1.97\n";
//                + "---\n";

        Yaml yaml = new Yaml(new Constructor(JournalRecord.class));
        Iterable records = yaml.loadAll(s);
        
        records.forEach(System.out::println);
        
//        System.out.println("out:" + record.toString());
    }
    
    @Test
    public void yamlNullValue(){
        Signal<Object> signal = new Signal<>(SignalKeys.PUBLISH_POSITIONS);
        JournalRecord record = new JournalRecord();
        record.setEvent(signal);
        Yaml yaml = new Yaml();
//        String yamlDump = yaml.dump(record);
//        final String yamlDump = yaml.dumpAs(record, Tag.MAP, null);
        final String yamlDump = yaml.dumpAsMap(record);
        System.out.println("yamlDump:" + yamlDump);
    }
    
    @Test
    public void yamlImmutable(){
        EventLogControlEvent loegControlEvent = new EventLogControlEvent(new Log4j2AuditLogger());
        
        DumperOptions dop = new DumperOptions();
        dop.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(dop);
//        final String yamlDump = yaml.dumpAs(loegControlEvent, Tag.MAP, null);
        final String yamlDump = yaml.dump(loegControlEvent);
        System.out.println( yamlDump);
    }

    @Test
    public void flowTest() {
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        EventFlow.flow(eventInjector)
                .pipeline(new Log4j2Filter())
//                .next(new Log4j2CsvJournaller())
                .next(new Log4j2SnakeYamlJournaller())
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
