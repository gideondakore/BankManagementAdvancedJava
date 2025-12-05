package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;

import java.text.DecimalFormat;

/**
 * Savings account class
 */
public class SavingsAccount extends Account{
    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(Customer customer){
        super(customer);
        this.interestRate = 0.035;
        this.minimumBalance = 500;
        this.setType(AccountType.SAVINGS);
    }

    /**
     *
     * @return Returns calculated interest amount
     */
    public double calculateInterest(){
        return this.getAccountBalance() * interestRate;
    }

    /**
     *
     * @return Returns the constant interest rate value
     */
    public double getInterestRate(){
        return interestRate;
    }

    /**
     *
     * @return Returns minimum balance
     */
    public double getMinimumBalance(){
        return minimumBalance;
    }

    public String viewAllAccounts(Customer customer){
        return """
                %-8s            |  %-25s             |  %-8s           |  $%,-5.2f           |  %-5s
                %-8s            | Interest Rate: %.1f%% %-18s |  Min Balance: $%.2f
                """.formatted(this.getAccountNumber(), customer.getName(), this.getType().getDescription(), this.getAccountBalance(), this.getAccountStatus(), "", this.interestRate * 100, "", this.minimumBalance);
    }

    @Override
    public void withdrawal(double amount) throws InvalidAmountException, InsufficientFundsException {
        if(amount <= 0){
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if((this.getAccountBalance() - amount) < 0 || (this.getAccountBalance() - amount) < minimumBalance){
            DecimalFormat df =  new DecimalFormat("#,###.00");
            throw new InsufficientFundsException("❌ Transaction Failed: Insufficient funds. Current balance: $" + df.format(this.getAccountBalance()));
        }

        super.withdrawal(amount);

    }

    @Override
    public Account deposit(double amount) throws IllegalArgumentException{
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        super.deposit(amount);

        return this;
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
