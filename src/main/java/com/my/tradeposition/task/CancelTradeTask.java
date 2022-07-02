package com.my.tradeposition.task;

import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.persistence.TradeCRUDService;

public class CancelTradeTask implements Runnable{

    private TradeCRUDService tradeCRUDService;
    private TradeTransactionModel trade;
    public CancelTradeTask(TradeTransactionModel trade, TradeCRUDService tradeCRUDService) {
        this.tradeCRUDService = tradeCRUDService;
        this.trade = trade;
    }

    @Override
    public void run() {
        // if trade is already not present
            // then wait
        // if trade is already present
            // then use id and increment version
        TradeTransactionModel dbTrade = tradeCRUDService.getLatestTradeById(trade.getTradeId());
        while(dbTrade==null) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(dbTrade!=null) {
            trade.setVersion(dbTrade.getVersion()+1);
            tradeCRUDService.insertTrade(trade);
        }

    }
}
