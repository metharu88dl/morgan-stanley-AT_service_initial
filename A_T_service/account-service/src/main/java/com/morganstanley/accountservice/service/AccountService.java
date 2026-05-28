package com.morganstanley.accountservice.service;

import com.morganstanley.accountservice.entity.Account;
import com.morganstanley.accountservice.exception.ResourceNotFoundException;
import com.morganstanley.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;

    // Constructor Injection (Lombok free)
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create or update an account.
     */
    public Account createAccount(Account account) {
        log.info("Creating or updating account: {}", account.getAccountNumber());
        return accountRepository.save(account);
    }

    /**
     * Get account details by account number.
     */
    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        log.info("Fetching details for account number: {}", accountNumber);
        return accountRepository.findById(accountNumber);
    }

    /**
     * Get all accounts.
     */
    public List<Account> getAllAccounts() {
        log.info("Fetching all accounts");
        return accountRepository.findAll();
    }

    /**
     * Update existing account details.
     */
    public Account updateAccount(String accountNumber, Account accountDetails) {
        log.info("Updating account number: {}", accountNumber);
        Account existingAccount = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account '" + accountNumber + "' not found"));
        
        // Update fields
        existingAccount.setAccountHolderName(accountDetails.getAccountHolderName());
        existingAccount.setCurrency(accountDetails.getCurrency());
        existingAccount.setBranch(accountDetails.getBranch());
        
        return accountRepository.save(existingAccount);
    }

    /**
     * Delete an account by account number.
     */
    public boolean deleteAccount(String accountNumber) {
        log.info("Attempting to delete account number: {}", accountNumber);
        if (accountRepository.existsById(accountNumber)) {
            accountRepository.deleteById(accountNumber);
            log.info("Account {} successfully deleted", accountNumber);
            return true;
        }
        log.warn("Account {} not found for deletion", accountNumber);
        return false;
    }
}
