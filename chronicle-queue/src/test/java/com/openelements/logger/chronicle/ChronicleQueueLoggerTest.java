package com.openelements.logger.chronicle;

import com.openelements.logger.api.LogLikeHell;
import com.openelements.logger.api.Logger;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.queue.ChronicleQueue;
import org.junit.jupiter.api.Test;

class ChronicleQueueLoggerTest {
    @Test
    public void runAll() {
        String pathName = "target/queue";
        ChronicleQueue queue = ChronicleQueue.single(pathName);
        Logger logger = new ChronicleQueueLogger(queue, ChronicleQueueLoggerTest.class);
        new LogLikeHell(logger).run();
        IOTools.deleteDirWithFiles(pathName);
    }

}