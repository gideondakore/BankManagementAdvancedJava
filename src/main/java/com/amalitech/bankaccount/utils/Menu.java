package com.amalitech.bankaccount.utils;


import com.amalitech.bankaccount.account.Account;
import com.amalitech.bankaccount.account.AccountManager;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.enums.TransferToOrFromType;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.exceptions.OverdraftExceededException;
import com.amalitech.bankaccount.interfaces.Transactable;
import com.amalitech.bankaccount.records.CustomerRecords;
import com.amalitech.bankaccount.transaction.Transaction;
import com.amalitech.bankaccount.transaction.TransactionManager;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu implements Transactable {
    int choice;
    static String validNumberErrMsg = "Please enter a valid number and must be 1 or 2";
    static String validNumRangeMsg = "Select type (1-2): ";
    static String enterAccNumMsg = "Enter Account Number: ";
    static String validAccNumRegex = "^ACC00\\d+$";
    static String invalidAccNumMsg = """
                    Please provide a valid account number!
                    
                    Example format:
                    ACC001
                    ACC002
                    ACC0010
                    ACC00120
                    """;

    TransactionManager transactionManager;
    Account accountSelectedForTransaction;
    Account recipientAccount;
    
    public void intro() {

        String introFormattedStr = """
                --------------------------------------------
                --------------------------------------------
                |                                          |
                
                |     BANK ACCOUNT MANAGEMENT SYSTEM       |
                --------------------------------------------
                --------------------------------------------
                
                1. Manage Account
                2. Perform Transaction
                3. Generate Account Statement
                4. Run Concurrent Simulation
                5. Save Data to Files
                6. View Statistics
                7. Run Tests
                8. Exit
                """;

        IO.println(introFormattedStr);

        while (true){
            try{
               this.choice = receiveChoice(1, 8);
                break;
            }catch (InputMismatchException e){
                IO.println("Please provide a valid input. Input must be only numbers from 1-8");
            }

        }
    }


    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getChoice(){
        return choice;
    }

    private static int receiveChoice(int lowerBound, int upperBound) throws InputMismatchException {
        int input;
        Scanner scanner = new Scanner(System.in);
        IO.print("Enter choice: ");
        input = scanner.nextInt();

        if(input > upperBound || input < lowerBound) {
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

        name = InputValidationHelper.validatedStringInputValue("Enter customer name: ", """
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
                """, "^[A-ZÀ-ÿ][-,a-z.' ]+( [A-ZÀ-ÿ][-,a-z.' ]+)+$");
        age = InputValidationHelper.validatedIntInputValue("Enter customer age: ", "Please provide a valid age (1-120)!", "^(?:120|1[01][0-9]|[1-9][0-9]?)$");
        contact = InputValidationHelper.validatedStringInputValue("Enter customer contact (+1-555-7890): ", "Please provide valid phone number!", "\\+\\d{1,3}-\\d{3}-\\d{4,12}");
        address = InputValidationHelper.validatedStringInputValue("Enter customer address: ", """
                Please provide a valid address!
                
                // Examples that match:
                // "123 Main Street"
                // "45 Oak Ave., Apt. 2B"
                // "12-34 Park Lane"
                // "P.O. Box 456"
                """, "^[a-zA-Z0-9\\s,.'\\-#]{5,100}$");

        return new CustomerRecords(name, age, contact, address);
    }

    public void performTransaction(List<Account> accounts, TransactionManager transactionManager){
        Account recipientAccNum;
        this.transactionManager = transactionManager;
        IO.println("""
                
                PROCESS TRANSACTION
                -----------------------------------------------
                """);

        TransactionType transactionType;
        double transactionAmount;
        char yesOrNo;



        this.accountSelectedForTransaction = getAccountSelectedForTransaction(accounts, enterAccNumMsg, invalidAccNumMsg);

        transactionType = this.transactionType();

        if (TransactionType.TRANSFER == transactionType){
            recipientAccNum = getAccountSelectedForTransaction(accounts, "Enter Recipient Account Number: ", invalidAccNumMsg);

            this.recipientAccount = recipientAccNum;

            while(recipientAccNum == this.accountSelectedForTransaction){
                IO.println("Sender and recipient account must not be the same!");

                recipientAccNum = getAccountSelectedForTransaction(accounts, "Enter Recipient Account Number: ", invalidAccNumMsg);
            }

            this.recipientAccount = recipientAccNum;

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


            new TransactionManager().previewTransactionConfirmation(this.accountSelectedForTransaction, transactionType, transactionAmount, transactionManager, this.accountSelectedForTransaction.getAccountNumber());

            yesOrNo = this.promptValidYesOrNo();

            if(yesOrNo == 'N') {
                IO.println("❌ Transaction unsuccessful!");
                done = true;
            }else{
                try{

                    done = this.processTransaction(transactionAmount, transactionType.getDescription());

                    IO.println("✔ Transaction completed successfully!");
                }catch (InvalidAmountException | InsufficientFundsException | OverdraftExceededException err){
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

        input = InputValidationHelper.validatedIntInputValueWithRange(1, 2, validNumRangeMsg, validNumberErrMsg);

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

        input = InputValidationHelper.validatedIntInputValueWithRange(1, 2, validNumRangeMsg, validNumberErrMsg);

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

        input = InputValidationHelper.validatedIntInputValueWithRange(1, 3, "Select type (1-3):", validNumberErrMsg);

        if(input == 1) return TransactionType.DEPOSIT;
        if(input == 2) return TransactionType.WITHDRAWAL;
        return TransactionType.TRANSFER;
    }


    public double acceptDoubleInputValue(String msg, String errMsg){
        double input;

        while (true){
            input = InputValidationHelper.validatedDoubleInputPositiveValue(msg, errMsg);
            if(input > 0){
                break;
            }else IO.println("❌ Error: Invalid amount. Amount must be greater than 0.");
        }

        return input;
    }

    public void pressEnterToContinue(){
        Scanner scanner = new Scanner(System.in);
        IO.println("\nPress enter to continue...");
        scanner.nextLine();
    }



    @Override
    public boolean processTransaction(double transactionAmount, String transactionType) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        Transaction transaction;
        Transaction recipient;
        // Collect all the transactions specific to an account number
        List<Transaction> userPerformingOperationTransactions = TransactionManager.getAllTransactions(this.accountSelectedForTransaction.getAccountNumber(), this.transactionManager.getTransactions());


        if(transactionType.equals(TransactionType.DEPOSIT.getDescription())) {
            this.accountSelectedForTransaction.deposit(transactionAmount);
        } else {
            // Withdraw from current user account
            this.accountSelectedForTransaction.withdrawal(transactionAmount);

            // Transfer to recipient account
            if(transactionType.equals(TransactionType.TRANSFER.getDescription())){
                List<Transaction> userRecipientTransactions = TransactionManager.getAllTransactions(this.recipientAccount.getAccountNumber(), this.transactionManager.getTransactions());

                this.recipientAccount.deposit(transactionAmount);
                recipient = new Transaction(this.recipientAccount.getAccountNumber(), transactionAmount, this.recipientAccount.getAccountBalance());
                recipient.setType(TransactionType.TRANSFER.getDescription());
                recipient.setTransferToOrFrom(TransferToOrFromType.TO);
                recipient.generateTransactionId(userRecipientTransactions.size() + 1);
                this.transactionManager.addTransaction(recipient);
            }
        }

        transaction = new Transaction(this.accountSelectedForTransaction.getAccountNumber(), transactionAmount, this.accountSelectedForTransaction.getAccountBalance());

        transaction.setType(transactionType);
        
        if(transactionType.equals(TransactionType.TRANSFER.getDescription())){
            transaction.setTransferToOrFrom(TransferToOrFromType.FROM);
        }
        
        transaction.generateTransactionId(userPerformingOperationTransactions.size() + 1);
        this.transactionManager.addTransaction(transaction);

        return true;
    }

    public char promptValidYesOrNo(){
        char yesOrNo;
        while(true){
            yesOrNo = InputValidationHelper.validatedCharInputValue("Confirm transaction? (Y/N): ", "Please select (Y for Yes) or (N for No)", "^[YN]$");

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
            accNumber = InputValidationHelper.validatedStringInputValue("Enter Account Number: ", invalidAccNumMsg, validAccNumRegex);

            selectedAcc = AccountManager.getAccountForTransaction(account, accNumber);

        } while (selectedAcc == null);


        IO.println("""
                
                Account: %s - %s
                Account Type: %s
                Current Balance: $%,.2f
                
                """.formatted(selectedAcc.getAccountNumber(), selectedAcc.getAccountCustomer().getName(), selectedAcc.getType().getDescription(), selectedAcc.getAccountBalance()));

        transactionManager.viewTransactionsByAccount(accNumber, "TRANSACTION HISTORY");
    }

    public void accountStatement(List<Account> account, TransactionManager transactionManager){
        String accNumber;
        Account selectedAcc;

        IO.println("""
                
                GENERATE ACCOUNT STATEMENT
                --------------------------
                """);

        do {
            accNumber = InputValidationHelper.validatedStringInputValue(enterAccNumMsg, invalidAccNumMsg, validAccNumRegex);

            selectedAcc = AccountManager.getAccountForTransaction(account, accNumber);

        } while (selectedAcc == null);


        IO.println("""
                Account: %s - %s
                Current Balance: $%,.2f
                """.formatted(selectedAcc.getAccountNumber(), selectedAcc.getAccountCustomer().getName(), selectedAcc.getAccountBalance()));

        transactionManager.viewTransactionsByAccount(accNumber, "Transactions:");

        List<Transaction> newTransactions = TransactionManager.getAllTransactions(accNumber, transactionManager.getTransactions());

        double netCharge;

        Transaction getLastTransaction = newTransactions.getLast();

        if(newTransactions.isEmpty()) netCharge = 0.0;
        else netCharge = getLastTransaction.getBalanceAfter();

        if (getLastTransaction.getBalanceAfter() < 0)
            IO.println("Net Charge: %s%,.2f".formatted("-$", getLastTransaction.getBalanceAfter()));
        else
            IO.println("Net Charge: %s%,.2f".formatted((getLastTransaction.getBalanceAfter() == 0 ? "" : "+$"), getLastTransaction.getBalanceAfter() == 0 ? netCharge : getLastTransaction.getBalanceAfter()));

        IO.println("✓ Statement generated successfully");

    }



    @Override
    public String toString(){

        return switch (choice) {
            case 1 -> "1. Manage Account";
            case 2 -> "2. Perform Transaction";
            case 3 -> "3. Generate Account Statement";
            case 4 -> "4. Run Concurrent Simulation";
            case 5 -> "5. Save Data to Files";
            case 6 -> "6. View Statistics";
            case 7 -> "7. Run Tests";
            case 8 -> "8. Exit";
            default -> "No menu selected";
        };
    }

    public static Account getAccountSelectedForTransaction(List<Account> accounts, String msg, String errMsg){
        String accNumber;
        Account selectedAcc;

        do {
            accNumber = InputValidationHelper.validatedStringInputValue(msg, errMsg, validAccNumRegex);

            selectedAcc = AccountManager.getAccountForTransaction(accounts, accNumber);

        } while (selectedAcc == null);

        return selectedAcc;
    }
}
