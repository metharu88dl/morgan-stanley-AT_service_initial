// A_T_Service/trade-service/src/main/java/com/morganstanley/tradeservice/model/TradeXml.java
package com.morganstanley.tradeservice.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "trade")
public class TradeXml {

    @JacksonXmlProperty(localName = "tradeId")
    private String tradeId;

    @JacksonXmlProperty(localName = "time")
    private String time;

    @JacksonXmlProperty(localName = "amount")
    private double amount;

    @JacksonXmlProperty(localName = "accountNumber")
    private String accountNumber;

    // Constructors
    public TradeXml() {
    }

    public TradeXml(String tradeId, String time, double amount, String accountNumber) {
        this.tradeId = tradeId;
        this.time = time;
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
