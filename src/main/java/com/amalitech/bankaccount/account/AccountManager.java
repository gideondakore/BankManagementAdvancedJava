package com.amalitech.bankaccount.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Account manager for managing account creation during program running in memory
 */
public class AccountManager {
    private final ArrayList<Account> accounts = new ArrayList<>(50);
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
        this.accountCount = this.accounts.size();
    }

    /**
     * AccountManager arg constructor. It accepts an array of Account
     * @param accArr
     */
    public AccountManager(Account[] accArr){
        Collections.addAll(accounts, accArr);
        this.accountCount = this.accounts.size();
    }

    /**
     * For adding account
     * @param acc
     */
    public void addAccount(Account acc){
        this.accounts.add(acc);
        this.accountCount++;
    }

    /**
     * For finding account
     * @param accNumber
     * @return
     */
    public Account findAccount(String accNumber){
        for(Account acc: this.accounts){
            if(acc.getAccountNumber().equals(accNumber)) return acc;
        }

        return null;
    }

    /**
     * For viewing all accounts in the Account Manager
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

        int numAccounts = 0;
        double totalBalance = 0.0;

        String line = "---------------------------------------------------------------------------------------------------------------------------";

        StringBuilder stringBuilder = new StringBuilder();

        String heading = """
                %s
                %-8s            |  %-25s             |  %-8s           |  %-5s           |  %-5s
                %s
                """.formatted(line, "ACC N0", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS", line);

        stringBuilder.append(heading);




        for(Account acc: this.accounts){

            stringBuilder.append(acc.viewAllAccounts(acc.getAccountCustomer())).append("\n").append(line).append("\n");
            totalBalance += acc.getAccountBalance();

            numAccounts++;

        }

        IO.println(stringBuilder.toString());
        IO.println("Total Account: " + numAccounts);
        IO.println("Total Account Balance: $%,.2f".formatted(totalBalance));
    }

    /**
     *
     * @return Get total account balance in the Account Manager
     */
    public double getTotalBalance(){
        double tempTotal = 0.0;

        for(Account acc: accounts){
            tempTotal += acc.getAccountBalance();
        }

        return tempTotal;

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
     * Custom override implementation of the String method
     * @return Strings of account number formatted as an array
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("[ ");
        for(int i = 0; i < this.accounts.size(); i++){

            if (i < (this.accounts.size() - 1)) {
                str.append(this.accounts.get(i).getAccountNumber()).append(", ");
            } else {
                str.append(this.accounts.get(i).getAccountNumber());
            }
        }
        str.append(" ]");

        return str.toString();
    }


    /**
     * For getting specific account from the List of Accounts
     * @param account
     * @param accNum
     * @return
     */
    public static Account getAccountForTransaction(List<Account> account, String accNum){
        Account selectedAcc = null;

        for(Account acc: account){
            if(acc.getAccountNumber().equals(accNum)){
                selectedAcc = acc;
                break;
            }
        }

        if(selectedAcc == null){

            IO.println("❌ Error: Account" + "'" + accNum + "'" + " not found. Please check the account number and try again.");
            return null;
        }

        return selectedAcc;

    }
}
