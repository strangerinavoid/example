/*
 * InLab Software Product
 *
 * Copyright (c) 2020, InLab Software, LLC. All rights reserved.
 * Use is subject to license terms.
 *
 * http://www.inlabsoft.com/products/product/license
 */
package com.inlabsoft.sen.shared.logging;

import com.inlabsoft.sen.shared.logging.internal.LogAwareService;

/**
 * The {@code LoggerFactory} is an interface of {@link Logger} producing service, where {@link Logger} represents
 * various logging APIs, such as {@code JUL} or {@code TinyLog}.
 *
 * @author  Andrey OCHIROV
 * @version 1.0
 */
public interface LoggerFactory {

    /**
     * Returns an instance of {@link Logger logger} for the caller's use.
     *
     * @apiNote
     * This method may defer calling the {@link internal} to create an actual logger supplied by the logging backend,
     * for instance, to allow loggers to be obtained during the application initialization time.
     *
     * @param    name the name of the logger.
     * @return   an instance of {@link Logger logger} that can be used by the calling class.
     * @throws   NullPointerException if {@code name} is {@code null}.
     * @throws   IllegalCallerException if there is no Java caller frame on the stack.
     */
    static Logger getLogger(final Class<?> source) {
        return LogAwareService.logger(source);
    }

    /**
     * Returns a logger named corresponding to the class passed as parameter.
     *
     * @param   source the non-null class, which name is used as a name for the returned logger.
     * @return  an instance of the {@link Logger}.
     * @throws  NullPointerException if {@code source} is {@code null}.
     * @throws  IllegalCallerException if there is no Java caller frame on the stack.
     */
    Logger logger(Class<?> source);

}