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

Since some loggers do not provide some of the features a simple implementation is part of the `com.openelements.logger.api.Logger` implementation for that logger lib.

## How to add a new logging library?

To add a new logging library, you need to create a new module that contains the implementation of the logging library.
The `logger-api` module contains a simple interface that needs to be implemented by the logging library.
By doing so a benchmark for the new logging library can be created that executes the `LogLikeHell` Runnable.
All logging libraries execute that code to generate comparable results.

## What are the results?

Since the project evolved over time, the results are not always comparable. 
The [benchmark archive](BENCHMARK_HISTORY.MD) contains the results of benchmarks of all previous version of the repository.

### Benchmark results for current version

I always try to execute the benchmark on as many setups as possible. If you have a different setup and are willing to contribute performance results please create an issue :)

#### Execution on M1 Max MacBook with 64 GB RAM and local SSD

The benchmark has been executed with the following options:

- Forks: 4
- Threads: 4
- Warmup iterations: 4
- Warmup time: 4 seconds
- Measurement iterations: 4
- Measurement time: 4 seconds

The following table contains the results of the benchmark for logging a simple "hello world" message:

| Logger            | Logging Appender       |   Operations per second |
|-------------------|------------------------|------------------------:|
| Chronicle Logger  | FILE_ASYNC             |                 2224759 |
| Log4J2            | FILE_ASYNC             |                  902715 |
| SLF4J Simple      | FILE                   |                  300924 |
| Log4J2            | FILE                   |                  163218 |
| Java Util Logging | FILE                   |                  103076 |
| Log4J2            | FILE_AND_CONSOLE       |                   89460 |
| Java Util Logging | CONSOLE                |                   83442 |
| Log4J2            | CONSOLE                |                   72365 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                   64143 |
| Java Util Logging | FILE_AND_CONSOLE       |                   49268 |

The following table contains the results of the benchmark for executing the `LogLikeHell` Runnable that contains all possible logging operations:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Chronicle Logger  | FILE_ASYNC             |                 57270 |
| Log4J2            | FILE_ASYNC             |                 33770 |
| Log4J2            | FILE                   |                  9880 |
| Java Util Logging | FILE                   |                  6373 |
| SLF4J Simple      | FILE                   |                  6091 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1918 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  1610 |
| Java Util Logging | CONSOLE                |                  1539 |
| Log4J2            | FILE_AND_CONSOLE       |                  1436 |
| Log4J2            | CONSOLE                |                   985 |

#### Execution on Linux box, 40 vCPUs, 250 GB RAM, and local SSD

The benchmark has been executed with the following options:

- Forks: 1
- Warmup iterations: 2
- Warmup time: 2 seconds
- Measurement iterations: 2
- Measurement time: 2 seconds

The following results are based on the setup with 4 threads executing the `LogLikeHell` Runnable:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Chronicle Logger  | FILE_ASYNC             |                 33599 |
| Log4J2            | FILE_ASYNC             |                 25775 |
| Log4J2            | FILE                   |                 11540 |
| Log4J2            | CONSOLE                |                  3516 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3451 |
| Log4J2            | FILE_AND_CONSOLE       |                  3399 |
| Java Util Logging | FILE                   |                  3031 |
| Java Util Logging | FILE_AND_CONSOLE       |                  2055 |
| Java Util Logging | CONSOLE                |                  2189 |

The following results are based on the setup with 40 threads executing the `LogLikeHell` Runnable:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Chronicle Logger  | FILE_ASYNC             |                 11070 |
| Log4J2            | FILE_ASYNC             |                  8401 |
| Log4J2            | FILE                   |                  6481 |
| Log4J2            | FILE_AND_CONSOLE       |                  3516 |
| Log4J2            | CONSOLE                |                  3302 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3104 |
| Java Util Logging | FILE                   |                  2124 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1153 |
| Java Util Logging | CONSOLE                |                  1928 |

The following results are based on the setup with 240 threads executing the `LogLikeHell` Runnable:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE_ASYNC             |                  7900 |
| Log4J2            | FILE                   |                  5863 |
| Log4J2            | CONSOLE                |                  3660 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3628 |
| Log4J2            | FILE_AND_CONSOLE       |                  3502 |
| Java Util Logging | FILE                   |                  2864 |
| Chronicle Logger  | FILE_ASYNC             |                  1884 |
| Java Util Logging | CONSOLE                |                  1155 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1153 |

### Logger initialization

Since `v0.2.0` the repo contains benchmarks that check the initialization time of the logger. 
All loggers are initialized really fast (about 40,000,000 calls per second on my machine) and the performance is independent of the logger configuration.

### Console VS File Logging

The benchmarks prove that independent of the used logging library the performance of logging to the console is always much slower than logging to a file.
We assume that the behavior is based on the implementation of the `java.io.PrintStream` that is used for `System.out` / `System.err`.
The class uses synchronized blocks to write to the console.

### Performance variance 1234

The setups that use an async file logging have a much higher variance than the setups that use a sync file logging.
Especially the Chronicle Logger has a high variance. 
We assume that this behavior is based on the internally used [Chronicle Queue library](https://github.com/OpenHFT/Chronicle-Queue).

The following table contains the results of the benchmark for executing the `LogLikeHell` Runnable:

| Logger            | Logging Appender | Variance in operations per second |
|-------------------|------------------|----------------------------------:|
| Chronicle Logger  | FILE_ASYNC       |                            ±15000 |
| Log4J2            | FILE_ASYNC       |                             ±1500 |
| All other setups  | ALL              |                              ±300 |


## Kudos

The following people helped to improve the benchmark:

- [Oleg Mazurov](https://github.com/OlegMazurov) helped me by executing the tests on a powerfull linux server.
- [Peter Lawrey](https://github.com/peter-lawrey) helped me to improve the chronicle benchmarks.
