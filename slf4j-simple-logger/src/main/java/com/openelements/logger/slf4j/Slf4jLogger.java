package com.openelements.logger.slf4j;

import com.openelements.logger.api.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

public class Slf4jLogger implements Logger {

    private final org.slf4j.Logger logger;

    private Marker marker;

    public Slf4jLogger(Class cls) {
        logger = org.slf4j.LoggerFactory.getLogger(cls);
    }

    @Override
    public void log(String message) {
        if (marker != null) {
            logger.info(marker, message);
        } else {
            logger.info(message);
        }
        marker = null;
        MDC.clear();
    }

    @Override
    public void log(String message, Throwable throwable) {
        if (marker != null) {
            logger.info(marker, message, throwable);
        } else {
            logger.info(message, throwable);
        }
        marker = null;
        MDC.clear();
    }

    @Override
    public void log(String message, Object... args) {
        if (marker != null) {
            logger.info(marker, message, args);
        } else {
            logger.info(message, args);
        }
        marker = null;
        MDC.clear();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        if (marker != null) {
            logger.info(marker, MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        } else {
            logger.info(MessageFormatter.arrayFormat(message, args).getMessage(), throwable);
        }
        marker = null;
        MDC.clear();
    }

    @Override
    public Logger withMetadata(String key, Object value) {
        MDC.put(key, value.toString());
        return this;
    }

    @Override
    public Logger withMarker(String marker) {
        if (this.marker == null) {
            this.marker = org.slf4j.MarkerFactory.getMarker(marker);
        } else {
            this.marker.add(org.slf4j.MarkerFactory.getMarker(marker));
        }
        return this;
    }

}
