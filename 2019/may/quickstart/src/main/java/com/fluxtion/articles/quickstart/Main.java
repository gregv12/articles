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

import com.fluxtion.articles.quickstart.tempFilter.generated.TempFilter;
import com.fluxtion.articles.quickstart.tempmonitor.Events.EndOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.StartOfDay;
import com.fluxtion.articles.quickstart.tempmonitor.Events.TempEvent;
import com.fluxtion.articles.quickstart.tempmonitor.generated.TempMonitor;
import com.fluxtion.articles.quickstart.wc.generated.Wc;
import com.fluxtion.articles.quickstart.wordfrequency.generated.WordFrequency;
import static com.fluxtion.ext.text.api.util.CharStreamer.stream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author V12 Technology Ltd.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String program = args[0].toLowerCase();
        switch (program) {
            case "wc":
                stream(checkFile(args), new Wc()).sync().stream();
                break;
            case "wordfreq":
                stream(checkFile(args), new WordFrequency()).sync().stream();
                break;
            case "allstats":
                stream(checkFile(args), new Wc()).sync().stream();
                stream(checkFile(args), new WordFrequency()).sync().stream();
                break;
            case "tempmonitor":
                runTempMonitor();
                break;
            case "lambdafilter":
                runLambda();
                break;
            default:
                throw new RuntimeException("program option not supported:" + program);
        }
    }

    private static File checkFile(String[] args) throws IOException {
        File inFile = new File(args[1]);
        if (!inFile.exists()) {
            throw new RuntimeException("cannot find file:" + inFile.getCanonicalPath());
        }
        return inFile;
    }

    private static void runTempMonitor() {
        TempMonitor monitor = new TempMonitor();
        monitor.init();
        //uncomment to have only WARNING
//        monitor.handleEvent(LogControlEvent.enableLevelFiltering(3));
        monitor.onEvent(new StartOfDay("01 Jun 2018"));
        monitor.onEvent(new TempEvent(10));
        monitor.onEvent(new TempEvent(20));
        monitor.onEvent(new TempEvent(14));
        monitor.onEvent(new TempEvent(4));
        monitor.onEvent(new TempEvent(16));
        monitor.onEvent(new TempEvent(20));
        monitor.onEvent(new EndOfDay());
        monitor.onEvent(new StartOfDay("02 Jun 2018"));
        monitor.onEvent(new TempEvent(6));
        monitor.onEvent(new TempEvent(4));
        // uncomment to remove logging meta data
//        monitor.handleEvent(LogControlEvent.recordMsgBuildTime(false));
//        monitor.handleEvent(LogControlEvent.recordMsgLogLevel(false));
        monitor.onEvent(new TempEvent(26));
        monitor.onEvent(new EndOfDay());
        monitor.onEvent(new StartOfDay("03 Jun 2018"));
        monitor.onEvent(new TempEvent(24));
        monitor.onEvent(new TempEvent(26));
        monitor.onEvent(new TempEvent(29));
        monitor.onEvent(new TempEvent(15));
        monitor.onEvent(new TempEvent(28));
        monitor.onEvent(new EndOfDay());
        monitor.onEvent(new StartOfDay("04 Jun 2018"));
        monitor.onEvent(new TempEvent(15));
        monitor.onEvent(new TempEvent(24));
        monitor.onEvent(new TempEvent(20));
        monitor.onEvent(new TempEvent(20));
        monitor.onEvent(new EndOfDay());
    }

    private static void runLambda() {
        TempFilter tempFilter = new TempFilter();
        tempFilter.init();
        tempFilter.onEvent(new TempEvent(10));
        tempFilter.onEvent(new TempEvent(19));
        tempFilter.onEvent(new TempEvent(22));
        tempFilter.onEvent(new TempEvent(27));
        tempFilter.onEvent(new TempEvent(18));
    }

}
