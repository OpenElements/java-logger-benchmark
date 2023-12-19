package com.openelements.logger.benchmarks;

import static com.openelements.logger.api.BenchmarkConstants.FORK_COUNT;
import static com.openelements.logger.api.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.openelements.logger.api.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.openelements.logger.api.BenchmarkConstants.PARALLEL_THREAD_COUNT;
import static com.openelements.logger.api.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.openelements.logger.api.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

import com.openelements.logger.api.LogLikeHell;
import com.openelements.logger.api.Logger;
import com.openelements.logger.jul.JulLogger;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.LogManager;
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
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class JulLoggerBenchmark {

    @Param({"FILE", "CONSOLE", "FILE_AND_CONSOLE"})
    public String loggingType;

    Logger logger;
    LogLikeHell logLikeHell;

    @Setup(Level.Iteration)
    public void init() throws Exception {
        Files.deleteIfExists(Path.of("target/jul-benchmark.log"));
        if (Objects.equals(loggingType, "FILE")) {
            updateLoggingConfig("jul-file-logging.properties");
        } else if (Objects.equals(loggingType, "CONSOLE")) {
            updateLoggingConfig("jul-console-logging.properties");
        } else if (Objects.equals(loggingType, "FILE_AND_CONSOLE")) {
            updateLoggingConfig("jul-file-and-console-logging.properties");
        }
        logger = new JulLogger(JulLoggerBenchmark.class);
        logLikeHell = new LogLikeHell(logger);
    }


    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runLogLikeHell() {
        logLikeHell.run();
    }

    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runSingleSimpleLog() {
        logger.log("Hello World");
    }

    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runSingleComplexLog() {
        logger.withMarker("MARKER1").
                withMarker("MARKER2").
                withMarker("MARKER3").
                withMetadata("user", "user1").
                withMetadata("transaction", "t34").
                withMetadata("session", "session56").
                log("Hello {}", new RuntimeException("OHOH"), "World");
    }

    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runLoggerInstantiation(Blackhole blackhole) {
        Logger logger1 = new JulLogger(JulLoggerBenchmark.class);
        blackhole.consume(logger1);
    }

    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runLoggerInstantiationAndSingleSimpleLog() {
        Logger logger1 = new JulLogger(JulLoggerBenchmark.class);
        logger1.log("Hello World");
    }

    private static void updateLoggingConfig(final String configFileName) throws Exception {
        final URL resource = JulLoggerBenchmark.class.getClassLoader().getResource(configFileName);
        try (final InputStream is = resource.openStream()) {
            LogManager.getLogManager().updateConfiguration(is, null);
        }
    }
}
