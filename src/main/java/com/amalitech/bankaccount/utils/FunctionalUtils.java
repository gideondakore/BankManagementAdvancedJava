package com.amalitech.bankaccount.utils;

import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.transaction.Transaction;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for functional programming operations
 * Provides Stream-based operations for filtering, mapping, sorting, and reduction
 * Uses Lambdas, Method References, and Functional Interfaces
 */
public class FunctionalUtils {
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    /**
     * Filters accounts by account type using Streams
     * 
     * @param accounts List of accounts
     * @param type Account type to filter by
     * @return List of accounts matching the type
     */
    public static List<Account> filterByAccountType(List<Account> accounts, AccountType type) {
        return accounts.stream()
            .filter(account -> account.getType() == type)
            .collect(Collectors.toList());
    }
    
    /**
     * Filters accounts by minimum balance using Streams and Predicate
     * 
     * @param accounts List of accounts
     * @param minBalance Minimum balance threshold
     * @return List of accounts with balance >= minBalance
     */
    public static List<Account> filterByMinBalance(List<Account> accounts, double minBalance) {
        Predicate<Account> hasMinBalance = account -> account.getAccountBalance() >= minBalance;
        return accounts.stream()
            .filter(hasMinBalance)
            .collect(Collectors.toList());
    }
    
