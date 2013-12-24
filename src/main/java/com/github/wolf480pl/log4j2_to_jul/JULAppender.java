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
package com.github.wolf480pl.log4j2_to_jul;

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
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "JULAppender", category = "Core", elementType = "appender", printObject = true)
public final class JULAppender extends AbstractAppender {
    protected static final Logger LOGGER = StatusLogger.getLogger();

    private final JULManager manager;

    private JULAppender(String name, Layout<? extends Serializable> layout, Filter filter, JULManager manager, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }

    public void append(LogEvent event) {
        Level level = levelToJUL(event.getLevel());
        LogRecord record = new LogRecord(level, event.getMessage().getFormattedMessage());
        record.setThrown(event.getThrown());
        record.setMillis(event.getMillis());
        record.setSourceMethodName(event.getSource().getMethodName());
        record.setSourceClassName(event.getFQCN());
        this.manager.getJUL().log(record);
    }

    private Level levelToJUL(org.apache.logging.log4j.Level lvl) {
        switch (lvl) {
        case OFF:
            return Level.OFF;
        case FATAL:
        case ERROR:
            return Level.SEVERE;
        case WARN:
            return Level.WARNING;
        case INFO:
            return Level.INFO;
        case DEBUG:
            return Level.FINE;
        case TRACE:
            return Level.FINER;
        case ALL:
            return Level.ALL;
        }
        return null;
    }

    @PluginFactory
    public static JULAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filters") Filter filter) {
        boolean ignoreExceptions = Boolean.parseBoolean(ignore);
        if (name == null) {
            LOGGER.error("No name provided for JULAppender");
            return null;
        }
        JULManager manager = JULManager.getJULManager(name);
        if (manager == null) {
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        return new JULAppender(name, layout, filter, manager, ignoreExceptions);
    }

}