/*
 * InLab Software Product
 *
 * Copyright (c) 2020, InLab Software, LLC. All rights reserved.
 * Use is subject to license terms.
 *
 * http://www.inlabsoft.com/products/product/license
 */
package com.inlabsoft.sen.shared.logging;

import static com.inlabsoft.sen.shared.core.ResourceScope.CLASSPATH;

/**
 * The {@code LogEnvironment} class is a class with major public constants that used to securely get values from System
 * properties and environment in order to get custom {@link LoggerFactory} implementation (if specified).
 *
 * @author  Andrey OCHIROV
 * @version 1.0
 */
public final class LogEnvironment {

    /**
     * The name of the environment parameter in {@link System#getenv(String)} which used to provide custom
     * implementation of the {@link LoggerFactory} interface. The parameter value is optional and can be used when the
     * logger factory from the application's configuration should be replaced with something else.
     *
     * @implSpec    The value from the environment parameter is taken only if optional value is missing in
     * {@link System#getProperties()}.
     *
     * @see     #LOG_FACTORY_NAME_PARAM
     */
    public static final String LOG_FACTORY_NAME_ENVIRONMENT_PARAM = "SEN_SHARED_LOG_FACTORY";

    /**
     * The name of the parameter in {@link System#getProperties()} which used to provide custom implementation of the
     * {@link LoggerFactory} interface. The parameter value is optional and can be used when the logger factory from
     * the application's configuration should be replaced with something else.
     */
    public static final String LOG_FACTORY_NAME_PARAM = "sen.shared.logging.factory";

    /**
     * The name of the configuration file with the name of logger factory to use. Such factory will be used only if
     * alternative name is not specified under {@link #LOG_FACTORY_NAME_ENVIRONMENT_PARAM} or
     * {@link #LOG_FACTORY_NAME_PARAM} variables.
     * <p>
     * @implSpec    The source file should be available via application's class path only.
     */
    public static final String LOG_FACTORY_CONFIG = CLASSPATH.resolve("META-INF/log");

    /**
     * A default file name for the logging configuration properties.
     */
    public static final String LOG_DEFAULT_CONFIG_FILE = CLASSPATH.resolve("META-INF/logging/log.properties");

    /**
     * A name of the variable inside system properties that used to specify custom logging configuration.
     * <p>
     * The name can use three different scopes:
     * <ul>
     *  <li>classpath (classpath:META-INF/logging/mycfg.properties)</li>
     *  <li>file (file:src/test/resources/META-INF/logging/mycfg.properties)</li>
     *  <li>url (url:file:/&lt;full path&gt;/lib/lib.jar!META-INF/logging/mycfg.properties)</li>
     * </ul>
     */
    public static final String LOG_CONFIG_NAME_PARAM = "sen.shared.logging.config";
    public static final String LOG_CONFIG_NAME_ENVIRONMENT_PARAM = "SEN_SHARED_LOG_CONFIG";

    /**
     * Constructor of the class {@code LogEnvironment} was declared as private to prevent its instantiation.
     */
    private LogEnvironment() {
        throw new UnsupportedOperationException("Unable to instantiate utility class");
    }

}