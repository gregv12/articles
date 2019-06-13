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
package com.fluxtion.examples.servicestarter.strat;

import com.fluxtion.examples.servicestarter.service.Platform;
import com.fluxtion.examples.servicestarter.service.Requires;
import com.fluxtion.examples.servicestarter.service.Service;
import java.util.Map;

/**
 *
 * @author V12 Technology Ltd.
 */
public class MyStrategy implements Service{
    
    @Requires
    public MyDataSvc dataSvc;

    @Override
    public void init(Map cfg, Platform platform) {
       platform.configSvc(dataSvc, cfg);
    }

    @Override
    public void ready(Platform platform) {
    }

    @Override
    public void running(Platform platform) {
    }

    @Override
    public void stopped(Platform platform) {
    }
    
}
