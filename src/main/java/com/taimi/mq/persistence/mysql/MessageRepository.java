package com.taimi.mq.persistence.mysql;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by superttmm on 26/07/2018.
 */
public interface MessageRepository extends CrudRepository<MessageEntity, String> {

    List<MessageEntity> findByQueueName(String queueName);

    long countByQueueName(String queueName);

    MessageEntity findFirstByQueueNameOrderByCreateTimeAsc(String queueName);

    MessageEntity findFirstByQueueNameAndConsumedOrderByCreateTimeAsc(String queueName, Boolean consumed);
}
