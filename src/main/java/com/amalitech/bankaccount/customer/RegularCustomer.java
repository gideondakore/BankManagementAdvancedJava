package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.utils.IO;


import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;

public class RegularCustomer extends Customer{
    static final String CUSTOMER_TYPE;

    static {
        CUSTOMER_TYPE = CustomerType.REGULAR.getDescription();
    }

    /**
     * Regular Customer constructor class
     * @param name
     * @param age
     * @param contact
     * @param address
     * @param email
     */
    public RegularCustomer(String name, int age, String contact, String address, String email) throws InputMismatchException {
        super(name, age, contact, address, email);
        this.setType(CustomerType.REGULAR);
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
        String customerDetails = "Account Number: " + this.getCustomerId() + "\n" + "Customer: " + this.getName()  + " (" + CUSTOMER_TYPE + ")";
        IO.println(customerDetails);
    }


}