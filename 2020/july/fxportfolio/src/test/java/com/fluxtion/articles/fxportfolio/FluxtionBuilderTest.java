/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.api.StaticEventProcessor;
import com.fluxtion.api.event.Signal;
import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.articles.fxportfolio.event.Rate;
import com.fluxtion.articles.fxportfolio.event.Trade;
import com.fluxtion.articles.fxportfolio.shared.Ccy;
import com.fluxtion.articles.fxportfolio.shared.CcyPair;
import com.fluxtion.articles.fxportfolio.shared.EventSink;
import com.fluxtion.articles.fxportfolio.shared.Order;
import com.fluxtion.articles.fxportfolio.shared.SignalKeys;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.reuseOrBuild;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.build;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author greg higgins <greg.higgins@v12technology.com>
 */
public class FluxtionBuilderTest {

    private ExecutionVenue orderExecutor;
    private StaticEventProcessor processor;
    private final boolean reuse = false;
    private final String cfgString = "CHF: [USDCHF, EURCHF]\n"
            + "USD: [GBPUSD]\n"
            + "EUR: [EURGBP, EURUSD]\n"
            + "JPY: [USDJPY, EURJPY]\n"
            + "GBP: []\n";

    @Before
    public void init() throws Exception {
        if (reuse) {
            processor = reuseOrBuild("PortfolioCalc", "com.fluxtion.articles.fxportfolio.generated", new FluxtionBuilder(cfgString)::buildPortfolioCalcFromFile);
        } else {
            processor = build("PortfolioCalc", "com.fluxtion.articles.fxportfolio.generated", new FluxtionBuilder(cfgString)::buildPortfolioCalcFromFile);
        }
        orderExecutor = new ExecutionVenue(processor);
        orderExecutor.addRate("USDCHF", 0.9);
        orderExecutor.addRate("EURCHF", 1.1);
        orderExecutor.addRate("EURGBP", 0.9);
        orderExecutor.addRate("USDJPY", 105);
        orderExecutor.addRate("GBPUSD", 1.3);
        orderExecutor.publishRates();
        List<String> idList = IntStream.rangeClosed(1, 1000).boxed().map(i -> "" + i).collect(Collectors.toList());
        processor.onEvent(new Signal<Queue<String>>(SignalKeys.ORDER_ID, new ConcurrentLinkedQueue<>(idList)));
        processor.onEvent(new Signal<EventSink>(SignalKeys.REGISTER_ORDER_PUBLISHER, orderExecutor));
        processor.onEvent(new Signal<>(SignalKeys.PUBLISH_POSITIONS));
    }

    @Test
    public void testSomeMethod() throws Exception {
//        processor.onEvent(new EventLogControlEvent(LogLevel.TRACE));
        processor.onEvent(new LimitConfig(Ccy.JPY, 30000, 5000));
        processor.onEvent(new LimitConfig(Ccy.USD, 80, 10));
        processor.onEvent(new Trade("USDCHF", 100, -70));
        processor.onEvent(new Trade("EURCHF", 350, -390));
        processor.onEvent(new Trade("USDJPY", 350, -36000));

//        orderExecutor.printActiveOrders();
//        orderExecutor.executeAllOrders();


    }
    
    private static class ExecutionVenue implements EventSink {

        private final StaticEventProcessor processor;
        private final LinkedHashMap<String, Order> liveOrders = new LinkedHashMap<>();
        private HashMap<CcyPair, Rate> ccyPair2Rate = new HashMap<>();

        public ExecutionVenue(StaticEventProcessor processor) {
            this.processor = processor;
        }

        public void addRate(String ccyPair, double rate) {
            CcyPair ccyPairParsed = CcyPair.ccyPairFromCharSeq(ccyPair);
            ccyPair2Rate.put(ccyPairParsed, new Rate(ccyPairParsed, rate));
        }

        private void printActiveOrders() {
            System.out.println("---- start active orders ----");
            liveOrders.values().stream().forEach(System.out::println);
            System.out.println("---- end active orders   ----");
        }

        public void publishRates() {
            ccyPair2Rate.values().forEach(r -> processor.onEvent(r));
        }

        private void fillOrder(Order order) {
            System.out.println("filling:" + order);
            Trade t = new Trade(order, ccyPair2Rate.get(order.getCcyPair()).value);
            processor.onEvent(t);
        }

        @Override
        public void handleEvent(Object e) {
            if (e instanceof Order) {
                Order order = (Order) e;
                if (order.isComplete()) {
                    System.out.println("cancelling:" + e);
                    liveOrders.remove(order.getOrderId());
                } else {
                    System.out.println("new:" + e);
                    liveOrders.put(order.getOrderId(), order);
                }
            }
        }

        public void executeAllOrders() {
            List<Order> orders = new ArrayList(liveOrders.values());
            orders.forEach(this::fillOrder);
        }

    }

}
