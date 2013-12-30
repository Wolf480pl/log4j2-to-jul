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

    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public Object getExternalContext() {
        return this.parent;
    }

    @Override
    public Logger getLogger(String name) {
        JULLogger logger = this.loggers.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new JULLogger(this.prefix + name);
        final JULLogger prev = this.loggers.putIfAbsent(name, logger);
        return prev == null ? logger : prev;
    }

    @Override
    public Logger getLogger(String name, MessageFactory messageFactory) {
        JULLogger logger = this.loggers.get(name);
        if (logger != null) {
            return logger;
        }
        logger = new JULLogger(this.prefix + name, messageFactory);
        final JULLogger prev = this.loggers.putIfAbsent(name, logger);
        return prev == null ? logger : prev;
    }

    @Override
    public boolean hasLogger(String name) {
        return this.loggers.containsKey(name);
    }

}
