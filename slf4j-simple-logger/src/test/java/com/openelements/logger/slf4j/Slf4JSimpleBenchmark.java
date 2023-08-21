package com.openelements.logger.slf4j;

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

@State(Scope.Benchmark)
public class Slf4JSimpleBenchmark {


    @Param({"FILE"})
    public String loggingType;

    @Setup(Level.Iteration)
    public void init() throws Exception {
        Files.deleteIfExists(Path.of("target/slf4j-simple.log"));
    }

    @Benchmark
    @Fork(1)
    @Threads(PARALLEL_THREAD_COUNT)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void run() {
        Logger logger = new Slf4jLogger(Slf4JSimpleBenchmark.class);
        new LogLikeHell(logger, true).run();
    }
}
