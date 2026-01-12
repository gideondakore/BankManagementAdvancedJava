package com.amalitech.bankaccount;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.account.*;
import com.amalitech.bankaccount.customer.*;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.services.FilePersistenceService;
import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.utils.ConcurrencyUtils;
import com.amalitech.bankaccount.utils.FunctionalUtils;
import com.amalitech.bankaccount.utils.InputValidationHelper;
import com.amalitech.bankaccount.utils.Menu;
import com.amalitech.bankaccount.records.CustomerRecords;
import com.amalitech.bankaccount.transaction.TransactionManager;

import java.util.List;


public class Main {

    private static final double SAVING_MINIMUM_BALANCE = 500;
    private static final double PREMIUM_CUSTOMER_MINIMUM_BALANCE = 10000;
    private static final String INITIAL_DEPOSIT_MSG = "Enter initial deposit amount: $";
    private static final String INITIAL_DEPOSIT_ERR_MSG = "Please provide a valid amount!";

    static FilePersistenceService persistenceService = new FilePersistenceService();
    static TransactionManager transactionManager = new TransactionManager();
    static AccountManager accountManager;
    static Menu menu = new Menu();

    static {
        // Try to load data from files first, otherwise use mock data
        initializeData();
    }

    private static void initializeData() {
        if (persistenceService.dataFilesExist()) {
            IO.println("\n--- Loading data from files ---");
            List<Account> loadedAccounts = persistenceService.loadAccounts();
            List<Transaction> loadedTransactions = persistenceService.loadTransactions();
            
            if (!loadedAccounts.isEmpty()) {
                accountManager = new AccountManager(loadedAccounts.toArray(new Account[0]));
                loadedTransactions.forEach(transactionManager::addTransaction);
                IO.println("✓ Data loaded successfully from files.\n");
            } else {
                IO.println("ℹ No valid accounts found in files. Using mock data.\n");
                initializeMockData();
            }
        } else {
            IO.println("\n--- Initializing with mock data ---");
            initializeMockData();
        }
    }

    private static void initializeMockData() {
        Account[] mockAccounts = populateWithCustomAccount(transactionManager);
        accountManager = new AccountManager(mockAccounts);
    }


    public static void main(String[] args) {
        while (true) {

            menu.intro();

            int input = menu.getChoice();

            if (input == 8) {
                // Save data before exiting
                saveDataToFiles();
                IO.println("""
                        
                        Thank you for using the Bank Management System!
                        All data has been saved to files.
                        Goodbye!
                        """);
                break;
            }



            switch (input) {
                case 1 -> manageAccount();
                case 2 -> Main.performTransaction();
                case 3 -> menu.accountStatement(accountManager.getAccounts(), transactionManager);
                case 4 -> runConcurrentSimulation();
                case 5 -> saveDataToFiles();
                case 6 -> displayStatistics();
                case 7 -> CustomTestRunner.runAllTestsInPackage();
                default -> IO.println("Oops! Incorrect choice,please try again.");
            }

            menu.pressEnterToContinue();
        }
    }

    private static void handleCreateAccount(Menu menu, AccountManager accountManager, TransactionManager transactionManager) {
        try{

        CustomerRecords info = menu.createAccount();
        CustomerType customerType = menu.customerType();
        AccountType accountType = menu.accountType();

        Customer customer = createCustomer(info, customerType);
        double requiredMinBalance = getMinimumBalance(customerType, accountType);

        double initialDeposit = promptValidDeposit(menu, requiredMinBalance);

        Account account = createAccountByType(customer, accountType);

        // Actually deposit the initial amount into the account
        account.deposit(initialDeposit);

        // Add first deposit of account creation as a deposit transaction
        Transaction transaction = new Transaction(account.getAccountNumber(), initialDeposit, account.getAccountBalance());
        transaction.setType(TransactionType.DEPOSIT.getDescription());

        transactionManager.addTransaction(transaction);

        account.displayAccountDetails();

        accountManager.addAccount(account);

        }catch (InputMismatchException e){
           IO.println("Error occurred!");
           IO.println(e.getMessage());
        }catch (InvalidAmountException e) {
            IO.println(e.getMessage());
        }
    }

