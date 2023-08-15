package com.openelements.logger.benchmarks;

import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

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
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class JulLoggerBenchmark {

    @Param({"FILE", "CONSOLE", "FILE_AND_CONSOLE"})
    public String loggingType;

    @Setup
    public void init() throws Exception {
        Files.deleteIfExists(Path.of("target/jul-benchmark.log"));
        if (Objects.equals(loggingType, "FILE")) {
            updateLoggingConfig("jul-file-logging.properties");
        } else if (Objects.equals(loggingType, "CONSOLE")) {
            updateLoggingConfig("jul-console-logging.properties");
        } else if (Objects.equals(loggingType, "FILE_AND_CONSOLE")) {
            updateLoggingConfig("jul-file-and-console-logging.properties");
        }
    }

    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void run() {
        Logger logger = new JulLogger(JulLoggerBenchmark.class);
        new LogLikeHell(logger).run();
    }

    private static void updateLoggingConfig(final String configFileName) throws Exception {
        final URL resource = JulLoggerBenchmark.class.getClassLoader().getResource(configFileName);
        try (final InputStream is = resource.openStream()) {
            LogManager.getLogManager().updateConfiguration(is, null);
        }
    }
}
