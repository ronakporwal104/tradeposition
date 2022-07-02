package com.my.tradeposition.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class InsertTaskService extends BaseTaskService {

    public InsertTaskService(ExecutorService executorService) {
        super(executorService);
    }
}
