import com.vr61v.notebook.model.Category;
import com.vr61v.notebook.port.repository.CategoryRepository;
import com.vr61v.notebook.port.repository.TransactionRepository;
import com.vr61v.notebook.port.service.CategoryService;
import com.vr61v.notebook.service.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;
import providers.category.TestShowCategorySpendingByMonthProvider;
import providers.category.TestShowSpendingInCategoriesInMonthProvider;

import java.time.Month;
import java.util.*;

public class CategoryServiceTests {
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

    @Test
    public void testAddCategory() {
        Category success = new Category("success", new ArrayList<>());
        Category exists = new Category("exists", new ArrayList<>());
        Category nullName = new Category(null, new ArrayList<>());
        Category emptyName = new Category("", new ArrayList<>());

        Mockito.when(categoryRepository.save(success)).thenReturn(success);
        Mockito.when(categoryRepository.findById("exists")).thenReturn(Optional.of(exists));
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, transactionRepository);

        Assertions.assertThrows(NullPointerException.class, () -> categoryService.addCategory(nullName));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(emptyName));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(exists));
        Assertions.assertEquals(success, categoryService.addCategory(success));
    }

    @Test
    public void testAddCategoriesToCategory() {
        String name = "category";
        List<String> names = List.of("child1", "child2", "child3");
        Category toAdd = new Category(name, new ArrayList<>());
        List<Category> children = new ArrayList<>();
        for (String string : names) children.add(new Category(string, new ArrayList<>()));

        for (Category category : children) Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findById(toAdd.getName())).thenReturn(Optional.of(toAdd));
        Mockito.when(categoryRepository.save(toAdd)).thenReturn(toAdd);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, transactionRepository);

        Category actual = categoryService.addCategoriesToCategory(name, names);

        Assertions.assertEquals(name, actual.getName());
        for (int i = 0; i < names.size(); ++i) Assertions.assertEquals(children.get(i), actual.getChildren().get(i));
        Assertions.assertThrows(NullPointerException.class, () -> categoryService.addCategoriesToCategory(name, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategoriesToCategory(name, List.of()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategoriesToCategory("Not exists", List.of()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategoriesToCategory(name, List.of("Not exists")));
    }

    @Test
    public void testAddCategoryToCategories() {
        String name = "category";
        List<String> names = List.of("child1", "child2", "child3");

        Category child = new Category(name, new ArrayList<>());
        List<Category> parents = new ArrayList<>();
        for (String string : names) parents.add(new Category(string, new ArrayList<>()));

        for (Category category : parents) Mockito.when(categoryRepository.findById(category.getName())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.findById(name)).thenReturn(Optional.of(child));
        Mockito.when(categoryRepository.save(child)).thenReturn(child);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, transactionRepository);

        Category actual = categoryService.addCategoryToCategories(name, names);

        for (Category parent : parents) Assertions.assertEquals(parent.getChildren().get(0), actual);
        Assertions.assertThrows(NullPointerException.class, () -> categoryService.addCategoryToCategories(name, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategoryToCategories(name, List.of()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategoryToCategories("Not exists", List.of()));
    }

    @ParameterizedTest
    @ArgumentsSource(TestShowSpendingInCategoriesInMonthProvider.class)
    public void testShowSpendingInCategoriesInMonth(CategoryService service, Month month, Map<Category, Integer> excepted) {
        Map<Category, Integer> actual = service.showSpendingInCategoriesInMonth(month);
        Assertions.assertEquals(excepted, actual);
    }

    @ParameterizedTest
    @ArgumentsSource(TestShowCategorySpendingByMonthProvider.class)
    public void testShowCategorySpendingByMonth(CategoryService service, Category category, Map<Month, Integer> excepted) {
        Map<Month, Integer> actual = service.showCategorySpendingByMonth(category);
        Assertions.assertEquals(excepted, actual);
    }
}
