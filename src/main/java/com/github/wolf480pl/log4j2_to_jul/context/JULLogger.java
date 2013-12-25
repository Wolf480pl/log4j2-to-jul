package com.github.wolf480pl.log4j2_to_jul.context;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.AbstractLogger;

import com.github.wolf480pl.log4j2_to_jul.Util;

public class JULLogger extends AbstractLogger {
    private final Logger jul;

    public JULLogger(Logger jul) {
        super(jul.getName());
        this.jul = jul;
    }

    public JULLogger(String name) {
        super(name);
        this.jul = Logger.getLogger(name);
    }

    public JULLogger(String name, MessageFactory messageFactory) {
        super(name, messageFactory);
        this.jul = Logger.getLogger(name);
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
        LogRecord record = new LogRecord(Util.levelToJUL(level), data.getFormattedMessage());
        record.setThrown(t);
        record.setSourceClassName(fqcn);
        record.setLoggerName(this.jul.getName());
        // TODO: Propagate the marker
        this.jul.log(record);
    }

}
