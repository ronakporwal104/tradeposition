package com.my.tradeposition.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TradeTransactionRequest implements Serializable {

    private static final long serialVersionUID = -7740603022770708703L;
    private Integer tradeId;
    private String securityCode;
    private Integer quantity;
    private Action action;
    private TradeType type;
}
