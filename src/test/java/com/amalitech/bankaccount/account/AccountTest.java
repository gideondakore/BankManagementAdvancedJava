package com.amalitech.bankaccount.account;

import com.amalitech.bankaccount.utils.IO;

import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.customer.PremiumCustomer;
import com.amalitech.bankaccount.customer.RegularCustomer;
import com.amalitech.bankaccount.enums.AccountType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.exceptions.OverdraftExceededException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Customer customer;
    private Customer premiumCustomer;

    @BeforeEach
    void setUp() {
        try {
            customer = new RegularCustomer("Gideon Dakore", 23, "+233-559-372538", "Bomso, Kumasi", "doe@example.com");
            premiumCustomer = new PremiumCustomer("Jane Smith", 35, "+233-559-123456", "Accra, Ghana", "jansmith@gmail.com");
        } catch (InputMismatchException e) {
            IO.println("Error occurred in the Test class");
            IO.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        IO.println("====================================================================");
    }

    // ==================== SAVINGS ACCOUNT TESTS ====================

    @Nested
    @DisplayName("Savings Account Tests")
    class SavingsAccountTests {

        @Test
        @DisplayName("Should create savings account with correct initial values")
        void createSavingsAccount() {
            SavingsAccount acc = new SavingsAccount(customer);
            
            assertNotNull(acc.getAccountNumber());
            assertTrue(acc.getAccountNumber().startsWith("ACC00"));
            assertEquals(customer, acc.getCustomer());
            assertEquals(0.0, acc.getAccountBalance());
            assertEquals("Active", acc.getAccountStatus());
            assertEquals(AccountType.SAVINGS, acc.getType());
        }

        @Test
        @DisplayName("Should have correct interest rate for savings account")
        void savingsAccountInterestRate() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(0.035, acc.getInterestRate());
        }

        @Test
        @DisplayName("Should have correct minimum balance for savings account")
        void savingsAccountMinimumBalance() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(500, acc.getMinimumBalance());
        }

        @Test
        @DisplayName("Should calculate interest correctly")
        void calculateInterest() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(10000);
            
            double expectedInterest = 10000 * 0.035;
            assertEquals(expectedInterest, acc.calculateInterest(), 0.01);
        }

        @Test
        @DisplayName("Should calculate zero interest when balance is zero")
        void calculateInterestZeroBalance() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(0.0, acc.calculateInterest());
        }

        @Test
        @DisplayName("Should throw InsufficientFundsException when withdrawal would go below minimum balance")
        void withdrawalBelowMinimumBalance() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(1000);
            
            assertThrows(InsufficientFundsException.class, () -> acc.withdrawal(600));
        }

        @Test
        @DisplayName("Should allow withdrawal that maintains minimum balance")
        void withdrawalMaintainsMinimumBalance() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(1000);
            acc.withdrawal(400); // Should leave 600, above 500 minimum
            
            assertEquals(600, acc.getAccountBalance(), 0.01);
        }
    }

    // ==================== CHECKING ACCOUNT TESTS ====================

    @Nested
    @DisplayName("Checking Account Tests")
    class CheckingAccountTests {

        @Test
        @DisplayName("Should create checking account with correct initial values")
        void createCheckingAccount() {
            CheckingAccount acc = new CheckingAccount(customer);
            
            assertNotNull(acc.getAccountNumber());
            assertTrue(acc.getAccountNumber().startsWith("ACC00"));
            assertEquals(customer, acc.getCustomer());
            assertEquals(0.0, acc.getAccountBalance());
            assertEquals("Active", acc.getAccountStatus());
            assertEquals(AccountType.CHECKING, acc.getType());
        }

        @Test
        @DisplayName("Should have correct overdraft limit for checking account")
        void checkingAccountOverdraftLimit() {
            CheckingAccount acc = new CheckingAccount(customer);
            assertEquals(1000, acc.getOverdraftLimit());
        }

        @Test
        @DisplayName("Should have correct monthly fee for checking account")
        void checkingAccountMonthlyFee() {
            CheckingAccount acc = new CheckingAccount(customer);
            assertEquals(10, acc.getMonthlyFee());
        }

        @Test
        @DisplayName("Should have creation timestamp")
        void checkingAccountCreatedAt() {
            CheckingAccount acc = new CheckingAccount(customer);
            assertNotNull(acc.getCreatedAt());
        }

        @Test
        @DisplayName("Should allow overdraft within limit")
        void allowOverdraftWithinLimit() throws InvalidAmountException, OverdraftExceededException {
            CheckingAccount acc = new CheckingAccount(customer);
            acc.deposit(500);
            acc.withdrawal(1000); // Should go to -500, within -1000 limit
            
            assertEquals(-500, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should throw OverdraftExceededException when exceeding overdraft limit")
        void overdraftExceeded() throws InvalidAmountException {
            CheckingAccount acc = new CheckingAccount(customer);
            acc.deposit(100);
            
            assertThrows(OverdraftExceededException.class, () -> acc.withdrawal(1200));
        }
    }

    // ==================== DEPOSIT TESTS ====================

    @Nested
    @DisplayName("Deposit Tests")
    class DepositTests {

        @Test
        @DisplayName("Should deposit valid amount successfully")
        void depositValidAmount() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(1000);
            
            assertEquals(1000, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should allow multiple deposits")
        void multipleDeposits() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(1000);
            acc.deposit(500);
            acc.deposit(250);
            
            assertEquals(1750, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should support method chaining for deposits")
        void depositMethodChaining() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(100).deposit(200).deposit(300);
            
            assertEquals(600, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for negative deposit")
        void depositNegativeFund() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertThrows(InvalidAmountException.class, () -> acc.deposit(-2));
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for zero deposit")
        void depositZeroFund() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertThrows(InvalidAmountException.class, () -> acc.deposit(0));
        }

        @Test
        @DisplayName("Should deposit small decimal amounts correctly")
        void depositDecimalAmount() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(0.01);
            
            assertEquals(0.01, acc.getAccountBalance(), 0.001);
        }

        @Test
        @DisplayName("Should deposit large amounts correctly")
        void depositLargeAmount() throws InvalidAmountException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(1000000.00);
            
            assertEquals(1000000.00, acc.getAccountBalance(), 0.01);
        }
    }

    // ==================== WITHDRAWAL TESTS ====================

    @Nested
    @DisplayName("Withdrawal Tests")
    class WithdrawalTests {

        @Test
        @DisplayName("Should withdraw valid amount successfully")
        void withdrawValidAmount() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(2000);
            acc.withdrawal(500);
            
            assertEquals(1500, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for negative withdrawal")
        void withdrawalNegativeAmount() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertThrows(InvalidAmountException.class, () -> acc.withdrawal(-2));
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for zero withdrawal")
        void withdrawalZeroAmount() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertThrows(InvalidAmountException.class, () -> acc.withdrawal(0));
        }

        @Test
        @DisplayName("Should throw InsufficientFundsException for savings account with insufficient funds")
        void withdrawalInsufficientFunds() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertThrows(InsufficientFundsException.class, () -> acc.withdrawal(20000));
        }

        @Test
        @DisplayName("Should allow multiple withdrawals")
        void multipleWithdrawals() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.deposit(5000);
            acc.withdrawal(1000);
            acc.withdrawal(500);
            acc.withdrawal(200);
            
            assertEquals(3300, acc.getAccountBalance(), 0.01);
        }
    }

    // ==================== ACCOUNT GETTERS AND SETTERS TESTS ====================

    @Nested
    @DisplayName("Account Getters and Setters Tests")
    class GettersSettersTests {

        @Test
        @DisplayName("Should get account number starting with ACC00")
        void getAccountNumber() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertNotNull(acc.getAccountNumber());
            assertTrue(acc.getAccountNumber().startsWith("ACC00"));
        }

        @Test
        @DisplayName("Should get correct customer from account")
        void getAccountCustomer() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(customer, acc.getAccountCustomer());
            assertEquals(customer, acc.getCustomer());
        }

        @Test
        @DisplayName("Should get initial balance as zero")
        void getAccountBalance() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(0.0, acc.getAccountBalance());
        }

        @Test
        @DisplayName("Should get default status as Active")
        void getAccountStatus() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals("Active", acc.getAccountStatus());
        }

        @Test
        @DisplayName("Should get correct account type for savings")
        void getTypeSavings() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(AccountType.SAVINGS, acc.getType());
        }

        @Test
        @DisplayName("Should get correct account type for checking")
        void getTypeChecking() {
            CheckingAccount acc = new CheckingAccount(customer);
            assertEquals(AccountType.CHECKING, acc.getType());
        }

        @Test
        @DisplayName("Should set account balance correctly")
        void setAccountBalance() {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.setAccountBalance(5000.50);
            assertEquals(5000.50, acc.getAccountBalance(), 0.01);
        }

        @Test
        @DisplayName("Should set account status correctly")
        void setAccountStatus() {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.setAccountStatus("Inactive");
            assertEquals("Inactive", acc.getAccountStatus());
        }

        @Test
        @DisplayName("Should set account type correctly")
        void setType() {
            SavingsAccount acc = new SavingsAccount(customer);
            acc.setType(AccountType.CHECKING);
            assertEquals(AccountType.CHECKING, acc.getType());
        }
    }

    // ==================== VIEW ALL ACCOUNTS TESTS ====================

    @Nested
    @DisplayName("View All Accounts Tests")
    class ViewAllAccountsTests {

        @Test
        @DisplayName("Should return formatted string for savings account view")
        void viewAllAccountsSavings() {
            SavingsAccount acc = new SavingsAccount(customer);
            String view = acc.viewAllAccounts(customer);
            
            assertNotNull(view);
            assertTrue(view.contains(acc.getAccountNumber()));
            assertTrue(view.contains(customer.getName()));
            assertTrue(view.contains("Interest Rate"));
            assertTrue(view.contains("Min Balance"));
        }

        @Test
        @DisplayName("Should return formatted string for checking account view")
        void viewAllAccountsChecking() {
            CheckingAccount acc = new CheckingAccount(customer);
            String view = acc.viewAllAccounts(customer);
            
            assertNotNull(view);
            assertTrue(view.contains(acc.getAccountNumber()));
            assertTrue(view.contains(customer.getName()));
            assertTrue(view.contains("Overdraft Limit"));
            assertTrue(view.contains("Monthly Fee"));
        }
    }

    // ==================== ACCOUNT WITH PREMIUM CUSTOMER TESTS ====================

    @Nested
    @DisplayName("Account with Premium Customer Tests")
    class PremiumCustomerAccountTests {

        @Test
        @DisplayName("Should create savings account for premium customer")
        void savingsAccountWithPremiumCustomer() {
            SavingsAccount acc = new SavingsAccount(premiumCustomer);
            
            assertEquals(premiumCustomer, acc.getCustomer());
            assertEquals("Premium", acc.getCustomer().getType().getDescription());
        }

        @Test
        @DisplayName("Should create checking account for premium customer")
        void checkingAccountWithPremiumCustomer() {
            CheckingAccount acc = new CheckingAccount(premiumCustomer);
            
            assertEquals(premiumCustomer, acc.getCustomer());
            assertEquals("Premium", acc.getCustomer().getType().getDescription());
        }
    }

    // ==================== TO STRING TESTS ====================

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return account number as toString")
        void toStringReturnsAccountNumber() {
            SavingsAccount acc = new SavingsAccount(customer);
            assertEquals(acc.getAccountNumber(), acc.toString());
        }
    }
}