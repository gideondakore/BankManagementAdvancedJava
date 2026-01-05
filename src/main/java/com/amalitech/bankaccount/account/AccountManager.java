package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.utils.IO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Account manager for managing account creation during program running in memory
 * Uses HashMap for O(1) account lookups by account number
 */
public class AccountManager {
    private final ArrayList<Account> accounts = new ArrayList<>(50);
    private final Map<String, Account> accountMap = new HashMap<>();
    private int accountCount;

    /**
     * AcountManager no-arg constructor
     */
    public AccountManager(){
        this.accountCount = 0;
    }

    /**
     * AccountManager arg constructor. It accepts an Account
     * @param account
     */
    public AccountManager(Account account){
        this.accounts.add(account);
        this.accountMap.put(account.getAccountNumber(), account);
        this.accountCount = this.accounts.size();
    }

    /**
     * AccountManager arg constructor. It accepts an array of Account
     * @param accArr
     */
    public AccountManager(Account[] accArr){
        Collections.addAll(accounts, accArr);
        // Populate HashMap for O(1) lookups
        for (Account acc : accArr) {
            accountMap.put(acc.getAccountNumber(), acc);
        }
        this.accountCount = this.accounts.size();
    }

    /**
     * For adding account
     * @param acc
     */
    public void addAccount(Account acc){
        this.accounts.add(acc);
        this.accountMap.put(acc.getAccountNumber(), acc);
        this.accountCount++;
    }

    /**
     * For finding account using O(1) HashMap lookup
     * @param accNumber
     * @return Account if found, null otherwise
     */
    public Account findAccount(String accNumber){
        return accountMap.get(accNumber);
    }

    /**
     * For viewing all accounts in the Account Manager
     * Uses Streams for processing
     */
    public void viewAllAccounts(){
        if(this.accounts.isEmpty()){
            IO.println("""
                    -------------------------------------------
                    No account account created yet.
                    -------------------------------------------
                    """);
            return;
        }

        String line = "---------------------------------------------------------------------------------------------------------------------------";

        StringBuilder stringBuilder = new StringBuilder();

        String heading = """
                %s
                %-8s            |  %-25s             |  %-8s           |  %-5s           |  %-5s
                %s
                """.formatted(line, "ACC N0", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS", line);

        stringBuilder.append(heading);

        // Use Stream to process accounts
        accounts.forEach(acc -> 
            stringBuilder.append(acc.viewAllAccounts(acc.getAccountCustomer()))
                        .append("\n")
                        .append(line)
                        .append("\n")
        );

        // Calculate totals using Streams
        int numAccounts = accounts.size();
        double totalBalance = accounts.stream()
            .mapToDouble(Account::getAccountBalance)
            .sum();

        IO.println(stringBuilder.toString());
        IO.println("Total Account: " + numAccounts);
        IO.println("Total Account Balance: $%,.2f".formatted(totalBalance));
    }

    /**
     *
     * @return Get total account balance in the Account Manager using Stream reduction
     */
    public double getTotalBalance(){
        return accounts.stream()
            .mapToDouble(Account::getAccountBalance)
            .sum();
    }

    /**
     *
     * @return Get number of accounts in the account Manager
     */
    public int getAccountCount() {
        return accountCount;
    }

    /**
     *
     * @return List of all Accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Custom override implementation of the String method using Streams
     * @return Strings of account number formatted as an array
     */
    @Override
    public String toString(){
        return accounts.stream()
            .map(Account::getAccountNumber)
            .collect(Collectors.joining(", ", "[ ", " ]"));
    }


    /**
     * For getting specific account from the List of Accounts using O(1) lookup
     * @param account
     * @param accNum
     * @return
     */
    public static Account getAccountForTransaction(List<Account> account, String accNum){
        // Use Stream to find account
        Account selectedAcc = account.stream()
            .filter(acc -> acc.getAccountNumber().equals(accNum))
            .findFirst()
            .orElse(null);

        if(selectedAcc == null){
            IO.println("❌ Error: Account '" + accNum + "' not found. Please check the account number and try again.");
            return null;
        }

        return selectedAcc;
    }
}
