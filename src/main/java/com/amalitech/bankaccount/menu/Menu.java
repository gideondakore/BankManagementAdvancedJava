package com.amalitech.bankaccount.menu;


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
               int input =  this.receiveChoice();
               this.choice = input;
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


}
