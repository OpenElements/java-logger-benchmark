package com.openelements.logger.benchmarks;

import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.openelements.logger.benchmarks.BenchmarkConstants.PARALLEL_THREAD_COUNT;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

import com.openelements.logger.api.Logger;
import com.openelements.logger.chronicle.ChronicleLogger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class ChronicleLoggerBenchmark {

    @Param({"FILE_ASYNC"})
    public String loggingType;

    @Setup(Level.Iteration)
    public void init() throws Exception {
        if (Files.exists(Path.of("target/chronicle-logging"))) {
            Files.walk(Path.of("target/chronicle-logging"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
    }

    @Benchmark
    @Fork(1)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void run() {
        Logger logger = new ChronicleLogger(ChronicleLoggerBenchmark.class);
        new LogLikeHell(logger).run();
    }

}
