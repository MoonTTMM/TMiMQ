package com.taimi.mq.producer;

import com.taimi.mq.message.DefaultMessageQueue;
import com.taimi.mq.message.Message;
import com.taimi.mq.message.MessageQueue;
import com.taimi.mq.persistence.PersistStrategy;

import java.util.HashMap;

/**
 * Created by superttmm on 25/07/2018.
 */
public class DefaultMessageProducer implements MessageProducer {

    private MessageQueue messageQueue;

    public DefaultMessageProducer(String queueName, PersistStrategy persistStrategy){
        if(messageQueue == null){
            messageQueue = new DefaultMessageQueue(queueName);
            if(persistStrategy != null){
                messageQueue.setPersistStrategy(persistStrategy);
            }
        }
    }

    @Override
    public void produce(HashMap<String, String> payload) {
        Message message = new Message(payload);
        messageQueue.enqueue(message);
    }

    public MessageQueue getMessageQueue(){
        return messageQueue;
    }
}
