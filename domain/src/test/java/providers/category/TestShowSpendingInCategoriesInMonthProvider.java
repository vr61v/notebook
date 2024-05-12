package providers.category;

import com.vr61v.notebook.model.Category;
import com.vr61v.notebook.model.Transaction;
import com.vr61v.notebook.port.repository.CategoryRepository;
import com.vr61v.notebook.port.repository.TransactionRepository;
import com.vr61v.notebook.port.service.CategoryService;
import com.vr61v.notebook.service.CategoryServiceImpl;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.mockito.Mockito;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestShowSpendingInCategoriesInMonthProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        List<Category> mccs = new ArrayList<>();
        for (int i = 0; i < 9; ++i) mccs.add(new Category("581" + i, new ArrayList<>()));
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                transactions.add(new Transaction("transaction", 100, Month.JANUARY, mccs.get(i)));
        for (int i = 3; i < 6; ++i)
            for (int j = 0; j < 3; ++j)
                transactions.add(new Transaction("transaction", 100, Month.FEBRUARY, mccs.get(i)));
        for (int i = 6; i < 9; ++i)
            for (int j = 0; j < 3; ++j)
                transactions.add(new Transaction("transaction", 100, Month.MARCH, mccs.get(i)));

        Category category1 = new Category("mock category 1", mccs.subList(0, 3));
        Category category2 = new Category("mock category 2", mccs.subList(3, 6));
        Category category3 = new Category("mock category 3", mccs.subList(6, 9));
        Category category4 = new Category("mock category 4", List.of(category1, category2));

        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        for (Category mcc : mccs) Mockito.when(transactionRepository.findAllByMcc(mcc)).thenReturn(transactions.stream().filter(x -> x.getMcc() == mcc).toList());
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1, category2, category3, category4));

        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, transactionRepository);

        Map<Category, Integer> mapJanuary = new HashMap<>();
        mapJanuary.put(null, 900);
        mapJanuary.put(category1, 900);
        mapJanuary.put(category2, 0);
        mapJanuary.put(category3, 0);
        mapJanuary.put(category4, 900);

        Map<Category, Integer> mapFebruary = new HashMap<>();
        mapFebruary.put(null, 900);
        mapFebruary.put(category1, 0);
        mapFebruary.put(category2, 900);
        mapFebruary.put(category3, 0);
        mapFebruary.put(category4, 900);

        Map<Category, Integer> mapMarch = new HashMap<>();
        mapMarch.put(null, 900);
        mapMarch.put(category1, 0);
        mapMarch.put(category2, 0);
        mapMarch.put(category3, 900);
        mapMarch.put(category4, 0);

        return Stream.of(
                Arguments.of(categoryService, Month.JANUARY, mapJanuary),
                Arguments.of(categoryService, Month.FEBRUARY, mapFebruary),
                Arguments.of(categoryService, Month.MARCH, mapMarch)
        );
    }
}
