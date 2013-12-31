/*
 * This file is part of Log4j2 to JUL, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Wolf480pl <wolf480@interia.pl/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.wolf480pl.log4j2_to_jul.context;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

import com.github.wolf480pl.log4j2_to_jul.Util;

/**
 * An implementation of {@link org.apache.logging.log4j.Logger} that redirects all the log messages to specified {@link java.util.logging.Logger}.
 */
public class JULLogger extends AbstractLogger {
    /**
     * The default format for adding {@link Marker Markers} to the message. It prepends the marker name followed by {@code " - "} at the beginning of the message.
     */
    public static final String DEFAULT_FORMAT = "%2$s - %1$s";
    /**
     * A passthru format - leaves the message intact.
     */
    public static final String PASSTHRU_FORMAT = "%s";
    private final Logger jul;
    private String format;

    /**
     * Creates a new JULLogger redirecting log messages to the given {@link java.util.logging.Logger}, with the same name as the given logger.
     * 
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     */
    public JULLogger(Logger jul) {
        this(jul.getName(), jul);
    }

    /**
     * Creates a new JULLogger redirecting log messages to the given {@link java.util.logging.Logger}, with the same name as the given logger.
     * 
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     * @param messageFactory the message factory, if null then use the default message factory
     */
    public JULLogger(Logger jul, MessageFactory messageFactory) {
        this(jul.getName(), jul, messageFactory);
    }

    /**
     * Creates a new JULLogger with the specified name, redirecting log messages to a {@link java.util.logging.Logger} with the same name.
     * 
     * @param name name of this logger
     */
    public JULLogger(String name) {
        this(name, name);
    }

    /**
     * Creates a new JULLogger with the specified name, redirecting log messages to a {@link java.util.logging.Logger} with the same name.
     * 
     * @param name name of this logger
     * @param messageFactory the message factory, if null then use the default message factory
     */
    public JULLogger(String name, MessageFactory messageFactory) {
        this(name, name, messageFactory);
    }

    /**
     * Creates a new JULLogger with the specified name, redirecting log messages to a {@link java.util.logging.Logger} with the name {@code julName}.
     * 
     * @param name name of this logger
     * @param julName name of the {@link java.util.logging.Logger} to which the messages should be redirected
     */
    public JULLogger(String name, String julName) {
        this(name, Logger.getLogger(julName));
    }

    /**
     * Creates a new JULLogger with the specified name, redirecting log messages to a {@link java.util.logging.Logger} with the name {@code julName}.
     * 
     * @param name name of this logger
     * @param julName name of the {@link java.util.logging.Logger} to which the messages should be redirected
     * @param messageFactory the message factory, if null then use the default message factory
     */
    public JULLogger(String name, String julName, MessageFactory messageFactory) {
        this(name, Logger.getLogger(julName), messageFactory);
    }

    /**
     * Creates a new JULLogger with he specified name, redirecting log messages to the given {@link java.util.logging.Logger}.
     * 
     * @param name name of this logger
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     */
    public JULLogger(String name, Logger jul) {
        super(name);
        this.jul = jul;
    }

    /**
     * Creates a new JULLogger with he specified name, redirecting log messages to the given {@link java.util.logging.Logger}.
     * 
     * @param name name of this logger
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     * @param messageFactory the message factory, if null then use the default message factory
     */
    public JULLogger(String name, Logger jul, MessageFactory messageFactory) {
        super(name, messageFactory);
        this.jul = jul;
    }

    /**
     * Sets the format to apply on the messages before passing them to {@link java.util.logging.Logger}.
     * <p>
     * The format string is in the same syntax as in {@link String#format(String, Object...)}.
     * <p>
     * If the format string is null (the default), for messages logged with this logger that have a non-empty {@link Marker}, the {@link #DEFAULT_FORMAT} will be used to add the marker to the message string. Messages that have empty ({@code null} or {@link Marker#getName() getName()} {@code == ""}) {@link Marker} will be passed to {@link java.util.logging.Logger} intact.
     * <p>
     * If the format string is not null, messages logged with this logger will be formatted with it. The format will be used like `message = {@link String#format(String, Object...) String.format(format, message, marker)}` so it can be used to add marker to the message.
     * 
     * @param format the format string
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Returns the format to apply on the messages before passing them to {@link java.util.logging.Logger}.
     * 
     * @see #setFormat(String)
     * @return the format string
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Determine if logging is enabled for the specified level.
     * 
     * @param level the logging Level to check.
     * @param marker a Marker, not checked
     * @param data the Message, not checked
     * @param t a Throwable, not checked
     * @return true if logging is enabled, false otherwise
     */
    @Override
    protected boolean isEnabled(Level level, Marker marker, Message data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    /**
     * Determine if logging is enabled for the specified level.
     * 
     * @param level the logging Level to check.
     * @param marker a Marker, not checked
     * @param data the Message, not checked
     * @param t a Throwable, not checked
     * @return true if logging is enabled, false otherwise
     */
    @Override
    protected boolean isEnabled(Level level, Marker marker, Object data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    /**
     * Determine if logging is enabled for the specified level.
     * 
     * @param level the logging Level to check.
     * @param marker a Marker, not checked
     * @param data the Message, not checked
     * @return true if logging is enabled, false otherwise
     */
    @Override
    protected boolean isEnabled(Level level, Marker marker, String data) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    /**
     * Determine if logging is enabled for the specified level.
     * 
     * @param level the logging Level to check.
     * @param marker a Marker, not checked
     * @param data the Message, not checked
     * @param p1 the parameters, not checked
     * @return true if logging is enabled, false otherwise
     */
    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Object... p1) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    /**
     * Determine if logging is enabled for the specified level.
     * 
     * @param level the logging Level to check.
     * @param marker a Marker, not checked
     * @param data the Message, not checked
     * @param t a Throwable, not checked
     * @return true if logging is enabled, false otherwise
     */
    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    /**
     * Logs a message with location information by redirecting it to the underlaying {@link java.util.logging.Logger}.
     *
     * @param marker the Marker
     * @param fqcn   the fully qualified class name of the <b>caller</b>
     * @param level  the logging level
     * @param data   the Message.
     * @param t      a Throwable or null.
     */
    @Override
    public void log(Marker marker, String fqcn, Level level, Message data, Throwable t) {
        final String msg;
        String mkName = (marker == null) ? "" : marker.getName();
        if (this.format == null) {
            if (mkName.isEmpty()) {
                msg = data.getFormattedMessage();
            } else {
                msg = String.format(DEFAULT_FORMAT, data.getFormattedMessage(), mkName);
            }
        } else {
            msg = String.format(this.format, data.getFormattedMessage(), mkName);
        }
        LogRecord record = new LogRecord(Util.levelToJUL(level), msg);
        record.setThrown(t);
        record.setSourceClassName(fqcn);
        record.setLoggerName(this.jul.getName());
        this.jul.log(record);
    }

}
