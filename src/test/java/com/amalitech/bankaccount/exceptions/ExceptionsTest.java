package com.amalitech.bankaccount.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    // ==================== INVALID AMOUNT EXCEPTION TESTS ====================

    @Nested
    @DisplayName("InvalidAmountException Tests")
    class InvalidAmountExceptionTests {

        @Test
        @DisplayName("Should create InvalidAmountException with message")
        void createWithMessage() {
            String message = "Amount must be greater than zero";
            InvalidAmountException exception = new InvalidAmountException(message);
            
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("InvalidAmountException should be RuntimeException")
        void shouldBeRuntimeException() {
            InvalidAmountException exception = new InvalidAmountException("Test");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should throw InvalidAmountException")
        void shouldThrowException() {
            assertThrows(InvalidAmountException.class, () -> {
                throw new InvalidAmountException("Invalid amount");
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveMessage() {
            String expectedMessage = "Deposit amount cannot be negative";
            
            InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
                throw new InvalidAmountException(expectedMessage);
            });
            
            assertEquals(expectedMessage, thrown.getMessage());
        }
    }

    // ==================== INSUFFICIENT FUNDS EXCEPTION TESTS ====================

    @Nested
    @DisplayName("InsufficientFundsException Tests")
    class InsufficientFundsExceptionTests {

        @Test
        @DisplayName("Should create InsufficientFundsException with message")
        void createWithMessage() {
            String message = "Insufficient funds for withdrawal";
            InsufficientFundsException exception = new InsufficientFundsException(message);
            
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("InsufficientFundsException should be RuntimeException")
        void shouldBeRuntimeException() {
            InsufficientFundsException exception = new InsufficientFundsException("Test");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should throw InsufficientFundsException")
        void shouldThrowException() {
            assertThrows(InsufficientFundsException.class, () -> {
                throw new InsufficientFundsException("Insufficient funds");
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveMessage() {
            String expectedMessage = "Cannot withdraw $1000. Current balance: $500";
            
            InsufficientFundsException thrown = assertThrows(InsufficientFundsException.class, () -> {
                throw new InsufficientFundsException(expectedMessage);
            });
            
            assertEquals(expectedMessage, thrown.getMessage());
        }
    }

    // ==================== OVERDRAFT EXCEEDED EXCEPTION TESTS ====================

    @Nested
    @DisplayName("OverdraftExceededException Tests")
    class OverdraftExceededExceptionTests {

        @Test
        @DisplayName("Should create OverdraftExceededException with message")
        void createWithMessage() {
            String message = "Overdraft limit exceeded";
            OverdraftExceededException exception = new OverdraftExceededException(message);
            
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("OverdraftExceededException should be RuntimeException")
        void shouldBeRuntimeException() {
            OverdraftExceededException exception = new OverdraftExceededException("Test");
            
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should throw OverdraftExceededException")
        void shouldThrowException() {
            assertThrows(OverdraftExceededException.class, () -> {
                throw new OverdraftExceededException("Overdraft exceeded");
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveMessage() {
            String expectedMessage = "Transaction would exceed overdraft limit of $1000";
            
            OverdraftExceededException thrown = assertThrows(OverdraftExceededException.class, () -> {
                throw new OverdraftExceededException(expectedMessage);
            });
            
            assertEquals(expectedMessage, thrown.getMessage());
        }
    }

    // ==================== INPUT MISMATCH EXCEPTION TESTS ====================

    @Nested
    @DisplayName("InputMismatchException Tests")
    class InputMismatchExceptionTests {

        @Test
        @DisplayName("Should create InputMismatchException with message")
        void createWithMessage() {
            String message = "Invalid input provided";
            InputMismatchException exception = new InputMismatchException(message);
            
            assertEquals(message, exception.getMessage());
        }

        @Test
        @DisplayName("InputMismatchException should be checked Exception")
        void shouldBeCheckedException() {
            InputMismatchException exception = new InputMismatchException("Test");
            
            assertTrue(exception instanceof Exception);
            // InputMismatchException is not a RuntimeException, so this class check verifies that
            assertFalse(RuntimeException.class.isAssignableFrom(InputMismatchException.class));
        }

        @Test
        @DisplayName("Should throw InputMismatchException")
        void shouldThrowException() {
            assertThrows(InputMismatchException.class, () -> {
                throw new InputMismatchException("Input mismatch");
            });
        }

        @Test
        @DisplayName("Should preserve exception message when thrown")
        void shouldPreserveMessage() {
            String expectedMessage = "Name must contain first and last name";
            
            InputMismatchException thrown = assertThrows(InputMismatchException.class, () -> {
                throw new InputMismatchException(expectedMessage);
            });
            
            assertEquals(expectedMessage, thrown.getMessage());
        }

        @Test
        @DisplayName("InputMismatchException requires handling (checked)")
        void requiresHandling() {
            // This test verifies that InputMismatchException is a checked exception
            // by checking it extends Exception but not RuntimeException
            Class<?> exceptionClass = InputMismatchException.class;
            
            assertTrue(Exception.class.isAssignableFrom(exceptionClass));
            assertFalse(RuntimeException.class.isAssignableFrom(exceptionClass));
        }
    }

    // ==================== EXCEPTION HIERARCHY TESTS ====================

    @Nested
    @DisplayName("Exception Hierarchy Tests")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("All banking exceptions should extend from Exception or RuntimeException")
        void exceptionsExtendThrowable() {
            assertTrue(Throwable.class.isAssignableFrom(InvalidAmountException.class));
            assertTrue(Throwable.class.isAssignableFrom(InsufficientFundsException.class));
            assertTrue(Throwable.class.isAssignableFrom(OverdraftExceededException.class));
            assertTrue(Throwable.class.isAssignableFrom(InputMismatchException.class));
        }

        @Test
        @DisplayName("Runtime exceptions should not require explicit handling")
        void runtimeExceptionsUnchecked() {
            assertTrue(RuntimeException.class.isAssignableFrom(InvalidAmountException.class));
            assertTrue(RuntimeException.class.isAssignableFrom(InsufficientFundsException.class));
            assertTrue(RuntimeException.class.isAssignableFrom(OverdraftExceededException.class));
        }

        @Test
        @DisplayName("InputMismatchException should require explicit handling")
        void inputMismatchIsChecked() {
            assertTrue(Exception.class.isAssignableFrom(InputMismatchException.class));
            assertFalse(RuntimeException.class.isAssignableFrom(InputMismatchException.class));
        }
    }
}
