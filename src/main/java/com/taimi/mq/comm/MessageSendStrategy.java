package com.taimi.mq.comm;

import com.taimi.mq.message.Message;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface MessageSendStrategy {
    void sendMessage(Message message, MessageSentCallback messageSentCallback, MessageSentErrorCallback errorCallback);
    void sendMessage(Message message, MessageSentCallback messageSentCallback);
}
