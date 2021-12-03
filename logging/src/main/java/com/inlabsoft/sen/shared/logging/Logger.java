/*
 * InLab Software Product
 *
 * Copyright (c) 2020, InLab Software, LLC. All rights reserved.
 * Use is subject to license terms.
 *
 * http://www.inlabsoft.com/products/product/license
 */
package com.inlabsoft.sen.shared.logging;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.Logger.Level.WARNING;

import java.lang.System.Logger.Level;
import java.util.function.Supplier;

/**
 * The {@code Logger} interface is n abstract logging interface that wraps in a modern manner existing logging
 * interfaces and supports message formatting and lazy logging by using functions and suppliers.
 * <p>
 * This interface lets program call logger's method with message template and arguments, like this:
 * <pre>info("Hello %s! Today is %tc.", "world", LocalDateTime.now());</pre>
 * or
 * <pre>error("Attention, {}! Exception '{}' occurred while reading news from '{}':\n", throwable,
 *     context.getSupport(), throwable.getMessage(), context.getSource());</pre>
 * <p>
 * NOTE: It is important to understand that format specifiers are not defined by this interface and depends on
 * particular implementation.
 *
 * @author	Andrey OCHIROV
 * @version	1.0
 */
public interface Logger extends LoggerBridge {

    /**
     * Logs a {@link Level#DEBUG} message.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger debug(final String message) {
        log(DEBUG, message);
        return this;
    }

    /**
     * Logs a {@link Level#DEBUG} message with given arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger debug(final String message, final Object... arguments) {
        log(DEBUG, message, arguments);
        return this;
    }

    /**
     * Logs a {@link Level#DEBUG} message with given throwable and arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   thrown a throwable associated with log message.
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger debug(final String message, final Throwable thrown, final Object... arguments) {
        log(DEBUG, thrown, message, arguments);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#DEBUG} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#DEBUG} level then the message is constructed by invoking
     * the provided supplier function.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger debug(final Supplier<?> msgSupplier) {
        log(DEBUG, msgSupplier);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#DEBUG} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#DEBUG} level then the message is constructed by
     * invoking the provided mapping function with newly constructed {@link LoggerMessageContext builder}.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @param   thrown a throwable associated with log message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger debug(final Supplier<?> msgSupplier, final Throwable thrown) {
        log(DEBUG, thrown, msgSupplier);
        return this;
    }

    /**
     * Logs a {@link Level#ERROR} message.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger error(final String message) {
        log(ERROR, message);
        return this;
    }

    /**
     * Logs a {@link Level#ERROR} message with given arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger error(final String message, final Object... arguments) {
        log(ERROR, message, arguments);
        return this;
    }

    /**
     * Logs a {@link Level#ERROR} message with given throwable and arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   thrown a throwable associated with log message.
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger error(final String message, final Throwable thrown, final Object... arguments) {
        log(ERROR, thrown, message, arguments);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#ERROR} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#ERROR} level then the message is constructed by invoking
     * the provided supplier function.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger error(final Supplier<?> msgSupplier) {
        log(ERROR, msgSupplier);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#ERROR} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#ERROR} level then the message is constructed by
     * invoking the provided mapping function with newly constructed {@link LoggerMessageContext builder}.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @param   thrown a throwable associated with log message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger error(final Supplier<?> msgSupplier, final Throwable thrown) {
        log(ERROR, thrown, msgSupplier);
        return this;
    }

    /**
     * Logs an {@link System.Logger.Level#INFO} message.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger info(final String message) {
        log(INFO, message);
        return this;
    }

    /**
     * Logs an {@link System.Logger.Level#INFO} message with given arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger info(final String message, final Object... arguments) {
        log(INFO, message, arguments);
        return this;
    }

    /**
     * Logs an {@link System.Logger.Level#INFO} message with given throwable and arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   thrown a throwable associated with log message.
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger info(final String message, final Throwable thrown, final Object... arguments) {
        log(INFO, thrown, message, arguments);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#INFO} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#INFO} level then the message is constructed by invoking
     * the provided supplier function.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger info(final Supplier<?> msgSupplier) {
        log(INFO, msgSupplier);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#INFO} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#INFO} level then the message is constructed by
     * invoking the provided mapping function with newly constructed {@link LoggerMessageContext builder}.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @param   thrown a throwable associated with log message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger info(final Supplier<?> msgSupplier, final Throwable thrown) {
        log(INFO, thrown, msgSupplier);
        return this;
    }

    /**
     * Logs an {@link Level#TRACE} message.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger trace(final String message) {
        log(TRACE, message);
        return this;
    }

    /**
     * Logs an {@link Level#TRACE} message with given arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger trace(final String message, final Object... arguments) {
        log(TRACE, message, arguments);
        return this;
    }

    /**
     * Logs an {@link Level#TRACE} message with given throwable and arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   thrown a throwable associated with log message.
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger trace(final String message, final Throwable thrown, final Object... arguments) {
        log(TRACE, thrown, message, arguments);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#TRACE} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#TRACE} level then the message is constructed by invoking
     * the provided supplier function.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger trace(final Supplier<?> msgSupplier) {
        log(TRACE, msgSupplier);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#TRACE} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#TRACE} level then the message is constructed by
     * invoking the provided mapping function with newly constructed {@link LoggerMessageContext builder}.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @param   thrown a throwable associated with log message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger trace(final Supplier<?> msgSupplier, final Throwable thrown) {
        log(TRACE, thrown, msgSupplier);
        return this;
    }

    /**
     * Logs a {@link Level#WARNING} message.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger warning(final String message) {
        log(WARNING, message);
        return this;
    }

    /**
     * Logs a {@link Level#WARNING} message with given arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger warning(final String message, final Object... arguments) {
        log(WARNING, message, arguments);
        return this;
    }

    /**
     * Logs a {@link Level#WARNING} message with given throwable and arguments.
     *
     * @param   message the string message/pattern (or a key in the message catalog).
     * @param   thrown a throwable associated with log message.
     * @param   arguments the optional arguments to pass to the given string pattern (or a key in the message catalog).
     * @return  this logger.
     */
    default Logger warning(final String message, final Throwable thrown, final Object... arguments) {
        log(WARNING, thrown, message, arguments);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#WARNING} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#WARNING} level then the message is constructed by
     * invoking the provided supplier function.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger warning(final Supplier<?> msgSupplier) {
        log(WARNING, msgSupplier);
        return this;
    }

    /**
     * Logs a lazily constructed {@link Level#WARNING} message.
     * <p>
     * If the logger is currently enabled for the {@link Level#WARNING} level then the message is constructed by
     * invoking the provided mapping function with newly constructed {@link LoggerMessageContext builder}.
     *
     * @param   msgSupplier a supplier function that produces a message.
     * @param   thrown a throwable associated with log message.
     * @return  this logger.
     * @throws  NullPointerException if {@code msgSupplier} is {@code null}.
     */
    default Logger warning(final Supplier<?> msgSupplier, final Throwable thrown) {
        log(WARNING, thrown, msgSupplier);
        return this;
    }

}