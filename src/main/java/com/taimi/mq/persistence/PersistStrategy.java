package com.taimi.mq.persistence;

import com.taimi.mq.message.ErrorCode;
import com.taimi.mq.message.Message;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface PersistStrategy {

    long getQueueSize(String queueName);

    void emptyQueue(String queueName);

    /**
     * enqueue message
     * @param queueName
     * @param message
     * @return message id.
     */
    String enqueueMessage(String queueName, Message message);

    Message dequeueMessage(String queueName, String consumer);

    void consumeMessage(String queueName, String consumer, String messageId);

    void markMessageConsuming(String queueName, String messageId);

    void markMessageError(String queueName, String consumer, String messageId, ErrorCode errorCode);
}
