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

import com.fluxtion.api.SepContext;
import com.fluxtion.api.partition.LambdaReflection.SerializableFunction;
import com.fluxtion.articles.crosscalc.CompoundBreachCalc;
import com.fluxtion.articles.crosscalc.MarketTick;
import com.fluxtion.articles.sales.Shop.Sale;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import com.fluxtion.ext.streaming.api.Wrapper;
import com.fluxtion.ext.streaming.api.numeric.MutableNumber;
import com.fluxtion.ext.streaming.api.stream.Argument;
import static com.fluxtion.ext.streaming.api.stream.Argument.arg;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.gt;
import com.fluxtion.ext.streaming.builder.factory.EventSelect;
import static com.fluxtion.ext.streaming.builder.factory.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.factory.FilterBuilder.filter;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.multiply;
import static com.fluxtion.ext.streaming.builder.factory.LibraryFunctionsBuilder.subtract;
import static com.fluxtion.ext.streaming.builder.factory.MappingBuilder.map;
import com.fluxtion.scratch.DefaultNumberWrapper;
import com.fluxtion.scratch.EventMutableNumber;
import com.fluxtion.scratch.EventMutableNumber.EventMutableInt;
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
//
//    public static <T> Wrapper<Number> number(int value, SerializableFunction<T, Number> supplier) {
//        return SepContext.service().addOrReuse(new DefaultNumberWrapper(select(supplier), value));
//    }

//    public static Wrapper<Number> number(int value, String key) {
//        EventMutableNumber num = new EventMutableInt();
//        num.setFilter(key);
//        num.set(value);
//        num.setValidOnStart(true);
//        return SepContext.service().addOrReuse(num);
//    }
//
//    public static Wrapper<Number> number(String key) {
//        EventMutableNumber num = new EventMutableNumber();
//        num.setFilter(key);
//        num.setValidOnStart(false);
//        return SepContext.service().addOrReuse(num);
//    }
//
//    public static Argument<Number> arg2(int value, String key) {
//        return (arg(number(value, key)));
//    }
//
//    public static <T> Wrapper<Number> numberX(int value, SerializableFunction<T, Number> supplier1) {
//        MutableNumber num = new MutableNumber();
//        num.set(value);
//        Wrapper<Number> target = EventSelect.select(supplier1).push(num::set);
//        target = SepContext.service().addOrReuse(target);
//        target.validOnStart(true);
//        return target;
//    }
//
//    public static <T, S extends Number> Wrapper<Number> subtractA(SerializableFunction<T, S> supplier1, Wrapper<S> supplier2) {
//        return map(subtract(), arg(supplier1), arg(supplier2));
//    }
//
//    public static <T, S> Wrapper<Number> subtractA(Wrapper<S> supplier1, SerializableFunction<T, Number> supplier2) {
//        return map(subtract(), arg(supplier1), arg(supplier2));
//    }

//    public static <T extends Number, S extends Number> Wrapper<Number> subtract(LambdaReflection.SerializableSupplier<T> supplier1, LambdaReflection.SerializableSupplier<T> supplier2) {
//        return map(subtract(), arg(supplier1), arg(supplier2));
//    }
//
//    public static <T extends Number, S extends Number> Wrapper<Number> subtract(Argument<T> arg1, Argument<S> arg2) {
//        return map(subtract(), arg1, arg2);
//    }
//
//    public static <T, S extends Number, R extends Number> Wrapper<Number> subtract(Wrapper<T> wrapper, LambdaReflection.SerializableFunction<T, S> supplier1, LambdaReflection.SerializableFunction<T, R> supplier2) {
//        return map(subtract(), arg(wrapper, supplier1), arg(wrapper, supplier2));
//    }
//
//    public static <T, U, S extends Number, R extends Number> Wrapper<Number> subtract(Wrapper<T> wrapper1, LambdaReflection.SerializableFunction<T, S> supplier1, Wrapper<U> wrapper2, LambdaReflection.SerializableFunction<U, R> supplier2) {
//        return map(subtract(), arg(wrapper1, supplier1), arg(wrapper2, supplier2));
//    }
//
//    public static <T extends Number, S extends Number> Wrapper<Number> subtract(Wrapper<T> wrapper1, Wrapper<S> wrapper2) {
//        return map(subtract(), arg(wrapper1), arg(wrapper2));
//    } 
}
