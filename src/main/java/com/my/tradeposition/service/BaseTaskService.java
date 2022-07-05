package com.my.tradeposition.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BaseTaskService implements Runnable{

    private BlockingQueue<Runnable> tradeQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService;
    public BaseTaskService(ExecutorService executorService) {
        this.executorService = executorService;
        ExecutorService mainExecutorService = Executors.newSingleThreadExecutor();
        mainExecutorService.submit(this);
    }

    public void addTrade(Runnable trade) {
        tradeQueue.add(trade);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        while(true) {
            try {
                Runnable trade = tradeQueue.take();

                if(trade!=null) {
                    executorService.submit(trade);
                } else {
                    Thread.currentThread().sleep(1000);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
