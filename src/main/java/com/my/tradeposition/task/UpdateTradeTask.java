package com.my.tradeposition.task;

import com.my.tradeposition.model.Action;
import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.persistence.TradeCRUDService;

public class UpdateTradeTask implements Runnable{

    private TradeCRUDService tradeCRUDService;
    private TradeTransactionModel trade;
    public UpdateTradeTask(TradeTransactionModel trade, TradeCRUDService tradeCRUDService) {
        this.trade = trade;
        this.tradeCRUDService = tradeCRUDService;
    }

    @Override
    public void run() {
        // if trade is already not present
            // then wait
        // if trade is already present
            // then check if there is any cancel action performed, if yes do nothing else use id and increment version
        TradeTransactionModel dbTrade = tradeCRUDService.getLatestTradeById(trade.getTradeId());
        while(dbTrade==null) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(dbTrade!=null) {
            if(!Action.CANCEL.equals(dbTrade.getAction())) {
                trade.setVersion(dbTrade.getVersion()+1);
                tradeCRUDService.insertTrade(trade);
            }
        }

    }
}
