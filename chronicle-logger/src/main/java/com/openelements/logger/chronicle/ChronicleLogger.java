package com.openelements.logger.chronicle;

import com.openelements.logger.api.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import net.openhft.chronicle.logger.ChronicleLogLevel;
import net.openhft.chronicle.logger.ChronicleLogManager;
import net.openhft.chronicle.logger.ChronicleLogWriter;
import org.slf4j.helpers.MessageFormatter;

public class ChronicleLogger implements Logger {

    private final ChronicleLogWriter writer;

    private Map<String, String> metadata = new HashMap<>();

    private final String name;

    public ChronicleLogger(Class source) {
        this.name = source.getName();
        this.writer = ChronicleLogManager.getInstance().getWriter(name);
    }

    private String addAllAdditionalInfos(String message) {
        final String metadataString = metadata.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return message + "[" + metadataString + "]";
    }


    @Override
    public void log(String message) {
        writer.write(ChronicleLogLevel.INFO, System.currentTimeMillis(), Thread.currentThread().getName(), name,
                addAllAdditionalInfos(message), null);
    }

    @Override
    public void log(String message, Throwable throwable) {
        writer.write(ChronicleLogLevel.INFO, System.currentTimeMillis(), Thread.currentThread().getName(), name,
                addAllAdditionalInfos(message), throwable);
    }

    @Override
    public void log(String message, Object... args) {
        writer.write(ChronicleLogLevel.INFO, System.currentTimeMillis(), Thread.currentThread().getName(), name,
                addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()), null, null);
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        writer.write(ChronicleLogLevel.INFO, System.currentTimeMillis(), Thread.currentThread().getName(), name,
                addAllAdditionalInfos(MessageFormatter.arrayFormat(message, args).getMessage()), throwable, null);
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
    public String createMessageWithPlaceholders(int placeholderCount) {
        return IntStream.range(0, placeholderCount)
                .mapToObj(i -> "{},")
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }
}
