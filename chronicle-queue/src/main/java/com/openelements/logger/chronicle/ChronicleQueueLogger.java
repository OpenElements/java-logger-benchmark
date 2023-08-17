package com.openelements.logger.chronicle;

import com.openelements.logger.api.Logger;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.DistributedUniqueTimeProvider;
import net.openhft.chronicle.core.time.TimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.Wire;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChronicleQueueLogger implements Logger {

    static final ThreadLocal<Wire> WIRE_TL = ThreadLocal.withInitial(() -> new BinaryWire(Bytes.allocateElasticOnHeap()));
    // provides a unique id across a cluster
    public static final TimeProvider CLOCK = DistributedUniqueTimeProvider.forHostId(1);

    private final Map<String, String> metadata = new LinkedHashMap<>();
    private String metadataStr = "[]";

    private final ChronicleQueue queue;
    private final String name;

    public ChronicleQueueLogger(ChronicleQueue queue, Class source) {
        this.queue = queue;
        this.name = source.getSimpleName();
    }

    @Override
    public void log(String message) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(message, wire);
        writeToQueue(wire);
    }

    private void writeToQueue(Wire wire) {
        Bytes<?> bytes = wire.bytes();
        queue.acquireAppender().writeBytes(bytes);
    }

    private void writeMessage(String message, Wire wire) {
        String threadName = getSimpleThreadName();
        wire.write("lvl").text("INFO")
                .write("ts").int64(CLOCK.currentTimeNanos())
                .write("th").text(threadName)
                .write("name").text(name)
                .write("msg").text(message)
                .write("md").text(metadataStr);
    }

    private static String getSimpleThreadName() {
        String threadName = Thread.currentThread().getName();
        int end = threadName.lastIndexOf('.');
        if (end > 0)
            threadName = threadName.substring(end);
        return threadName;
    }

    @Override
    public void log(String message, Throwable throwable) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(message, wire);
        wire.write("thrown").object(throwable);
        writeToQueue(wire);
    }

    @Override
    public void log(String message, Object... args) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(message, wire);
        wire.write("args").object(args);
        writeToQueue(wire);
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(message, wire);
        wire.write("thrown").object(throwable);
        wire.write("args").object(args);
        writeToQueue(wire);
    }

    @Override
    public Logger withMetadata(String key, Object value) {
        metadata.put(key, value.toString());
        metadataStr = metadata.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
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

    @Override
    public void reset() {
        metadata.clear();
        metadataStr = "[]";
    }
}
