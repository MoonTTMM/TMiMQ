package com.taimi.mq.consumer;

/**
 * Created by superttmm on 26/07/2018.
 */
public interface ThreadingStrategy {

    void start(String queueName, Runnable callback);

    void stop();
}
