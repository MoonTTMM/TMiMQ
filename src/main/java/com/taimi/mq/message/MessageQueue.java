package com.taimi.mq.message;

import com.taimi.mq.persistence.PersistStrategy;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface MessageQueue {

    String getQueueName();

    long getSize();

    void empty();

    String enqueue(Message message);

    void dequeue(String consumer, MessageCallback callback);

    void markMessageConsumed(String consumer, String messageId);

    void markMessageConsuming(String messageId);

    void markMessageError(String consumer, String messageId, ErrorCode errorCode);

    void setPersistStrategy(PersistStrategy persistStrategy);
}
