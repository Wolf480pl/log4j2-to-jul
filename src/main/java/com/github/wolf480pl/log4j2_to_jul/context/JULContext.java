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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.LoggerContext;

/**
 * An implementation of LoggerContext that creates {@link JULLogger JULLoggers} which redirect all the logging to a {@link java.util.logging.Logger} whose name is the name of the JULLogger appended after the prefix of the JULContext.
 */
public class JULContext implements LoggerContext {
    private final String prefix;
    private final java.util.logging.Logger parent;
    private final ConcurrentMap<String, JULLogger> loggers = new ConcurrentHashMap<>();

    public JULContext() {
        this("");
    }

    public JULContext(java.util.logging.Logger parent) {
        this.prefix = parent.getName();
        this.parent = parent;
    }

    public JULContext(String prefix) {
        this.prefix = prefix;
        this.parent = java.util.logging.Logger.getLogger(prefix);
    }

    /**
     * Returns the prefix of this JULLogger
     * 
     * @return the prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Returns the {@link java.util.logging.Logger} with the same name as the prefix of this JULContext
     * 
     * @return a {@link java.util.logging.Logger}
     */
    @Override
    public java.util.logging.Logger getExternalContext() {
        return this.parent;
    }

    /**
     * Returns a {@link JULLogger} with the specified name, and the name prepended with the prefix of this JULContext as its julName (the name of the underlaying {@link java.util.logging.Logger})
     * 
     * @param name name of the logger to return
     */
    @Override
    public Logger getLogger(String name) {
        JULLogger logger = this.loggers.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new JULLogger(name, this.prefix + name);
        final JULLogger prev = this.loggers.putIfAbsent(name, logger);
        return prev == null ? logger : prev;
    }

    /**
     * Returns a {@link JULLogger} with the specified name, and the name prepended with the prefix of this JULContext as its julName (the name of the underlaying {@link java.util.logging.Logger})
     * 
     * @param name name of the logger to return
     * @param messageFactory the message factory is used only when creating a logger, subsequent use fails silently
     */
    @Override
    public Logger getLogger(String name, MessageFactory messageFactory) {
        JULLogger logger = this.loggers.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new JULLogger(name, this.prefix + name, messageFactory);
        final JULLogger prev = this.loggers.putIfAbsent(name, logger);
        return prev == null ? logger : prev;
    }

    /**
     * Detects if a Logger with the specified name exists.
     * 
     * @param name The Logger name to search for.
     * @return true if the Logger exists, false otherwise.
     */
    @Override
    public boolean hasLogger(String name) {
        return this.loggers.containsKey(name);
    }

}
