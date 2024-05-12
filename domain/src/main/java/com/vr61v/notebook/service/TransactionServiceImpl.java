package com.vr61v.notebook.service;

import lombok.RequiredArgsConstructor;
import com.vr61v.notebook.model.Transaction;
import org.springframework.stereotype.Service;
import com.vr61v.notebook.port.repository.TransactionRepository;
import com.vr61v.notebook.port.service.TransactionService;

import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public int removeTransaction(String name, int value, Month month) {
        List<Transaction> transactions = transactionRepository.findAllByNameAndValueAndMonth(name, value, month);
        transactionRepository.deleteAll(transactions);
        return transactions.size();
    }
}
