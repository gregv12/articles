/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.lombok.temperature;

import com.fluxtion.api.event.Event;
import com.fluxtion.api.lifecycle.EventHandler;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.max;
import static com.fluxtion.generator.compiler.InprocessSepCompiler.sepInstance;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class AotLombok {



    @SepBuilder(name = "TempMonitor",
            packageName = "com.fluxtion.articles.lombok.temperature.generated.aotlombok",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void build1(SEPConfig cfg) {
        select(TempEvent::getTemp)
                .map(max()).notifyOnChange(true).
                push(new MyTempProcessor()::setMaxTemp);
    }

    @Data
    @AllArgsConstructor
    public static class TempEvent extends Event {

        private double temp;
    }

}
