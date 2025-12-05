package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.exceptions.OverdraftExceededException;
import com.amalitech.bankaccount.interfaces.DisplayAccountDetails;


public abstract class Account implements DisplayAccountDetails {
    private final String accountNumber;
    private final Customer customer;
    private double balance;
    private String status;
    static int accountCounter;
    private AccountType type;

    protected Account(Customer customer){
        this.customer = customer;
        ++accountCounter;
        this.accountNumber = "ACC00" + accountCounter;
        this.status = "Active";
    }

    // Getters
    public String getAccountNumber(){
        return this.accountNumber;
    }

    public Customer getAccountCustomer(){
        return this.customer;
    }

    public double getAccountBalance(){
        return this.balance;
    }

    public String getAccountStatus(){
        return this.status;
    }

    public AccountType getType(){
        return type;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Setters
    public void setAccountBalance(double balance){
        this.balance = balance;
    }

    public void setAccountStatus(String status){
        this.status = status;
    }

    public void setType(AccountType accType){
        this.type = accType;
    }


    // Abstract methods
    public abstract String viewAllAccounts(Customer customer);


    // Regular methods
    public Account deposit(double amount) throws IllegalArgumentException {

          this.balance += amount;

          return this;
      }

    public void withdrawal(double amount) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return this.accountNumber;
    }
}

