package com.openelements.logger.log4j;

import com.openelements.logger.api.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.helpers.MessageFormatter;

public class Log4JLogger implements com.openelements.logger.api.Logger {

    String loggerName;

    private ThreadLocal<Marker> markerHolder = new ThreadLocal<>();

    public Log4JLogger(Class source) {
        loggerName = source.getName();
    }

    @Override
    public void log(String message) {
        org.apache.logging.log4j.Logger logger = getLogger();
        Marker currentMarker = getCurrentMarker();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message);
        } else {
            logger.log(Level.INFO, message);
        }
        reset();
    }

    @Override
    public void log(String message, Throwable throwable) {
        org.apache.logging.log4j.Logger logger = getLogger();
        Marker currentMarker = getCurrentMarker();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, throwable);
        } else {
            logger.log(Level.INFO, message, throwable);
        }
        reset();
    }

    @Override
    public void log(String message, Object... args) {
        org.apache.logging.log4j.Logger logger = getLogger();
        Marker currentMarker = getCurrentMarker();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, args);
        } else {
            logger.log(Level.INFO, message, args);
        }
        reset();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        org.apache.logging.log4j.Logger logger = getLogger();
        Marker currentMarker = getCurrentMarker();
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        } else {
            logger.log(Level.INFO, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        }
        reset();
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
        Marker parent = getCurrentMarker();
        if (parent != null) {
            setCurrentMarker(MarkerManager.getMarker(marker).addParents(parent));
        } else {
            setCurrentMarker(MarkerManager.getMarker(marker));
        }
        return this;
    }

    private Marker getCurrentMarker() {
        return markerHolder.get();
    }

    private void setCurrentMarker(Marker marker) {
        markerHolder.set(marker);
    }

    public void reset() {
        ThreadContext.clearAll();
        markerHolder.remove();
    }
}
