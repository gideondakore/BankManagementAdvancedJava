package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.exceptions.InputMismatchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    Customer customer;

    @BeforeEach
    void setUp() {
        try{
            customer = new RegularCustomer("Mark Anthony", 23, "+233-559-372538", "Bomso");

        } catch (InputMismatchException e) {
            IO.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        IO.println("All test run successfully...");
    }

    @Test
    void getName() {
    }

    @Test
    void getCustomerId() {
        // Customer ID should start with "CUS00" followed by a number
        assertNotNull(customer.getCustomerId());
        assertTrue(customer.getCustomerId().startsWith("CUS00"), "Customer ID should start with CUS00");
    }

    @Test
    void getContact() {
    }

    @Test
    void getAddress() {
    }

    @Test
    void getAge() {
    }

    @Test
    void getType() {
    }

    @Test
    void setCustomerId() {
    }

    @Test
    void setCustomerName() {
    }

    @Test
    void setCustomerContact() {
    }

    @Test
    void setCustomerAddress() {
    }

    @Test
    void setCustomerAge() {
    }

    @Test
    void setType() {
    }

    @Test
    void testToString() {
    }
}