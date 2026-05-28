// A_T_Service/trade-service/src/main/java/com/morganstanley/tradeservice/model/AccountDto.java
package com.morganstanley.tradeservice.model;

public class AccountDto {
    private String accountNumber;
    private String accountHolderName;
    private String currency;
    private String branch;

    // Constructors
    public AccountDto() {
    }

    public AccountDto(String accountNumber, String accountHolderName, String currency, String branch) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.currency = currency;
        this.branch = branch;
    }

    // Getters and Setters
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
}
