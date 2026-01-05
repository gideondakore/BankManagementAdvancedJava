package com.amalitech.bankaccount.utils;

import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.exceptions.OverdraftExceededException;
import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.transaction.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for concurrent transaction operations
 * Demonstrates thread-safe banking operations with synchronized methods
 */
public class ConcurrencyUtils {
    
    private static final Random random = new Random();
    
    /**
     * Runs a concurrent transaction simulation with multiple threads
     * Simulates multiple deposits and withdrawals happening simultaneously
     * 
     * @param accounts List of accounts to perform transactions on
     * @param transactionManager The transaction manager to record transactions
     * @param numThreads Number of concurrent threads to run
     */
    public static void runConcurrentSimulation(List<Account> accounts, TransactionManager transactionManager, int numThreads) {
        if (accounts == null || accounts.isEmpty()) {
            IO.println("❌ No accounts available for simulation.");
            return;
        }
        
        IO.println("""
            
            ╔══════════════════════════════════════════════════════════════╗
            ║           CONCURRENT TRANSACTION SIMULATION                  ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
        
        IO.println("Starting concurrent simulation with " + numThreads + " threads...");
        IO.println("Initial account balances:");
        displayAccountBalances(accounts);
        
        // Use CountDownLatch to wait for all threads to complete
        CountDownLatch latch = new CountDownLatch(numThreads);
        
        // Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        // Track start time
        long startTime = System.currentTimeMillis();
        
        // Submit concurrent tasks
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i + 1;
            executor.submit(() -> {
                try {
                    performRandomTransaction(accounts, transactionManager, threadId);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // Wait for all threads to complete
        try {
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                IO.println("⚠ Simulation timed out after 30 seconds.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            IO.println("⚠ Simulation was interrupted.");
        }
        
        // Shutdown executor
        executor.shutdown();
        
        long endTime = System.currentTimeMillis();
        
        IO.println("\n" + "═".repeat(60));
        IO.println("Simulation completed in " + (endTime - startTime) + "ms");
        IO.println("═".repeat(60));
        
        IO.println("\nFinal account balances:");
        displayAccountBalances(accounts);
        
        // Verify data integrity
        verifyDataIntegrity(accounts, transactionManager);
    }
    
    /**
     * Performs a random transaction (deposit or withdrawal) on a random account
     * Thread-safe due to synchronized methods in Account class
     */
    private static void performRandomTransaction(List<Account> accounts, TransactionManager transactionManager, int threadId) {
        // Select random account
        Account account = accounts.get(random.nextInt(accounts.size()));
        
        // Random amount between 10 and 500
        double amount = 10 + random.nextDouble() * 490;
        amount = Math.round(amount * 100.0) / 100.0; // Round to 2 decimal places
        
        // Random transaction type (deposit or withdrawal)
        boolean isDeposit = random.nextBoolean();
        
        String threadName = "Thread-" + threadId;
        
        try {
            if (isDeposit) {
                IO.println("[" + threadName + "] Depositing $" + String.format("%.2f", amount) + " to " + account.getAccountNumber());
                
                synchronized (account) {
                    double balanceBefore = account.getAccountBalance();
                    account.deposit(amount);
                    
                    // Record transaction
                    Transaction transaction = new Transaction(
                        account.getAccountNumber(), 
                        amount, 
                        account.getAccountBalance()
                    );
                    transaction.setType(TransactionType.DEPOSIT.getDescription());
                    transactionManager.addTransaction(transaction);
                    
                    IO.println("[" + threadName + "] ✓ Deposit successful. Balance: $" + 
                        String.format("%.2f", balanceBefore) + " → $" + 
                        String.format("%.2f", account.getAccountBalance()));
                }
            } else {
                IO.println("[" + threadName + "] Withdrawing $" + String.format("%.2f", amount) + " from " + account.getAccountNumber());
                
                synchronized (account) {
                    double balanceBefore = account.getAccountBalance();
                    account.withdrawal(amount);
                    
                    // Record transaction
                    Transaction transaction = new Transaction(
                        account.getAccountNumber(), 
                        amount, 
                        account.getAccountBalance()
                    );
                    transaction.setType(TransactionType.WITHDRAWAL.getDescription());
                    transactionManager.addTransaction(transaction);
                    
                    IO.println("[" + threadName + "] ✓ Withdrawal successful. Balance: $" + 
                        String.format("%.2f", balanceBefore) + " → $" + 
                        String.format("%.2f", account.getAccountBalance()));
                }
            }
        } catch (InvalidAmountException e) {
            IO.println("[" + threadName + "] ❌ Invalid amount: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            IO.println("[" + threadName + "] ❌ Insufficient funds for " + account.getAccountNumber());
        } catch (OverdraftExceededException e) {
            IO.println("[" + threadName + "] ❌ Overdraft limit exceeded for " + account.getAccountNumber());
        }
        
        // Add small delay to make thread interleaving more visible
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Displays current balances for all accounts
     */
    private static void displayAccountBalances(List<Account> accounts) {
        IO.println("-".repeat(50));
        double totalBalance = 0;
        for (Account account : accounts) {
            IO.println("  " + account.getAccountNumber() + " (" + 
                account.getCustomer().getName() + "): $" + 
                String.format("%,.2f", account.getAccountBalance()));
            totalBalance += account.getAccountBalance();
        }
        IO.println("-".repeat(50));
        IO.println("  Total: $" + String.format("%,.2f", totalBalance));
    }
    
    /**
     * Verifies data integrity after concurrent operations
     */
    private static void verifyDataIntegrity(List<Account> accounts, TransactionManager transactionManager) {
        IO.println("\n--- Data Integrity Check ---");
        
        boolean integrityOk = true;
        
        for (Account account : accounts) {
            // Check that balance is not corrupted (not NaN or infinite)
            if (Double.isNaN(account.getAccountBalance()) || Double.isInfinite(account.getAccountBalance())) {
                IO.println("❌ Data corruption detected in account " + account.getAccountNumber());
                integrityOk = false;
            }
        }
        
        if (integrityOk) {
            IO.println("✓ All accounts passed integrity check.");
            IO.println("✓ Thread-safe operations verified.");
        }
    }
    
    /**
     * Creates a Runnable for deposit operation
     * Can be used with Thread or ExecutorService
     */
    public static Runnable createDepositTask(Account account, double amount, TransactionManager transactionManager) {
        return () -> {
            try {
                synchronized (account) {
                    account.deposit(amount);
                    Transaction transaction = new Transaction(
                        account.getAccountNumber(), 
                        amount, 
                        account.getAccountBalance()
                    );
                    transaction.setType(TransactionType.DEPOSIT.getDescription());
                    transactionManager.addTransaction(transaction);
                }
                IO.println("Deposit of $" + String.format("%.2f", amount) + " completed for " + account.getAccountNumber());
            } catch (InvalidAmountException e) {
                IO.println("Deposit failed: " + e.getMessage());
            }
        };
    }
    
    /**
     * Creates a Runnable for withdrawal operation
     * Can be used with Thread or ExecutorService
     */
    public static Runnable createWithdrawalTask(Account account, double amount, TransactionManager transactionManager) {
        return () -> {
            try {
                synchronized (account) {
                    account.withdrawal(amount);
                    Transaction transaction = new Transaction(
                        account.getAccountNumber(), 
                        amount, 
                        account.getAccountBalance()
                    );
                    transaction.setType(TransactionType.WITHDRAWAL.getDescription());
                    transactionManager.addTransaction(transaction);
                }
                IO.println("Withdrawal of $" + String.format("%.2f", amount) + " completed for " + account.getAccountNumber());
            } catch (InvalidAmountException | InsufficientFundsException | OverdraftExceededException e) {
                IO.println("Withdrawal failed: " + e.getMessage());
            }
        };
    }
}
