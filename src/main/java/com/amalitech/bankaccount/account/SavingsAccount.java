package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;

public class SavingsAccount extends Account{
    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(Customer customer){
        super(customer);
        this.interestRate = 0.035;
        this.minimumBalance = 500;
        this.setType(AccountType.SAVINGS);
    }

    public double calculateInterest(){
        return this.getAccountBalance() * interestRate;
    }

    public double getInterestRate(){
        return interestRate;
    }

    public double getMinimumBalance(){
        return minimumBalance;
    }

    public String viewAllAccounts(Customer customer){
        return """
                %s            |  %s             |  %s           |  %s           |  %s
                              |  Interest Rate: %,.1f%%          | Min Balance: %,.2f
                """.formatted(this.getAccountNumber(), customer.getName(), this.getType().getDescription(), this.getAccountBalance(), this.getAccountStatus(), this.interestRate * 100, this.minimumBalance);
    }

    @Override
    public void withdrawal(double amount) throws IllegalArgumentException{
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if((this.getAccountBalance() - amount) < 0 || (this.getAccountBalance() - amount) < minimumBalance){
            throw new IllegalArgumentException("You have insufficient amount.");
        }

        super.withdrawal(amount);

    }

    @Override
    public void deposit(double amount) throws IllegalArgumentException{
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        super.deposit(amount);
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
                    Interest Rate: %,.2f%%
                    Minimum Balance: $%,.2f
                    Status: %s
                    """.formatted(
                        this.getAccountNumber(),
                        customer.getName(),
                        customer.getType().getDescription(),
                        this.getType().getDescription(),
                        this.getAccountBalance(),
                        this.interestRate * 100,
                        this.minimumBalance,
                        this.getAccountStatus()
                ));
        }

}
