package com.github.wolf480pl.log4j2_to_jul;

import java.util.logging.Level;

public class Util {

    private Util() {
    }

    public static Level levelToJUL(org.apache.logging.log4j.Level lvl) {
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
}
