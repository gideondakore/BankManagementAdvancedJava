package com.amalitech.bankaccount.utils;


import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.enums.TransferToOrFromType;
import com.amalitech.bankaccount.interfaces.Transactable;
import com.amalitech.bankaccount.records.CustomerRecords;
import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.transaction.TransactionManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu implements Transactable {
    int choice;
    static String validNumberMsg = "Please enter a valid number and must be 1 or 2";
    static String validNumRange = "Select type (1-2): ";
    TransactionManager transactionManager;
    Account accountSelectedForTransaction;
    Account transferAccount;
    
    public void intro() {

        String introFormattedStr = """
                --------------------------------------------
                --------------------------------------------
                |                                          |
                
                |     BANK ACCOUNT MANAGEMENT - MAIN MENU  |                                 |
                --------------------------------------------
                --------------------------------------------
                
                1. Create Account
                2. View Account
                3. Process Transaction
                4. View Transaction History
                5. Exit
                """;

        IO.println(introFormattedStr);

        while (true){
            try{
               this.choice = this.receiveChoice();
                break;
            }catch (InputMismatchException _){
                IO.println("Please provide a valid input. Input must be only numbers from 1-5");
            }

        }
    }


    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getChoice(){
        return choice;
    }

    private int receiveChoice() throws InputMismatchException {
        int input;
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter choice: ");
        input = scanner.nextInt();

        if(input > 5 || input < 1) {
            throw new InputMismatchException();
        }

        return input;


    }

    public CustomerRecords createAccount() {
        IO.println("""
                
                ACCOUNT CREATION
                -----------------------------------------------
                """);
        String name;
        int age;
        String contact;
        String address;

        name = new InputValidationHelper("Enter customer name: ", """
                Please provide valid name!
                
                //Example valid name:
                "Gideon Dakore"   ✓
                "Mary Jane Smith" ✓
                "Susana-Lawrence Van Damme" ✓
                Herman Melville ✓
                "A. A. Milne" ✓
                "Abraham Van Helsing" ✓
                "Mathis d'Arias" ✓
                "Martin Luther King, Jr." ✓
                "Tony Montana Prez Rodriguez DeJesus del Rosario Mercedes Pilar Martínez Molina Baeza" ✓
                """, "^[A-ZÀ-ÿ][-,a-z.' ]+( [A-ZÀ-ÿ][-,a-z.' ]+)+$").validatedStringInputValue();
        age = new InputValidationHelper("Enter customer age: ", "Please provide a valid age (1-120)!", "^(?:120|1[01][0-9]|[1-9][0-9]?)$").validatedIntInputValue();
        contact = new InputValidationHelper("Enter customer contact (+1-555-7890): ", "Please provide valid phone number!", "\\+\\d{1,3}-\\d{3}-\\d{4,12}").validatedStringInputValue();
        address = new InputValidationHelper("Enter customer address: ", """
                Please provide a valid address!
                
                // Examples that match:
                // "123 Main Street"
                // "45 Oak Ave., Apt. 2B"
                // "12-34 Park Lane"
                // "P.O. Box 456"
                """, "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$").validatedStringInputValue();

        return new CustomerRecords(name, age, contact, address);
    }

    public void performTransaction(List<Account> accounts, TransactionManager transactionManager){
        Account transferAcc;
        this.transactionManager = transactionManager;
        IO.println("""
                
                PROCESS TRANSACTION
                -----------------------------------------------
                """);

        TransactionType transactionType;
        double transactionAmount;
        char yesOrNo;



        this.accountSelectedForTransaction = getAccountSelectedForTransaction(accounts, "Enter Account Number: ", """
                    Please provide a valid account number!
                    
                    Example format:
                    ACC001
                    ACC002
                    ACC0010
                    ACC00120
                    """);

        transactionType = this.transactionType();

        if (TransactionType.TRANSFER == transactionType){
            transferAcc = getAccountSelectedForTransaction(accounts, "Enter Recipient Account Number: ", """
                    Please provide a valid recipient account number!
                    
                    Example format:
                    ACC001
                    ACC002
                    ACC0010
                    ACC00120
                    """);

            this.transferAccount = transferAcc;
        }else{

            IO.println("""
                
                Account Details:
                Customer: %s
                Account Type: %s
                Current Balance: $%,.2f
                """.formatted(this.accountSelectedForTransaction.getAccountCustomer().getName(), this.accountSelectedForTransaction.getType(), this.accountSelectedForTransaction.getAccountBalance()));
        }


        boolean done = false;

        while (!done){

            transactionAmount = this.acceptDoubleInputValue("Enter amount: $", "Please provide a valid amount");


            new TransactionManager().previewTransactionConfirmation(this.accountSelectedForTransaction, transactionType, transactionAmount);

            yesOrNo = this.promptValidYesOrNo();

            if(yesOrNo == 'N') {
                IO.println("❌ Transaction unsuccessful!");
                done = true;
            }else{
                try{

                    done = this.processTransaction(transactionAmount, transactionType.getDescription());

                    IO.println("✔ Transaction completed successfully!");
                }catch (IllegalArgumentException err){
                    IO.println(err.getMessage());
                }
            }


        }
    }


    // Getting customer type
    public CustomerType customerType(){
        int input;

        IO.println("""
                
                Customer type:
                1. Regular Customer (Standard banking service)
                2. Premium Customer (Enhanced benefits, min balance $10,000)
                """);

        input = new InputValidationHelper(validNumRange, validNumberMsg, "").validatedIntInputValueWithRange(1, 2);

        if(input == 1) return CustomerType.REGULAR;

        return CustomerType.PREMIUM;
    }

    // Getting account type
    public AccountType accountType(){
        int input;

        IO.println("""
                
                Account type:
                1. Savings Account (Interest: 3.5%, Min Balance: $500)
                2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)
                """);

        input = new InputValidationHelper(validNumRange, validNumberMsg, "").validatedIntInputValueWithRange(1, 2);

        if(input == 1) return AccountType.SAVINGS;

        return AccountType.CHECKING;
    }

    // Getting transaction type
    public TransactionType transactionType(){
        int input;

        IO.println("""
                Transaction type:
                1. Deposit
                2. Withdrawal
                3. Transfer
                """);

        input = new InputValidationHelper(validNumRange, validNumberMsg, "").validatedIntInputValueWithRange(1, 3);

        if(input == 1) return TransactionType.DEPOSIT;
        if(input == 2) return TransactionType.WITHDRAWAL;
        return TransactionType.TRANSFER;
    }


    public double acceptDoubleInputValue(String msg, String errMsg){
        double input;

        while (true){
            input = new InputValidationHelper(msg, errMsg, "").validatedDoubleInputPositiveValue();
            if(input > 0){
                break;
            }else IO.println("Amount must be greater than zero!");
        }

        return input;
    }

    public void pressEnterToContinue(){
            Scanner scanner = new Scanner(System.in);
            IO.println("\nPress enter to continue...");

            if(scanner.hasNextLine()){
                scanner.nextLine();
            }

            scanner.nextLine();
    }

    public Account getAccountForTransaction(List<Account> account, String accNum){
        Account selectedAcc = null;

        for(Account acc: account){
            if(acc.getAccountNumber().equals(accNum)){
                selectedAcc = acc;
                break;
            }
        }

        if(selectedAcc == null){
            IO.println("Account with the account number " + accNum + " not found! Please check your account number and try again.");
            return null;
        }

        return selectedAcc;

    }



    @Override
    public boolean processTransaction(double transactionAmount, String transactionType) throws IllegalArgumentException{
        Transaction transaction;
        Transaction transfer;

        if(transactionType.equals(TransactionType.DEPOSIT.getDescription())) {
            this.accountSelectedForTransaction.deposit(transactionAmount);
        } else {
            // Withdraw from current user account
            this.accountSelectedForTransaction.withdrawal(transactionAmount);

            // Transfer to recipient account
            if(transactionType.equals(TransactionType.TRANSFER.getDescription())){
                this.transferAccount.deposit(transactionAmount);
                transfer = new Transaction(this.transferAccount.getAccountNumber(), transactionAmount, this.transferAccount.getAccountBalance());
                transfer.setType(TransactionType.TRANSFER.getDescription());
                this.transactionManager.addTransaction(transfer);
                transfer.setTransferToOrFrom(TransferToOrFromType.TO);
            }
        }

        transaction = new Transaction(this.accountSelectedForTransaction.getAccountNumber(), transactionAmount, this.accountSelectedForTransaction.getAccountBalance());
        transaction.setType(transactionType);
        this.transactionManager.addTransaction(transaction);

        if(transactionType.equals(TransactionType.TRANSFER.getDescription())){
            transaction.setTransferToOrFrom(TransferToOrFromType.FROM);
        }

        return true;

    }

    public char promptValidYesOrNo(){
        char yesOrNo;
        while(true){
            yesOrNo = new InputValidationHelper("Confirm transaction? (Y/N): ", "Please select (Y for Yes) or (N for No)", "^[YN]$").validatedCharInputValue();

            if((yesOrNo != 'Y') && (yesOrNo != 'N')){
                IO.println("Wrong input provided. Select (Y for Yes) or (N for No)");
            }else break;

        }

        return yesOrNo;

    }

    public void viewTransactionHistory(List<Account> account, TransactionManager transactionManager){
        String accNumber;
        Account selectedAcc;

        IO.println("""
                
                VIEW TRANSACTION HISTORY
                --------------------------
                """);

        do {
            accNumber = new InputValidationHelper("Enter Account Number: ", """
                    Please provide a valid account number!
                    
                    Example format:
                    ACC001
                    ACC002
                    ACC0010
                    ACC00120
                    """, "^ACC00\\d+$").validatedStringInputValue();

            selectedAcc = this.getAccountForTransaction(account, accNumber);

        } while (selectedAcc == null);


        IO.println("""
                
                Account: %s - %s
                Account Type: %s
                Current Balance: $%,.2f
                
                """.formatted(selectedAcc.getAccountNumber(), selectedAcc.getAccountCustomer().getName(), selectedAcc.getType().getDescription(), selectedAcc.getAccountBalance()));

        transactionManager.viewTransactionsByAccount(accNumber);
    }


    @Override
    public String toString(){

        return switch (choice) {
            case 1 -> "1. Create Account";
            case 2 -> "2. View Account";
            case 3 -> "3. Process Transaction";
            case 4 -> "4. View Transaction History";
            case 5 -> "5. Exit";
            default -> "No menu selected";
        };
    }

    private Account getAccountSelectedForTransaction(List<Account> accounts, String msg, String errMsg){
        String accNumber;
        Account selectedAcc;

        do {
            accNumber = new InputValidationHelper(msg, errMsg, "^ACC00\\d+$").validatedStringInputValue();

            selectedAcc = this.getAccountForTransaction(accounts, accNumber);

        } while (selectedAcc == null);

        return selectedAcc;
    }
}
