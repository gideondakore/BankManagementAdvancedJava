package com.amalitech.bankaccount.transaction;

import com.amalitech.bankaccount.enums.TransferToOrFromType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction{
    private int transactionCounter;
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;
    private TransferToOrFromType transferToOrFrom;

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

    public void generateTransactionId(int counter){
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

    public TransferToOrFromType getTransferToOrFrom(){
        return transferToOrFrom;
    }

    public int getTransactionCounter(){
        return transactionCounter;
    }


    // Setters
    public void setType(String type){
        this.type = type;
    }

    public void setTransferToOrFrom(TransferToOrFromType transferToOrFrom){
        this.transferToOrFrom = transferToOrFrom;
    }

    public void increaseTransactionCounter(){
        this.transactionCounter++;
    }

    @Override
    public String toString(){
        return transactionId;
    }

}
