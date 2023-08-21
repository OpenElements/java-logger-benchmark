package com.openelements.logger.jul;

import com.openelements.logger.api.Logger;
import com.openelements.logger.api.ThreadBasedMetadata;
import java.util.logging.Level;
import org.slf4j.helpers.MessageFormatter;

public class JulLogger implements com.openelements.logger.api.Logger {

    private final java.util.logging.Logger logger;

    private final ThreadBasedMetadata metadata = new ThreadBasedMetadata();

    public JulLogger(final Class source) {
        this.logger = java.util.logging.Logger.getLogger(source.getName());
    }

    private String addAllAdditionalInfos(String message) {
        final String metadataString = metadata.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return "[" + Thread.currentThread().getName() + "] " + message + "[" + metadataString + "]";
    }

    @Override
    public void log(String message) {
        logger.log(Level.INFO, addAllAdditionalInfos(message));
        reset();
    }

    @Override
    public void log(String message, Throwable throwable) {
        logger.log(Level.INFO, addAllAdditionalInfos(message), throwable);
        reset();
    }

    @Override
    public void log(String message, Object... args) {
        logger.log(Level.INFO, addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()));
        reset();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        logger.log(Level.INFO, addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()),
                throwable);
        reset();
    }

    @Override
    public Logger withMetadata(String key, Object value) {
        metadata.put(key, value.toString());
        return this;
    }

    @Override
    public Logger withMarker(String marker) {
        String markerValue = metadata.get("marker");
        if (markerValue == null) {
            markerValue = marker;
        } else {
            markerValue = markerValue + "&" + marker;
        }
        withMetadata("marker", markerValue);
        return this;
    }

    public void reset() {
        metadata.clear();
    }
}
