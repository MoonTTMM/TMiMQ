package com.taimi.mq.consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by superttmm on 26/07/2018.
 */
public class MultiThreadingStrategy implements ThreadingStrategy {

    private static final long MAX_WAIT_MILLIS_WHEN_STOPPING_THREADS = 30000;

    private int numThreads;
    private List<DequeueThread> dequeueThreads;

    /**
     * @param numThreads number of item processing threads to spawn.
     */
    public MultiThreadingStrategy(int numThreads) {
        this.numThreads = numThreads;
        this.dequeueThreads = new ArrayList<>(numThreads);
    }

    @Override
    public void start(String queueName, Runnable callback) {
        for (int i = 0; i < numThreads; i++) {
            DequeueThread dequeueThread = new DequeueThread(callback);

            dequeueThread.setName(String.format("TMiMQ-consumer[%s]%s", queueName, i));
            dequeueThread.start();

            dequeueThreads.add(dequeueThread);
        }
    }

    @Override
    public void stop() {
        try {
            for (DequeueThread dequeueThread : dequeueThreads) {
                dequeueThread.stopRequested = true;
            }
            waitForAllThreadsToTerminate();
        } finally {
            dequeueThreads.clear();
        }
    }

    private void waitForAllThreadsToTerminate() {
        for (DequeueThread dequeueThread : dequeueThreads) {
            try {
                dequeueThread.join(MAX_WAIT_MILLIS_WHEN_STOPPING_THREADS);
            } catch (InterruptedException e) {
            }
        }
    }

    private class DequeueThread extends Thread {
        private boolean stopRequested = false;
        private Runnable callback;

        DequeueThread(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            while (!stopRequested && !isInterrupted()) {
                try {
                    callback.run();
                } catch (Throwable t) {
                }
            }
        }
    }
}
