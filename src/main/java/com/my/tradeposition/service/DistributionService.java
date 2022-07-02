package com.my.tradeposition.service;

import com.my.tradeposition.model.Action;
import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.model.TradeType;
import com.my.tradeposition.persistence.TradeCRUDService;
import com.my.tradeposition.task.CancelTradeTask;
import com.my.tradeposition.task.InsertTradeTask;
import com.my.tradeposition.task.UpdateTradeTask;
import org.springframework.stereotype.Service;

@Service
public class DistributionService {

    private final InsertTaskService insertTaskService;
    private final CancelTaskService cancelTaskService;
    private final UpdateTaskService updateTaskService;

    private final TradeCRUDService tradeCRUDService;

    public DistributionService(InsertTaskService insertTaskService, CancelTaskService cancelTaskService,
                               UpdateTaskService updateTaskService, TradeCRUDService tradeCRUDService) {
        this.cancelTaskService = cancelTaskService;
        this.updateTaskService = updateTaskService;
        this.insertTaskService = insertTaskService;
        this.tradeCRUDService = tradeCRUDService;
    }

    public void distributeTask(TradeTransactionModel tradeTransactionModel) {

        if(Action.INSERT.equals(tradeTransactionModel.getAction())) {
            insertTaskService.addTrade(new InsertTradeTask(tradeTransactionModel, tradeCRUDService));
        } else if(Action.CANCEL.equals(tradeTransactionModel.getAction())) {
            cancelTaskService.addTrade(new CancelTradeTask(tradeTransactionModel, tradeCRUDService));
        } else if(Action.UPDATE.equals(tradeTransactionModel.getAction())) {
            updateTaskService.addTrade(new UpdateTradeTask(tradeTransactionModel, tradeCRUDService));
        }
    }
}
