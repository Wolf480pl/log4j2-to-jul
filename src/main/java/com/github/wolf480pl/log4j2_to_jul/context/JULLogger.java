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
    public static final String DEFAULT_FORMAT = "%2$s - %1$s";
    public static final String PASSTHRU_FORMAT = "%s";
    private final Logger jul;
    private final String format;

    public JULLogger(Logger jul) {
        this(jul, null);
    }

    public JULLogger(Logger jul, String format) {
        super(jul.getName());
        this.jul = jul;
        this.format = format;
    }

    public JULLogger(String name) {
        this(name, (String) null);
    }

    public JULLogger(String name, String format) {
        super(name);
        this.jul = Logger.getLogger(name);
        this.format = format;
    }

    public JULLogger(String name, MessageFactory messageFactory) {
        this(name, messageFactory, null);
    }

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
                msg = String.format(DEFAULT_FORMAT, mkName, data.getFormattedMessage());
            }
        } else {
            msg = String.format(this.format, mkName, data.getFormattedMessage());
        }
        LogRecord record = new LogRecord(Util.levelToJUL(level), msg);
        record.setThrown(t);
        record.setSourceClassName(fqcn);
        record.setLoggerName(this.jul.getName());
        this.jul.log(record);
    }

}
