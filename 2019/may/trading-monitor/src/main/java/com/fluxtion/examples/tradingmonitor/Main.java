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
package com.fluxtion.examples.tradingmonitor;

import com.fluxtion.api.partition.Partitioner;
import com.fluxtion.examples.tradingmonitor.generated.fluxCsvAssetPrice.Csv2AssetPrice;
import com.fluxtion.examples.tradingmonitor.generated.fluxCsvDeal.Csv2Deal;
import com.fluxtion.examples.tradingmonitor.generated.symbol.SymbolTradeMonitor;
import static com.fluxtion.examples.tradingmonitor.util.TestDataGenerator.DEFAULT_OURFILENAME;
import static com.fluxtion.examples.tradingmonitor.util.TestDataGenerator.DEFAULT_ROWCOUNT;
import static com.fluxtion.examples.tradingmonitor.util.TestDataGenerator.generate;
import static com.fluxtion.examples.tradingmonitor.util.TestDataGenerator.generateIfMissing;
import com.fluxtion.ext.text.api.util.CharStreamer;
import com.fluxtion.ext.text.api.util.marshaller.DispatchingCsvMarshaller;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Main {

    /**
     * main method to run example
     * <ul>
     * <li>No args: run with data/generated-data.csv
     * <li>int arg: generate data/generated-data.csv with the row count provided
     * </ul>
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        File inFile;
        if(args.length==0){
            inFile = generateIfMissing();
        }else{
            int rowCount = DEFAULT_ROWCOUNT;
            try {
                rowCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException numberFormatException) {
            }
            inFile = generate(rowCount, new File(DEFAULT_OURFILENAME), true);
        }
        if (inFile.exists()) {
            System.out.println("processing file:" + inFile.getCanonicalPath());
            for (int i = 0; i < 6; i++) {
                runAnalysis(inFile);
            }
        } else {
            System.out.println("cannot find file:" + inFile.getCanonicalPath());
        }
    }

    /**
     * Executes a portfolio analysis on a csv file and prints a report
     * at the end of the file. Also prints to console execution time.
     *
     * @param inFile The cvs file containing events
     * @return The PortfolioTradePos after file processing.
     * @throws IOException
     */
    public static PortfolioTradePos runAnalysis(File inFile) throws IOException {
        System.out.println("\n");
        long now = System.nanoTime();
        //partiioning strategy - by asset symbol
        AssetPartitioner strat = new AssetPartitioner();
        Partitioner<SymbolTradeMonitor> partitioner = strat.newPartitioner();
        //read file, register parsers and dispatch
        CharStreamer.stream(inFile, new DispatchingCsvMarshaller()
                .addMarshaller(AssetPrice.class, new Csv2AssetPrice())
                .addMarshaller(Deal.class, new Csv2Deal())
                .addSink(partitioner)
        ).noInit().sync().stream();
        //teardown logic for portfolio
        strat.teardown();
        System.out.println("millis:" + ((System.nanoTime() - now) / 1_000_000));
        return strat.getPortfolio().portfolio;
    }
}
