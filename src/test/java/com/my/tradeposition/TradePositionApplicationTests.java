package com.my.tradeposition;

import com.my.tradeposition.model.Action;
import com.my.tradeposition.model.Position;
import com.my.tradeposition.model.TradeTransactionRequest;
import com.my.tradeposition.model.TradeType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradePositionApplicationTests {

    @LocalServerPort
    int anyPort;

    public String getApiUrl(String apiPath) {
        return "http://localhost:" + anyPort + apiPath;
    }

    @Test
    public void testApiCall() {
        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> result = restTemplate.getForEntity(getApiUrl("/api/insert/1/REL/10/INSERT/BUY"), String.class);
        ResponseEntity<String> result = restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
    }

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

    /**
     * Test 1 - if 3 different security inserts are applied then what is the position.
     */
    @Test
    public void test1() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }
    /**
     * Test 2 - if 3 different security inserts are applied and one security is cancelled then what is the position.
     */
    @Test
    public void test2() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.CANCEL, TradeType.BUY), String.class);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }

    /**
     * Test 3 - if 3 different security inserts are applied and one security update is done on quantity then what is the position.
     */
    @Test
    public void test3() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 20, Action.UPDATE, TradeType.BUY), String.class);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }

    /**
     * Test 4 - if 3 different security inserts are applied and then again TCS security insert is applied for buy
     * and then again TCS security update is done for trade id 4 on trade type then what is the position.
     */
    @Test
    public void test4() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 100, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(4, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(4, "TCS", 10, Action.UPDATE, TradeType.SELL), String.class);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }

    /**
     * Test 5 - if 3 different security inserts are applied and then again TCS security insert is applied for buy
     *      and then again TCS security update is done for security code change to ACC then what is the position.
     */
    @Test
    public void test5() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(4, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(4, "ACC", 10, Action.UPDATE, TradeType.BUY), String.class);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }

    /**
     * Test 6 - if 3 different security inserts are applied and one security update event comes before insert than what is the position.
     */
    @Test
    public void test6() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 20, Action.UPDATE, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        Thread.sleep(5000);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }

    /**
     * Test 7 - if 3 different security inserts are applied and one security cancel event comes before insert than what is the position.
     */
    @Test
    public void test7() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(1, "REL", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(2, "ONGC", 10, Action.INSERT, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 20, Action.CANCEL, TradeType.BUY), String.class);
        restTemplate.postForEntity(getApiUrl("/api/add/trade"),
                new TradeTransactionRequest(3, "TCS", 10, Action.INSERT, TradeType.BUY), String.class);
        Thread.sleep(5000);
        Set<Position> result = restTemplate.getForObject(getApiUrl("/api/trade/position"), Set.class);
        printPosition(result);
        Assert.notEmpty(result);
    }


    /**
     * Added for better presentation
     * @param positions
     */
    private void printPosition(Set<Position> positions) {
        System.out.println("***************************POSITION****************************");
        System.out.println(positions.toString());
    }
}
