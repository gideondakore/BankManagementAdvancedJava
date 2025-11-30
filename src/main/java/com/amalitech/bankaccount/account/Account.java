package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.interfaces.DisplayCustomerDetailsAndGetCustomerType;


public abstract class Account implements DisplayCustomerDetailsAndGetCustomerType {
    private final String accountNumber;
    private final Customer customer;
    private double balance;
    private String status;
    static int accountCounter;
    private AccountType type;

    protected Account(Customer customer){
        this.customer = customer;
        this.accountNumber = "ACC00" + customer.getCustomerId();
        this.status = "Active";
        accountCounter++;
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
    public double deposit(double amount) throws IllegalArgumentException {
          if(amount <= 0){
              throw new IllegalArgumentException("Amount must be greater than zero");
          }

          this.balance += amount;

          return this.balance;
      }

    public double withdrawal(double amount) throws IllegalArgumentException {
        this.balance -= amount;

        return this.balance;
    }
}