    /**
     * Filters accounts using a custom predicate
     * 
     * @param accounts List of accounts
     * @param predicate Custom filter condition
     * @return Filtered list of accounts
     */
    public static List<Account> filterAccounts(List<Account> accounts, Predicate<Account> predicate) {
        return accounts.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts accounts by balance in descending order
     * 
     * @param accounts List of accounts
     * @return Sorted list of accounts
     */
    public static List<Account> sortByBalanceDescending(List<Account> accounts) {
        return accounts.stream()
            .sorted(Comparator.comparingDouble(Account::getAccountBalance).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts accounts by balance in ascending order
     * 
     * @param accounts List of accounts
     * @return Sorted list of accounts
     */
    public static List<Account> sortByBalanceAscending(List<Account> accounts) {
        return accounts.stream()
            .sorted(Comparator.comparingDouble(Account::getAccountBalance))
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts accounts by customer name alphabetically
     * 
     * @param accounts List of accounts
     * @return Sorted list of accounts
     */
    public static List<Account> sortByCustomerName(List<Account> accounts) {
        return accounts.stream()
            .sorted(Comparator.comparing(account -> account.getCustomer().getName()))
            .collect(Collectors.toList());
    }
    
    /**
     * Calculates total balance across all accounts using Stream reduction
     * 
     * @param accounts List of accounts
     * @return Total balance
     */
    public static double calculateTotalBalance(List<Account> accounts) {
        return accounts.stream()
            .mapToDouble(Account::getAccountBalance)
            .sum();
    }
    
    /**
     * Calculates average balance using Streams
     * 
     * @param accounts List of accounts
     * @return Average balance
     */
    public static double calculateAverageBalance(List<Account> accounts) {
        return accounts.stream()
            .mapToDouble(Account::getAccountBalance)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Finds account with highest balance
     * 
     * @param accounts List of accounts
     * @return Optional containing the account with highest balance
     */
    public static Optional<Account> findHighestBalanceAccount(List<Account> accounts) {
        return accounts.stream()
            .max(Comparator.comparingDouble(Account::getAccountBalance));
    }
    
    /**
     * Finds account with lowest balance
     * 
     * @param accounts List of accounts
     * @return Optional containing the account with lowest balance
     */
    public static Optional<Account> findLowestBalanceAccount(List<Account> accounts) {
        return accounts.stream()
            .min(Comparator.comparingDouble(Account::getAccountBalance));
    }
    
    /**
     * Maps accounts to their account numbers
     * 
     * @param accounts List of accounts
     * @return List of account numbers
     */
    public static List<String> getAccountNumbers(List<Account> accounts) {
        return accounts.stream()
            .map(Account::getAccountNumber)
            .collect(Collectors.toList());
    }
    
    /**
     * Groups accounts by account type
     * 
     * @param accounts List of accounts
     * @return Map of account type to list of accounts
     */
    public static Map<AccountType, List<Account>> groupByAccountType(List<Account> accounts) {
        return accounts.stream()
            .collect(Collectors.groupingBy(Account::getType));
    }
    
    /**
     * Gets balance statistics for accounts
     * 
     * @param accounts List of accounts
     * @return DoubleSummaryStatistics with count, sum, min, max, average
     */
    public static DoubleSummaryStatistics getBalanceStatistics(List<Account> accounts) {
        return accounts.stream()
            .mapToDouble(Account::getAccountBalance)
            .summaryStatistics();
    }
    
    // ==================== TRANSACTION OPERATIONS ====================
    
    /**
     * Filters transactions by type
     * 
     * @param transactions List of transactions
     * @param type Transaction type to filter by
     * @return List of matching transactions
     */
    public static List<Transaction> filterByTransactionType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
            .filter(t -> t.getType().equals(type.getDescription()))
            .collect(Collectors.toList());
    }
    
    /**
     * Filters transactions by account number
     * 
     * @param transactions List of transactions
     * @param accountNumber Account number to filter by
     * @return List of transactions for that account
     */
    public static List<Transaction> filterByAccountNumber(List<Transaction> transactions, String accountNumber) {
        return transactions.stream()
            .filter(t -> t.getAccountNumber().equals(accountNumber))
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts transactions by amount in descending order
     * 
     * @param transactions List of transactions
     * @return Sorted list of transactions
     */
    public static List<Transaction> sortByAmountDescending(List<Transaction> transactions) {
        return transactions.stream()
            .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts transactions by amount in ascending order
     * 
     * @param transactions List of transactions
     * @return Sorted list of transactions
     */
    public static List<Transaction> sortByAmountAscending(List<Transaction> transactions) {
        return transactions.stream()
            .sorted(Comparator.comparingDouble(Transaction::getAmount))
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts transactions by date/time (newest first)
     * 
     * @param transactions List of transactions
     * @return Sorted list of transactions
     */
    public static List<Transaction> sortByDateDescending(List<Transaction> transactions) {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::parseTimeStamp).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts transactions by date/time (oldest first)
     * 
     * @param transactions List of transactions
     * @return Sorted list of transactions
     */
    public static List<Transaction> sortByDateAscending(List<Transaction> transactions) {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::parseTimeStamp))
            .collect(Collectors.toList());
    }
    
    /**
     * Calculates total deposits using Stream reduction
     * 
     * @param transactions List of transactions
     * @return Total deposit amount
     */
    public static double calculateTotalDeposits(List<Transaction> transactions) {
        return transactions.stream()
            .filter(t -> t.getType().equals(TransactionType.DEPOSIT.getDescription()))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Calculates total withdrawals using Stream reduction
     * 
     * @param transactions List of transactions
     * @return Total withdrawal amount
     */
    public static double calculateTotalWithdrawals(List<Transaction> transactions) {
        return transactions.stream()
            .filter(t -> t.getType().equals(TransactionType.WITHDRAWAL.getDescription()))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Calculates total transfers using Stream reduction
     * 
     * @param transactions List of transactions
     * @return Total transfer amount
     */
    public static double calculateTotalTransfers(List<Transaction> transactions) {
        return transactions.stream()
            .filter(t -> t.getType().equals(TransactionType.TRANSFER.getDescription()))
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    /**
     * Counts transactions by type
     * 
     * @param transactions List of transactions
     * @return Map of transaction type to count
     */
    public static Map<String, Long> countByTransactionType(List<Transaction> transactions) {
        return transactions.stream()
            .collect(Collectors.groupingBy(Transaction::getType, Collectors.counting()));
    }
    
    /**
     * Gets transaction statistics
     * 
     * @param transactions List of transactions
     * @return DoubleSummaryStatistics for transaction amounts
     */
    public static DoubleSummaryStatistics getTransactionStatistics(List<Transaction> transactions) {
        return transactions.stream()
            .mapToDouble(Transaction::getAmount)
            .summaryStatistics();
    }
    
    /**
     * Creates a custom mapper function
     * 
     * @param <T> Input type
     * @param <R> Output type
     * @param mapper The mapping function
     * @return The mapper function
     */
    public static <T, R> Function<T, R> createMapper(Function<T, R> mapper) {
        return mapper;
    }
    
    /**
     * Prints a summary of account statistics
     * 
     * @param accounts List of accounts
     */
    public static void printAccountSummary(List<Account> accounts) {
        DoubleSummaryStatistics stats = getBalanceStatistics(accounts);
        Map<AccountType, List<Account>> grouped = groupByAccountType(accounts);
        
        IO.println("""
            
            ╔══════════════════════════════════════════════════════════════╗
            ║                    ACCOUNT SUMMARY                           ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
        
        IO.println("Total Accounts: " + stats.getCount());
        IO.println("Total Balance: $" + String.format("%,.2f", stats.getSum()));
        IO.println("Average Balance: $" + String.format("%,.2f", stats.getAverage()));
        IO.println("Highest Balance: $" + String.format("%,.2f", stats.getMax()));
        IO.println("Lowest Balance: $" + String.format("%,.2f", stats.getMin()));
        
        IO.println("\nAccounts by Type:");
        grouped.forEach((type, accts) -> 
            IO.println("  " + type.getDescription() + ": " + accts.size() + " accounts")
        );
    }
    
    /**
     * Prints a summary of transaction statistics
     * 
     * @param transactions List of transactions
     */
    public static void printTransactionSummary(List<Transaction> transactions) {
        DoubleSummaryStatistics stats = getTransactionStatistics(transactions);
        Map<String, Long> countByType = countByTransactionType(transactions);
        
        IO.println("""
            
            ╔══════════════════════════════════════════════════════════════╗
            ║                  TRANSACTION SUMMARY                         ║
            ╚══════════════════════════════════════════════════════════════╝
            """);
        
        IO.println("Total Transactions: " + stats.getCount());
        IO.println("Total Amount: $" + String.format("%,.2f", stats.getSum()));
        IO.println("Average Amount: $" + String.format("%,.2f", stats.getAverage()));
        IO.println("Largest Transaction: $" + String.format("%,.2f", stats.getMax()));
        IO.println("Smallest Transaction: $" + String.format("%,.2f", stats.getMin()));
        
        IO.println("\nTransactions by Type:");
        countByType.forEach((type, count) -> 
            IO.println("  " + type + ": " + count + " transactions")
        );
        
        IO.println("\nTotals by Type:");
        IO.println("  Total Deposits: $" + String.format("%,.2f", calculateTotalDeposits(transactions)));
        IO.println("  Total Withdrawals: $" + String.format("%,.2f", calculateTotalWithdrawals(transactions)));
        IO.println("  Total Transfers: $" + String.format("%,.2f", calculateTotalTransfers(transactions)));
    }
}
