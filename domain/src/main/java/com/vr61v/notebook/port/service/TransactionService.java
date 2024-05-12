package com.vr61v.notebook.port.service;

import com.vr61v.notebook.model.Transaction;

import java.time.Month;

public interface TransactionService {

    /**
     * Adds a new transaction in repository.
     * @return added transaction
     */
    Transaction addTransaction(Transaction transaction);

    /**
     * Removes a transaction(s) from repository with the specified parameters.
     * @return quantity of removed transactions
     */
    int removeTransaction(String name, int value, Month month);
}
