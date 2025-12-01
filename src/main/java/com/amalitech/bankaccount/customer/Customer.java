package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.interfaces.DisplayCustomerDetailsAndGetCustomerType;

public abstract class Customer implements DisplayCustomerDetailsAndGetCustomerType {
    // instance variable
    private String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;
    static int customerCounter;
    private CustomerType type;


    protected Customer(String name, int age, String contact, String address){
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        ++customerCounter;
        this.customerId = String.valueOf(customerCounter);
    }



    // Getters
    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public int getAge(){
        return age;
    }

    public CustomerType getType(){
        return this.type;
    }

    // Setters
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String name) {
        this.name = name;
    }

    public void setCustomerContact(String contact) {
        this.contact = contact;
    }

    public void setCustomerAddress(String address) {
        this.address = address;
    }

    public void setCustomerAge(int age) {
        this.age = age;
    }

    public void setType(CustomerType customerType){
        this.type = customerType;
    }

    @Override
    public String toString(){
        return this.customerId;
    }
}




