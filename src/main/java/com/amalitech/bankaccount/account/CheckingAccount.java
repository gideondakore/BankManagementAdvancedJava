package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CheckingAccount extends Account{
    private final double overdraftLimit;
    private final double monthlyFee;
    private final LocalDate createdAt = LocalDate.now();


    /**
     * Checking account class constructor method
     * @param customer
     */
    public CheckingAccount(Customer customer){
        super(customer);
        overdraftLimit = 1000;
        monthlyFee = 10;
        this.setType(AccountType.CHECKING);

    }

    /**
     * Overdraft limit is a constant
     * @return Returns overdraft limit
     */
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     *
     * @return Returns monthly fee
     */
    public double getMonthlyFee() {
        return monthlyFee;
    }

    /**
     *
     * @return Returns the timestamp for the creation of the account
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String viewAllAccounts(Customer customer){
        return """
                %-8s            |  %-25s             |  %-8s           |  $%,-5.2f           |  %-5s
                %-8s            |  Overdraft Limit: $%-8.2f %-10s |  Monthly Fee: $%-9.2f
                """.formatted(this.getAccountNumber(), customer.getName(), this.getType().getDescription(), this.getAccountBalance(), this.getAccountStatus(), "", this.overdraftLimit, "", this.monthlyFee);
    }

    @Override
    public void withdrawal(double amount) throws IllegalArgumentException{
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // apply monthly fess before withdrawal
        this.applyMonthlyFee();

        if((this.getAccountBalance() - amount) < -overdraftLimit){
            throw new IllegalArgumentException("The transaction will exceed your overdraft limit of $" + overdraftLimit);
        }

        super.withdrawal(amount);
    }


    private void applyMonthlyFee(){
        LocalDate currentDate = LocalDate.now();
        long days = ChronoUnit.DAYS.between(createdAt, currentDate);

        if(days >= 30){
            this.setAccountBalance(this.getAccountBalance() - monthlyFee);
        }
    }




    @Override
    public void displayAccountDetails() {
        Customer customer = this.getCustomer();
        IO.println("""
                    
                    ✔ Account created successfully!
                    Account Number: %s
                    Customer: %s (%s)
                    Account Type: %s
                    Initial Balance: $%,.2f
                    Overdraft Limit: $%,.2f
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
