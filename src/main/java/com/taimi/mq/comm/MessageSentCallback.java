package com.taimi.mq.comm;

/**
 * Created by superttmm on 26/07/2018.
 */
public interface MessageSentCallback {
    void handle(String messageId);
}
