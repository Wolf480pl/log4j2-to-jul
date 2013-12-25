package com.github.wolf480pl.log4j2_to_jul.context;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class JULContextFactory implements LoggerContextFactory {
    private final ConcurrentMap<String, JULContext> map = new ConcurrentHashMap<>();

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return getContext("");
    }

    public LoggerContext getContext(String prefix) {
        JULContext ctx = this.map.get(prefix);
        if (ctx != null) {
            return ctx;
        }
        ctx = new JULContext();
        final JULContext prev = this.map.putIfAbsent(prefix, ctx);
        return prev == null ? ctx : prev;
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        return getContext(fqcn, loader, currentContext);
    }

    @Override
    public void removeContext(LoggerContext context) {
        if (context instanceof JULContext) {
            this.map.remove(((JULContext) context).getPrefix());
        }
    }

}
