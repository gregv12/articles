/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.articles.fxportfolio.shared.SignalKeys;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.build;
import org.junit.Test;

/**
 *
 * @author greg higgins <greg.higgins@v12technology.com>
 */
public class FluxtionBuilderTest {

    @Test
    public void testSomeMethod() throws Exception {
//        StaticEventProcessor processor = reuseOrBuild("PortfolioCalc", "com.fluxtion.articles.fxportfolio.generated",FluxtionBuilder::buildPortfolioCalc);
        StaticEventProcessor processor = build("PortfolioCalc", "com.fluxtion.articles.fxportfolio.generated",new FluxtionBuilder()::buildPortfolioCalc);
        processor.onEvent(new Trade("USDCHF", 100, -90));
        processor.onEvent(new Trade("USDCHF", 100, -90));
        processor.onEvent(new Trade("USDCHF", -250, 225));
        processor.onEvent(new Trade("USDCHF", 300, -270));
        processor.onEvent(new Signal<>(SignalKeys.PUBLISH_POSITIONS));
    }
    
}
