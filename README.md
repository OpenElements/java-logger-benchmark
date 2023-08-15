# Java Logger Benchmark

This project is a benchmark for several Java logging libraries. It is using JMH to measure the performance of the
different logging libraries.

## What libraries are tested?

Currently, the following libraries are tested:

- Java Util Logging
- Log4j 2
- Log4j 2 with Async Logger
- SLF4J Simple
- Chronicle Logger

## How to run the benchmark?

The project is based on Maven and the Maven wrapper is included. To run the benchmark, simply execute the following
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

I still need to run the code in a long running benchmark. For a short running benchmark the values look like that (
sorted from fastest to slowest):

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE_ASYNC             |             26443,286 |
| Chronicle Logger  | FILE_ASYNC             |             23435,042 |
| Log4J2            | FILE                   |             13262,229 |
| Java Util Logging | FILE                   |              5793,153 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |              3853,433 |
| Log4J2            | FILE_AND_CONSOLE       |              3686,111 |
| Log4J2            | CONSOLE                |              3720,956 |
| Java Util Logging | CONSOLE                |              3430,061 |
| Java Util Logging | FILE_AND_CONSOLE       |              2712,309 |


