# Benchmark History

This document contains a list of all benchmarks that have been run on the previous versions of the project.
All versions are defined as tags in the repository.

## 0.1.0

The following bechmarks have been executed on release `v0.1.0` by using the [Temurin JDK](https://adoptium.net) version 17.

### Execution on M1 Max MacBook with 64 GB RAM and local SSD

The benchmark has been executed on 4 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE_ASYNC             |                 26443 |
| Chronicle Logger  | FILE_ASYNC             |                 23435 |
| Log4J2            | FILE                   |                 13262 |
| Java Util Logging | FILE                   |                  5793 |
| SLF4J Simple      | FILE                   |                  5715 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3853 |
| Log4J2            | FILE_AND_CONSOLE       |                  3686 |
| Log4J2            | CONSOLE                |                  3720 |
| Java Util Logging | CONSOLE                |                  3430 |
| Java Util Logging | FILE_AND_CONSOLE       |                  2712 |

The benchmark has been executed on 60 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Chronicle Logger  | FILE_ASYNC             |                 52659 |
| Log4J2            | FILE_ASYNC             |                 14515 |
| Log4J2            | FILE                   |                 14290 |
| Java Util Logging | FILE                   |                  6158 |
| SLF4J Simple      | FILE                   |                  5357 |
| Log4J2            | CONSOLE                |                  4044 |
| Java Util Logging | CONSOLE                |                  3745 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                  3619 |
| Log4J2            | FILE_AND_CONSOLE       |                  3323 |
| Java Util Logging | FILE_AND_CONSOLE       |                  2754 |

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

### Execution on Linux vServer with 2 vCPUs and 4 GB RAM

The benchmark has been executed on 2 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Chronicle Logger  | FILE_ASYNC             |                  6091 |
| Log4J2            | FILE                   |                  5937 |
| SLF4J Simple      | FILE                   |                  2377 |
| Log4J2            | FILE_ASYNC             |                  2240 |
| Java Util Logging | FILE                   |                  1124 |
| Log4J2            | FILE_AND_CONSOLE       |                   869 |
| Log4J2            | CONSOLE                |                   633 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                   577 |
| Java Util Logging | CONSOLE                |                   404 |
| Java Util Logging | FILE_AND_CONSOLE       |                   279 |

The benchmark has been executed on 12 threads in parallel:

| Logger            | Logging Appender       | Operations per second |
|-------------------|------------------------|----------------------:|
| Log4J2            | FILE                   |                  5251 |
| Java Util Logging | FILE                   |                  1188 |
| Log4J2            | FILE_ASYNC             |                  1142 |
| Log4J2            | CONSOLE                |                   866 |
| Chronicle Logger  | FILE_ASYNC             |                   788 |
| SLF4J Simple      | FILE                   |                   788 |
| Log4J2            | FILE_AND_CONSOLE       |                   608 |
| Log4J2            | FILE_ASYNC_AND_CONSOLE |                   518 |
| Java Util Logging | CONSOLE                |                   514 |
| Java Util Logging | FILE_AND_CONSOLE       |                   317 |

### CPU usage

The Chronicle Logger consumes 100% CPU (all 40 vCPUs are busy, synchronization is done with [CAS](https://en.wikipedia.org/wiki/Compare-and-swap)) while Log4JLoggerBenchmark only consumes 15% CPU.

### Heap allocation

The Chronicle Logger allocates around 0.65 GB of [heap](https://www.baeldung.com/java-stack-heap) when the benchmark is executed. Log4J needs much more memory and allocated over 3 GB in the benchmark.

