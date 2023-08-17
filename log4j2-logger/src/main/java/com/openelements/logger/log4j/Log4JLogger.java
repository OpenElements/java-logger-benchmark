package com.openelements.logger.log4j;

import com.openelements.logger.api.Logger;
import org.apache.logging.log4j.*;
import org.slf4j.helpers.MessageFormatter;

public class Log4JLogger implements com.openelements.logger.api.Logger {

    String loggerName;

    private Marker currentMarker;

    public Log4JLogger(Class source) {
        loggerName = source.getName();
    }

    @Override
    public void log(String message) {
        org.apache.logging.log4j.Logger logger = getLogger();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message);
        }
    }

    @Override
    public void log(String message, Throwable throwable) {
        org.apache.logging.log4j.Logger logger = getLogger();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, throwable);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message, throwable);
        }
    }

    @Override
    public void log(String message, Object... args) {
        org.apache.logging.log4j.Logger logger = getLogger();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, args);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message, args);
        }
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        org.apache.logging.log4j.Logger logger = getLogger();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        }
    }

    private org.apache.logging.log4j.Logger getLogger() {
        // has to be re-aquired or it doesn't do anything.
        return LogManager.getLogger(loggerName);
    }

    @Override
    public Logger withMetadata(String key, Object value) {
        ThreadContext.put(key, value.toString());
        return this;
    }

    @Override
    public Logger withMarker(String marker) {
        if (currentMarker != null) {
            Marker parent = currentMarker;
            currentMarker = MarkerManager.getMarker(marker).addParents(parent);
        } else {
            currentMarker = MarkerManager.getMarker(marker);
        }
        return this;
    }

    @Override
    public void reset() {
        ThreadContext.clearAll();
    }
}
