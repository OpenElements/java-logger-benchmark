package com.openelements.logger.jul;

import com.openelements.logger.api.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.helpers.MessageFormatter;

public class JulLogger implements com.openelements.logger.api.Logger {

    private final java.util.logging.Logger logger;

    private Map<String, String> metadata = new HashMap<>();

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
        metadata.clear();
    }

    @Override
    public void log(String message, Throwable throwable) {
        logger.log(Level.INFO, addAllAdditionalInfos(message), throwable);
        metadata.clear();
    }

    @Override
    public void log(String message, Object... args) {
        logger.log(Level.INFO, addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()));
        metadata.clear();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        logger.log(Level.INFO, addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()),
                throwable);
        metadata.clear();
    }

    @Override
    public Logger withMetadata(String key, Object value) {
        metadata.put(key, value.toString());
        return this;
    }

    @Override
    public Logger withMarker(String marker) {
        if (metadata.containsKey("marker")) {
            final String markerValue = metadata.get("marker") + "&" + marker;
            metadata.put("marker", markerValue);
        } else {
            metadata.put("marker", marker);
        }
        return this;
    }

    @Override
    public void reset() {
metadata.clear();
    }
}
