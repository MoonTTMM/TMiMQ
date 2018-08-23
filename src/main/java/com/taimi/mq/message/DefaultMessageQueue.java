package com.taimi.mq.message;

import com.taimi.mq.persistence.PersistStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by superttmm on 25/07/2018.
 */
public class DefaultMessageQueue implements MessageQueue {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String queueName;

    private PersistStrategy persistStrategy;

    public DefaultMessageQueue(String queueName){
        this.queueName = queueName;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public long getSize() {
        return persistStrategy.getQueueSize(queueName);
    }

    @Override
    public void empty() {
        persistStrategy.emptyQueue(queueName);
    }

    @Override
    public String enqueue(Message message) {
        return persistStrategy.enqueueMessage(queueName, message);
    }

    @Override
    public void dequeue(String consumer, MessageCallback callback) {
        Message message = persistStrategy.dequeueMessage(queueName, consumer);
        if(message == null){
            return;
        }
        callback.handle(message);
    }

    @Override
    public void markMessageConsumed(String consumer, String messageId){
        persistStrategy.consumeMessage(queueName, consumer, messageId);
    }

    @Override
    public void setPersistStrategy(PersistStrategy persistStrategy){
        this.persistStrategy = persistStrategy;
    }

    @Override
    public void markMessageConsuming(String messageId){
        persistStrategy.markMessageConsuming(queueName, messageId);
    }
}
