package com.amalitech.bankaccount;

import com.amalitech.bankaccount.account.*;
import com.amalitech.bankaccount.customer.*;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.menu.Menu;
import com.amalitech.bankaccount.records.CustomerRecords;
import com.amalitech.bankaccount.transaction.TransactionManager;

public class Main {

    private static final double SAVING_MINIMUM_BALANCE = 500;
    private static final double PREMIUM_CUSTOMER_MINIMUM_BALANCE = 10000;
    private static final String INITIAL_DEPOSIT_MSG = "Enter initial deposit amount: $";
    private static final String INITIAL_DEPOSIT_ERR_MSG = "Please provide a valid amount!";

    public static void main(String[] args) {
        Menu menu = new Menu();
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();



        while (true) {
            IO.println("Account Manager toString: " + accountManager);

            menu.intro();

            IO.println("Menu: " + menu);
            IO.println("Transaction Manager toString: " + transactionManager);


            int input = menu.getChoice();

            if (input == 5) {
                IO.println("Application exited successfully...");
                break;
            }

            switch (input) {
                case 1 -> handleCreateAccount(menu, accountManager);
                case 2 -> accountManager.viewAllAccounts();
                case 3 -> menu.processTransaction(accountManager.getAccount(), transactionManager);
                case 4 -> menu.viewTransactionHistory(accountManager.getAccount(), transactionManager);
                default -> IO.println("Oops! Incorrect choice, try again.");
            }

            menu.pressEnterToContinue();
        }
    }

    private static void handleCreateAccount(Menu menu, AccountManager accountManager) {
        CustomerRecords info = menu.createAccount();

        CustomerType customerType = menu.customerType();
        AccountType accountType = menu.accountType();

        Customer customer = createCustomer(info, customerType);
        double requiredMinBalance = getMinimumBalance(customerType, accountType);

        double initialDeposit = promptValidDeposit(menu, requiredMinBalance);

        Account account = createAccountByType(customer, accountType);

        account.deposit(initialDeposit);
        account.displayCustomerDetails();

        accountManager.addAccount(account);
    }

    private static Customer createCustomer(CustomerRecords info, CustomerType type) {
        return (type == CustomerType.REGULAR)
                ? new RegularCustomer(info.name(), info.age(), info.contact(), info.address())
                : new PremiumCustomer(info.name(), info.age(), info.contact(), info.address());
    }

    private static double getMinimumBalance(CustomerType cType, AccountType aType) {
        if (cType == CustomerType.PREMIUM) return PREMIUM_CUSTOMER_MINIMUM_BALANCE;
        if (aType == AccountType.SAVINGS) return SAVING_MINIMUM_BALANCE;
        return 0;
    }

    private static double promptValidDeposit(Menu menu, double minRequired) {
        double amount;

        while (true) {
            amount = menu.acceptDoubleInputValue(INITIAL_DEPOSIT_MSG, INITIAL_DEPOSIT_ERR_MSG);

            if (amount >= minRequired) break;

            if (minRequired > 0) {
                IO.println("Minimum required deposit is $" + minRequired);
            }

        }

        return amount;
    }

    private static Account createAccountByType(Customer customer, AccountType type) {
        return (type == AccountType.SAVINGS)
                ? new SavingsAccount(customer)
                : new CheckingAccount(customer);
    }

}
