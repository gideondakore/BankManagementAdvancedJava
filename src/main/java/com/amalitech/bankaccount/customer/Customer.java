package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.interfaces.DisplayCustomerDetails;
import com.amalitech.bankaccount.utils.AppConstants;

import java.util.regex.Pattern;

public abstract class Customer implements DisplayCustomerDetails {
    // instance variable
    private String customerId;
    private String name;
    private int age;
    private String contact;
    private String address;
    static int customerCounter;
    private CustomerType type;
    private String email;


    /**
     * Abstract constructor method of Customer class
     * @param name
     * @param age
     * @param contact
     * @param address
     * @param email
     */
    protected Customer(String name, int age, String contact, String address, String email) throws InputMismatchException {
        this.validateInputs(name, age, contact, address);
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.email = email;
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

    public String getEmail(){
        return email;
    }

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

    private void validateInputs(String name, int age, String contact, String address) throws InputMismatchException {
        Pattern p = Pattern.compile(AppConstants.AGE_REGEX);

        if(!name.matches(AppConstants.NAME_REGEX)){
            throw new InputMismatchException("""
                    Please provide valid name!
                
                    //Example Valid Names:
                    "Gideon Dakore"   ✓
                    "Mary Jane Smith" ✓
                    "Susana-Lawrence Van Damme" ✓
                    Herman Melville ✓
                    "A. A. Milne" ✓
                    "Abraham Van Helsing" ✓
                    "Mathis d'Arias" ✓
                    "Martin Luther King, Jr." ✓
                    "Tony Montana Prez Rodriguez DeJesus del Rosario Mercedes Pilar Martínez Molina Baeza" ✓
                    
                    YOU PROVIDED THE NAME '%s' WHICH IS INVALID!
                    """.formatted(name));
        }

        if(!p.matcher(String.valueOf(age)).matches()){
            throw new InputMismatchException("""
                    Please provide a valid age (1-120)!
                    
                    YOU PROVIDED THE AGE '%d' WHICH IS INVALID!
                    """.formatted(age));
        }

        if(!contact.matches(AppConstants.PHONE_NUMBER_REGEX)){
            throw new InputMismatchException(String.format("""
                    Please provide valid phone number!
                    
                    ✅ Example Valid Numbers.
                    +233-559-372538
                    +1-415-7829364
                    +44-203-92847125
                    +91-222-4839201
                    +234-803-729183746
                    +81-120-583920
                    +49-301-74829365
                    +61-420-593847
                    +33-501-748291
                    +55-119-4829357201
                    +27-101-69284735
                    
                    YOU PROVIDED THE PHONE NUMBER '%s' WHICH IS INVALID!
                    """.formatted(contact)));
        }

        if(!address.matches(AppConstants.CONTACT_ADDRESS)){
            throw new InputMismatchException("""
                Please provide a valid address!
                
                // Examples that match:
                // "123 Main Street"
                // "45 Oak Ave., Apt. 2B"
                // "12-34 Park Lane"
                // "P.O. Box 456"
                
                YOU PROVIDED THE ADDRESS '%s' WHICH IS INVALID!
                """.formatted(address));
        }
    }
}




