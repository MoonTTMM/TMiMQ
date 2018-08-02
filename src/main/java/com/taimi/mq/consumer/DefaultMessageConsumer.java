package com.taimi.mq.consumer;

import com.taimi.mq.comm.MessageSendStrategy;
import com.taimi.mq.comm.http.HttpMessageSendStrategy;
import com.taimi.mq.message.DefaultMessageQueue;
import com.taimi.mq.message.MessageQueue;
import com.taimi.mq.persistence.PersistStrategy;

/**
 * Created by superttmm on 25/07/2018.
 */
public class DefaultMessageConsumer implements MessageConsumer {

    private MessageQueue messageQueue;
    private String consumerName;
    private MessageSendStrategy messageSendStrategy;
    private ThreadingStrategy threadingStrategy;

    public DefaultMessageConsumer(String queueName, String consumerName, PersistStrategy persistStrategy){
        if(messageQueue == null){
            messageQueue = new DefaultMessageQueue(queueName);
            if(persistStrategy != null){
                messageQueue.setPersistStrategy(persistStrategy);
            }
        }
        if(messageSendStrategy == null){
            messageSendStrategy = new HttpMessageSendStrategy();
        }
        if(threadingStrategy == null){
            // MultiThreadingStrategy will not ensure the sequence of consume message.
            threadingStrategy = new SingleThreadingStrategy();
        }
        this.consumerName = consumerName;
    }

    @Override
    public void startConsumer(){
        String queueName = messageQueue.getQueueName();
        threadingStrategy.start(queueName, this::consume);
    }

    @Override
    public void stopConsumer(){
        threadingStrategy.stop();
    }

    @Override
    public void consume(){
        messageQueue.dequeue(consumerName, message ->
                messageSendStrategy.sendMessage(message, messageId ->
                        messageQueue.markMessageConsumed(consumerName, messageId)));
    }

    @Override
    public void setThreadingStrategy(ThreadingStrategy threadingStrategy){
        this.threadingStrategy = threadingStrategy;
    }
}
