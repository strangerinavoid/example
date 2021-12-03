/*
 * InLab Software Product
 *
 * Copyright (c) 2020, InLab Software, LLC. All rights reserved.
 * Use is subject to license terms.
 *
 * http://www.inlabsoft.com/products/product/license
 */
package com.inlabsoft.sen.shared.logging;

import java.lang.System.Logger.Level;

import com.inlabsoft.sen.shared.logging.internal.LogAwareService;

/**
 * The {@code LogAware} interface is used to add {@link Logger} to any class just by implementation of this
 * interface.
 * <p>
 * @implNote
 * This interface provides default implementation of {@link #log()} method, that returns instance of {@link Logger},
 * configured for each implementing class.
 *
 * @author  Andrey OCHIROV
 * @version 1.0
 *
 * @see     Level
 * @see     Logger
 * @see     LoggerFactory
 */
public interface LogAware {

    /**
     * Returns an instance of {@link Logger logger} for the caller's use.
     *
     * @apiNote
     * This method may defer calling the {@link internal} to create an actual logger supplied by the logging backend,
     * for instance, to allow loggers to be obtained during the application initialization time.
     *
     * @return  an instance of {@link Logger logger} that can be used by the calling class.
     * @throws  NullPointerException if {@code name} is {@code null}.
     * @throws  IllegalCallerException if there is no Java caller frame on the stack.
     *
     * @see     Logger
     */
    default Logger log() {
        return LogAwareService.logger(getClass());
    }

}