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
 * 
 * @author wolf480
 *
 */
public class JULLogger extends AbstractLogger {
    public static final String DEFAULT_FORMAT = "%2$s - %1$s";
    public static final String PASSTHRU_FORMAT = "%s";
    private final Logger jul;
    private final String format;

    /**
     * Creates a new JULLogger redirecting log messages to the given {@link java.util.logging.Logger}.
     * <p>
     * For messages logged with this logger that have a non-empty {@link Marker}, the {@link #DEFAULT_FORMAT} will be used to add the marker to the message string. Messages that have empty ({@code null} or {@link Marker#getName() getName()} {@code == ""}) {@link Marker} will be passed to {@link java.util.logging.Logger} intact.
     * 
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     */
    public JULLogger(Logger jul) {
        this(jul, null);
    }

    /**
     * Creates a new JULLogger redirecting log messages to the given {@link java.util.logging.Logger}.
     * <p>
     * Messages logged with this logger will be formatted with the specified format. The format will be used like `message = {@link String#format(String, Object...) String.format(format, message, marker)}` so it can be used to add marker to the message.
     * 
     * @param jul the {@link java.util.logging.Logger} to which messages should be redirected
     * @param format the format string which should be used
     */
    public JULLogger(Logger jul, String format) {
        super(jul.getName());
        this.jul = jul;
        this.format = format;
    }

    /**
     * Creates a new JULLogger redirecting log messages to a {@link java.util.logginLogger} with the given name.
     * <p>
     * For messages logged with this logger that have a non-empty {@link Marker}, the {@link #DEFAULT_FORMAT} will be used to add the marker to the message string. Messages that have empty ({@code null} or {@link Marker#getName() getName()} {@code == ""}) {@link Marker} will be passed to {@link java.util.logging.Logger} intact.
     * 
     * @param name name of the {@link java.util.logging.Logger} to which messages should be redirected
     */
    public JULLogger(String name) {
        this(name, (String) null);
    }

    /**
     * Creates a new JULLogger redirecting log messages to a {@link java.util.logginLogger} with the given name.
     * <p>
     * Messages logged with this logger will be formatted with the specified format. The format will be used like `message = {@link String#format(String, Object...) String.format(format, message, marker)}` so it can be used to add marker to the message.
     * 
     * @param name name of the {@link java.util.logging.Logger} to which messages should be redirected
     * @param format the format string which should be used
     */
    public JULLogger(String name, String format) {
        super(name);
        this.jul = Logger.getLogger(name);
        this.format = format;
    }

    /**
     * Creates a new JULLogger redirecting log messages to a {@link java.util.logginLogger} with the given name.
     * <p>
     * For messages logged with this logger that have a non-empty {@link Marker}, the {@link #DEFAULT_FORMAT} will be used to add the marker to the message string. Messages that have empty ({@code null} or {@link Marker#getName() getName()} {@code == ""}) {@link Marker} will be passed to {@link java.util.logging.Logger} intact.
     * 
     * @param name name of the {@link java.util.logging.Logger} to which messages should be redirected
     * @param messageFactory the message factory, if null then use the default message factory
     */
    public JULLogger(String name, MessageFactory messageFactory) {
        this(name, messageFactory, null);
    }

    /**
     * Creates a new JULLogger redirecting log messages to a {@link java.util.logginLogger} with the given name.
     * <p>
     * Messages logged with this logger will be formatted with the specified format. The format will be used like `message = {@link String#format(String, Object...) String.format(format, message, marker)}` so it can be used to add marker to the message.
     * 
     * @param name name of the {@link java.util.logging.Logger} to which messages should be redirected
     * @param messageFactory the message factory, if null then use the default message factory
     * @param format the format string which should be used
     */
    public JULLogger(String name, MessageFactory messageFactory, String format) {
        super(name, messageFactory);
        this.jul = Logger.getLogger(name);
        this.format = format;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Message data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Object data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Object... p1) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Throwable t) {
        return this.jul.isLoggable(Util.levelToJUL(level));
    }

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
