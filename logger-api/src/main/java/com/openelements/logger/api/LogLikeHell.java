package com.openelements.logger.api;

import java.util.UUID;

public class LogLikeHell implements Runnable {

    public static final RuntimeException THROWABLE = new RuntimeException("Oh no!");
    private final Logger logger;
    private final boolean needsId;
    private int count = 0;

    public LogLikeHell(Logger logger, boolean needsId) {
        this.logger = logger;
        this.needsId = needsId;
    }

    @Override
    public void run() {
        int next = count++ % 14;
        switch (next) {
            case 0:
                logger.log("L0, Hello world!");
                break;

            case 1:
                logger.log("L1, A quick brown fox jumps over the lazy dog.");
                break;

            case 2:
                logger.log("L2, Hello world!", THROWABLE);
                break;

            case 3:
                logger.log("L3, Hello {}!", "placeholder");
                break;

            case 4:
                logger.log("L4, Hello {}!", THROWABLE, "placeholder");
                break;

            case 5:
                logger.withMetadata("key", "value").log("L5, Hello world!");
                break;

            case 6:
                logger.withMarker("marker").log("L6, Hello world!");
                break;


            case 7:
                if (needsId)
                    logger.withMetadata("id", UUID.randomUUID().toString());
                logger.log("L7, Hello world!");
                break;

            case 8:
                if (needsId)
                    logger.withMetadata("id", UUID.randomUUID().toString());
                logger.log("L8, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!",
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
                break;

            case 9:
                if (needsId)
                    logger.withMetadata("id", UUID.randomUUID().toString());
                logger.log("L9, Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!", THROWABLE,
                        1, 2, 3, 4, 5, 6, 7, 8, 9);
                break;

            case 10:
                if (needsId)
                    logger.withMetadata("id", UUID.randomUUID().toString());
                logger.withMetadata("key", "value")
                        .log("L10, Hello world!");
                break;

            case 11:
                logger.withMarker("marker")
                        .log("L11, Hello world!");
                break;

            case 12:
                logger.withMarker("marker1")
                        .withMarker("marker2")
                        .log("L12, Hello world!");
                break;
            case 13:
                String messageWith9Placeholder = "Hello {}, {}, {}, {}, {}, {}, {}, {}, {}!";
                logger.withMetadata("key", "value")
                        .withMarker("marker1").withMarker("marker2")
                        .log("L13, " + messageWith9Placeholder,
                                1, 2, 3, 4, 5, 6, 7, 8, 9);
                break;

            default:
                throw new AssertionError(next);
        }
        logger.reset();
    }
}
