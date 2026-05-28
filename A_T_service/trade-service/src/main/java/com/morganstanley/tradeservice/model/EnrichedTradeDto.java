// A_T_Service/trade-service/src/main/java/com/morganstanley/tradeservice/model/EnrichedTradeDto.java
package com.morganstanley.tradeservice.model;

public class EnrichedTradeDto {
    private String tradeId;
    private String time;
    private double amount;
    private String accountNumber;
    
    // Enriched fields from Account Service
    private String accountHolderName;
    private String currency;
    private String branch;

    // Constructors
    public EnrichedTradeDto() {
    }

    public EnrichedTradeDto(String tradeId, String time, double amount, String accountNumber, 
                             String accountHolderName, String currency, String branch) {
        this.tradeId = tradeId;
        this.time = time;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.currency = currency;
        this.branch = branch;
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

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    // Manual Builder Pattern
    public static EnrichedTradeDtoBuilder builder() {
        return new EnrichedTradeDtoBuilder();
    }

    public static class EnrichedTradeDtoBuilder {
        private String tradeId;
        private String time;
        private double amount;
        private String accountNumber;
        private String accountHolderName;
        private String currency;
        private String branch;

        public EnrichedTradeDtoBuilder tradeId(String tradeId) {
            this.tradeId = tradeId;
            return this;
        }

        public EnrichedTradeDtoBuilder time(String time) {
            this.time = time;
            return this;
        }

        public EnrichedTradeDtoBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public EnrichedTradeDtoBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public EnrichedTradeDtoBuilder accountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public EnrichedTradeDtoBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public EnrichedTradeDtoBuilder branch(String branch) {
            this.branch = branch;
            return this;
        }

        public EnrichedTradeDto build() {
            return new EnrichedTradeDto(tradeId, time, amount, accountNumber, accountHolderName, currency, branch);
        }
    }
}
