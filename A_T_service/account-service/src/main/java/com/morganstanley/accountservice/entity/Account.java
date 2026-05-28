// A_T_Service/account-service/src/main/java/com/morganstanley/accountservice/entity/Account.java
package com.morganstanley.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String accountNumber;
    private String accountHolderName;
    private String currency;
    private String branch;

    // Default Constructor (Required by JPA)
    public Account() {
    }

    // All-Arguments Constructor
    public Account(String accountNumber, String accountHolderName, String currency, String branch) {
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

    // Builder Pattern (Simple implementation to mimic Lombok @Builder)
    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public static class AccountBuilder {
        private String accountNumber;
        private String accountHolderName;
        private String currency;
        private String branch;

        public AccountBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder accountHolderName(String accountHolderName) {
            this.accountHolderName = accountHolderName;
            return this;
        }

        public AccountBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public AccountBuilder branch(String branch) {
            this.branch = branch;
            return this;
        }

        public Account build() {
            return new Account(accountNumber, accountHolderName, currency, branch);
        }
    }
}
