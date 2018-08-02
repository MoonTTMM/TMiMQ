package com.taimi.mq.message;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface MessageCallback {
    void handle(Message message);
}
