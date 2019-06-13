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
package com.fluxtion.articles.quickstart;

import com.fluxtion.articles.quickstart.tempmonitor.EnvironmentalController;
import com.fluxtion.articles.quickstart.tempmonitor.Events;
import com.fluxtion.articles.quickstart.tempmonitor.Events.EndOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.StartOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.articles.quickstart.wordfrequency.StatsPrinter;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.gt;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.lt;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.inBand;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.log.LogBuilder.*;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.max;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.avg;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.min;
import static com.fluxtion.ext.text.builder.ascii.AsciiHelper.wordSplitter;
import static com.fluxtion.ext.text.builder.math.WordFrequency.wordFrequency;

/**
 * A set of static event processor builders for the quick start examples. Each
 * builder is a method annotated with {@link SepBuilder} annotation. Fluxtion
 * maven plugin scans for the annotated methods and generates a processor for
 * each method.
 *
 * @author V12 Technology Ltd.
 */
public class EventProcessorBuilders {

    /**
     * Declarative form for simple filter using lambda.
     *
     * @param cfg
     */
    @SepBuilder(name = "TempFilter",
            packageName = "com.fluxtion.articles.quickstart.tempFilter.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void buildLambda(SEPConfig cfg) {
        select(Events.TempEvent.class)
                .filter(t -> t.temp() > 20)
                .console("Too hot!! ");
    }

    /**
     * Fluxtion builder for word frequency analyser. Mixes imperative and
     * declarative
     * forms:
     * <ul>
     * <li>Nodes are added imperatively using cfg.addNode
     * <li>wordFrequency uses declarative groupBy internally
     * </ul>
     *
     * @param cfg SEPConfig context
     */
    @SepBuilder(name = "WordFrequency",
            packageName = "com.fluxtion.articles.quickstart.wordfrequency.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void buildWordFrequency(SEPConfig cfg) {
        cfg.addNode(new StatsPrinter(
                cfg.addNode(wordFrequency(wordSplitter()))
        ));
    }

    /**
     * Fluxtion builder for daily temperature monitoring and AC/Heating signals. Explicit 
     * mixing of declarative and imperative forms:
     * <ul>
     * <li>
     * <li>
     * </ul>
     *
     * @param cfg SEPConfig context
     */
    @SepBuilder(name = "TempMonitor",
            packageName = "com.fluxtion.articles.quickstart.tempmonitor.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void buildTemperatureMonitor(SEPConfig cfg) {
        //select a stream of tmep values from temperature events
        Wrapper<Number> tempInC = select(TempEvent::getTemp);
        //start/end of day triggers
        Wrapper<StartOfDay> newDay = select(StartOfDay.class);
        Wrapper<EndOfDay> endOfDay = select(EndOfDay.class);
        //record daily temp max/min stats reset at start of day. Publish daily average at end of day
        Wrapper<Number> max = tempInC.map(max()).notifyOnChange(true).resetNotifier(newDay);
        Wrapper<Number> min = tempInC.map(min()).notifyOnChange(true).resetNotifier(newDay);
        Wrapper<Number> avg = tempInC.map(avg()).publishAndReset(endOfDay);
        Log("===== Start of day {} =====", newDay, StartOfDay::day);
        Log("NEW day max temp {}C", max, Number::intValue);
        Log("NEW day min temp {}C", min, Number::intValue);
        Log("NEW day avg temp {}C", avg, Number::doubleValue);
        //heating + airconditioning signals to controller
        final double acOnTemp = 25;
        final int heatingOnTemp = 12;
        EnvironmentalController controller = new EnvironmentalController();
        tempInC.filter(gt(acOnTemp)).notifyOnChange(true).push(controller::airConOn);
        tempInC.filter(lt(heatingOnTemp)).notifyOnChange(true).push(controller::heatingOn);
        tempInC.filter(inBand(heatingOnTemp, acOnTemp)).notifyOnChange(true).push(controller::airConAndHeatingOff);
    }

}
