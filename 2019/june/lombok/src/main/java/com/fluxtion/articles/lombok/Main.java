/*
 * Copyright (c) 2019, V12 Technology Ltd.
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
package com.fluxtion.articles.lombok;

import com.fluxtion.articles.lombok.flight.generated.FlightDelayAnalyser;
import static com.fluxtion.ext.text.api.util.AsciiCharEventFileStreamer.streamFromFile;
import java.io.File;
import java.io.IOException;

/**
 * Process a year's worth of all US flight landing records stored in CSV
 * format, approximately 7 million flights. The solution demonstrates the use of GroupBy with aggregate functions
 * to calculate delay average, count and sum.<p>
 * 
 * The yearly data is stored in CVS format
 * @see
 * <a href="http://stat-computing.org/dataexpo/2009/the-data.html">http://stat-computing.org/dataexpo/2009/the-data.html</a>.
 * <p>
 * 
 * Download and unzip the file, then run pass the file location as an argument to this main
 * 
 * 
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Main {

    public static void main(String[] args) throws IOException {
        File f = new File(args[0]);
        long now = System.nanoTime();
        streamFromFile(f, new FlightDelayAnalyser(), true);
        System.out.println("millis:" + ((System.nanoTime() - now) / 1_000_000));
    }
}
