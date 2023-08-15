package com.openelements.logger.benchmarks;

import com.openelements.logger.api.Logger;
import java.util.UUID;

public class LogLikeHell implements Runnable {

    private final Logger logger;

    public LogLikeHell(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void run() {
        String messageWithPlaceholder = "Hello " + logger.createMessageWithPlaceholders(1) + "!";
        String messageWith9Placeholder = "Hello " + logger.createMessageWithPlaceholders(9) + "!";

        logger.log("L1, Hello world!");
        logger.log("L2, Hello world!", new RuntimeException("Oh no!"));
        logger.log("L3, " + messageWithPlaceholder, "placeholder");
        logger.log("L4, " + messageWithPlaceholder, new RuntimeException("Oh no!"), "placeholder");

        logger.withMetadata("key", "value").log("L5, Hello world!");
        logger.withMarker("marker").log("L6, Hello world!");

        final String id = UUID.randomUUID().toString();
        logger.withMetadata("id", id).log("L7, Hello world!");
        logger.withMetadata("id", id).log("L8, " + messageWith9Placeholder, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        logger.withMetadata("id", id)
                .log("L9, " + messageWith9Placeholder, new RuntimeException("Oh no!"), 1, 2, 3, 4, 5, 6, 7,
                        8, 9);

        logger.withMetadata("id", id)
                .withMetadata("key", "value")
                .log("L10, Hello world!");

        logger.withMetadata("id", id)
                .withMarker("marker")
                .log("L11, Hello world!");

        logger.withMarker("marker1")
                .withMarker("marker2")
                .log("L12, Hello world!");

        logger.withMetadata("id", id).withMetadata("key", "value")
                .withMarker("marker1").withMarker("marker2")
                .log("L13, " + messageWith9Placeholder, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}