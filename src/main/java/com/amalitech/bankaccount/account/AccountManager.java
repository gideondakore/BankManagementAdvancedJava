package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.transaction.TransactionManager;
import com.amalitech.bankaccount.utils.InputValidationHelper;
import com.amalitech.bankaccount.utils.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountManager {
    private final ArrayList<Account> accounts = new ArrayList<>(50);
    private int accountCount;

    public AccountManager(){
        this.accountCount = 0;
    }

    public AccountManager(Account account){
        this.accounts.add(account);
        this.accountCount = this.accounts.size();
    }

    public AccountManager(Account[] accArr){
        Collections.addAll(accounts, accArr);
        this.accountCount = this.accounts.size();
    }

    public void addAccount(Account acc){
        this.accounts.add(acc);
        this.accountCount++;
    }

    public Account findAccount(String accNumber){
        for(Account acc: this.accounts){
            if(acc.getAccountNumber().equals(accNumber)) return acc;
        }

        return null;
    }

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

    public double getTotalBalance(){
        double tempTotal = 0.0;

        for(Account acc: accounts){
            tempTotal += acc.getAccountBalance();
        }

        return tempTotal;

    }

    public int getAccountCount() {
        return accountCount;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

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

    public void getAccountStatement(String accNumber, List<Transaction> transactions, List<Account> accountstList){
        Account selectedAcc;
        List<Transaction> newTransactions = TransactionManager.getAllTransactions(accNumber, transactions);

        IO.println("""
                GENERATE ACCOUNT STATEMENT
                -----------------------------------------------------
                """);

        accNumber = InputValidationHelper.validatedStringInputValue("Enter Account Number: ", """
                    Please provide a valid account number!
                    
                    Example format:
                    ACC001
                    ACC002
                    ACC0010
                    ACC00120
                    """, "^ACC00\\d+$");

        selectedAcc = Menu.getAccountForTransaction(accountstList, accNumber);

        IO.println("""
                
                Account: %s (%s)
                Current Balance: %,.2f
                """);

        IO.println("""
                Transactions:
                -------------------------------------------------------------------
                
                """);



    }
}
