package com.amalitech.bankaccount.transaction;

import com.amalitech.bankaccount.account.CheckingAccount;
import com.amalitech.bankaccount.account.SavingsAccount;
import com.amalitech.bankaccount.customer.Customer;
import com.amalitech.bankaccount.customer.RegularCustomer;
import com.amalitech.bankaccount.enums.TransactionType;
import com.amalitech.bankaccount.enums.TransferToOrFromType;
import com.amalitech.bankaccount.exceptions.InputMismatchException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.utils.IO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Customer customer;
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private TransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        try {
            customer = new RegularCustomer("John Doe", 30, "+233-559-123456", "Accra, Ghana");
            savingsAccount = new SavingsAccount(customer);
            checkingAccount = new CheckingAccount(customer);
            transactionManager = new TransactionManager();
        } catch (InputMismatchException e) {
            IO.println("Error setting up tests: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        IO.println("Transaction test completed.");
    }

    // ==================== TRANSACTION CREATION TESTS ====================

    @Nested
    @DisplayName("Transaction Creation Tests")
    class TransactionCreationTests {

        @Test
        @DisplayName("Should create transaction with correct values")
        void createTransaction() {
            Transaction transaction = new Transaction("ACC001", 1000.0, 1000.0);
            
            assertNotNull(transaction);
            assertEquals("ACC001", transaction.getAccountNumber());
            assertEquals(1000.0, transaction.getAmount());
            assertEquals(1000.0, transaction.getBalanceAfter());
        }

        @Test
        @DisplayName("Should generate transaction ID starting with TXN00")
        void transactionIdFormat() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            
            assertNotNull(transaction.getTransactionId());
            assertTrue(transaction.getTransactionId().startsWith("TXN00"));
        }

        @Test
        @DisplayName("Should generate timestamp on creation")
        void transactionTimestamp() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            
            assertNotNull(transaction.getTimestamp());
            assertFalse(transaction.getTimestamp().isEmpty());
        }

        @Test
        @DisplayName("Should create transaction with no-arg constructor")
        void noArgConstructor() {
            Transaction transaction = new Transaction();
            
            assertNotNull(transaction);
            assertNull(transaction.getAccountNumber());
            assertNull(transaction.getTransactionId());
        }
    }

    // ==================== TRANSACTION SETTERS TESTS ====================

    @Nested
    @DisplayName("Transaction Setter Tests")
    class TransactionSetterTests {

        @Test
        @DisplayName("Should set transaction type")
        void setTransactionType() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            transaction.setType(TransactionType.DEPOSIT.getDescription());
            
            assertEquals("Deposit", transaction.getType());
        }

        @Test
        @DisplayName("Should set withdrawal type")
        void setWithdrawalType() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            transaction.setType(TransactionType.WITHDRAWAL.getDescription());
            
            assertEquals("Withdrawal", transaction.getType());
        }

        @Test
        @DisplayName("Should set transfer type")
        void setTransferType() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            transaction.setType(TransactionType.TRANSFER.getDescription());
            
            assertEquals("Transfer", transaction.getType());
        }

        @Test
        @DisplayName("Should set transfer direction to FROM")
        void setTransferFromDirection() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            transaction.setTransferToOrFrom(TransferToOrFromType.FROM);
            
            assertEquals(TransferToOrFromType.FROM, transaction.getTransferToOrFrom());
        }

        @Test
        @DisplayName("Should set transfer direction to TO")
        void setTransferToDirection() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            transaction.setTransferToOrFrom(TransferToOrFromType.TO);
            
            assertEquals(TransferToOrFromType.TO, transaction.getTransferToOrFrom());
        }
    }

    // ==================== TRANSACTION TIMESTAMP PARSING TESTS ====================

    @Nested
    @DisplayName("Timestamp Parsing Tests")
    class TimestampParsingTests {

        @Test
        @DisplayName("Should parse timestamp to LocalDateTime")
        void parseTimestamp() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            LocalDateTime parsedTime = transaction.parseTimeStamp();
            
            assertNotNull(parsedTime);
        }

        @Test
        @DisplayName("Parsed timestamp should be close to current time")
        void parsedTimestampIsRecent() {
            Transaction transaction = new Transaction("ACC001", 500.0, 500.0);
            LocalDateTime parsedTime = transaction.parseTimeStamp();
            LocalDateTime now = LocalDateTime.now();
            
            // The parsed time should be within the last minute
            assertTrue(parsedTime.isBefore(now.plusMinutes(1)));
            assertTrue(parsedTime.isAfter(now.minusMinutes(1)));
        }
    }

    // ==================== TRANSACTION MANAGER TESTS ====================

    @Nested
    @DisplayName("Transaction Manager Tests")
    class TransactionManagerTests {

        @Test
        @DisplayName("Should add transaction to manager")
        void addTransaction() {
            Transaction transaction = new Transaction(savingsAccount.getAccountNumber(), 1000.0, 1000.0);
            transaction.setType(TransactionType.DEPOSIT.getDescription());
            transactionManager.addTransaction(transaction);
            
            assertEquals(1, transactionManager.getTransactions().size());
        }

        @Test
        @DisplayName("Should add multiple transactions")
        void addMultipleTransactions() {
            Transaction t1 = new Transaction(savingsAccount.getAccountNumber(), 1000.0, 1000.0);
            Transaction t2 = new Transaction(savingsAccount.getAccountNumber(), 500.0, 1500.0);
            Transaction t3 = new Transaction(savingsAccount.getAccountNumber(), 200.0, 1300.0);
            
            t1.setType(TransactionType.DEPOSIT.getDescription());
            t2.setType(TransactionType.DEPOSIT.getDescription());
            t3.setType(TransactionType.WITHDRAWAL.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            transactionManager.addTransaction(t3);
            
            assertEquals(3, transactionManager.getTransactions().size());
        }

        @Test
        @DisplayName("Should get empty list when no transactions")
        void getTransactionsEmpty() {
            assertTrue(transactionManager.getTransactions().isEmpty());
        }

        @Test
        @DisplayName("Should get all transactions for specific account")
        void getAllTransactionsForAccount() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 1000.0, 1000.0);
            Transaction t2 = new Transaction(accNumber, 500.0, 1500.0);
            Transaction t3 = new Transaction("OTHER_ACC", 200.0, 200.0);
            
            t1.setType(TransactionType.DEPOSIT.getDescription());
            t2.setType(TransactionType.DEPOSIT.getDescription());
            t3.setType(TransactionType.DEPOSIT.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            transactionManager.addTransaction(t3);
            
            List<Transaction> accountTransactions = TransactionManager.getAllTransactions(accNumber, transactionManager.getTransactions());
            
            assertEquals(2, accountTransactions.size());
        }

        @Test
        @DisplayName("Should return empty list for account with no transactions")
        void getAllTransactionsForNonExistentAccount() {
            Transaction t1 = new Transaction(savingsAccount.getAccountNumber(), 1000.0, 1000.0);
            t1.setType(TransactionType.DEPOSIT.getDescription());
            transactionManager.addTransaction(t1);
            
            List<Transaction> accountTransactions = TransactionManager.getAllTransactions("NON_EXISTENT", transactionManager.getTransactions());
            
            assertTrue(accountTransactions.isEmpty());
        }
    }

    // ==================== CALCULATE TOTAL DEPOSITS TESTS ====================

    @Nested
    @DisplayName("Calculate Total Deposits Tests")
    class CalculateTotalDepositsTests {

        @Test
        @DisplayName("Should calculate total deposits correctly")
        void calculateTotalDeposits() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 1000.0, 1000.0);
            Transaction t2 = new Transaction(accNumber, 500.0, 1500.0);
            
            t1.setType(TransactionType.DEPOSIT.getDescription());
            t2.setType(TransactionType.DEPOSIT.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            
            double totalDeposits = transactionManager.calculateTotalDeposits(accNumber);
            
            assertEquals(2500.0, totalDeposits, 0.01); // Sum of balanceAfter values
        }

        @Test
        @DisplayName("Should return zero when no deposits")
        void calculateTotalDepositsNoDeposits() {
            double totalDeposits = transactionManager.calculateTotalDeposits(savingsAccount.getAccountNumber());
            
            assertEquals(0.0, totalDeposits);
        }

        @Test
        @DisplayName("Should exclude withdrawals from deposit calculation")
        void calculateTotalDepositsExcludesWithdrawals() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 1000.0, 1000.0);
            Transaction t2 = new Transaction(accNumber, 500.0, 500.0);
            
            t1.setType(TransactionType.DEPOSIT.getDescription());
            t2.setType(TransactionType.WITHDRAWAL.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            
            double totalDeposits = transactionManager.calculateTotalDeposits(accNumber);
            
            assertEquals(1000.0, totalDeposits, 0.01);
        }
    }

    // ==================== CALCULATE TOTAL WITHDRAWALS TESTS ====================

    @Nested
    @DisplayName("Calculate Total Withdrawals Tests")
    class CalculateTotalWithdrawalsTests {

        @Test
        @DisplayName("Should calculate total withdrawals correctly")
        void calculateTotalWithdrawals() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 500.0, 500.0);
            Transaction t2 = new Transaction(accNumber, 200.0, 300.0);
            
            t1.setType(TransactionType.WITHDRAWAL.getDescription());
            t2.setType(TransactionType.WITHDRAWAL.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            
            double totalWithdrawals = transactionManager.calculateTotalWithdrawals(accNumber);
            
            assertEquals(800.0, totalWithdrawals, 0.01); // Sum of balanceAfter values
        }

        @Test
        @DisplayName("Should return zero when no withdrawals")
        void calculateTotalWithdrawalsNoWithdrawals() {
            double totalWithdrawals = transactionManager.calculateTotalWithdrawals(savingsAccount.getAccountNumber());
            
            assertEquals(0.0, totalWithdrawals);
        }

        @Test
        @DisplayName("Should exclude deposits from withdrawal calculation")
        void calculateTotalWithdrawalsExcludesDeposits() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 1000.0, 1000.0);
            Transaction t2 = new Transaction(accNumber, 500.0, 500.0);
            
            t1.setType(TransactionType.DEPOSIT.getDescription());
            t2.setType(TransactionType.WITHDRAWAL.getDescription());
            
            transactionManager.addTransaction(t1);
            transactionManager.addTransaction(t2);
            
            double totalWithdrawals = transactionManager.calculateTotalWithdrawals(accNumber);
            
            assertEquals(500.0, totalWithdrawals, 0.01);
        }
    }

    // ==================== TRANSACTION TYPE ENUM TESTS ====================

    @Nested
    @DisplayName("Transaction Type Enum Tests")
    class TransactionTypeEnumTests {

        @Test
        @DisplayName("DEPOSIT should have description 'Deposit'")
        void depositDescription() {
            assertEquals("Deposit", TransactionType.DEPOSIT.getDescription());
        }

        @Test
        @DisplayName("WITHDRAWAL should have description 'Withdrawal'")
        void withdrawalDescription() {
            assertEquals("Withdrawal", TransactionType.WITHDRAWAL.getDescription());
        }

        @Test
        @DisplayName("TRANSFER should have description 'Transfer'")
        void transferDescription() {
            assertEquals("Transfer", TransactionType.TRANSFER.getDescription());
        }

        @Test
        @DisplayName("Should have exactly three transaction types")
        void transactionTypeCount() {
            assertEquals(3, TransactionType.values().length);
        }
    }

    // ==================== TRANSFER TO OR FROM TYPE ENUM TESTS ====================

    @Nested
    @DisplayName("Transfer To Or From Type Enum Tests")
    class TransferToOrFromTypeEnumTests {

        @Test
        @DisplayName("Should have FROM type")
        void hasFromType() {
            assertNotNull(TransferToOrFromType.FROM);
        }

        @Test
        @DisplayName("Should have TO type")
        void hasToType() {
            assertNotNull(TransferToOrFromType.TO);
        }

        @Test
        @DisplayName("Should have exactly two transfer direction types")
        void transferDirectionTypeCount() {
            assertEquals(2, TransferToOrFromType.values().length);
        }
    }

    // ==================== VIEW TRANSACTIONS TESTS ====================

    @Nested
    @DisplayName("View Transactions Tests")
    class ViewTransactionsTests {

        @Test
        @DisplayName("Should not throw exception when viewing transactions for empty account")
        void viewTransactionsEmpty() {
            assertDoesNotThrow(() -> 
                transactionManager.viewTransactionsByAccount(savingsAccount.getAccountNumber(), "Test Message"));
        }

        @Test
        @DisplayName("Should not throw exception when viewing transactions with data")
        void viewTransactionsWithData() {
            String accNumber = savingsAccount.getAccountNumber();
            
            Transaction t1 = new Transaction(accNumber, 1000.0, 1000.0);
            t1.setType(TransactionType.DEPOSIT.getDescription());
            transactionManager.addTransaction(t1);
            
            assertDoesNotThrow(() -> 
                transactionManager.viewTransactionsByAccount(accNumber, "Account Transactions"));
        }
    }

    // ==================== PREVIEW TRANSACTION CONFIRMATION TESTS ====================

    @Nested
    @DisplayName("Preview Transaction Confirmation Tests")
    class PreviewTransactionConfirmationTests {

        @Test
        @DisplayName("Should not throw exception when previewing deposit confirmation")
        void previewDepositConfirmation() throws InvalidAmountException {
            savingsAccount.deposit(1000);
            
            assertDoesNotThrow(() -> 
                transactionManager.previewTransactionConfirmation(
                    savingsAccount, 
                    TransactionType.DEPOSIT, 
                    500.0, 
                    transactionManager, 
                    savingsAccount.getAccountNumber()
                ));
        }

        @Test
        @DisplayName("Should not throw exception when previewing withdrawal confirmation")
        void previewWithdrawalConfirmation() throws InvalidAmountException {
            savingsAccount.deposit(2000);
            
            assertDoesNotThrow(() -> 
                transactionManager.previewTransactionConfirmation(
                    savingsAccount, 
                    TransactionType.WITHDRAWAL, 
                    500.0, 
                    transactionManager, 
                    savingsAccount.getAccountNumber()
                ));
        }

        @Test
        @DisplayName("Should not throw exception when previewing transfer confirmation")
        void previewTransferConfirmation() throws InvalidAmountException {
            savingsAccount.deposit(2000);
            
            assertDoesNotThrow(() -> 
                transactionManager.previewTransactionConfirmation(
                    savingsAccount, 
                    TransactionType.TRANSFER, 
                    500.0, 
                    transactionManager, 
                    savingsAccount.getAccountNumber()
                ));
        }
    }
}
