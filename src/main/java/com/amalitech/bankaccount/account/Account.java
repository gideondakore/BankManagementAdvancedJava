package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;
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

    /**
     *
     * @return Getter method to return account number
     */
    public String getAccountNumber(){
        return this.accountNumber;
    }

    /**
     *
     * @return Getter method to return Customer object
     */
    public Customer getAccountCustomer(){
        return this.customer;
    }

    /**
     *
     * @return Getter method to return account balance
     */
    public double getAccountBalance(){
        return this.balance;
    }

    /**
     *
     * @return Getter method to return account status
     */
    public String getAccountStatus(){
        return this.status;
    }

    /**
     *
     * @return Getter method to return account type (i.e Savings or Checking account)
     */
    public AccountType getType(){
        return type;
    }

    /**
     *
     * @return Getter method to return Customer of an account
     */
    public Customer getCustomer() {
        return customer;
    }

    // Setters

    /**
     * Set account balance
     * @param balance
     */
    public void setAccountBalance(double balance){
        this.balance = balance;
    }

    /**
     * Set account status
     * @param status
     */
    public void setAccountStatus(String status){
        this.status = status;
    }

    /**
     * Set account type
     * accType can be Checking or Savinngs account and must a type of <b><u>AccountType</u></b>
     * @param accType
     */
    public void setType(AccountType accType){
        this.type = accType;
    }


    // Abstract methods

    /**
     * Abstract method for viewing All accounts
     * @param customer
     * @return
     */
    public abstract String viewAllAccounts(Customer customer);


    // Regular methods

    /**
     * Method for depositing from account
     * @param amount
     * @return
     * @throws IllegalArgumentException
     */
    public Account deposit(double amount) throws IllegalArgumentException {

          this.balance += amount;

          return this;
      }


    /**
     * Method for withdrawing from account
     * @param amount
     * @throws IllegalArgumentException
     */
    public void withdrawal(double amount) throws IllegalArgumentException {
        this.balance -= amount;
    }

    /**
     * Custom override implementation of the String method
     * @return Account number
     */
    @Override
    public String toString() {
        return this.accountNumber;
    }
}

