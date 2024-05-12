package com.vr61v.notebook.port.repository;

import com.vr61v.notebook.model.Category;
import com.vr61v.notebook.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByMcc(Category mcc);

    List<Transaction> findAllByNameAndValueAndMonth(String name, int value, Month month);
}
