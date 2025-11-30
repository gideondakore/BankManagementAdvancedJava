package com.amalitech.bankaccount.transaction;

import java.time.ZonedDateTime;

public class Transaction {
    static int transactionCounter;
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Transaction(String accNumber, double amt, double balAfter){
        this.accountNumber = accNumber;
        this.amount = amt;
        this.balanceAfter = balAfter;
        ++transactionCounter;
        generateTransactionId(transactionCounter);
        generateTimeStamp();
    }

    private void generateTransactionId(int counter){
        this.transactionId = "TXN00" + counter;
    }

    private void generateTimeStamp(){
        timestamp = ZonedDateTime.now().toString();
    }

    // Getters
    public String getTransactionId(){
        return transactionId;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setType(String type){
        this.type = type;
    }


}
