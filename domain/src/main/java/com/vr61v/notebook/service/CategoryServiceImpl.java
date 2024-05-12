package com.vr61v.notebook.service;

import com.vr61v.notebook.model.Category;
import lombok.RequiredArgsConstructor;
import com.vr61v.notebook.model.Transaction;
import org.springframework.stereotype.Service;
import com.vr61v.notebook.port.repository.CategoryRepository;
import com.vr61v.notebook.port.repository.TransactionRepository;
import com.vr61v.notebook.port.service.CategoryService;

import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Traversing the category tree with cost calculation for each node. The mcc code is taken
     * as a leaf of the tree, when it is reached, the calculation of all transactions with
     * the same mcc code for a given month begins.
     * @param month the month for which it is needed to get spending by category
     * @param category current category (node)
     * @param categoriesByMonth map in which the key is the category and the value is spending, the null key is the total spending for a given month
     * @return total spending in current node
     */
    private int showSpendingInCategoriesInMonthTravel(Month month, Category category, Map<Category, Integer> categoriesByMonth) {
        int sum = 0;
        if (category.isLeaf()) {
            for (Transaction transaction : transactionRepository.findAllByMcc(category))
                if (transaction.getMonth().equals(month)) sum += transaction.getValue();
            categoriesByMonth.put(null, categoriesByMonth.getOrDefault(null, 0) + sum);
            return sum;
        }

        for (Category child : category.getChildren()) {
            sum += categoriesByMonth.containsKey(child) ?
                    categoriesByMonth.get(child) :
                    showSpendingInCategoriesInMonthTravel(month, child, categoriesByMonth);
        }
        categoriesByMonth.put(category, sum);
        return sum;
    }

    /**
     * Traversing the category tree with spending calculation per month. The mcc code is taken
     * as a leaf of the tree, upon reaching which the calculation of all transactions with
     * the same mcc code begins, after which the result is summed with the value in the map.
     * @param category current category (node)
     * @param categoryByMonth map in which the key is the month and the value is spending
     */
    private void showCategorySpendingByMonthTravel(Category category, Map<Month, Integer> categoryByMonth) {
        if (category.isLeaf()) {
            for (Transaction transaction : transactionRepository.findAllByMcc(category)) {
                categoryByMonth.put(
                        transaction.getMonth(),
                        categoryByMonth.getOrDefault(transaction.getMonth(), 0) + transaction.getValue());
            }
        } else {
            for (Category child : category.getChildren()) {
                showCategorySpendingByMonthTravel(child, categoryByMonth);
            }
        }
    }

    @Override
    public Category addCategory(Category category) {
        if (category == null) throw new NullPointerException("Category cannot be null");
        else if (category.getName().isEmpty()) throw new IllegalArgumentException("Category name cannot be empty");
        else if (categoryRepository.findById(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category addCategoriesToCategory(String name, List<String> categories) {
        if (categories == null) throw new NullPointerException("Categories cannot be null");
        else if (categories.isEmpty()) throw new IllegalArgumentException("Categories cannot be empty");

        Category category = categoryRepository.findById(name)
                .orElseThrow(() -> new IllegalArgumentException("Category with name " + name + " does not exist"));

        categories.forEach(c -> category.getChildren().add(categoryRepository.findById(c)
                .orElseThrow(() -> new IllegalArgumentException("Category with name " + c + " does not exist"))));

        return categoryRepository.save(category);
    }

    @Override
    public Category addCategoryToCategories(String name, List<String> categories) {
        if (categories == null) throw new NullPointerException("Categories cannot be null");
        else if (categories.isEmpty()) throw new IllegalArgumentException("Categories cannot be empty");

        Category category = categoryRepository.findById(name)
                .orElseThrow(() -> new IllegalArgumentException("Category with name " + name + " does not exist"));
        List<Category> parents = categories
                .stream()
                .map(c -> categoryRepository.findById(c)
                        .orElseThrow(() -> new IllegalArgumentException("Category with name " + c + " does not exist")))
                .toList();
        for (Category p : parents) {
            p.getChildren().add(category);
            categoryRepository.save(p);
        }

        return categoryRepository.save(category);
    }

    @Override
    public void removeCategory(String name) {
        categoryRepository.deleteById(name);
    }

    @Override
    public Category getCategory(String name) {
        return categoryRepository.findById(name).orElse(null);
    }

    @Override
    public List<Category> showCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> !category.isLeaf())
                .toList();
    }

    @Override
    public Map<Category, Integer> showSpendingInCategoriesInMonth(Month month) {
        Map<Category, Integer> categoriesByMonth = new HashMap<>();
        for (Category category : categoryRepository.findAll()) {
            if (category.isLeaf() || categoriesByMonth.containsKey(category)) continue;
            showSpendingInCategoriesInMonthTravel(month, category, categoriesByMonth);
        }
        return categoriesByMonth;
    }

    @Override
    public Map<Month, Integer> showCategorySpendingByMonth(Category category) {
        Map<Month, Integer> categoryByMonth = new HashMap<>();
        showCategorySpendingByMonthTravel(category, categoryByMonth);
        return categoryByMonth;
    }
}
