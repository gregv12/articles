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
package com.fluxtion.examples.tradingmonitor;

import com.fluxtion.api.event.Event;
import com.fluxtion.api.event.RegisterEventHandler;
import com.fluxtion.api.partition.Partitioner;
import com.fluxtion.examples.tradingmonitor.generated.portfolio.PortfolioTradeMonitor;
import com.fluxtion.examples.tradingmonitor.generated.symbol.SymbolTradeMonitor;
import com.fluxtion.ext.text.api.event.EofEvent;

/**
 * Used in conjunction with a {@link Partitioner} to generate partition keys for
 * incoming events and initialise partitioned EventHandlers. The events keys are
 * generated for are:
 * <ul>
 * <li>{@link AssetPrice}
 * <li>{@link Deal}
 * </ul>
 *
 * The keys generated for price and deal events are equal if they both represent
 * the same underlying asset. This ensures that related deal and price events
 * are dispatched to the same {@link SymbolTradeMonitor} instance.
 * <p>
 * A consumer method {@link #initMonitor(SymbolTradeMonitor)} initialises any
 * newly
 * created event handler before processing events. The initialiser points any
 * newly
 * created event handlers to the same {@link PortfolioTradeMonitor}.
 * The initialiser methods is registered with {@link Partitioner#Partitioner(java.util.function.Supplier, java.util.function.Consumer)
 * } constructor.
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class AssetPartitioner {

    private CharSequence ret;
    private final PortfolioTradeMonitor portfolio;

    public AssetPartitioner() {
        portfolio = new PortfolioTradeMonitor();
        portfolio.init();
    }

    public void initMonitor(SymbolTradeMonitor monitor) {
        monitor.assetTradePos.setSymbol(ret.toString());
        monitor.handleEvent(new RegisterEventHandler(portfolio));
    }

    public CharSequence keyGen(Event event) {
        ret = null;
        if (event instanceof AssetPrice) {
            ret = ((AssetPrice) event).getSymbol();
        }
        if (event instanceof Deal) {
            ret = ((Deal) event).getSymbol();
        }
        return ret;
    }


    public Partitioner<SymbolTradeMonitor> newPartitioner() {
        Partitioner<SymbolTradeMonitor> partitioner = new Partitioner<>(SymbolTradeMonitor::new, this::initMonitor);
        partitioner.keyPartitioner(this::keyGen);
        return partitioner;
    }

    public void teardown() {
        getPortfolio().onEvent(EofEvent.EOF);
        getPortfolio().tearDown();
    }

    public PortfolioTradeMonitor getPortfolio() {
        return portfolio;
    }

}
