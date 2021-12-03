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
import java.util.function.Supplier;

/**
 * The {@code LoggerBridge} interface is a basic wrapper around abstract logger. It defines basic logging methods that
 * used and extended by it descendant such as {@link Logger}.
 * <p>
 * This interface lets program call logger's method with level, message template and arguments, like this:
 * <pre>log(System.Logger.Level.INFO, "Hello %s! Today is %tc.", "world", LocalDateTime.now());</pre>
 * or
 * <pre>logWithThrowable(System.Logger.Level.ERROR, "Attention, %s! An exception '%s' occurred while reading news from "
 *     + "'%s':%n", throwable, context.getSupport(), throwable.getMessage(), context.getSource());</pre>
 *
 * @author  Andrey OCHIROV
 * @version 1.0
 *
 * @see     java.lang.System.Logger
 * @see     java.lang.System.Logger.Level
 * @see     String#format(String, Object...)
 */
public interface LoggerBridge {

    /**
     * Checks if a message of the given level would be logged by this logger.
     *
     * @param   level the log message level.
     * @return  {@code true} if the given log message level is currently being logged.
     * @throws  NullPointerException if {@code level} is {@code null}.
     */
    boolean isLoggable(Level level);

    /**
     * Logs a message.
     *
     * @param   level one of the message level identifiers, e.g., {@link Level#ERROR}.
     * @param   message the string message. Can be {@code null}.
     * @throws  NullPointerException if {@code level} is {@code null}.
     */
    default void log(final Level level, final String message) {
        log(level, (Throwable)null, message);
    }

    /**
     * Logs a lazily constructed message.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking
     * the provided supplier function.
     *
     * @param   level one of the message level identifiers, e.g., {@link Level#ERROR}.
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     */
    default void log(final Level level, final String message, final Object... arguments) {
        log(level, (Throwable)null, message, arguments);
    }

    /**
     * Logs a message, constructed by the given message supplier.
     *
     * @param   level one of the message level identifiers, e.g., {@link Level#ERROR}.
     * @param   messageSupplier a supplier function that produces a message.
     * @throws  NullPointerException if {@code level} is {@code null}, or {@code messageSupplier} is {@code null}.
     */
    default void log(final Level level, final Supplier<?> messageSupplier) {
        log(level, (Throwable)null, messageSupplier);
    }

    /**
     * Logs a lazily constructed message, with associated {@link Throwable} information.
     * <p>
     * If the logger is currently enabled for the given message level then the message is constructed by invoking the
     * provided supplier function.
     *
     * @param   level one of the message level identifiers, e.g., {@link Level#ERROR}.
     * @param   thrown throwable associated with log message.
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     */
    void log(final Level level, final Throwable thrown, final String message, final Object... arguments);

    /**
     * Logs a message, constructed by the given message supplier.
     *
     * @param   level one of the message level identifiers, e.g., {@link Level#ERROR}.
     * @param   thrown throwable associated with log message.
     * @param   messageSupplier a supplier function that produces a message.
     * @throws  NullPointerException if {@code level} is {@code null}, or {@code messageSupplier} is {@code null}.
     */
    void log(Level level, Throwable thrown, Supplier<?> messageSupplier);

}