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

import java.util.logging.Logger;

import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class JULManager extends AbstractManager {
    private final Logger jul;

    protected JULManager(String name) {
        super(name);
        this.jul = Logger.getLogger(name);
    }

    public Logger getJUL() {
        return this.jul;
    }

    public static JULManager getJULManager(String name) {
        return getManager(name, FACTORY, null);
    }

    private static JULManagerFactory FACTORY = new JULManagerFactory();

    private static class JULManagerFactory implements ManagerFactory<JULManager, Void> {
        @Override
        public JULManager createManager(String name, Void data) {
            return new JULManager(name);
        }
    }
}
