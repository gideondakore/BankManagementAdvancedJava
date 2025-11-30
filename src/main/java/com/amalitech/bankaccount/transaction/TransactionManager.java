package com.amalitech.bankaccount.transaction;


import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.enums.TransactionType;

import java.time.ZonedDateTime;
import java.util.ArrayList;

public class TransactionManager {
    private ArrayList<Transaction> transactions = new ArrayList<>(200);
    private int transactionCount;
    private static int transactionCounter;

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    public void previewTransactionConfirmation(Account account, TransactionType transactionType, double transactionAmount){

        String txnID = String.valueOf(Transaction.transactionCounter);
        double newBalance = transactionType == TransactionType.DEPOSIT ? account.getAccountBalance() + transactionAmount : account.getAccountBalance() - transactionAmount;

        IO.println("""
                
                TRANSACTION CONFIRMATION
                ----------------------------------------------------------------------------------------
                Transaction ID: TXN00%s
                Account: %s
                Type: %s
                Amount: $%,.2f
                Previous Balance: $%,.2f
                New Balance: $%,.2f
                Date/Time: %s
                """.formatted(txnID, account.getAccountNumber(), transactionType.getDescription(), transactionAmount, account.getAccountBalance(), newBalance, ZonedDateTime.now().toString()));
    }

    public void viewTransactionsByAccount(String accountNumber){
        if(this.transactions.isEmpty()){
            IO.println("""
                    -------------------------------------------
                    No transaction recorded for this account.
                    -------------------------------------------
                    """);

            return;
        }


        StringBuilder stringBuilder = new StringBuilder();

        String heading = """
                %s            |
                %s            |
                %s            |
                %s            |
                %s
                """.formatted("TXN ID", "DATE/TIME", "TYPE", "AMOUNT", "BALANCE");


        stringBuilder.append(heading);

        for(Transaction trn: this.transactions){

            if(!accountNumber.equals(trn.getAccountNumber()))
                continue;

            String tempStr;
            tempStr = """
                    %s            |
                    %s            |
                    %s            |
                    %s            |
                    %s
                   """.formatted(trn.getTransactionId(), trn.getTimestamp(), trn.getType(), trn.getAmount(), trn.getBalanceAfter());

            stringBuilder.append(tempStr).append("\n");
        }

        IO.println(stringBuilder.toString());
    }

    public double calculateTotalDeposits(String accountNumber) {

        if(this.transactions.isEmpty()){
            return 0.0;
        }

        double tempBal = 0.0;
        transactionCount = 0;

        for(Transaction trn: transactions){
            if(trn.getType().equals(TransactionType.DEPOSIT.getDescription()) && trn.getAccountNumber().equals(accountNumber)){
                tempBal += trn.getBalanceAfter();
                ++transactionCount;
            }
        }

        return tempBal;

    }

    public double calculateTotalWithdrawals(String accountNumber) {


        if(this.transactions.isEmpty()){
            return 0.0;
        }

        transactionCount = 0;

        double tempBal = 0.0;

        for(Transaction trn: transactions){
            if(trn.getType().equals(TransactionType.WITHDRAWAL.getDescription()) && trn.getAccountNumber().equals(accountNumber)){
                tempBal += trn.getBalanceAfter();
                ++transactionCount;
            }
        }

        return tempBal;

    }

    public int getTransactionCount(){
        return transactionCount;
    }

}
