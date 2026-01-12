package com.amalitech.bankaccount.services;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.account.CheckingAccount;
import com.amalitech.bankaccount.account.SavingsAccount;
import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.customer.PremiumCustomer;
import com.amalitech.bankaccount.customer.RegularCustomer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.enums.TransferToOrFromType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.transaction.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for persisting and loading account and transaction data to/from files
 * Uses Java NIO for file operations and Streams for data processing
 */
public class FilePersistenceService {
    
    private static final String DATA_DIR = "data";
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";
    
    private final Path dataDirectory;
    private final Path accountsPath;
    private final Path transactionsPath;
    
    /**
     * Constructor initializes paths and ensures data directory exists
     */
    public FilePersistenceService() {
        this.dataDirectory = Paths.get(DATA_DIR);
        this.accountsPath = dataDirectory.resolve(ACCOUNTS_FILE);
        this.transactionsPath = dataDirectory.resolve(TRANSACTIONS_FILE);
        ensureDataDirectoryExists();
    }
    
    /**
     * Ensures the data directory exists, creates it if not
     */
    private void ensureDataDirectoryExists() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectories(dataDirectory);
                IO.println("✓ Data directory created: " + dataDirectory.toAbsolutePath());
            }
        } catch (IOException e) {
            IO.println("⚠ Warning: Could not create data directory: " + e.getMessage());
        }
    }
    
    // ==================== ACCOUNT PERSISTENCE ====================
    
    /**
     * Saves all accounts to the accounts file
     * Format: accountNumber|customerType|customerName|age|contact|address|accountType|balance|status
     * 
     * @param accounts List of accounts to save
     * @return true if successful, false otherwise
     */
    public boolean saveAccounts(List<Account> accounts) {
        try {
            List<String> lines = accounts.stream()
                .map(this::accountToLine)
                .collect(Collectors.toList());
            
            Files.write(accountsPath, lines, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING);
            
            IO.println("✓ Accounts saved successfully to " + accountsPath.getFileName() + " (" + accounts.size() + " accounts)");
            return true;
        } catch (IOException e) {
            IO.println("❌ Error saving accounts: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Converts an Account to a pipe-delimited string for file storage
     */
    private String accountToLine(Account account) {
        Customer customer = account.getCustomer();
        return String.join("|",
            account.getAccountNumber(),
            customer.getType().name(),
            customer.getName(),
            String.valueOf(customer.getAge()),
            customer.getContact(),
            customer.getAddress(),
            account.getType().name(),
            String.valueOf(account.getAccountBalance()),
            account.getAccountStatus(),
            String.valueOf(customer.getEmail())
        );
    }
    
    /**
     * Loads accounts from the accounts file using Streams
     * 
     * @return List of loaded accounts, empty list if file doesn't exist or error occurs
     */
    public List<Account> loadAccounts() {
        if (!Files.exists(accountsPath)) {
            IO.println("ℹ No accounts file found. Starting with empty account list.");
            return new ArrayList<>();
        }
        
        try (Stream<String> lines = Files.lines(accountsPath)) {
            List<Account> accounts = lines
                .filter(line -> !line.isBlank())
                .map(this::lineToAccount)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            
            IO.println("✓ Loaded " + accounts.size() + " accounts from " + accountsPath.getFileName());
            return accounts;
        } catch (IOException e) {
            IO.println("❌ Error loading accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Converts a pipe-delimited string back to an Account object
     */
    private Account lineToAccount(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 9) {
                IO.println("⚠ Invalid account line (insufficient fields): " + line);
                return null;
            }
            
            String accountNumber = parts[0];
            CustomerType customerType = CustomerType.valueOf(parts[1]);
            String customerName = parts[2];
            int age = Integer.parseInt(parts[3]);
            String contact = parts[4];
            String address = parts[5];
            AccountType accountType = AccountType.valueOf(parts[6]);
            double balance = Double.parseDouble(parts[7]);
            String status = parts[8];
            String email = parts[9];
            
            // Create customer based on type
            Customer customer = (customerType == CustomerType.PREMIUM)
                ? new PremiumCustomer(customerName, age, contact, address, email)
                : new RegularCustomer(customerName, age, contact, address, email);
            
            // Create account based on type
            Account account = (accountType == AccountType.SAVINGS)
                ? new SavingsAccount(customer)
                : new CheckingAccount(customer);
            
            // Set the balance and status
            account.setAccountBalance(balance);
            account.setAccountStatus(status);
            
            return account;
        } catch (InputMismatchException | InvalidAmountException | IllegalArgumentException e) {
            IO.println("⚠ Error parsing account line: " + line + " - " + e.getMessage());
            return null;
        }
    }
    
    // ==================== TRANSACTION PERSISTENCE ====================
    
    /**
     * Saves all transactions to the transactions file
     * Format: transactionId|accountNumber|type|amount|balanceAfter|timestamp|transferToOrFrom
     * 
     * @param transactions List of transactions to save
     * @return true if successful, false otherwise
     */
    public boolean saveTransactions(List<Transaction> transactions) {
        try {
            List<String> lines = transactions.stream()
                .map(this::transactionToLine)
                .collect(Collectors.toList());
            
            Files.write(transactionsPath, lines, 
                StandardOpenOption.CREATE, 
                StandardOpenOption.TRUNCATE_EXISTING);
            
            IO.println("✓ Transactions saved successfully to " + transactionsPath.getFileName() + " (" + transactions.size() + " transactions)");
            return true;
        } catch (IOException e) {
            IO.println("❌ Error saving transactions: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Converts a Transaction to a pipe-delimited string for file storage
     */
    private String transactionToLine(Transaction transaction) {
        String transferType = transaction.getTransferToOrFrom() != null 
            ? transaction.getTransferToOrFrom().name() 
            : "NONE";
        
        return String.join("|",
            transaction.getTransactionId(),
            transaction.getAccountNumber(),
            transaction.getType(),
            String.valueOf(transaction.getAmount()),
            String.valueOf(transaction.getBalanceAfter()),
            transaction.getTimestamp(),
            transferType
        );
    }
    
    /**
     * Loads transactions from the transactions file using Streams
     * 
     * @return List of loaded transactions, empty list if file doesn't exist or error occurs
     */
    public List<Transaction> loadTransactions() {
        if (!Files.exists(transactionsPath)) {
            IO.println("ℹ No transactions file found. Starting with empty transaction list.");
            return new ArrayList<>();
        }
        
        try (Stream<String> lines = Files.lines(transactionsPath)) {
            List<Transaction> transactions = lines
                .filter(line -> !line.isBlank())
                .map(this::lineToTransaction)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            
            IO.println("✓ Loaded " + transactions.size() + " transactions from " + transactionsPath.getFileName());
            return transactions;
        } catch (IOException e) {
            IO.println("❌ Error loading transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Converts a pipe-delimited string back to a Transaction object
     */
    private Transaction lineToTransaction(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 7) {
                IO.println("⚠ Invalid transaction line (insufficient fields): " + line);
                return null;
            }
            
            String transactionId = parts[0];
            String accountNumber = parts[1];
            String type = parts[2];
            double amount = Double.parseDouble(parts[3]);
            double balanceAfter = Double.parseDouble(parts[4]);
            String timestamp = parts[5];
            String transferTypeStr = parts[6];
            
            // Create transaction with basic info
            Transaction transaction = new Transaction(accountNumber, amount, balanceAfter);
            transaction.setType(type);
            
            // Set transfer direction if applicable
            if (!transferTypeStr.equals("NONE")) {
                try {
                    TransferToOrFromType transferType = TransferToOrFromType.valueOf(transferTypeStr);
                    transaction.setTransferToOrFrom(transferType);
                } catch (IllegalArgumentException ignored) {
                    // Not a transfer transaction
                }
            }
            
            return transaction;
        } catch (IllegalArgumentException e) {
            IO.println("⚠ Error parsing transaction line: " + line + " - " + e.getMessage());
            return null;
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Saves both accounts and transactions
     * 
     * @param accounts List of accounts to save
     * @param transactions List of transactions to save
     * @return true if both saved successfully
     */
    public boolean saveAll(List<Account> accounts, List<Transaction> transactions) {
        boolean accountsSaved = saveAccounts(accounts);
        boolean transactionsSaved = saveTransactions(transactions);
        return accountsSaved && transactionsSaved;
    }
    
    /**
     * Checks if the data files exist
     * 
     * @return true if both files exist
     */
    public boolean dataFilesExist() {
        return Files.exists(accountsPath) && Files.exists(transactionsPath);
    }
    
    /**
     * Gets the path to the accounts file
     */
    public Path getAccountsPath() {
        return accountsPath;
    }
    
    /**
     * Gets the path to the transactions file
     */
    public Path getTransactionsPath() {
        return transactionsPath;
    }
}
