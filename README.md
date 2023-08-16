# Java Logger Benchmark

This project is a benchmark for several Java logging libraries. It is using [JMH](https://github.com/openjdk/jmh) to measure the performance of the
different logging libraries. The goal of the project is to help people that work on performance critical projects to choose the best logger and setup.

## What libraries are tested?

Currently, the following libraries are tested:

- [Java Util Logging](https://www.vogella.com/tutorials/Logging/article.html)
- [Log4j 2](https://logging.apache.org/log4j/2.x/)
- [Log4j 2 with Async Logger](https://logging.apache.org/log4j/2.x/manual/async.html)
- [SLF4J Simple](https://www.slf4j.org/api/org/slf4j/simple/SimpleLogger.html)
- [Chronicle Logger](https://github.com/OpenHFT/Chronicle-Logger)

## How to run the benchmark?

The project is based on Maven and the [Maven Wrapper](https://maven.apache.org/wrapper/) is included. To run the benchmark, simply execute the following
command:

```
./mvnw clean test
```

The JMH benchmark is executed by several parameters that are all defined in the
`com.openelements.logger.api.BenchmarkConstants` class in the `logger-api` module.

## How to add a new logging library?

To add a new logging library, you need to create a new module that contains the implementation of the logging library.
The `logger-api` module contains a simple interface that needs to be implemented by the logging library. By doing so a
benchmark for the new logging library can be created that executes the `LogLikeHell` Runnable. All logging libraries
execute that code to generate comparable results.

## What are the results?

I still need to run the code in a long running benchmark. The metric "Operations per second" defines how often `LogLikeHell` has been executed per second. For a short running benchmark the values look like that (
sorted from fastest to slowest).

### Execution on M1 Max MacBook with 64 GB RAM and local SSD

The benchmark has been executed on 4 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE_ASYNC             |                 26443 |
| Chronicle Logger  | FILE_ASYNC             |                 23435 |
| Log4J2            | FILE                   |                 13262 |
| Java Util Logging | FILE                   |                  5793 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3853 |
| Log4J2            | FILE_AND_CONSOLE       |                  3686 |
| Log4J2            | CONSOLE                |                  3720 |
| Java Util Logging | CONSOLE                |                  3430 |
| Java Util Logging | FILE_AND_CONSOLE       |                  2712 |

### Execution on Linux box with 40 vCPUs, 250 GB RAM, and local SSD

The benchmark has been executed on 4 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE_ASYNC             |                 16379 |
| Log4J2            | FILE                   |                 12527 |
| Chronicle Logger  | FILE_ASYNC             |                  9711 |
| SLF4J Simple      | FILE                   |                  4195 |
| Log4J2            | CONSOLE                |                  3383 |
| Log4J2            | FILE_AND_CONSOLE       |                  3273 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3199 |
| Java Util Logging | FILE                   |                  2834 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1987 |
| Java Util Logging | CONSOLE                |                  1722 |

The benchmark has been executed on 40 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE                   |                  8816 |
| Log4J2            | FILE_ASYNC             |                  8141 |
| SLF4J Simple      | FILE                   |                  4735 |
| Chronicle Logger  | FILE_ASYNC             |                  4226 |
| Log4J2            | CONSOLE                |                  3656 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3627 |
| Log4J2            | FILE_AND_CONSOLE       |                  3412 |
| Java Util Logging | FILE                   |                  2771 |
| Java Util Logging | CONSOLE                |                  1468 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1137 |

The benchmark has been executed on 240 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE                   |                  8583 |
| Log4J2            | FILE_ASYNC             |                  7481 |
| SLF4J Simple      | FILE                   |                  4542 |
| Log4J2            | CONSOLE                |                  3543 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3448 |
| Log4J2            | FILE_AND_CONSOLE       |                  3401 |
| Java Util Logging | FILE                   |                  2248 |
| Java Util Logging | CONSOLE                |                  1496 |
| Java Util Logging | FILE_AND_CONSOLE       |                  1131 |
| Chronicle Logger  | FILE_ASYNC             |                   432 |

### CPU usage

The Chronicle Logger consumes 100% CPU (all 40 vCPUs are busy, synchronization is done with CAS) while Log4JLoggerBenchmark only consumes 15% CPU

### Heap allocation

The Chronicle Logger allocates around 0.65 GB of heap when the benchmark is executed. Log4J needs much more memory and allocated over 3 GB in the benchmark.

## Kudos

[Oleg Mazurov](https://github.com/OlegMazurov) helped me by executing the tests on a powerfull linux server.
