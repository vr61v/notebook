package com.vr61v.notebook;

import com.vr61v.notebook.model.Category;
import com.vr61v.notebook.model.Transaction;
import com.vr61v.notebook.port.service.CategoryService;
import com.vr61v.notebook.port.service.TransactionService;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.time.Month;

@Command(group = "Transaction commands")
public class TransactionController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @Command(command = "add-transaction", description = "adds a transaction with the specified parameters")
    public String addTransaction(
            @Option(required = true) String name,
            @Option(required = true) int value,
            @Option(required = true) Month month,
            @Option(required = true) String mcc ) {
        Category category = categoryService.getCategory(mcc);
        if (category == null || !category.isLeaf()) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
        Transaction transaction = new Transaction(name, value, month, category);
        transactionService.addTransaction(transaction);

        return "added transaction " + transaction.getName();
    }

    @Command(command = "remove-transaction", description = "removes a transaction(s) with the specified parameters")
    public String removeTransaction(
            @Option(required = true) String name,
            @Option(required = true) int value,
            @Option(required = true) Month month) {
        int deleted = transactionService.removeTransaction(name, value, month);

        return "removed transaction(s) " + deleted + " " + name;
    }
}
