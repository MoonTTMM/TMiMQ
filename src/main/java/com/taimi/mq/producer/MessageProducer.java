package com.taimi.mq.producer;

import java.util.HashMap;

/**
 * Created by superttmm on 25/07/2018.
 */
public interface MessageProducer {

    void produce(HashMap<String, String> payload);
}
