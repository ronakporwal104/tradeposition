package com.my.tradeposition.task;

import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.persistence.TradeCRUDService;
import com.my.tradeposition.service.CancelTaskService;

public class CancelTradeTask implements Runnable{

    private TradeCRUDService tradeCRUDService;
    private TradeTransactionModel trade;
    private CancelTaskService cancelTaskService;

    public CancelTradeTask(TradeTransactionModel trade, TradeCRUDService tradeCRUDService, CancelTaskService cancelTaskService) {
        this.tradeCRUDService = tradeCRUDService;
        this.trade = trade;
        this.cancelTaskService = cancelTaskService;
    }

    @Override
    public void run() {
        // if trade is already not present
            // then wait
        // if trade is already present
            // then use id and increment version
        TradeTransactionModel dbTrade = tradeCRUDService.getLatestTradeById(trade.getTradeId());
        if(dbTrade==null) {
            try {
                Thread.currentThread().sleep(1000);
                cancelTaskService.addTrade(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if(dbTrade!=null) {
            trade.setVersion(dbTrade.getVersion()+1);
            tradeCRUDService.insertTrade(trade);
        }

    }
}
