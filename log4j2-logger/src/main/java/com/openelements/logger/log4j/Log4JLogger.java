package com.openelements.logger.log4j;

import com.openelements.logger.api.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.helpers.MessageFormatter;

public class Log4JLogger implements com.openelements.logger.api.Logger {

    private final org.apache.logging.log4j.Logger logger;

    private Marker currentMarker;

    public Log4JLogger(Class source) {
        logger = org.apache.logging.log4j.LogManager.getLogger(source.getName());
    }

    @Override
    public void log(String message) {
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message);
        }
        ThreadContext.clearAll();
    }

    @Override
    public void log(String message, Throwable throwable) {
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, throwable);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message, throwable);
        }
        ThreadContext.clearAll();
    }

    @Override
    public void log(String message, Object... args) {
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, message, args);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, message, args);
        }
        ThreadContext.clearAll();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        if (currentMarker != null) {
            logger.log(Level.INFO, currentMarker, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
            currentMarker = null;
        } else {
            logger.log(Level.INFO, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        }
        ThreadContext.clearAll();
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
    public String createMessageWithPlaceholders(int placeholderCount) {
        return IntStream.range(0, placeholderCount)
                .mapToObj(i -> "{},")
                .collect(Collectors.joining(" "));
    }
}
