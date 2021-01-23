/*
 * Copyright (c) 2020, V12 Technology Ltd.
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.articles.fxportfolio;

import com.fluxtion.articles.fxportfolio.event.LimitConfig;
import com.fluxtion.integration.eventflow.PipelineFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author Greg Higgins greg.higgins@v12technology.com
 */
@Log4j2(topic = "fluxtion.journaller.csv")
public class TestingCsvJournaller extends PipelineFilter {

    StringBuilder sb = new StringBuilder();
    
    @Override
    public void processEvent(Object o) {
        sb.setLength(0);
        if(o instanceof LimitConfig){
            try {
                LimitConfig cfg = (LimitConfig) o;
                LimitConfigCsvDecoder.asCsv(cfg, sb);
                log.info(sb);
            } catch (IOException ex) {
                Logger.getLogger(TestingCsvJournaller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        propagate(o);
    }
    
    

}
