package com.my.tradeposition.task;

import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.persistence.TradeCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class InsertTradeTask implements Runnable{

    private TradeCRUDService tradeCRUDService;
    private TradeTransactionModel trade;
    public InsertTradeTask(TradeTransactionModel trade, TradeCRUDService tradeCRUDService) {
        this.trade = trade;
        this.tradeCRUDService = tradeCRUDService;
    }

    @Override
    public void run() {
        // if trade is already not present
            // then new try of id and version as 1
        // if trade is already present
            // then use id and increment version
        System.out.println("INSERT TASK - " + Thread.currentThread().getName());
        TradeTransactionModel dbTrade = tradeCRUDService.getLatestTradeById(trade.getTradeId());
        if(dbTrade==null) {
            trade.setVersion(1);
            tradeCRUDService.insertTrade(trade);
        }
    }
}
