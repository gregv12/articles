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

import com.fluxtion.api.event.EventPublsher;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.builder.annotation.Disabled;
import com.fluxtion.builder.annotation.SepBuilder;
import static com.fluxtion.builder.event.EventPublisherBuilder.eventSource;
import com.fluxtion.builder.node.SEPConfig;
//import com.fluxtion.examples.tradingmonitor.generated.symbol.SymbolTradeMonitor;
import com.fluxtion.ext.streaming.api.Wrapper;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.lt;
import static com.fluxtion.ext.streaming.api.stream.NumericPredicates.outsideBand;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamBuilder.stream;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.add;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.count;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.cumSum;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.multiply;
import static com.fluxtion.ext.streaming.builder.util.FunctionArg.arg;
import com.fluxtion.ext.text.api.event.EofEvent;

/**
 * This class contains definitions for two static event processors:
 * <ul>
 * <li>SymbolTradeMonitor: calculates position, profit, mark to market etc for a
 * traded asset in a portfolio. The generated static event processor handles
 * {@link Deal} and {@link AssetPrice} events. Rules are added that track
 * position and profit, a count is made for each new breach. Results for rule
 * breaches, positions etc. are collected in an {@link AssetTradePos} instance.
 * The results instance is registered as an {@link EventPublsher}, which acts as
 * an event source for any registered {@link EventHandler}. The builder uses the
 * declarative form provided in the
 * <a href="https://www.javadoc.io/doc/com.fluxtion.extension/fluxtion-streaming-api/1.7.11">Fluxtion
 * streaming extension</a> specifically
 * <a href="https://static.javadoc.io/com.fluxtion.extension/fluxtion-streaming-api/1.7.11/com/fluxtion/ext/streaming/api/Wrapper.html">
 * Wrapper</a>
 *
 * <li>PortfolioTradeMonitor: processes {@link AssetTradePos} events to
 * calculate portfolio profit and applies a profit warning rule at the portfolio
 * level. The solution mixes the imperative and declarative form. The
 * {@link PortfolioTradePos} imperatively calculates profit and declares a
 * method as an event handler for {@link AssetTradePos}. A declarative profit
 * rule is defined in the builder and prints to console when the
 * {@link EofEvent} is received.
 * </ul>
 *
 * Multiple {@link SymbolTradeMonitor} feed into a single
 * {@link PortfolioTradeMonitor} by publishing {@link AssetTradePos}. The
 * binding of generated event processors from source to sink is performed here:
 * {@link AssetPartitioner#initMonitor(com.fluxtion.examples.tradingmonitor.generated.symbol.SymbolTradeMonitor)}
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class FluxtionBuilder {

    @SepBuilder(name = "SymbolTradeMonitor",
            packageName = "com.fluxtion.examples.tradingmonitor.generated.symbol",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void buildAssetAnalyser(SEPConfig cfg) {
        //entry points subsrcibe to events
        Wrapper<Deal> deals = select(Deal.class);
        Wrapper<AssetPrice> prices = select(AssetPrice.class);
        //result collector, and republish as an event source
        AssetTradePos results = cfg.addPublicNode(new AssetTradePos(), "assetTradePos");
        eventSource(results);
        //calculate derived values
        Wrapper<Number> cashPosition = deals
                .map(multiply(), Deal::getSize, Deal::getPrice)
                .map(multiply(), -1)
                .map(cumSum());
        Wrapper<Number> pos = deals.map(cumSum(), Deal::getSize);
        Wrapper<Number> mtm = pos.map(multiply(), arg(prices, AssetPrice::getPrice));
        Wrapper<Number> pnl = add(mtm, cashPosition);
        //collect into results
        cashPosition.push(results::setCashPos);
        pos.push(results::setAssetPos);
        mtm.push(results::setMtm);
        pnl.push(results::setPnl);
        deals.map(count()).push(results::setDealsProcessed);
        prices.map(count()).push(results::setPricesProcessed);
        //add some rules - only fires on first breach
        pnl.filter(lt(-200))
                .notifyOnChange(true)
                .map(count())
                .push(results::setPnlBreaches);
        pos.filter(outsideBand(-200, 200))
                .notifyOnChange(true)
                .map(count())
                .push(results::setPositionBreaches);
        //human readable names to nodes in generated code - not required 
        deals.id("deals");
        prices.id("prices");
        cashPosition.id("cashPos");
        pos.id("assetPos");
        mtm.id("mtm");
        pnl.id("pnl");
    }

    @SepBuilder(name = "PortfolioTradeMonitor",
            packageName = "com.fluxtion.examples.tradingmonitor.generated.portfolio",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void buildPortfolioAnalyser(SEPConfig cfg) {
        PortfolioTradePos portfolio = cfg.addPublicNode(new PortfolioTradePos(), "portfolio");
        stream(portfolio).filter(PortfolioTradePos::getPnl, lt(-1_000))
                .map(count())
                .notifierOverride(select(EofEvent.class))
                .console("portfolio loss gt 1,000 breach count:");
    }
}
