package com.my.tradeposition.service;

import com.my.tradeposition.model.Action;
import com.my.tradeposition.model.Position;
import com.my.tradeposition.model.TradeTransactionModel;
import com.my.tradeposition.model.TradeType;
import com.my.tradeposition.persistence.TradeCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComputePositionService {

    @Autowired
    private TradeCRUDService tradeCRUDService;

    public Set<Position> positions() {
        Map<String, List<TradeTransactionModel>> mapOfSecurityAndTrades = tradeCRUDService.getMapofSecurityAndTrade();

        if(CollectionUtils.isEmpty(mapOfSecurityAndTrades)) {
            return null;
        }

        Set<Position> result = new HashSet<>();
        mapOfSecurityAndTrades.keySet().stream().forEach(securityCode -> {
            result.add(getPositionForSecurity(mapOfSecurityAndTrades.get(securityCode), securityCode));
        });
        return result;
    }

    private Position getPositionForSecurity(List<TradeTransactionModel> trades, String securityCode) {
        // if trade has cancel then 0;
        Optional<TradeTransactionModel> optionalCancelledTradeTransactionModel =
                trades.stream().filter(trade -> Action.CANCEL.equals(trade.getAction())).findFirst();

        if(optionalCancelledTradeTransactionModel.isPresent()) {
            return new Position(optionalCancelledTradeTransactionModel.get().getSecurityCode(), 0);
        }

        //if there is only insert and no update
        if(onlyInsertTrades(trades)) {
            Integer insertBuyCount =
                    trades.stream().filter(trade -> Action.INSERT.equals(trade.getAction()) &&
                            TradeType.BUY.equals(trade.getType())).collect(Collectors.summingInt(trade -> trade.getQuantity()));

            Integer insertSellCount =
                    trades.stream().filter(trade -> Action.INSERT.equals(trade.getAction()) &&
                            TradeType.SELL.equals(trade.getType())).collect(Collectors.summingInt(trade -> trade.getQuantity()));

            return new Position(securityCode, insertBuyCount-insertSellCount);
        } else {
        //if there is insert with update
            // get map of trade id with latest version trade
            List<TradeTransactionModel> uniqueTrades = getListOfUniqueWithLatestTrade(trades);
            Integer insertBuyCount =
                    uniqueTrades.stream().filter(trade -> TradeType.BUY.equals(trade.getType()))
                            .collect(Collectors.summingInt(trade -> trade.getQuantity()));

            Integer insertSellCount =
                    uniqueTrades.stream().filter(trade -> TradeType.SELL.equals(trade.getType()))
                            .collect(Collectors.summingInt(trade -> trade.getQuantity()));

            return new Position(securityCode, insertBuyCount-insertSellCount);
        }
    }

    private boolean onlyInsertTrades(List<TradeTransactionModel> trades) {
        List<Action> actions = Arrays.asList(Action.UPDATE, Action.CANCEL);
        return !trades.stream().filter(trade -> actions.contains(trade.getAction())).findFirst().isPresent();
    }

    private List<TradeTransactionModel> getListOfUniqueWithLatestTrade(List<TradeTransactionModel> trades) {
        if(CollectionUtils.isEmpty(trades)) {
            return null;
        }

        Set<Integer> tradeIds = trades.stream().map(trade -> trade.getTradeId()).collect(Collectors.toSet());
        List<TradeTransactionModel> result = new ArrayList<>();
        tradeIds.stream().forEach(tradeId -> {
            result.add(getLatestTradeById(trades.stream().filter(trade-> tradeId!=null && tradeId.equals(trade.getTradeId())).collect(Collectors.toList())));
        });
        return result;
    }

    private TradeTransactionModel getLatestTradeById(List<TradeTransactionModel> tradeList) {
        Collections.sort(tradeList, (o1, o2) -> {
            return o1.getVersion()<o2.getVersion() ? 1 : -1;
        });

        return tradeList.get(0);
    }
}
