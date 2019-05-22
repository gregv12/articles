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
 * along with this program. If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.examples.tradingmonitor.util;

import com.fluxtion.examples.tradingmonitor.AssetPrice;
import com.fluxtion.examples.tradingmonitor.Deal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Utility for generating test data that will be used to run the trade analysis
 * example.
 *
 * <ul>
 * <li>Generates data with a pseudorandom algorithm
 * <li>Default is 4 million rows to generate
 * <li>Default file is data/generated-data.csv
 * </ul>
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class TestDataGenerator {

    private static final String[] ASSETS = new String[]{"MSFT", "ORCL", "GOOG", "AMZN", "APPL", "FORD", "BTMN"};
    private static final long RANDOM_SEED = 4567;
    public static final String DEFAULT_OURFILENAME = "data/generated-data.csv";
    public static final int DEFAULT_ROWCOUNT = 4_000_000;

    public static void main(String[] args) throws IOException {
        generate(DEFAULT_ROWCOUNT, new File(DEFAULT_OURFILENAME), true);
    }

    /**
     * Generate trading test data as a csv file.Row types are
    {@link AssetPrice} and
    {@link Deal}.
     *
     * @param rowCount number of data rows to be generated
     * @param f the output csv file
     * @param overwrite Overwite an existing file if present
     * @return the newly generated data file
     * @throws IOException
     */
    public static File generate(int rowCount, File f, boolean overwrite) throws IOException {
        TestDataGenerator generator = new TestDataGenerator();
        generator.execute(rowCount, f, overwrite);
        return f;
    }

    /**
     * Generates a test data file if data/generated-data.csv is missing.The new
     * file has 4 million rows
     *
     * @return the newly generated data file
     * @throws IOException
     */
    public static File generateIfMissing() throws IOException {
        File file = new File(DEFAULT_OURFILENAME);
        if (!file.exists()) {
            generate(DEFAULT_ROWCOUNT, file, true);
        }
        return file;
    }

    private void execute(int rowCount, File f, boolean overwrite) throws IOException {
        System.out.println("generating test data with rowcount:" + rowCount);
        if (rowCount < 1) {
            throw new IllegalArgumentException("cannot generate test data file, supply a row count > 0");
        }
        if (f.exists() && !overwrite) {
            System.err.println("Move or delete file before generating test data file:" + f.getCanonicalPath());
            return;
        } else {
            if (f.exists()) {
                f.delete();
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, true), 2_000)) {
            writer.append("Deal,symbol,size,price\n");
            writer.append("AssetPrice,symbol,price\n");
            Random random = new java.util.Random(RANDOM_SEED);
            for (int i = 0; i < rowCount; i++) {
                String asset = ASSETS[(int) (random.nextDouble() * (ASSETS.length))];
                double price = (5 + random.nextDouble()) * 3;
                price = Math.round(price * 10000) / 10000.0;
                int size = (int) ((10 * random.nextDouble()) - 5);
                if (random.nextDouble() > 0.9) {
                    writer.append("Deal," + asset + "," + size + "," + price + "\n");
                } else {
                    writer.append("AssetPrice," + asset + "," + price + "\n");
                }
            }
        }
    }

}
