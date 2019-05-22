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

import com.fluxtion.api.partition.Partitioner;
import com.fluxtion.examples.tradingmonitor.generated.fluxCsvAssetPrice.Csv2AssetPrice;
import com.fluxtion.examples.tradingmonitor.generated.fluxCsvDeal.Csv2Deal;
import com.fluxtion.examples.tradingmonitor.generated.symbol.SymbolTradeMonitor;
import com.fluxtion.ext.text.api.event.EofEvent;
import com.fluxtion.ext.text.api.util.CharStreamer;
import com.fluxtion.ext.text.api.util.marshaller.DispatchingCsvMarshaller;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author V12 Technology Ltd.
 */
public class MonitorTest {
    
    @Test
    public void testAllAssetMonitor() throws IOException {
        long now = System.nanoTime();
        //partiioning strategy - by asset symbol
        AssetPartitioner strat = new AssetPartitioner();
        Partitioner<SymbolTradeMonitor> partitioner = new Partitioner<>(SymbolTradeMonitor::new, strat::initMonitor);
        partitioner.keyPartitioner(strat::keyGen);
        //read file, register parsers and dispatch
        CharStreamer.stream(new File("data/small-data.csv"), new DispatchingCsvMarshaller()
                .addMarshaller(AssetPrice.class, new Csv2AssetPrice())
                .addMarshaller(Deal.class, new Csv2Deal())
                .addSink(partitioner)
        ).noInit().sync().stream();
        //teardown logic
        strat.getPortfolio().onEvent(EofEvent.EOF);
        strat.getPortfolio().tearDown();
        System.out.println("millis:" + ((System.nanoTime() - now)/1_000_000));
        
        PortfolioTradePos portfolio = strat.getPortfolio().portfolio;
        HashMap<String, AssetTradePos> symbol2Pos = portfolio.getSymbol2Pos();
        assertThat(portfolio.getDealsProcessed(), is(2));
        assertThat(portfolio.getPricesProcessed(), is(17));
        assertThat(symbol2Pos.size(), is(7));
        AssetTradePos orcl = symbol2Pos.get("ORCL");
        assertThat(orcl.getPnl(), closeTo(-0.236200, 0.00001));
        assertThat(orcl.getAssetPos(), is(-1.0));
        assertThat(orcl.getMtm(), closeTo(-16.2221, 0.00001));
        assertThat(orcl.getCashPos(), closeTo(15.9859, 0.00001));
        assertThat(orcl.getPositionBreaches(), is(0));
        assertThat(orcl.getPnlBreaches(), is(0));
        assertThat(orcl.getDealsProcessed(), is(1));
        assertThat(orcl.getPricesProcessed(), is(1));
        //AMZN
        AssetTradePos amzn = symbol2Pos.get("AMZN");
        assertThat(amzn.getPnl(), closeTo(-68129.2, 0.00001));
        assertThat(amzn.getAssetPos(), is(-2000.0));
        assertThat(amzn.getMtm(), closeTo(-100000.0, 0.00001));
        assertThat(amzn.getCashPos(), closeTo(31870.8, 0.00001));
        assertThat(amzn.getPositionBreaches(), is(1));
        assertThat(amzn.getPnlBreaches(), is(1));
        assertThat(amzn.getDealsProcessed(), is(1));
        assertThat(amzn.getPricesProcessed(), is(1));
        //no deals
        assertPircesOnly(symbol2Pos.get("MSFT"), 3);
        assertPircesOnly(symbol2Pos.get("GOOG"), 3);
        assertPircesOnly(symbol2Pos.get("APPL"), 2);
        assertPircesOnly(symbol2Pos.get("FORD"), 3);
        assertPircesOnly(symbol2Pos.get("BTMN"), 4);

        
//MSFT : AssetTradePos{symbol=MSFT, pnl=0.0, assetPos=0.0, mtm=0.0, cashPos=0.0, positionBreaches=0, pnlBreaches=0, dealsProcessed=0, pricesProcessed=3}
//GOOG : AssetTradePos{symbol=GOOG, pnl=0.0, assetPos=0.0, mtm=0.0, cashPos=0.0, positionBreaches=0, pnlBreaches=0, dealsProcessed=0, pricesProcessed=3}
//APPL : AssetTradePos{symbol=APPL, pnl=0.0, assetPos=0.0, mtm=0.0, cashPos=0.0, positionBreaches=0, pnlBreaches=0, dealsProcessed=0, pricesProcessed=2}
//ORCL : AssetTradePos{symbol=ORCL, pnl=-0.2362000000000002, assetPos=-1.0, mtm=-16.2221, cashPos=15.9859, positionBreaches=0, pnlBreaches=0, dealsProcessed=1, pricesProcessed=1}
//FORD : AssetTradePos{symbol=FORD, pnl=0.0, assetPos=0.0, mtm=0.0, cashPos=0.0, positionBreaches=0, pnlBreaches=0, dealsProcessed=0, pricesProcessed=3}
//BTMN : AssetTradePos{symbol=BTMN, pnl=0.0, assetPos=0.0, mtm=0.0, cashPos=0.0, positionBreaches=0, pnlBreaches=0, dealsProcessed=0, pricesProcessed=4}
//AMZN : AssetTradePos{symbol=AMZN, pnl=-68129.2, assetPos=-2000.0, mtm=-100000.0, cashPos=31870.8, positionBreaches=1, pnlBreaches=1, dealsProcessed=1, pricesProcessed=1}
//   
    }
    
    private void assertPircesOnly(AssetTradePos pos, int priceCount){
        double sum  = pos.getAssetPos() 
                + pos.getCashPos() 
                + pos.getDealsProcessed()
                + pos.getPnl()
                + pos.getMtm()
                + pos.getPnlBreaches()
                + pos.getPositionBreaches()
                ;
        assertThat(sum, closeTo(0, 0.00000001));
        assertThat(pos.getPricesProcessed(), is(priceCount));
    }


}
