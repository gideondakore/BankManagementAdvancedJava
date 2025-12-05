package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.interfaces.DisplayCustomerDetails;

public abstract class Customer implements DisplayCustomerDetails {
    // instance variable
    private String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;
    static int customerCounter;
    private CustomerType type;


    /**
     * Abstract constructor method of Customer class
     * @param name
     * @param age
     * @param contact
     * @param address
     */
    protected Customer(String name, int age, String contact, String address){
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        ++customerCounter;
        this.customerId = "CUS00" + customerCounter;
    }



    // Getters

    /**
     * For getting customer name
     * @return Get customer name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Returns customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     *
     * @return Returns customer contact
     */
    public String getContact() {
        return contact;
    }

    /**
     *
     * @return Returns customer address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return Returns customer age
     */
    public int getAge(){
        return age;
    }

    /**
     *
     * @return Returns Customer type (i.e Premium or Regular)
     */
    public CustomerType getType(){
        return this.type;
    }

    // Setters

    /**
     * For setting customer id
     * @param customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * For setting customer name
     * @param name
     */
    public void setCustomerName(String name) {
        this.name = name;
    }

    /**
     * For setting customer contact
     * @param contact
     */
    public void setCustomerContact(String contact) {
        this.contact = contact;
    }

    /**
     * For setting customer address
     * @param address
     */
    public void setCustomerAddress(String address) {
        this.address = address;
    }

    /**
     * For setting customer age
     * @param age
     */
    public void setCustomerAge(int age) {
        this.age = age;
    }

    /**
     * For setting Customer type (i.e Premium or Regular). Type must be <b><u>CustomerType</u></b> enum
     * @param customerType
     */
    public void setType(CustomerType customerType){
        this.type = customerType;
    }

    @Override
    public String toString(){
        return this.customerId;
    }
}




