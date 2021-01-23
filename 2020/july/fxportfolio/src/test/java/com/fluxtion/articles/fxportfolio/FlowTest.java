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
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.EventSink;
import com.fluxtion.articles.fxportfolio.shared.SignalKeys;
import com.fluxtion.integration.eventflow.filters.Log4j2Filter;
import com.fluxtion.integration.log4j2.Log4j2AuditLogger;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.generated.PortfolioCalc;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.ext.text.builder.csv.CsvToBeanBuilder;
import com.fluxtion.integration.eventflow.EventFlow;
import com.fluxtion.integration.eventflow.filters.ConsoleFilter;
import com.fluxtion.integration.eventflow.sources.DelimitedPullSource;
import com.fluxtion.integration.eventflow.sources.DelimitedSource;
import com.fluxtion.integration.eventflow.sources.ManualEventSource;
import static com.fluxtion.integration.eventflow.sources.TransformSource.transform;
import static com.fluxtion.integration.eventflow.sources.TransformPullSource.transform;
import com.fluxtion.integration.log4j2.JournalRecord;
import com.fluxtion.integration.log4j2.Log4j2CsvJournaller;
import com.fluxtion.integration.log4j2.Log4j2SnakeYamlJournaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 *
 * @author V12 Technology Ltd.
 */
public class FlowTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    @Ignore
    public void genCsv() {
        CsvToBeanBuilder.buildRowProcessor(HedgeRouteConfig.class);
    }

    @Test
    public void auditTest() {
        ManualEventSource eventInjector1 = new ManualEventSource("manualEventSrc_1");
        ManualEventSource eventInjector2 = new ManualEventSource("manuaiEventSrc_2");
        EventFlow.flow(eventInjector1)
                .source(eventInjector2)
                .first(new Log4j2CsvJournaller())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new ConsoleFilter())
                .start();

