package com.amalitech.bankaccount;

import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.account.AccountManager;
import com.amalitech.bankaccount.account.CheckingAccount;
import com.amalitech.bankaccount.account.SavingsAccount;
import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.customer.PremiumCustomer;
import com.amalitech.bankaccount.customer.RegularCustomer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.menu.Menu;
import com.amalitech.bankaccount.records.CustomerRecords;



public class Main {
    public static void main(String[] args){
        Menu menu  = new Menu();
        AccountManager accountManager = new AccountManager();


        while(true){
            menu.intro();
            int input = menu.getChoice();

            if(input == 5){
                IO.println("Application existed successfully...");
                break;
            }

            switch (input){
                case 1: {
                    String name;
                    int age;
                    String contact;
                    String address;
                    CustomerType customerType;
                    AccountType accountType;
                    double initialDeposit;
                    Account account;
                    Customer customer;
                    final double SAVING_MINIMUM_BALANCE = 500;
                    final double PREMIUM_CUSTOMER_MINIMUM_BALANCE = 10000;
                    String initialDepositMsg = "Enter initial deposit amount: $";
                    String initialDepositErrMsg = "Please provide a valid amount!";

                    CustomerRecords customerRecords = menu.createAccount();
                    name = customerRecords.name();
                    age = customerRecords.age();
                    contact = customerRecords.contact();
                    address = customerRecords.address();

                    customerType = menu.customerType();
                    accountType = menu.accountType();


                    if(customerType == CustomerType.REGULAR){
                        //Customer(String name, int age, String contact, String address)
                        customer = new RegularCustomer(name, age, contact, address);

                        initialDeposit = menu.acceptDoubleInputValue(initialDepositMsg, initialDepositErrMsg);

                        // Regular savings
                        if(accountType == AccountType.SAVINGS){
                            while (true){

                                if(initialDeposit >= SAVING_MINIMUM_BALANCE){
                                    break;
                                }else{
                                    IO.println("With Savings account you must have at least $" + SAVING_MINIMUM_BALANCE);
                                    initialDeposit = menu.acceptDoubleInputValue(initialDepositMsg, initialDepositErrMsg);
                                }
                            }

                            account = new SavingsAccount(customer);
                        }else{
                            account = new CheckingAccount(customer);
                        }
                        account.deposit(initialDeposit);
                        account.displayCustomerDetails();
                    }
                    else{
                        //PREMIUM CUSTOMER

                        while (true){
                            initialDeposit = menu.acceptDoubleInputValue(initialDepositMsg, initialDepositErrMsg);

                            if(initialDeposit >= PREMIUM_CUSTOMER_MINIMUM_BALANCE){
                                break;
                            }else{
                                IO.println("With Premium customer account you must have at least $" + PREMIUM_CUSTOMER_MINIMUM_BALANCE);
                            }
                        }

                        customer = new PremiumCustomer(name, age, contact, address);

                        if(accountType == AccountType.SAVINGS){
                            account = new SavingsAccount(customer);
                        }else{
                            account = new CheckingAccount(customer);
                        }

                        account.deposit(initialDeposit);
                        account.displayCustomerDetails();

                    }

                    // Add account to centralize account manager
                    accountManager.addAccount(account);
                }
                break;

                case 2: {
                  accountManager.viewAllAccounts();
                }
                break;
            }


            menu.pressEnterToContinue();
//            break;
        }


    }
}
