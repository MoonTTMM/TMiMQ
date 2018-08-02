package com.taimi.mq.consumer;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface MessageConsumer {
    void consume();
    void startConsumer();
    void stopConsumer();

    void setThreadingStrategy(ThreadingStrategy threadingStrategy);
}
