package com.taimi.mq.consumer;

/**
 * Created by superttmm on 26/07/2018.
 */
public class SingleThreadingStrategy extends MultiThreadingStrategy {
    public SingleThreadingStrategy(){
        super(1);
    }
}
