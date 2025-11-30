package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CheckingAccount extends Account{
    private final double overdraftLimit;
    private final double monthlyFee;
    private final LocalDate createdAt = LocalDate.now();



    public  CheckingAccount(Customer customer){
        super(customer);
        overdraftLimit = 1000;
        monthlyFee = 10;
        this.setType(AccountType.CHECKING);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String viewAllAccounts(Customer customer){
        return """
                %s            |  %s             |  %s           |  %s           |  %s
                              |  Overdraft Limit: %,.2f         | Monthly Fee: %,.2f
                """.formatted(this.getAccountNumber(), customer.getName(), this.getType().getDescription(), this.getAccountBalance(), this.getAccountStatus(), this.overdraftLimit, this.monthlyFee);
    }

    @Override
    public double withdrawal(double amount) throws IllegalArgumentException{
        if(amount < 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // apply monthly fess before withdrawal
        this.applyMonthlyFee();

        if((this.getAccountBalance() - amount) < -overdraftLimit){
            throw new IllegalArgumentException("You have reach your overdraft limit.");
        }

        if((this.getAccountBalance() - amount) < 0){
            throw new IllegalArgumentException("You have insufficient amount.");
        }

        super.withdrawal(amount);

        return this.getAccountBalance();
    }


    private void applyMonthlyFee(){
        LocalDate currentDate = LocalDate.now();
        long days = ChronoUnit.DAYS.between(createdAt, currentDate);

        if(days >= 30){
            this.setAccountBalance(this.getAccountBalance() - monthlyFee);
        }
    }



    @Override
    public void displayCustomerDetails() {
        Customer customer = this.getCustomer();
        IO.println("""
                    
                    ✔ Account created successfully!
                    Account Number: %s
                    Customer: %s (%s)
                    Account Type: %s
                    Initial Balance: %,.2f
                    Overdraft Limit: %,.2f
                    Monthly Fee: $%,.2f
                    Status: %s
                    """.formatted(
                this.getAccountNumber(),
                customer.getName(),
                customer.getType().getDescription(),
                this.getType().getDescription(),
                this.getAccountBalance(),
                this.overdraftLimit,
                this.monthlyFee,
                this.getAccountStatus()
        ));
    }

}
