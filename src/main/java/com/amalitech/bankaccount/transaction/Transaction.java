package com.amalitech.bankaccount.transaction;

import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.interfaces.Transactable;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction{
    static int transactionCounter;
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;

    public Transaction(){
        super();
    }

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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        timestamp = now.format(formatter);
    }

    public LocalDateTime parseTimeStamp(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        return LocalDateTime.parse(timestamp, formatter);
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

    @Override
    public String toString(){
        return transactionId;
    }



}
