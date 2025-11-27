package com.amalitech.bankaccount;
import com.amalitech.bankaccount.menu.Menu;

public class Main {
    public static void main(String[] args){
        Menu menu  = new Menu();
        menu.intro();
        int input = menu.getChoice();

        IO.println("User input: " + menu.getChoice());


        if(input == 5){
            IO.println("Existing program...");
            System.exit(0);
        }
    }
}
