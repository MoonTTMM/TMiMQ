package com.taimi.mq.comm;

import com.taimi.mq.message.ErrorCode;

/**
 * Created by superttmm on 25/09/2018.
 */
public interface MessageSentErrorCallback {
    void handle(String messageId, ErrorCode errorInfo);
}
