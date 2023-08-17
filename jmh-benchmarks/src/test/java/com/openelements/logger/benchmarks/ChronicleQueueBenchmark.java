package com.openelements.logger.benchmarks;

import com.openelements.logger.api.LogLikeHell;
import com.openelements.logger.api.Logger;
import com.openelements.logger.chronicle.ChronicleQueueLogger;
import net.openhft.chronicle.queue.ChronicleQueue;
import org.openjdk.jmh.annotations.*;

import static com.openelements.logger.api.BenchmarkConstants.*;

@State(Scope.Thread)
public class ChronicleQueueBenchmark {

    public static final String QUEUE_NAME = "target/chronicle-queue-" + System.nanoTime();
    @Param({"FILE_ASYNC"})
    public String loggingType;

    private static final ChronicleQueue QUEUE = ChronicleQueue.single(QUEUE_NAME);
    private Logger logger;
    private LogLikeHell logLikeHell;

    @Setup
    public void setUp() {
        logger = new ChronicleQueueLogger(QUEUE, ChronicleQueueBenchmark.class);
        logLikeHell = new LogLikeHell(logger, false);
    }

    @Benchmark
    @Fork(jvmArgsAppend = {
            "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
            "--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED",
            "--add-exports=java.base/jdk.internal.misc=ALL-UNNAMED",
            "--add-exports=java.base/jdk.internal.util=ALL-UNNAMED",
            "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
            "--add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
            "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED",
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens=java.base/java.util=ALL-UNNAMED",
            "--add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED"
    })
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void run() {
        logLikeHell.run();
    }
}
