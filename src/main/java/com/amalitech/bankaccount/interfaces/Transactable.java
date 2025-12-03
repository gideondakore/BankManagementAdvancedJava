package com.amalitech.bankaccount.interfaces;

public interface Transactable {

    boolean processTransaction(double amount, String type);
}
