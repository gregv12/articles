/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.aot_csv;

import com.fluxtion.api.annotations.EventHandler;
import com.fluxtion.api.annotations.Initialise;
import com.fluxtion.ext.text.api.event.EofEvent;

/**
 *
 * @author gregp
 */
public class StatsCollector {

    private long start;
    private int rowCount;

    public void rowProcessed(int count) {
        this.rowCount = count;
    }

    @EventHandler
    public void eof(EofEvent eof) {
            long delta = (long) ((System.nanoTime() - start) / 1e6);
            System.out.println("time:" + delta + "ms  rowcount:" + rowCount);
    }

    @Initialise
    public void startTimer() {
        start = System.nanoTime();
    }
}
