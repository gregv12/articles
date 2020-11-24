/*
 * Copyright (C) 2020 V12 Technology Ltd.
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
package com.fluxtion.articles.crosscalc.builder;

import com.fluxtion.articles.crosscalc.CompoundBreachCalc;
import com.fluxtion.articles.crosscalc.MarketTick;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.gt;
import static com.fluxtion.ext.streaming.builder.factory.FilterBuilder.filter;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.multiply;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.subtract;
import lombok.Value;

/**
 *
 * @author V12 Technology Ltd.
 */
@Disabled
public class GraphBuilder {

    @SepBuilder(name = "TradeExecutor", packageName = "com.fluxtion.articles.crosscalc.generated.fluxtion_functional")
    @Disabled
    public void buildGraphFunctional(SEPConfig cfg) {
        var cross = multiply(mid("EURCHF"), mid("USDCHF")).id("crossEURUSD");
        subtract(mid("EURUSD"), cross).id("crossDeltaEURUSD")
                .get(Number::doubleValue)
                .map(Math::abs)
                .filter(gt(0.5)).id("crossBreaachEURUSD");

    }

    //helper for building
    private Wrapper<Double> mid(String instrument) {
        return filter(MarketTick::getInstrument, instrument::equalsIgnoreCase)
                .get(MarketTick::mid)
                .id("mid" + instrument);
    }

    @SepBuilder(name = "TradeExecutor", packageName = "com.fluxtion.articles.crosscalc.generated.fluxtion_compound")
    @Disabled
    public void buildGraphCompound(SEPConfig cfg) {
        cfg.addNode(new CompoundBreachCalc("EURUSD", "EURCHF", "USDCHF"), "crossBreaachEURUSD");
    }

    @SepBuilder(name = "TradeExecutor", packageName = "com.fluxtion.articles.crosscalc.generated.fluxtion_annotated")
    public void buildGraphAnnotated(SEPConfig cfg) {
//        cfg.addNode(new TestHandler(new MyDependency(22.12)), "rootHanlder");
//
//        EventDrivenNumber edn = new EventDrivenNumber();
//        edn.set(100);
//        edn.setFilter("myIntFilter");
//        cfg.addNode(edn);   
//        Wrapper<EventDrivenNumber> streamEdn = stream(edn);
//        streamEdn.validOnStart(true);  
//        subtract(arg(20), arg(edn));
//        subtract(arg(20), arg(stream(edn).validOnStart(true)));
//        EventDrivenNumber edn2 = new EventDrivenNumber();
//        edn2.set(33.567);
//        edn2.setFilter("filter33");
//        final Wrapper<EventDrivenNumber> validOnStart = stream(edn2).validOnStart(true);
//        final Argument<Number> arg = arg(validOnStart);
//        subtract(arg, arg(12.45));
        //
//        subtract(arg2(55,"key_a"), arg(12.45));
//        subtract(number(55,"key_b"), number(12, "key_c"));
//        subtractA(Sale::getAmountSold, number(12, "key_c"));
//        subtractA(Sale::getAmountSold, number(456, "key_a"));
//        number(99,"www");
//        number("sdee");

//        Wrapper<Number> number = number(123, Sale::getAmountSold);
//        subtractA(Sale::getAmountSold, number);
//        subtract(number(55, "key_b"), number(12, Sale::getAmountSold));
//        select(Sale::getAmountSold).map(new NumberStore(23)::getValue).map(cumSum());

        //use select to drive subtract
//        subtractA(Sale::getAmountSold, select(Sale::getAmountSold));
//        subtract(select(Sale::getAmountSold), select(Number::doubleValue));
//        var saleStream = select(Sale.class);
//        var numberStream = select(Number.class);
//        subtract(saleStream.get(Sale::getAmountSold), numberStream.get(Number::intValue));
//        subtract(Sale::getAmountSold, Number::intValue);
    }

    @Value
    public static class NumberStore {

        int defaultVal;

        public int getValue(int val) {
            return val;
        }
    }

}
