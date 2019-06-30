/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.articles.lombok;

import com.fluxtion.api.event.Event;
import com.fluxtion.builder.annotation.SepBuilder;
import com.fluxtion.builder.node.SEPConfig;
import static com.fluxtion.ext.streaming.builder.event.EventSelect.select;
import static com.fluxtion.ext.streaming.builder.stream.StreamFunctionsBuilder.max;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
public class Main {

    @SepBuilder(name = "TempMonitor",
            packageName = "com.fluxtion.articles.lombok.generated.nolombok",
            outputDir = "src/main/java",
            cleanOutputDir = true
    )
//    @Disabled
    public void build1(SEPConfig cfg) {

        select(TempEvent::getTemp).map(max()).push(new MyTempProcessor()::setMaxTemp);
    }

//    @Data
//    @AllArgsConstructor
    public static class TempEvent extends Event {

        private double temp;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }
        
    }

    public static class MyTempProcessor {

        private double maxTemp;

        public double getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(double maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

}
