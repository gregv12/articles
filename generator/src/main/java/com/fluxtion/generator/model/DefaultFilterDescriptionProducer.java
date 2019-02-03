/* 
 * Copyright (C) 2018 V12 Technology Ltd.
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
package com.fluxtion.generator.model;

import com.fluxtion.api.event.Event;
import com.fluxtion.builder.generation.FilterDescription;
import com.fluxtion.builder.generation.FilterDescriptionProducer;
import com.fluxtion.builder.generation.GenerationContext;
import java.util.ArrayList;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg Higgins
 */
public class DefaultFilterDescriptionProducer implements FilterDescriptionProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFilterDescriptionProducer.class);
    private ArrayList<FilterDescriptionProducer> namingStrategies;

    public DefaultFilterDescriptionProducer() {
        loadServices();
    }

    public final void loadServices() {
        LOGGER.debug("DefaultFilterDescriptionProducer (re)loading strategies");
        ServiceLoader<FilterDescriptionProducer> loadServices;
        namingStrategies = new ArrayList<>();
        if (GenerationContext.SINGLETON != null && GenerationContext.SINGLETON.getClassLoader() != null) {
            LOGGER.debug("using custom class loader to search for NodeNameProducer");
            loadServices = ServiceLoader.load(FilterDescriptionProducer.class, GenerationContext.SINGLETON.getClassLoader());
        } else {
            loadServices = ServiceLoader.load(FilterDescriptionProducer.class);
        }
        loadServices.forEach(namingStrategies::add);
//        Collections.sort(namingStrategies);
        LOGGER.debug("sorted FilterDescriptionProducer strategies : {}", namingStrategies);
    }

    @Override
    public FilterDescription getFilterDescription(Class<? extends Event> event, int filterId) {
        final FilterDescription filterDescription = FilterDescriptionProducer.super.getFilterDescription(event, filterId);
        filterDescription.comment = "Event Class:[" + event.getCanonicalName() + "]"
                + " filterId:[" + filterId + "]";
        for (FilterDescriptionProducer namingStrategy : namingStrategies) {
            String commnent = namingStrategy.getFilterDescription(event, filterId).comment;
            if (commnent != null) {
                filterDescription.comment = commnent;
                break;
            }
        }
        return filterDescription;
    }
    
    @Override
    public FilterDescription getFilterDescription(Class<? extends Event> event, String filterId) {
        final FilterDescription filterDescription = FilterDescriptionProducer.super.getFilterDescription(event, filterId);
        filterDescription.comment = "Event Class:[" + event.getCanonicalName() + "]"
                + " filterString:[" + filterId + "]";
        for (FilterDescriptionProducer namingStrategy : namingStrategies) {
            String commnent = namingStrategy.getFilterDescription(event, filterId).comment;
            if (commnent != null) {
                filterDescription.comment = commnent;
                break;
            }
        }
        return filterDescription;
    }

}
