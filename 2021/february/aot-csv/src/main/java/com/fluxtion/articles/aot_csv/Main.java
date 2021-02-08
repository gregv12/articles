/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.aot_csv;

import com.fluxtion.articles.aot_csv.generated.CsvCityProcessor;
import static com.fluxtion.ext.text.api.util.AsciiCharEventFileStreamer.streamFromBuffer;
import com.fluxtion.ext.text.api.util.CharStreamer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author gregp
 */
public class Main {

    public static void main(String[] args) throws IOException {
        int runCount = parseInt(args, 0, 3);
        int rowsToProcess = parseInt(args, 1, 10);
        testRun(runCount, rowsToProcess);
    }

    public static void testRun(int runCount, int rowsToProcess) throws IOException {
        final File dataFile = new File("data.csv");
        System.out.println("start streaming file:" + dataFile.getAbsolutePath());
        for (int i = 0; i < runCount; i++) {
            final CsvCityProcessor csvCityProcessor = new CsvCityProcessor();
            csvCityProcessor.init();
            CharStreamer.stream(dataFile, csvCityProcessor).sync().noInit().stream();
        }
    }

    private static int parseInt(String[] args, int index, int defaultValue) {
        try {
            String value = args[index];
            return Integer.parseInt(value);
        } catch (Exception nfe) {
            return defaultValue;
        }
    }
}
