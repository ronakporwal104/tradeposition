package com.my.tradeposition.persistence;

import com.my.tradeposition.model.TradeTransactionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TradeCRUDService {

    private TreeSet<TradeTransactionModel> dataBase = new TreeSet<>();

    public void insertTrade(TradeTransactionModel trade) {
        trade.setTransactionId(getLatestTransactionId());
        dataBase.add(trade);
        log.info(dataBase.toString());
    }

    public Map<String, List<TradeTransactionModel>> getMapofSecurityAndTrade() {
        if(CollectionUtils.isEmpty(dataBase)) {
            return null;
        }

        Set<String> securityCodes = dataBase.stream().map(trade -> trade.getSecurityCode()).collect(Collectors.toSet());
        Map<String, List<TradeTransactionModel>> result = new HashMap<>();
        securityCodes.stream().forEach(securityCode -> {
            result.put(securityCode, getTradesBySecurity(securityCode));
        });
        return result;
    }

    private List<TradeTransactionModel> getTradesBySecurity(String securityCode) {
        if(CollectionUtils.isEmpty(dataBase)) {
            return null;
        }
        return dataBase.stream().filter(tradeTransactionModel ->
                securityCode.equalsIgnoreCase(tradeTransactionModel.getSecurityCode())).collect(Collectors.toList());
    }

    private Integer getLatestTransactionId() {
        // as treeset assuming the last item is the latest transaction index.
        return (CollectionUtils.isEmpty(dataBase)) ? 1 : (dataBase.last().getTransactionId()+1);
    }

    // this will always give latest version trade as collection is already sorted
    public TradeTransactionModel getLatestTradeById(Integer tradeId) {

        if(tradeId==null)
            return null;

        List<TradeTransactionModel> tradeList =
                dataBase.stream().filter(trade -> tradeId.equals(trade.getTradeId())).collect(Collectors.toList());

        if(CollectionUtils.isEmpty(tradeList)) {
            return null;
        }

        Collections.sort(tradeList, (o1, o2) -> {
            return o1.getVersion()<o2.getVersion() ? 1 : -1;
        });

        return tradeList.get(0);
    }

}
