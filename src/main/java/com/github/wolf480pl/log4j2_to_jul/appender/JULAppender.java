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
package com.github.wolf480pl.log4j2_to_jul.appender;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusLogger;

import com.github.wolf480pl.log4j2_to_jul.Util;

/**
 * A Log4j2 appender that redirects all log events to a specified {@link java.util.logging.Logger}. It's config name is JUL.
 */
@Plugin(name = "JUL", category = "Core", elementType = "appender", printObject = true)
public final class JULAppender extends AbstractAppender {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    protected static final String DEFAULT_FCQN = AbstractLogger.class.getName();
    protected static final String DEFAULT_PATTERN = "%m%rEx{0}";

    private final JULManager manager;

    private JULAppender(String name, Layout<? extends Serializable> layout, Filter filter, JULManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }

    /**
     * Logs the event by redirecting its contents to the {@link java.util.logging.Logger} configured in this appender.
     */
    @Override
    public void append(LogEvent event) {
        Level level = Util.levelToJUL(event.getLevel());
        String message;
        Serializable ser = getLayout().toSerializable(event);
        if (ser instanceof String) {
            message = (String) ser;
        } else {
            byte[] bytes = getLayout().toByteArray(event);
            message = new String(bytes);
        }
        LogRecord record = new LogRecord(level, message);
        java.util.logging.Logger jul = this.manager.getJUL();
        record.setThrown(event.getThrown());
        record.setMillis(event.getMillis());
        if (jul.getName().isEmpty()) {
            record.setLoggerName(event.getLoggerName());
        } else {
            record.setLoggerName(jul.getName() + "." + event.getLoggerName());
        }
        StackTraceElement source = event.getSource();
        if (source != null) {
            record.setSourceClassName(source.getClassName());
            record.setSourceMethodName(source.getMethodName());
        } else if (!event.getFQCN().equals(DEFAULT_FCQN)) {
            record.setSourceClassName(event.getFQCN());
        }
        jul.log(record);
    }

    /**
     * Creates a new instance of JULAppender.
     * 
     * @param name name of the appender
     * @param logger name of the {@link java.util.logging.Logger} to which log events should be redirected
     * @param ignore if true, no exceptions from this appender will be propagated to the application; if false, then undefined (and is a subject to change when new features are added)
     * @param layout the layout to use to format the message field of {@link java.util.logging.LogRecord}
     * @param filter the filter to associate with this appender
     * @return
     */
    @PluginFactory
    public static JULAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("logger") String logger, @PluginAttribute("ignoreExceptions") String ignore,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filters") Filter filter) {
        boolean ignoreExceptions = Boolean.parseBoolean(ignore);
        if (name == null) {
            LOGGER.error("No name provided for JULAppender");
            return null;
        }
        if (logger == null) {
            logger = "";
        }
        JULManager manager = JULManager.getJULManager(logger);
        if (manager == null) {
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(DEFAULT_PATTERN, null, null, null, null);
        }
        return new JULAppender(name, layout, filter, manager, ignoreExceptions);
    }

}