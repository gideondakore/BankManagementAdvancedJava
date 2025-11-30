package com.amalitech.bankaccount.menu;


import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.helpers.InputValidationHelper;
import com.amalitech.bankaccount.records.CustomerRecords;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    int choice;
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

    public CustomerType customerType(){
        int input;

        IO.println("""
                
                Customer type:
                1. Regular Customer (Standard banking service)
                2. Premium Customer (Enhanced benefits, min balance $10,000)
                """);

        input = new InputValidationHelper("Select type (1-2): ", "Please enter a valid number and must be 1 or 2", "").validatedIntInputValueWithRange(1, 2);

        if(input == 1) return CustomerType.REGULAR;

        return CustomerType.PREMIUM;
    }

    public AccountType accountType(){
        int input;

        IO.println("""
                
                Account type:
                1. Savings Account (Interest: 3.5%, Min Balance: $500)
                2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)
                """);

        input = new InputValidationHelper("Select type (1-2): ", "Please enter a valid number and must be 1 or 2", "").validatedIntInputValueWithRange(1, 2);

        if(input == 1) return AccountType.SAVINGS;

        return AccountType.CHECKING;
    }

    public int acceptInterInputValue(String msg, String errMsg){
        int input;
       input = new InputValidationHelper(msg, errMsg, "").validatedIntInputPositiveValue();
       return input;
    }

    public double acceptDoubleInputValue(String msg, String errMsg){
        double input;
        input = new InputValidationHelper(msg, errMsg, "").validatedDoubleInputPositiveValue();
        return input;
    }

    public void pressEnterToContinue(){
            Scanner scanner = new Scanner(System.in);
            IO.println("\nPress enter to continue...");
            scanner.nextLine();
    }

}
