package com.openelements.logger.benchmarks;

import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_ITERATIONS;
import static com.openelements.logger.benchmarks.BenchmarkConstants.WARMUP_TIME_IN_SECONDS_PER_ITERATION;

import com.openelements.logger.api.Logger;
import com.openelements.logger.log4j.Log4JLogger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
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
public class Log4JLoggerBenchmark {


    private final static String PATTERN = "%d %c [%t] %-5level: %msg [%marker] %X %n%throwable";

    @Param({"FILE", "CONSOLE", "FILE_AND_CONSOLE"})
    public String loggingType;

    @Setup(org.openjdk.jmh.annotations.Level.Iteration)
    public void init() throws Exception {
        Files.deleteIfExists(Path.of("target/log4j-benchmark.log"));
        if (Objects.equals(loggingType, "FILE")) {
            configureFileLogging();
        } else if (Objects.equals(loggingType, "CONSOLE")) {
            configureConsoleLogging();
        } else if (Objects.equals(loggingType, "FILE_AND_CONSOLE")) {
            configureFileAndConsoleLogging();
        }
    }

    @Benchmark
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = WARMUP_ITERATIONS, time = WARMUP_TIME_IN_SECONDS_PER_ITERATION)
    @Measurement(iterations = MEASUREMENT_ITERATIONS, time = MEASUREMENT_TIME_IN_SECONDS_PER_ITERATION)
    public void run() {
        Logger logger = new Log4JLogger(Log4JLoggerBenchmark.class);
        new LogLikeHell(logger).run();
    }

    private static AppenderComponentBuilder createConsoleAppender(final String name,
            final ConfigurationBuilder<BuiltConfiguration> builder) {
        final LayoutComponentBuilder layoutComponentBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", PATTERN);
        return builder.newAppender(name, "CONSOLE")
                .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT)
                .add(layoutComponentBuilder);
    }

    private static AppenderComponentBuilder createFileAppender(final String name,
            final ConfigurationBuilder<BuiltConfiguration> builder) {
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", PATTERN);
        return builder.newAppender(name, "File")
                .addAttribute("fileName", "target/log4j-benchmark.log")
                .add(layoutBuilder);
    }

    private static void configureConsoleLogging() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.ERROR);
        builder.setConfigurationName("loggingConfig");

        // create a console appender
        builder.add(createConsoleAppender("console", builder));

        // create the new logger
        builder.add(builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef("console")));
        Configurator.initialize(builder.build());
    }

    private static void configureFileLogging() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.ERROR);
        builder.setConfigurationName("loggingConfig");

        // create a console appender
        builder.add(createFileAppender("file", builder));

        // create the new logger
        builder.add(builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef("file")));
        Configurator.initialize(builder.build());
    }

    private static void configureFileAndConsoleLogging() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.ERROR);
        builder.setConfigurationName("loggingConfig");

        // create a console appender
        builder.add(createFileAppender("file", builder));
        builder.add(createConsoleAppender("console", builder));

        // create the new logger
        builder.add(builder.newRootLogger(Level.DEBUG)
                .add(builder.newAppenderRef("file"))
                .add(builder.newAppenderRef("console")));
        Configurator.initialize(builder.build());
    }
}
