/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.aot_csv;

import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.count;
import static com.fluxtion.ext.text.builder.csv.CsvMarshallerBuilder.csvMarshaller;

/**
 *
 * @author gregp
 */
public class Builder {

    @SepBuilder(name = "CsvCityProcessor", packageName = "com.fluxtion.articles.aot_csv.generated")
    public static void build(SEPConfig cfg) {
        csvMarshaller(City.class).build(cfg)
                .map(count())
                .push(new StatsCollector()::rowProcessed);
    }

}
