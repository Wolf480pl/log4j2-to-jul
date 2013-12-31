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

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

/**
 * An implementation of LogContextFactory for creating instances of {@link JULContext}.
 */
public class JULContextFactory implements LoggerContextFactory {
    private final ConcurrentMap<String, JULContext> map = new ConcurrentHashMap<>();

    /**
     * Creates a {@link JULContext}.
     * <p>
     * If the loader is null, just calls {@link #getContext(String) getContext("")}. Otherwise the behavior is <strong>undefined</strong>, and is a subject to change when new features are added.
     * 
     * @param fqcn does nothing
     * @param loader does nothing if null; if not null, some behavior may be implemented in the future
     * @param currentContext does nothing
     * @return the JULContext.
     */
    @Override
    public JULContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return getContext("");
    }

    /**
     * Creates a {@link JULContext} (or returns an existing one) with a specified prefix. All the loggers created by that context will use  their name appended after that prefix as the name of the underlying {@link java.util.logging.Logger}.
     * 
     * @param prefix the prefix
     * @return the {@link JULContext}.
     */
    public JULContext getContext(String prefix) {
        JULContext ctx = this.map.get(prefix);
        if (ctx != null) {
            return ctx;
        }
        ctx = new JULContext(prefix);
        final JULContext prev = this.map.putIfAbsent(prefix, ctx);
        return prev == null ? ctx : prev;
    }

    /**
     * Creates a {@link JULContext}.
     * <p>
     * <strong>If's undefined (and a subject to change when new features are added) whether any of the arguments of this method has any impact on the returned JULContext</strong>
     * 
     * @param fqcn the fully qualified class name of the caller - may be implemented in the future
     * @param loader the ClassLoader to use or null - may be implemented in the future
     * @param currentContext if true returns the current Context, if false returns the Context appropriate
     * for the caller if a more appropriate Context can be determined - may be implemented in the future
     * @param configLocation the location of the configuration for the JULContext - may be implemented in the future
     * @return the JULContext.
     */
    @Override
    public JULContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        return getContext(fqcn, loader, currentContext);
    }

    /**
     * Removes knowledge of a {@link JULContext}.
     * <p>
     * Does nothing if the context is not an instance of {@link JULContext}.
     * If the context is an instance of {@link JULContext}, but is not known to this factory, then the behavior is undefined, and is a subject to change when new features are added.
     * 
     * @param context the context to remove
     */
    @Override
    public void removeContext(LoggerContext context) {
        if (context instanceof JULContext) {
            this.map.remove(((JULContext) context).getPrefix());
        }
    }

}
