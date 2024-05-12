package com.vr61v.notebook;

import com.vr61v.notebook.model.Category;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import com.vr61v.notebook.port.service.CategoryService;

import java.time.Month;
import java.util.*;

@Command(group = "Category commands")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Command(command = "add-mcc-codes", description = "adds new mccs")
    public String addMccCodes(@Option(required = true) String[] mccs) {
        try {
            for (String mcc : mccs) {
                Category mccToAdd = new Category(mcc, new ArrayList<>());
                if (mccToAdd.isLeaf()) categoryService.addCategory(mccToAdd);
                else throw new IllegalArgumentException(mcc + " is not a mcc");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            return e.getMessage();
        }

        return "created new mccs " + Arrays.toString(mccs);
    }

    @Command(command = "add-category", description = "adds a category and binds mcc codes to it (mcc codes must be already existing)")
    public String addCategory(@Option(required = true) String name, @Option(required = true) String[] mccs) {
        try {
            categoryService.addCategory(new Category(name, new ArrayList<>()));
            categoryService.addCategoriesToCategory(name, List.of(mccs));
        } catch (IllegalArgumentException | NullPointerException e) {
            return e.getMessage();
        }

        return "created new category " + name + " with mccs " + Arrays.toString(mccs);
    }

    @Command(command = "add-categories-to-category", description = "adds categories or mcc codes to the category")
    public String addCategoriesToCategory(@Option(required = true) String name, @Option(required = true) String[] categories) {
        try {
            categoryService.addCategoriesToCategory(name, List.of(categories));
        } catch (IllegalArgumentException | NullPointerException e) {
            return e.getMessage();
        }
        return "added to category " + name + " categories " + Arrays.toString(categories);
    }

    @Command(command = "add-category-to-categories", description = "adds category or mcc code to the categories")
    public String addCategoryToCategories(@Option(required = true) String name, @Option(required = true) String[] categories) {
        try {
            categoryService.addCategoryToCategories(name, List.of(categories));
        } catch (IllegalArgumentException | NullPointerException e) {
            return e.getMessage();
        }
        return "added category " + name + " to categories " + Arrays.toString(categories);
    }

    @Command(command = "remove-category", description = "removes category")
    public String removeCategory(@Option(required = true) String name) {
        try {
            categoryService.removeCategory(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return e.getMessage();
        }
        return "removed category " + name;
    }

    @Command(command = "show-categories", description = "shows all categories")
    public String showCategories() {
        List<Category> categories = categoryService.showCategories();
        StringBuilder response = new StringBuilder();
        categories.forEach(category -> response.append(category.getName()).append("\n"));
        return response.toString();
    }

    @Command(command = "show-categories-spending-in-month", description = "shows spending in categories in month")
    public String showCategoriesSpendingInMonth(@Option(required = true) Month month) {
        Map<Category, Integer> result = categoryService.showSpendingInCategoriesInMonth(month);
        int total = result.get(null);
        if (total == 0) return "there were no transactions in month " + month;

        StringBuilder response = new StringBuilder();
        for (Category c : result.keySet()) {
            if (c == null) continue;
            int sum = result.get(c);
            response.append(c.getName()).append(" ")
                    .append(sum).append("р ")
                    .append((int) ((float) sum / total * 100)).append("%\n");
        }

        return response.toString();
    }

    @Command(command = "show-category-spending-by-month", description = "shows spending in the category by month")
    public String showCategorySpendingByMonth(@Option(required = true) String name) {
        Category category = categoryService.getCategory(name);
        Map<Month, Integer> result = categoryService.showCategorySpendingByMonth(category);
        int total = result.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return "there were no transactions in category " + category.getName();

        StringBuilder response = new StringBuilder();
        for (Month m : result.keySet()) {
            int sum = result.get(m);
            response.append(m).append(" ")
                    .append(sum).append("р\n");
        }

        return response.toString();
    }
}