//        eventInjector.publishToFlow("hello world");
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.CHF));
//        eventInjector.publishToFlow(new HedgeRouteConfig(Ccy.EUR));
        eventInjector1.publishToFlow(new Rate(CcyPair.ccyPairFromCharSeq("EURUSD"), 1.15));
        eventInjector2.publishToFlow(new Rate(CcyPair.ccyPairFromCharSeq("EURGBP"), 0.97));
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
    public void yamlNullValue() {
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
    @Ignore
    public void yamlImmutable() {
        EventLogControlEvent loegControlEvent = new EventLogControlEvent(new Log4j2AuditLogger());

        DumperOptions dop = new DumperOptions();
        dop.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(dop);
//        final String yamlDump = yaml.dumpAs(loegControlEvent, Tag.MAP, null);
        final String yamlDump = yaml.dump(loegControlEvent);
        System.out.println(yamlDump);
    }

    @Test
    public void flowTest() {
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        EventFlow.flow(eventInjector)
                .first(new Log4j2Filter())
                //                .next(new Log4j2CsvJournaller())
                .next(new Log4j2SnakeYamlJournaller())
                //                .next(new TestingCsvJournaller())
                .next(new PortfolioCalc())
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

    @Test
    public void fromCsv() {
        StringReader reader = new StringReader("ccy,limit,target\n"
                + "CHF,22,4555\n"
                + "JPY,30000,5000\n"
                + "USD,80,10"
        );
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        DelimitedSource limitCfgCsv = new DelimitedSource(new LimitConfigCsvDecoder(), reader, "limitFromCsv");
        //cache manual events before start
        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //start - manual events, then read from csv
        EventFlow.flow(eventInjector)
                .source(limitCfgCsv)
                .first(new Log4j2Filter())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new PortfolioCalc())
                .start();
    }

    @Test
    public void fromCsvWithTransformer() {
        StringReader reader = new StringReader("ccy,limit,target\n"
                + "CHF,22,4555\n"
                + "JPY,30000,5000\n"
                + "USD,80,10"
        );
        //cache manual events before start
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //start - manual events, then read from csv
        EventFlow.flow(eventInjector)
                .source(transform("t1", new DelimitedSource(new LimitConfigCsvDecoder(), reader, "limitFromCsv"), this::printLimit))
                .first(new Log4j2Filter())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new PortfolioCalc())
                .start();
    }

    private LimitConfig printLimit(LimitConfig limitCfg) {
        System.out.println("transform LimitConfig ccy:" + limitCfg.getCcy());
        return limitCfg;
    }

    @Test
    public void fromPolledFile() throws IOException, InterruptedException {
        final File csvFile = folder.newFile("limits.csv");
        var limitCsvReader = new FileReader(csvFile);
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        DelimitedPullSource limitCfgCsv = new DelimitedPullSource(new LimitConfigCsvDecoder(), limitCsvReader, "limitFromCsv");
        //cache manual events before start
//        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //start - manual events, then read from csv
        EventFlow flow = EventFlow.flow(eventInjector)
                .source(limitCfgCsv)
                .first(new Log4j2Filter())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new PortfolioCalc())
                .start();

        String data = "ccy,limit,target\n"
                + "CHF,22,4555\n"
                + "JPY,30000,5000\n"
                + "USD,80,10";
        FileUtils.writeStringToFile(csvFile, data, Charset.defaultCharset());
        Thread.sleep(100);
        flow.stop();
    }
    
    @Test
    public void fromPolledFileTransformed() throws IOException, InterruptedException {
        final File csvFile = folder.newFile("limits.csv");
        var limitCsvReader = new FileReader(csvFile);
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
        DelimitedPullSource limitCfgCsv = new DelimitedPullSource(new LimitConfigCsvDecoder(), limitCsvReader, "limitFromCsv");
        //cache manual events before start
//        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //start - manual events, then read from csv
        EventFlow flow = EventFlow.flow(eventInjector)
                .source(transform("t1", limitCfgCsv, this::printLimit))
                .first(new Log4j2Filter())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new PortfolioCalc())
                .start();

        String data = "ccy,limit,target\n"
                + "CHF,22,4555\n"
                + "JPY,30000,5000\n"
                + "USD,80,10";
        FileUtils.writeStringToFile(csvFile, data, Charset.defaultCharset());
        Thread.sleep(100);
        flow.stop();
    }

    @Test
    public void pushfromFile() throws IOException, InterruptedException {
        final File csvFile = folder.newFile("limits.csv");
        var limitCsvReader = new FileReader(csvFile);
        ManualEventSource eventInjector = new ManualEventSource("manualSource1");
//        DelimitedPullSource limitCfgCsv = new DelimitedPullSource(new LimitConfigCsvDecoder(), limitCsvReader, "limitFromCsv");
//        DelimitedSource limitCfgCsv = new DelimitedSource(new LimitConfigCsvDecoder(), limitCsvReader, "limitFromCsv").pollForever();

        //cache manual events before start
//        eventInjector.publishToFlow(new EventLogControlEvent(new Log4j2AuditLogger()));
        //start - manual events, then read from csv
        EventFlow flow = EventFlow.flow(eventInjector)
                .sourceAsync(transform("t1", new DelimitedSource(new LimitConfigCsvDecoder(), limitCsvReader, "limitFromCsv").pollForever(), this::printLimit))
                .first(new Log4j2Filter())
                .next(new Log4j2SnakeYamlJournaller())
                .next(new PortfolioCalc())
                .start();
//        Executors.newSingleThreadExecutor().submit(() -> flow.start());

        String data = "ccy,limit,target\n"
                + "CHF,22,4555\n"
                + "JPY,30000,5000\n"
                + "USD,80,10\n";
        FileUtils.writeStringToFile(csvFile, data, Charset.defaultCharset());
        FileUtils.writeStringToFile(csvFile, "JPY,56555,25000\n", Charset.defaultCharset(), true);
        Thread.sleep(100);
        flow.stop();
//        Thread.sleep(1000);
    }

}
