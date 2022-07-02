package com.my.tradeposition.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TradeTransactionModel implements Comparable<TradeTransactionModel>{
    private Integer transactionId;
    private Integer tradeId;
    private Integer version;
    private String securityCode;
    private Integer quantity;
    private Action action;
    private TradeType type;

    @Override
    public int compareTo(TradeTransactionModel o) {
        return this.getTransactionId()>o.getTransactionId() ? 1 : -1;
    }
}
