# Java Logger Benchmark

This project is a benchmark for several Java logging libraries.
It is using [JMH](https://github.com/openjdk/jmh) to measure the performance of the different logging libraries.
The goal of the project is to help people that work on performance critical projects to choose the best logger and setup.

## What libraries are tested?

Currently, the following libraries are tested:

- [Java Util Logging](https://www.vogella.com/tutorials/Logging/article.html)
- [Log4j 2](https://logging.apache.org/log4j/2.x/)
- [Log4j 2 with Async Logger](https://logging.apache.org/log4j/2.x/manual/async.html)
- [SLF4J Simple](https://www.slf4j.org/api/org/slf4j/simple/SimpleLogger.html)
- [Chronicle Logger](https://github.com/OpenHFT/Chronicle-Logger)

## How to run the benchmark?

The project is based on Maven and the [Maven Wrapper](https://maven.apache.org/wrapper/) is included.
The benchmark module uses the [jmh-maven-plugin](https://github.com/metlos/jmh-maven-plugin) that automatically execute all benchmarks in the `test` phase of Maven.
To run the benchmark, simply execute the following
command:

```
./mvnw clean test
```

The JMH benchmark is executed by several parameters that are all defined in the `com.openelements.logger.api.BenchmarkConstants` class in the `logger-api` module.

## What is tested

Since not all loggers provide the same set of functionalities the `com.openelements.logger.api.Logger` interface contains all features that should be tested.
That feature set reflects the features that a modern logging api should provide:

- log messages
- add stack trace (by Throwable)
- support `{}` placeholders in message
- support [markers](https://logging.apache.org/log4j/2.x/manual/markers.html)
- support a context (like [MDC](https://logback.qos.ch/manual/mdc.html) or [thread context](https://logging.apache.org/log4j/2.x/manual/thread-context.html))
- add a timestamp, the current thread and the name of the caller class to the message
- log to file system and/or console

Since some loggers does not provide some of the features a simple implementation is part of the `com.openelements.logger.api.Logger` implementation for that logger lib.

## How to add a new logging library?

To add a new logging library, you need to create a new module that contains the implementation of the logging library.
The `logger-api` module contains a simple interface that needs to be implemented by the logging library.
By doing so a benchmark for the new logging library can be created that executes the `LogLikeHell` Runnable.
All logging libraries execute that code to generate comparable results.

## What are the results?

Since the project evolved over time, the results are not always comparable. 
The [benchmark archive](BENCHMARK_HISTORY.MD) contains the results of benchmarks of all previous version of the repository.

### Logger initialization

Since `v0.2.0` the repo contains benchmarks that check the initialization time of the logger. 
All loggers are initialized really fast (about 40,000,000 calls per second on my machine) and the performance is independent of the logger configuration.

### Console VS File Logging

The benchmarks prove that independent of the used logging library the performance of logging to the console is always much slower than logging to a file.
We assume that the behavior is based on the implementation of the `java.io.PrintStream` that is used for `System.out` / `System.err`.
The class uses synchronized blocks to write to the console.

## Kudos

The following people helped to improve the benchmark:

- [Oleg Mazurov](https://github.com/OlegMazurov) helped me by executing the tests on a powerfull linux server.
- [Peter Lawrey](https://github.com/peter-lawrey) helped me to improve the chronicle benchmarks.
