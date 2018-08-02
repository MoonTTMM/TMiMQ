package com.taimi.mq.persistence.mysql;

import com.taimi.mq.message.Message;

/**
 * Created by superttmm on 31/07/2018.
 */
public class MessageConverter {
    public static Message convertToMessage(MessageEntity messageEntity){
        Message message = new Message();
        message.setQueueName(messageEntity.getQueueName());
        message.setConsumed(messageEntity.getConsumed());
        message.setId(messageEntity.getId());
        message.setPayload(messageEntity.getPayload());
        message.setRetryCount(messageEntity.getRetryCount());
        message.setTimeToLiveSeconds(messageEntity.getTimeToLiveSeconds());
        return message;
    }
}
