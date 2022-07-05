package com.my.tradeposition.task;

import com.my.tradeposition.model.Action;
import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.persistence.TradeCRUDService;
import com.my.tradeposition.service.UpdateTaskService;

public class UpdateTradeTask implements Runnable{

    private TradeCRUDService tradeCRUDService;
    private TradeTransactionModel trade;
    private UpdateTaskService updateTaskService;

    public UpdateTradeTask(TradeTransactionModel trade, TradeCRUDService tradeCRUDService, UpdateTaskService updateTaskService) {
        this.trade = trade;
        this.tradeCRUDService = tradeCRUDService;
        this.updateTaskService = updateTaskService;
    }

    @Override
    public void run() {
        // if trade is already not present
            // then wait
        // if trade is already present
            // then check if there is any cancel action performed, if yes do nothing else use id and increment version
        TradeTransactionModel dbTrade = tradeCRUDService.getLatestTradeById(trade.getTradeId());
       if(dbTrade==null) {
            try {
                System.out.println("UPDATE TASK - " + Thread.currentThread().getName());
                Thread.currentThread().sleep(1000);
                updateTaskService.addTrade(this);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else if(dbTrade!=null) {
            if(!Action.CANCEL.equals(dbTrade.getAction())) {
                trade.setVersion(dbTrade.getVersion()+1);
                tradeCRUDService.insertTrade(trade);
            }
        }

    }
}
