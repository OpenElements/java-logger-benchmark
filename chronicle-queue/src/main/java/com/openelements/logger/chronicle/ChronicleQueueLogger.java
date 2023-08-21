package com.openelements.logger.chronicle;

import com.openelements.logger.api.Logger;
import com.openelements.logger.api.ThreadBasedMetadata;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.DistributedUniqueTimeProvider;
import net.openhft.chronicle.core.time.TimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.wire.BinaryWire;
import net.openhft.chronicle.wire.Wire;
import org.slf4j.helpers.MessageFormatter;

public class ChronicleQueueLogger implements Logger {

    static final ThreadLocal<Wire> WIRE_TL = ThreadLocal.withInitial(
            () -> new BinaryWire(Bytes.allocateElasticOnHeap()));
    // provides a unique id across a cluster
    public static final TimeProvider CLOCK = DistributedUniqueTimeProvider.forHostId(1);

    private final ThreadBasedMetadata metadata = new ThreadBasedMetadata();

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
        reset();
    }

    @Override
    public void log(String message, Throwable throwable) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(message, wire);
        wire.write("thrown").object(throwable);
        writeToQueue(wire);
        reset();
    }

    @Override
    public void log(String message, Object... args) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(MessageFormatter.arrayFormat(message, args).getMessage(), wire);
        writeToQueue(wire);
        reset();
    }

    @Override
    public void log(String message, Throwable throwable, Object... args) {
        Wire wire = WIRE_TL.get();
        wire.clear();
        writeMessage(MessageFormatter.arrayFormat(message, args).getMessage(), wire);
        wire.write("thrown").object(throwable);
        writeToQueue(wire);
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

    private static String getSimpleThreadName() {
        String threadName = Thread.currentThread().getName();
        int end = threadName.lastIndexOf('.');
        if (end > 0) {
            threadName = threadName.substring(end);
        }
        return threadName;
    }

    private void writeToQueue(Wire wire) {
        Bytes<?> bytes = wire.bytes();
        queue.acquireAppender().writeBytes(bytes);
    }

    private void writeMessage(String message, Wire wire) {
        String threadName = getSimpleThreadName();
        String metadataStr = metadata.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        wire.write("lvl").text("INFO")
                .write("ts").int64(CLOCK.currentTimeNanos())
                .write("th").text(threadName)
                .write("name").text(name)
                .write("msg").text(message)
                .write("md").text(metadataStr);
    }
}
