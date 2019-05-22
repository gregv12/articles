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

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.api.annotations.TearDown;
import com.fluxtion.api.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Portfolio class collects together {@link AssetTradePos} updates and
 * calculates profit
 * for the portfolio on each update.
 * <p>
 *
 * A report is generated and published at the end of the processing. The report
 * generation is attached to the {@link Lifecycle#tearDown() } method.
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class PortfolioTradePos {

    private HashMap<String, AssetTradePos> symbol2Pos;
    private ArrayList<AssetTradePos> assets;
    private double pnl;
    private int dealsProcessed;
    private int pricesProcessed;

    @EventHandler
    public void positionUpdate(AssetTradePos pos) {
        symbol2Pos.putIfAbsent(pos.getSymbol(), pos);
        if (assets.size() != symbol2Pos.size()) {
            assets.clear();
            assets.addAll(symbol2Pos.values());
        }
        pnl = 0;
        for (int i = 0; i < assets.size(); i++) {
            final AssetTradePos asset = assets.get(i);
            pnl += asset.getPnl();
        }
        //uses streams slows the processing down by 25% and introduces gc's
//        pnl = symbol2Pos.values().stream().mapToDouble(AssetTradePos::getPnl).sum();
    }

    public double getPnl() {
        return pnl;
    }

    public HashMap<String, AssetTradePos> getSymbol2Pos() {
        return symbol2Pos;
    }

    public int getDealsProcessed() {
        return dealsProcessed;
    }

    public int getPricesProcessed() {
        return pricesProcessed;
    }
    
    @TearDown
    public void publishReport() {
        for (int i = 0; i < assets.size(); i++) {
            final AssetTradePos asset = assets.get(i);
            pnl += asset.getPnl();
            dealsProcessed += asset.getDealsProcessed();
            pricesProcessed += asset.getPricesProcessed();
        }
        System.out.println("Portfolio PnL:" + pnl);
        System.out.println("Deals processed:" + dealsProcessed);
        System.out.println("Prices processed:" + pricesProcessed);
        System.out.println("Assett positions:\n-----------------------------");
        symbol2Pos.forEach((s, a) -> {
            System.out.println(s + " : " + a);
        });
        System.out.println("-----------------------------");
        System.out.println("Events proecssed:" + (pricesProcessed + dealsProcessed));
    }

    @Initialise
    public void init() {
        symbol2Pos = new HashMap<>();
        assets = new ArrayList<>();
    }

}
