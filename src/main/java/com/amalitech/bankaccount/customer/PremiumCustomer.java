package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;

public class PremiumCustomer extends Customer{
    private double minimumBalance = 10000;
    static final String CUSTOMER_TYPE = CustomerType.PREMIUM.getDescription();

    /**
     * Premium Customer constructor
     * @param name
     * @param age
     * @param contact
     * @param address
     */
    public PremiumCustomer(String name, int age, String contact, String address) throws InputMismatchException {
        super(name, age, contact, address);
        this.setType(CustomerType.PREMIUM);
    }


    /**
     *
     * @return Minimum balance
     */
    public double getMinimumBalance(){
        return minimumBalance;
    }


    /**
     * For setting minimum balance
     * @param minimumBalance
     */
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    /**
     *
     * @return boolean indicating whether customer has waived fees
     */
    public boolean hasWaivedFees(){
        return true;
    }

    @Override
    public void displayCustomerDetails() {
        String customerDetails = "Account Number: " + this.getCustomerId() + "\n" + "Customer: " + this.getName() + " (" + CUSTOMER_TYPE + ")";
        IO.println(customerDetails);
    }

}
