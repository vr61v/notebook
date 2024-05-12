package com.vr61v.notebook.port.service;

import com.vr61v.notebook.model.Category;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    /**
     * Adds a new category or mcc code in repository.
     * @param category category or mcc code to add
     */
    Category addCategory(Category category);

    /**
     * Adds categories or mcc codes to the category. The categories and mcc codes should already be stored in the repository.
     * @param name name of category to add (can't be the mcc code)
     * @param categories names categories or mcc codes for add
     */
    Category addCategoriesToCategory(String name, List<String> categories);

    /**
     * Adds category or mcc code to the categories.
     * @param name name of category or mcc code for add (should already be stored in the repository)
     * @param categories names categories to add
     */
    Category addCategoryToCategories(String name, List<String> categories);

    /**
     * Removes category.
     *
     * @param name name of the category to remove
     */
    void removeCategory(String name);

    /**
     * Get category by name.
     * @param name name of the category to get
     * @return category from the repository
     */
    Category getCategory(String name);

    /**
     * Get all categories.
     * @return categories from the repository
     */
    List<Category> showCategories();

    /**
     * Shows spending in categories in month.
     * @param month the month for which it is needed to get spending by category
     * @return map in which the key is the category and the value is spending
     */
    Map<Category, Integer> showSpendingInCategoriesInMonth(Month month);

    /**
     * Shows spending in the category by month
     * @param category the category for which it is needed to get spending by month
     * @return map in which the key is the month and the value is spending
     */
    Map<Month, Integer> showCategorySpendingByMonth(Category category);
}