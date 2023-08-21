package com.openelements.logger.slf4j;

import static com.openelements.logger.api.BenchmarkConstants.FORK_COUNT;
import static com.openelements.logger.api.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.openelements.logger.api.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.openelements.logger.api.BenchmarkConstants.PARALLEL_THREAD_COUNT;
import static com.openelements.logger.api.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.openelements.logger.api.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

import com.openelements.logger.api.LogLikeHell;
import com.openelements.logger.api.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class Slf4JSimpleBenchmark {


    @Param({"FILE"})
    public String loggingType;

    Logger logger;

    LogLikeHell logLikeHell;


    @Setup(Level.Iteration)
    public void init() throws Exception {
        Files.deleteIfExists(Path.of("target/slf4j-simple.log"));
        logger = new Slf4jLogger(Slf4JSimpleBenchmark.class);
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
    public void runLoggerInstantiation(Blackhole blackhole) {
        Logger logger1 = new Slf4jLogger(Slf4JSimpleBenchmark.class);
        blackhole.consume(logger1);
    }

    @Benchmark
    @Fork(FORK_COUNT)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void runLoggerInstantiationAndSingleSimpleLog() {
        Logger logger1 = new Slf4jLogger(Slf4JSimpleBenchmark.class);
        logger1.log("Hello World");
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
}
