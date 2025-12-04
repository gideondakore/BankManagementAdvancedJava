package com.amalitech.bankaccount.account;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountManager {
    private final ArrayList<Account> account = new ArrayList<>(50);
    private int accountCount;

    public AccountManager(){
        this.accountCount = 0;
    }

    public AccountManager(Account account){
        this.account.add(account);
        this.accountCount = this.account.size();
    }

    public AccountManager(Account[] accArr){
        Collections.addAll(account, accArr);
        this.accountCount = this.account.size();
    }

    public void addAccount(Account acc){
        this.account.add(acc);
        this.accountCount++;
    }

    public Account findAccount(String accNumber){
        for(Account acc: this.account){
            if(acc.getAccountNumber().equals(accNumber)) return acc;
        }

        return null;
    }

    public void viewAllAccounts(){
        if(this.account.isEmpty()){
            IO.println("""
                    -------------------------------------------
                    No account account created yet.
                    -------------------------------------------
                    """);
            return;
        }

        int numAccounts = 0;
        double totalBalance = 0.0;

        String line = "---------------------------------------------------------------------------------------------------------------------------";

        StringBuilder stringBuilder = new StringBuilder();

        String heading = """
                %s
                %-8s            |  %-25s             |  %-8s           |  %-5s           |  %-5s
                %s
                """.formatted(line, "ACC N0", "CUSTOMER NAME", "TYPE", "BALANCE", "STATUS", line);

        stringBuilder.append(heading);




        for(Account acc: this.account){

            stringBuilder.append(acc.viewAllAccounts(acc.getAccountCustomer())).append("\n").append(line).append("\n");
            totalBalance += acc.getAccountBalance();

            numAccounts++;

        }

        IO.println(stringBuilder.toString());
        IO.println("Total Account: " + numAccounts);
        IO.println("Total Account Balance: $%,.2f".formatted(totalBalance));
    }

    public double getTotalBalance(){
        double tempTotal = 0.0;

        for(Account acc: account){
            tempTotal += acc.getAccountBalance();
        }

        return tempTotal;

    }

    public int getAccountCount() {
        return accountCount;
    }

    public List<Account> getAccount() {
        return account;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append("[ ");
        for(int i = 0; i < this.account.size(); i++){

            if (i < (this.account.size() - 1)) {
                str.append(this.account.get(i).getAccountNumber()).append(", ");
            } else {
                str.append(this.account.get(i).getAccountNumber());
            }
        }
        str.append(" ]");

        return str.toString();
    }
}
