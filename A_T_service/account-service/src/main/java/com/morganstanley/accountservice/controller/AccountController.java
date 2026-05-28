package com.morganstanley.accountservice.controller;

import com.morganstanley.accountservice.entity.Account;
import com.morganstanley.accountservice.exception.InvalidRequestException;
import com.morganstanley.accountservice.exception.ResourceNotFoundException;
import com.morganstanley.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    // Constructor Injection (Lombok free)
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * POST /accounts
     * Create a new account.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
            throw new InvalidRequestException("Account number is required and cannot be empty");
        }
        if (account.getAccountHolderName() == null || account.getAccountHolderName().trim().isEmpty()) {
            throw new InvalidRequestException("Account holder name is required");
        }
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    /**
     * GET /accounts/{accountNumber}
     * Retrieve account details for a given account number.
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account '" + accountNumber + "' not found"));
        return ResponseEntity.ok(account);
    }

    /**
     * GET /accounts
     * Retrieve a list of all accounts.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * PUT /accounts/{accountNumber}
     * Update existing account details.
     */
    @PutMapping("/{accountNumber}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable String accountNumber,
            @RequestBody Account accountDetails) {
        
        if (accountDetails.getAccountHolderName() == null || accountDetails.getAccountHolderName().trim().isEmpty()) {
            throw new InvalidRequestException("Account holder name is required for updates");
        }
        
        Account updatedAccount = accountService.updateAccount(accountNumber, accountDetails);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * DELETE /accounts/{accountNumber}
     * Delete an account by its account number.
     */
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable String accountNumber) {
        boolean deleted = accountService.deleteAccount(accountNumber);
        if (!deleted) {
            throw new ResourceNotFoundException("Account '" + accountNumber + "' not found for deletion");
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account successfully deleted");
        return ResponseEntity.ok(response);
    }
}
