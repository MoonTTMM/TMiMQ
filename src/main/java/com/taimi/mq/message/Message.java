package com.taimi.mq.message;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by superttmm on 24/07/2018.
 */
public class Message implements Serializable {

    protected String id;
    protected Integer retryCount = 0;
    protected HashMap<String, String> payload;
    protected Long timeToLiveSeconds;
    protected Boolean consumed = false;
    protected String queueName;
    protected Boolean consuming = false;

    public Message(){}

    public Message(HashMap<String, String> payload){
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
