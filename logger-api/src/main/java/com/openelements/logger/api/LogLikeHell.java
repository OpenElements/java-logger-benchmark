package com.openelements.logger.api;

import java.util.UUID;

public class LogLikeHell implements Runnable {

    public static final RuntimeException THROWABLE = new RuntimeException("Oh no!");
    private final Logger logger;

    public LogLikeHell(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log("L0, Hello world!");
        logger.log("L1, A quick brown fox jumps over the lazy dog.");
        logger.log("L2, Hello world!", THROWABLE);
        logger.log("L3, Hello {}!", "placeholder");
        logger.log("L4, Hello {}!", THROWABLE, "placeholder");
        logger.withMetadata("key", "value").log("L5, Hello world!");
        logger.withMarker("marker").log("L6, Hello world!");
        logger.withMetadata("user-id", UUID.randomUUID().toString())
                .log("L7, Hello world!");
        logger.withMetadata("user-id", UUID.randomUUID().toString())
                .log("L8, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
        logger.withMetadata("user-id", UUID.randomUUID().toString())
                .log("L9, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!", THROWABLE,
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
        logger.withMetadata("user-id", UUID.randomUUID().toString())
                .withMetadata("key", "value")
                .log("L10, Hello world!");
        logger.withMarker("marker")
                .log("L11, Hello world!");
        logger.withMarker("marker1")
                .withMarker("marker2")
                .log("L12, Hello world!");
        logger.withMetadata("key", "value")
                .withMarker("marker1").withMarker("marker2")
                .log("L13, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}
