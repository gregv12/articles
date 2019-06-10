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
package com.fluxtion.articles.largefiles;

import com.fluxtion.ext.streaming.api.stream.ListCollector;
import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.api.group.AggregateFunctions.Count;
import com.fluxtion.ext.streaming.api.group.GroupBy;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subSeqBefore;
import static com.fluxtion.ext.streaming.api.stream.CharSeqFunctions.subSequence;
import static com.fluxtion.ext.streaming.api.util.GroupByPrint.printFrequencyMap;
import static com.fluxtion.ext.streaming.api.util.GroupByPrint.printTopN;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.count;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.util.CharStreamer;
import com.fluxtion.ext.text.builder.csv.CharTokenConfig;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author V12 Technology Ltd.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CharStreamer.stream(new File("C:\\Users\\gregp\\Downloads\\indiv18\\itcont.txt"), 
                (Class<com.fluxtion.api.lifecycle.EventHandler>) Class.forName("com.fluxtion.articles.largefiles.generated.VoterProcessor"))
                .async().stream();

    }

    @SepBuilder(name = "VoterProcessor",
            packageName = "com.fluxtion.articles.largefiles.generated",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
    public void buildLambda(SEPConfig cfg) {
        RowProcessor<Voter> voter = csvMarshaller(Voter.class, 0)
                .map(4, Voter::setDateString, subSequence(0, 6))
                .map(7, Voter::setFirstName, subSeqBefore(','))
                .map(7, Voter::setFullName)
                .tokenConfig(new CharTokenConfig('\n', '|', '\r')).build();
        cfg.addNode(new ResultsPrinter(
                voter.group(Voter::getFirstName, Voter::hashCode, Count),
                voter.group(Voter::getDateString, Voter::hashCode, Count),
                voter.map(count()),
                voter.map(v->v.getFullName().toString()).map(new ListCollector()::addItem))
        );
    }

    public static class ResultsPrinter {

        private final GroupBy<Number> groupByName;
        private final GroupBy<Number> groupByDate;
        private final Wrapper<Number> count;
        private final Wrapper<List> nameList;

        public ResultsPrinter(GroupBy groupByName, GroupBy groupByDate, Wrapper count, Wrapper<List> nameList) {
            this.groupByName = groupByName;
            this.groupByDate = groupByDate;
            this.count = count;
            this.nameList = nameList;
        }

        @EventHandler
        public void eof(EofEvent eof) {
            System.out.println("results:");
            System.out.println("count:" + count.event().intValue() + "\n");
            printTopN("groupByName:", groupByName, 3);
            printFrequencyMap("groupByDate:", groupByDate);
            System.out.println("3rd name:" + nameList.event().get(3));
        }
    }
    

}
