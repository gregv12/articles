/*
 * Copyright (c) 2020, V12 Technology Ltd.
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
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.articles.fxportfolio.csvmarshaller.LimitConfigCsvDecoder0;
import com.fluxtion.articles.fxportfolio.event.HedgeRouteConfig;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.ext.text.api.csv.RowProcessor;
import static com.fluxtion.ext.text.builder.csv.CsvToBeanBuilder.buildRowProcessor;
import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class CsvTest {

    @Test
//    @Ignore
    public void genTest() {
        buildRowProcessor(LimitConfig.class);
        buildRowProcessor(Rate.class);
        buildRowProcessor(Trade.class);
        buildRowProcessor(HedgeRouteConfig.class);
    }

    @Test
    public void serCsv() throws IOException {
//        final String csvOut = LimitConfigCsvDecoder0.csvHeader() 
//                + "\n"
//                + new LimitConfigCsvDecoder0().toCsv(new LimitConfig(Ccy.CHF, 22, 4555), new StringBuilder()).toString();
//        System.out.println(csvOut);
//        LimitConfigCsvDecoder0.stream(System.out::println, csvOut);
        
    }

}
