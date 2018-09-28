package com.taimi.mq.persistence.mysql;

import com.taimi.mq.message.ErrorCode;
import com.taimi.mq.message.Message;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by superttmm on 26/07/2018.
 */
@Entity
public class MessageEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "payload", columnDefinition = "json")
    @Convert(converter = PayloadJsonConverter.class)
    private HashMap<String, String> payload;

    /**
     * Milli-seconds precision.
     */
    @Column(name = "createdTime", columnDefinition = "DATETIME(3) NOT NULL")
    private Date createTime;

    private String consumerName;
    private Integer retryCount = 0;
    private Long timeToLiveSeconds;
    private Boolean consumed = false;
    protected String queueName;
    private Boolean consuming;
    @Enumerated(EnumType.STRING)
    private ErrorCode errorCode;

    public MessageEntity(){}

    public MessageEntity(Message message){
        this.payload = message.getPayload();
        this.retryCount = message.getRetryCount();
        this.consumed = message.getConsumed();
        this.timeToLiveSeconds = message.getTimeToLiveSeconds();
        this.queueName = message.getQueueName();
        this.consuming = message.getConsuming();
        this.createTime = new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getConsumerName() { return consumerName; }

    public void setConsumerName(String consumerName){
        this.consumerName = consumerName;
    }

    public String getId() {
        return id;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public HashMap<String, String> getPayload() {
        return payload;
    }

    public void setPayload(HashMap<String, String> payload) {
        this.payload = payload;
    }

    public Long getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(Long timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    public Boolean getConsumed() {
        return consumed;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Boolean getConsuming() {
        return consuming;
    }

    public void setConsuming(Boolean consuming) {
        this.consuming = consuming;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
