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
