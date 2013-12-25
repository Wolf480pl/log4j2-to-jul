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
