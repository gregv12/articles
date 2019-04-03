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
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a member of an execution path. This method will be invoked
 * invoked in topological order on the execution path, i.e. after all its
 * dependents have processed the event.<p>
 *
 * A valid OnEvent accepts no arguments and optionally returns a boolean
 * indicating a change
 * has occurred. If conditional processing is enabled for the SEP, the following
 * strategy is employed for interpreting notification and branching execution:
 * <ul>
 * <li>return = true : indicates a change has occurred at this node
 * <li>return = false : indicates a change has NOT occurred at this node
 * </ul>
 *
 * The {@link #dirty() } method controls the propagation strategy for
 * conditional branching execution
 *
 * A node must be in the execution graph to be included in the invocation chain.
 * The Fluxtion builder api provides methods to register an instance in the
 * event processor.
 *
 *
 * @author Greg Higgins
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnEvent {

    /**
     * Controls the event calling strategy for this node. If conditional
     * execution is enabled in the SEP, the notification status of the dependent
     * determines if this method is invoked.
     *
     * <ul>
     * <li>dirty = true : invoked if any dependent returns true from their event
     * handling method
     * <li>dirty = false : invoked if any dependent returns false from their
     * event handling method
     * </ul>
     *
     * @return
     */
    boolean dirty() default true;
}
