package com.openelements.logger.api;

public interface Logger {

    void log(String message);

    void log(String message, Throwable throwable);

    void log(String message, Object... args);

    void log(String message, Throwable throwable, Object... args);

    Logger withMetadata(String key, Object value);

    Logger withMarker(String marker);

}
