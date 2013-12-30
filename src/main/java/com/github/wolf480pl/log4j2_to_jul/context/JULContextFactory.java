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

public class JULContextFactory implements LoggerContextFactory {
    private final ConcurrentMap<String, JULContext> map = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     * <p>
     * Note that this method completely ignores all its arguments and just calls {@link #getContext(String) getContext("")}.
     */
    @Override
    public JULContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        return getContext("");
    }

    /**
     * Creates a {@link JULContext} (or returns an existing one) with a specified prefix. All the loggers created by that context will use  their name appended after that prefix as the name of the underlaying {@link java.util.logging.Logger}.
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
     * {@inheritDoc}
     * <p>
     * Note that this method completely ignores configLocation and just calls {@link #getContext(String, ClassLoader, boolean)}.
     */
    @Override
    public JULContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        return getContext(fqcn, loader, currentContext);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Actually, it doesn't check if we know the given context. If it's a {@link JULContext}, we remove knowledge of any known JULContext with the same prefix.
     */
    @Override
    public void removeContext(LoggerContext context) {
        if (context instanceof JULContext) {
            this.map.remove(((JULContext) context).getPrefix());
        }
    }

}
