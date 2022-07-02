package com.my.tradeposition.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class CancelTaskService extends BaseTaskService {

    public CancelTaskService(ExecutorService executorService) {
        super(executorService);
    }
}
