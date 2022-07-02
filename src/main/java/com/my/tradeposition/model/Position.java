package com.my.tradeposition.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Position implements Serializable {

    private static final long serialVersionUID = 5114575465895091111L;

    private String securityCode;
    private Integer quantity;
}
