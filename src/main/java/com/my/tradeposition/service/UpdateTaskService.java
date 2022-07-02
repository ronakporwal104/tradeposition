package com.my.tradeposition.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class UpdateTaskService extends BaseTaskService {

    public UpdateTaskService(ExecutorService executorService) {
        super(executorService);
    }
}
