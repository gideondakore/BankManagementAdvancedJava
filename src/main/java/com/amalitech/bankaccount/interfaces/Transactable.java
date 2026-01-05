package com.amalitech.bankaccount.interfaces;

import com.amalitech.bankaccount.exceptions.InsufficientFundsException;
import com.amalitech.bankaccount.exceptions.InvalidAmountException;
import com.amalitech.bankaccount.exceptions.OverdraftExceededException;

public interface Transactable {

    /**
     * For checking whether transaction was successfully or not
     * @param amount
     * @param type
     * @return Boolean true indicating success or false for transaction failure
     * @throws InvalidAmountException if amount is invalid
     * @throws InsufficientFundsException if insufficient funds
     * @throws OverdraftExceededException if overdraft limit exceeded
     */
    boolean processTransaction(double amount, String type) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException;
}