    private static Customer createCustomer(CustomerRecords info, CustomerType type) throws InputMismatchException {
        try {
            return (type == CustomerType.REGULAR)
                    ? new RegularCustomer(info.name(), info.age(), info.contact(), info.address(), info.email())
                    : new PremiumCustomer(info.name(), info.age(), info.contact(), info.address(), info.email());
        } catch (InputMismatchException e) {
            throw new InputMismatchException(e.getMessage());
        }
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
        try{

        Transaction transaction;
        Account acc1 = new SavingsAccount(new PremiumCustomer("John Smith", 23, "+1-555-7890", "123 Main Street, United State", "jsmith@example.com")).deposit(5250);
        Account acc2 = new CheckingAccount(new PremiumCustomer("Sarah Johnson", 21, "+44-207-9463821", "45 Oak Ave., Apt. 2B, United Kingdom", "sjohnson@example.com")).deposit(3450);
        Account acc3 = new SavingsAccount(new RegularCustomer("Michael Chen", 19, "+49-301-2345678", "12-34 Park Lane", "mchen@gmail.com")).deposit(15750);
        Account acc4 = new CheckingAccount(new RegularCustomer("Emily Brown", 22, "+33-142-869753", "12-34 Park Lane, Germany", "mbrown@outlook.com")).deposit(890);
        Account acc5 = new SavingsAccount(new RegularCustomer("David Wilson", 28, "+61-298-765432", "P.O. Box 234 - Australia", "dwilson@exmaple.com")).deposit(25300);

        Account[] accsArr = new Account[]{acc1, acc2, acc3, acc4, acc5};

        for(Account acc: accsArr){
            transaction = new Transaction(acc.getAccountNumber(), acc.getAccountBalance(), acc.getAccountBalance());
            transaction.setType(TransactionType.DEPOSIT.getDescription());
            transactionManager.addTransaction(transaction);
        }

        return accsArr;
        } catch (InputMismatchException e){
            IO.println(e.getMessage());
            IO.println("""
                        
                        Note:
                        Internal error occurred. Don't worry, everything is in control.
                        The application will start correctly but without our internal mock data which does not affect it functionalities.
                        
                        """);
        } catch (InvalidAmountException e){
            IO.println("Error: " + e.getMessage());
            IO.println("""
                        
                        Note:
                        Internal error occurred. Don't worry, everything is in control.
                        The application will start correctly but without our internal mock data which does not affect it functionalities.
                        
                        """);
        }
        return new Account[0];
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

    private static void runConcurrentSimulation() {
        IO.println("""
                
                CONCURRENT TRANSACTION SIMULATION
                ----------------------------------
                This will run multiple threads performing random deposits and withdrawals.
                """);
        
        int numThreads = InputValidationHelper.validatedIntInputValueWithRange(
            3, 10, 
            "Enter number of threads (3-10): ", 
            "Please enter a number between 3 and 10"
        );
        
        ConcurrencyUtils.runConcurrentSimulation(
            accountManager.getAccounts(), 
            transactionManager, 
            numThreads
        );
    }

    private static void saveDataToFiles() {
        IO.println("\n--- Saving data to files ---");
        persistenceService.saveAll(
            accountManager.getAccounts(), 
            transactionManager.getTransactions()
        );
    }

    private static void displayStatistics() {
        IO.println("""
                
                STATISTICS MENU
                ---------------
                1. Account Summary
                2. Transaction Summary
                3. Both
                """);
        
        int choice = InputValidationHelper.validatedIntInputValueWithRange(
            1, 3, 
            "Select option: ", 
            "Please enter 1, 2, or 3"
        );
        
        switch (choice) {
            case 1 -> FunctionalUtils.printAccountSummary(accountManager.getAccounts());
            case 2 -> FunctionalUtils.printTransactionSummary(transactionManager.getTransactions());
            case 3 -> {
                FunctionalUtils.printAccountSummary(accountManager.getAccounts());
                FunctionalUtils.printTransactionSummary(transactionManager.getTransactions());
            }
        }
    }
}

