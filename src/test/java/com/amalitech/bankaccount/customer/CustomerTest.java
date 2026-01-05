package com.amalitech.bankaccount.customer;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.enums.CustomerType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    Customer regularCustomer;
    PremiumCustomer premiumCustomer;

    @BeforeEach
    void setUp() {
        try {
            regularCustomer = new RegularCustomer("Mark Anthony", 23, "+233-559-372538", "Bomso");
            premiumCustomer = new PremiumCustomer("Jane Doe", 35, "+233-559-123456", "Accra, Ghana");
        } catch (InputMismatchException e) {
            IO.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        IO.println("All test run successfully...");
    }

    // ==================== REGULAR CUSTOMER TESTS ====================

    @Nested
    @DisplayName("Regular Customer Tests")
    class RegularCustomerTests {

        @Test
        @DisplayName("Should create regular customer with valid inputs")
        void createRegularCustomer() throws InputMismatchException {
            Customer customer = new RegularCustomer("John Smith", 30, "+233-559-111222", "Kumasi, Ghana");
            
            assertNotNull(customer);
            assertEquals("John Smith", customer.getName());
            assertEquals(30, customer.getAge());
            assertEquals("+233-559-111222", customer.getContact());
            assertEquals("Kumasi, Ghana", customer.getAddress());
        }

        @Test
        @DisplayName("Should set customer type as REGULAR")
        void regularCustomerType() {
            assertEquals(CustomerType.REGULAR, regularCustomer.getType());
            assertEquals("Regular", regularCustomer.getType().getDescription());
        }

        @Test
        @DisplayName("Regular customer should have waived fees")
        void regularCustomerHasWaivedFees() {
            assertTrue(((RegularCustomer) regularCustomer).hasWaivedFees());
        }

        @Test
        @DisplayName("Should generate customer ID starting with CUS00")
        void getCustomerId() {
            assertNotNull(regularCustomer.getCustomerId());
            assertTrue(regularCustomer.getCustomerId().startsWith("CUS00"));
        }
    }

    // ==================== PREMIUM CUSTOMER TESTS ====================

    @Nested
    @DisplayName("Premium Customer Tests")
    class PremiumCustomerTests {

        @Test
        @DisplayName("Should create premium customer with valid inputs")
        void createPremiumCustomer() throws InputMismatchException {
            PremiumCustomer customer = new PremiumCustomer("Alice Wonder", 45, "+233-559-999888", "Lagos, Nigeria");
            
            assertNotNull(customer);
            assertEquals("Alice Wonder", customer.getName());
            assertEquals(45, customer.getAge());
        }

        @Test
        @DisplayName("Should set customer type as PREMIUM")
        void premiumCustomerType() {
            assertEquals(CustomerType.PREMIUM, premiumCustomer.getType());
            assertEquals("Premium", premiumCustomer.getType().getDescription());
        }

        @Test
        @DisplayName("Premium customer should have waived fees")
        void premiumCustomerHasWaivedFees() {
            assertTrue(premiumCustomer.hasWaivedFees());
        }

        @Test
        @DisplayName("Premium customer should have minimum balance of 10000")
        void premiumCustomerMinimumBalance() {
            assertEquals(10000, premiumCustomer.getMinimumBalance());
        }

        @Test
        @DisplayName("Should be able to set minimum balance for premium customer")
        void setMinimumBalance() {
            premiumCustomer.setMinimumBalance(15000);
            assertEquals(15000, premiumCustomer.getMinimumBalance());
        }
    }

    // ==================== VALIDATION TESTS ====================

    @Nested
    @DisplayName("Input Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw InputMismatchException for invalid name format")
        void invalidNameFormat() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("john", 25, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for single word name")
        void singleWordName() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John", 25, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for name with numbers")
        void nameWithNumbers() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John123 Smith", 25, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for invalid age - negative")
        void invalidAgeNegative() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John Smith", -5, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for invalid age - zero")
        void invalidAgeZero() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John Smith", 0, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for age over 120")
        void invalidAgeOver120() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John Smith", 121, "+233-559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should accept valid age of 120")
        void validAgeMax() throws InputMismatchException {
            Customer customer = new RegularCustomer("John Smith", 120, "+233-559-372538", "Bomso");
            assertEquals(120, customer.getAge());
        }

        @Test
        @DisplayName("Should throw InputMismatchException for invalid phone format")
        void invalidPhoneFormat() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John Smith", 25, "0559372538", "Bomso"));
        }

        @Test
        @DisplayName("Should throw InputMismatchException for phone without country code")
        void phoneWithoutCountryCode() {
            assertThrows(InputMismatchException.class, () -> 
                new RegularCustomer("John Smith", 25, "559-372538", "Bomso"));
        }

        @Test
        @DisplayName("Should accept valid international phone format")
        void validInternationalPhone() throws InputMismatchException {
            Customer customer = new RegularCustomer("John Smith", 25, "+1-555-1234567", "New York");
            assertEquals("+1-555-1234567", customer.getContact());
        }
    }

    // ==================== GETTERS TESTS ====================

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get customer name")
        void getName() {
            assertEquals("Mark Anthony", regularCustomer.getName());
        }

        @Test
        @DisplayName("Should get customer ID")
        void getCustomerId() {
            assertNotNull(regularCustomer.getCustomerId());
            assertTrue(regularCustomer.getCustomerId().startsWith("CUS00"));
        }

        @Test
        @DisplayName("Should get customer contact")
        void getContact() {
            assertEquals("+233-559-372538", regularCustomer.getContact());
        }

        @Test
        @DisplayName("Should get customer address")
        void getAddress() {
            assertEquals("Bomso", regularCustomer.getAddress());
        }

        @Test
        @DisplayName("Should get customer age")
        void getAge() {
            assertEquals(23, regularCustomer.getAge());
        }

        @Test
        @DisplayName("Should get customer type")
        void getType() {
            assertEquals(CustomerType.REGULAR, regularCustomer.getType());
            assertEquals(CustomerType.PREMIUM, premiumCustomer.getType());
        }
    }

    // ==================== SETTERS TESTS ====================

    @Nested
    @DisplayName("Setter Methods Tests")
    class SetterTests {

        @Test
        @DisplayName("Should set customer ID")
        void setCustomerId() {
            regularCustomer.setCustomerId("CUS00999");
            assertEquals("CUS00999", regularCustomer.getCustomerId());
        }

        @Test
        @DisplayName("Should set customer name")
        void setCustomerName() {
            regularCustomer.setCustomerName("New Name Here");
            assertEquals("New Name Here", regularCustomer.getName());
        }

        @Test
        @DisplayName("Should set customer contact")
        void setCustomerContact() {
            regularCustomer.setCustomerContact("+1-555-9876543");
            assertEquals("+1-555-9876543", regularCustomer.getContact());
        }

        @Test
        @DisplayName("Should set customer address")
        void setCustomerAddress() {
            regularCustomer.setCustomerAddress("New Address, City");
            assertEquals("New Address, City", regularCustomer.getAddress());
        }

        @Test
        @DisplayName("Should set customer age")
        void setCustomerAge() {
            regularCustomer.setCustomerAge(30);
            assertEquals(30, regularCustomer.getAge());
        }

        @Test
        @DisplayName("Should set customer type")
        void setType() {
            regularCustomer.setType(CustomerType.PREMIUM);
            assertEquals(CustomerType.PREMIUM, regularCustomer.getType());
        }
    }

    // ==================== TO STRING TESTS ====================

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should return customer ID")
        void testToString() {
            String customerId = regularCustomer.getCustomerId();
            assertEquals(customerId, regularCustomer.toString());
        }

        @Test
        @DisplayName("Premium customer toString should return customer ID")
        void testPremiumToString() {
            String customerId = premiumCustomer.getCustomerId();
            assertEquals(customerId, premiumCustomer.toString());
        }
    }

    // ==================== DISPLAY CUSTOMER DETAILS TESTS ====================

    @Nested
    @DisplayName("Display Customer Details Tests")
    class DisplayDetailsTests {

        @Test
        @DisplayName("Regular customer displayCustomerDetails should not throw exception")
        void regularCustomerDisplayDetails() {
            assertDoesNotThrow(() -> ((RegularCustomer) regularCustomer).displayCustomerDetails());
        }

        @Test
        @DisplayName("Premium customer displayCustomerDetails should not throw exception")
        void premiumCustomerDisplayDetails() {
            assertDoesNotThrow(() -> premiumCustomer.displayCustomerDetails());
        }
    }

    // ==================== CUSTOMER TYPE ENUM TESTS ====================

    @Nested
    @DisplayName("Customer Type Enum Tests")
    class CustomerTypeEnumTests {

        @Test
        @DisplayName("CustomerType REGULAR should have description 'Regular'")
        void regularTypeDescription() {
            assertEquals("Regular", CustomerType.REGULAR.getDescription());
        }

        @Test
        @DisplayName("CustomerType PREMIUM should have description 'Premium'")
        void premiumTypeDescription() {
            assertEquals("Premium", CustomerType.PREMIUM.getDescription());
        }

        @Test
        @DisplayName("Should have exactly two customer types")
        void customerTypeCount() {
            assertEquals(2, CustomerType.values().length);
        }
    }
}