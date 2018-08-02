package com.taimi.mq.persistence.mysql;

import com.taimi.mq.message.Message;
import com.taimi.mq.persistence.PersistStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by superttmm on 25/07/2018.
 */
@Service
public class MySqlPersistStrategy implements PersistStrategy {

    @Autowired
    MessageRepository messageRepository;

    @Override
    public long getQueueSize(String queueName) {
        return messageRepository.countByQueueName(queueName);
    }

    @Override
    public void emptyQueue(String queueName) {
        messageRepository.deleteAll();
    }

    @Override
    public String enqueueMessage(String queueName, Message message) {
        MessageEntity messageEntity = new MessageEntity(message);
        messageEntity.setQueueName(queueName);
        messageEntity = messageRepository.save(messageEntity);
        return messageEntity.getId();
    }

    @Override
    public Message dequeueMessage(String queueName, String consumer) {
        MessageEntity messageEntity = messageRepository.findFirstByQueueNameAndConsumedOrderByCreateTimeAsc(queueName, false);
        if(messageEntity == null){
            return null;
        }
        messageEntity.setConsumerName(consumer);
        messageEntity = messageRepository.save(messageEntity);
        return MessageConverter.convertToMessage(messageEntity);
    }

    @Override
    public void consumeMessage(String queueName, String consumer, String messageId) {
        MessageEntity messageEntity = messageRepository.findOne(messageId);
        messageEntity.setConsumed(true);
        messageEntity.setConsuming(false);
        messageRepository.save(messageEntity);
    }

    @Override
    public void markMessageConsuming(String queueName, String messageId) {
    }
}
