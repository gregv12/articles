/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.lombok.temperature;

import com.fluxtion.api.event.Event;
import com.fluxtion.api.lifecycle.EventHandler;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.max;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.sepInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class InlineLombok {

    public EventHandler handler() throws Exception {
        return sepInstance(c
                -> select(TempEvent::getTemp)
                        .map(max()).notifyOnChange(true)
                        .push(new MyTempProcessor()::setMaxTemp),
                "com.fluxtion.articles.lombok.temperature.generated.nolombok", "TempMonitor");
    }

    @Data
    @AllArgsConstructor
    public static class TempEvent extends Event {

        private double temp;
    }
}
