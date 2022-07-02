package com.my.tradeposition.controller;

import com.my.tradeposition.model.*;
import com.my.tradeposition.service.ComputePositionService;
import com.my.tradeposition.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class TradeController {
    @Autowired
    private DistributionService distributionService;

    @Autowired
    private ComputePositionService computePositionService;

    // did this as some issue with setting up  open api3.0, and do not want to waste time
//    http://localhost:8080/api/insert/1/REL/10/INSERT/BUY
//    http://localhost:8080/api/insert/2/REL/10/INSERT/BUY
//    http://localhost:8080/api/insert/3/TCS/10/INSERT/BUY
//
//    http://localhost:8080/api/insert/3/TCS/10/CANCEL/BUY
//    http://localhost:8080/api/insert/2/REL/20/UPDATE/BUY
//
//    http://localhost:8080/api/insert/3/TCS/20/UPDATE/BUY
//
//    http://localhost:8080/api/insert/4/REL/20/INSERT/SELL
    @GetMapping("/insert/{tradeId}/{securityCode}/{quantity}/{action}/{type}")
    public String temporaryEndPoint(@PathVariable Integer tradeId,
                                    @PathVariable String securityCode,
                                    @PathVariable Integer quantity,
                                    @PathVariable Action action,
                                    @PathVariable TradeType type){
        return addTradeTransaction(new TradeTransactionRequest(tradeId, securityCode, quantity, action, type));
    }

    @PostMapping("/add/trade")
    public String addTradeTransaction(@RequestBody TradeTransactionRequest request) {
        try{
            distributionService.distributeTask(convertRequestToTradeModel(request));
        } catch(Exception ex) {
            throw ex;
        }
        return "Success";
    }

    @GetMapping("/trade/position")
    public Set<Position> getPositions() {
        return computePositionService.positions();
    }
    private TradeTransactionModel convertRequestToTradeModel(TradeTransactionRequest request) {
        return new TradeTransactionModel(null, request.getTradeId(), null,
                            request.getSecurityCode(), request.getQuantity(), request.getAction(), request.getType());
    }
}
