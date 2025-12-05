package com.amalitech.bankaccount;

import com.amalitech.bankaccount.account.*;
import com.amalitech.bankaccount.customer.*;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.utils.InputValidationHelper;
import com.amalitech.bankaccount.utils.Menu;
import com.amalitech.bankaccount.records.CustomerRecords;
import com.amalitech.bankaccount.transaction.TransactionManager;



public class Main {

    private static final double SAVING_MINIMUM_BALANCE = 500;
    private static final double PREMIUM_CUSTOMER_MINIMUM_BALANCE = 10000;
    private static final String INITIAL_DEPOSIT_MSG = "Enter initial deposit amount: $";
    private static final String INITIAL_DEPOSIT_ERR_MSG = "Please provide a valid amount!";

    static TransactionManager transactionManager = new TransactionManager();
    static Account[] mockAccounts = Main.populateWithCustomAccount(transactionManager);
    static AccountManager accountManager = new AccountManager(mockAccounts);
    static Menu menu = new Menu();


    public static void main(String[] args) {

        while (true) {

            menu.intro();

            int input = menu.getChoice();

            if (input == 5) {
                IO.println("Application exited successfully...");
                break;
            }



            switch (input) {
                case 1 -> manageAccount();
                case 2 -> Main.performTransaction();
                case 3 -> menu.accountStatement(accountManager.getAccounts(), transactionManager);
                case 4 -> menu.viewTransactionHistory(accountManager.getAccounts(), transactionManager);
                default -> IO.println("Oops! Incorrect choice,please try again.");
            }

            menu.pressEnterToContinue();
        }
    }

    private static void handleCreateAccount(Menu menu, AccountManager accountManager, TransactionManager transactionManager) {
        CustomerRecords info = menu.createAccount();
        CustomerType customerType = menu.customerType();
        AccountType accountType = menu.accountType();

        Customer customer = createCustomer(info, customerType);
        double requiredMinBalance = getMinimumBalance(customerType, accountType);

        double initialDeposit = promptValidDeposit(menu, requiredMinBalance);

        Account account = createAccountByType(customer, accountType);

        account.deposit(initialDeposit);

        // Add first deposit of account creation as a deposit transaction
        Transaction transaction = new Transaction(account.getAccountNumber(), initialDeposit, initialDeposit);
        transaction.setType(TransactionType.DEPOSIT.getDescription());

        transactionManager.addTransaction(transaction);

        account.displayAccountDetails();

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

    private static Account[] populateWithCustomAccount (TransactionManager transactionManager){
        Transaction transaction;

        Account acc1 = new SavingsAccount(new PremiumCustomer("John Smith", 23, "+1-415-782-9364", "123 Main Street, United State")).deposit(5250);
        Account acc2 = new CheckingAccount(new PremiumCustomer("Sarah Johnson", 21, "+44-207-9463821", "45 Oak Ave., Apt. 2B, United Kingdom")).deposit(3450);
        Account acc3 = new SavingsAccount(new RegularCustomer("Michael Chen", 19, "+49-301-2345678", "12-34 Park Lane")).deposit(15750);
        Account acc4 = new CheckingAccount(new RegularCustomer("Emily Brown", 22, "+33-142-869753", "12-34 Park Lane, Germany")).deposit(890);
        Account acc5 = new SavingsAccount(new RegularCustomer("David Wilson", 28, "+61-298-765432", "P.O. Box 234 - Australia")).deposit(25300);

        Account[] accsArr = new Account[]{acc1, acc2, acc3, acc4, acc5};

        for(Account acc: accsArr){
            transaction = new Transaction(acc.getAccountNumber(), acc.getAccountBalance(), acc.getAccountBalance());
            transaction.setType(TransactionType.DEPOSIT.getDescription());
            transactionManager.addTransaction(transaction);
        }

        return accsArr;

    }

    private static void manageAccount(){
        int input;
        IO.println("""                
                1. Create Account
                2. View Account
                """);

        input = InputValidationHelper.validatedIntInputValueWithRange(1, 2, "Select action: ", "Please provide a valid input. Input must be only numbers from 1-2");

        switch (input){
            case 1 -> handleCreateAccount(menu, Main.accountManager, Main.transactionManager);
            case 2 -> accountManager.viewAllAccounts();
            default -> IO.println("Oops! Wrong input choice selected");
        }

    }

    private static void performTransaction(){
        int input;

        IO.println("""                
                1. Process Transaction
                2. View Transaction History
                """);
        input = InputValidationHelper.validatedIntInputValueWithRange(1, 2, "Select action: ", "Please provide a valid input. Input must be only numbers from 1-2");

        switch (input){
            case 1 -> menu.performTransaction(accountManager.getAccounts(), transactionManager);
            case 2 -> menu.viewTransactionHistory(accountManager.getAccounts(), transactionManager);
            default -> IO.println("Oops! Wrong input choice selected");
        }
    }

}
