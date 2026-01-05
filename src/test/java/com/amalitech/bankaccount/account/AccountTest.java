package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.customer.RegularCustomer;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Customer customer;
    @BeforeEach
    void setUp() {
        try{
            customer  = new RegularCustomer("Gideon Dakore", 23, "+233-559-372538", "Bomso, Kumasi");

        }catch(InputMismatchException e){
            IO.println("Error occurred in the Test class");
            IO.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        IO.println("====================================================================");
    }

    @Test
    void getAccountNumber() {
    }

    @Test
    void getAccountCustomer() {
    }

    @Test
    void getAccountBalance() {
    }

    @Test
    void getAccountStatus() {
    }

    @Test
    void getType() {
    }

    @Test
    void getCustomer() {
    }

    @Test
    void setAccountBalance() {
    }

    @Test
    void setAccountStatus() {
    }

    @Test
    void setType() {
    }

    @Test
    void viewAllAccounts() {
    }

    @Test
    void depositNegativeFund() {
        var acc = new SavingsAccount(customer);
        assertThrows(InvalidAmountException.class, () -> {
            acc.withdrawal(-2);
        });
    }

    @Test
    void depositZeroFund() {
        var acc = new SavingsAccount(customer);
        assertThrows(InvalidAmountException.class, () -> {
            acc.withdrawal(0);
        });
    }

    @Test
    void withdrawalNegativeAmount() {
        var acc = new SavingsAccount(customer);
        assertThrows(InvalidAmountException.class, () -> {
            acc.withdrawal(-2);
        });
    }

    @Test
    void withdrawalInsufficientFunds() {
        var acc = new SavingsAccount(customer);
        assertThrows(InsufficientFundsException.class, () -> {
            acc.withdrawal(20000);
        });
    }
}