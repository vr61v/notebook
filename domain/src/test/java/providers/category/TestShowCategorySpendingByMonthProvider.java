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

public class TestShowCategorySpendingByMonthProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        List<Category> mccs = new ArrayList<>();
        for (int i = 1000; i < 1012; ++i) mccs.add(new Category(String.valueOf(i), new ArrayList<>()));

        List<Transaction> transactions = new ArrayList<>();
        for (Month month : Month.values()) transactions.add(new Transaction("transaction", 100, month, mccs.get(month.ordinal())));

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 4; ++i) categories.add(new Category("mock category " + i, new ArrayList<>()));
        for (int i = 0; i < mccs.size(); ++i) categories.get(i % 3).getChildren().add(mccs.get(i));
        categories.get(3).getChildren().add(categories.get(0));
        categories.get(3).getChildren().add(categories.get(1));
        categories.add(new Category("empty category", new ArrayList<>()));

        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        for (Category mcc : mccs)
            Mockito.when(transactionRepository.findAllByMcc(mcc)).thenReturn(transactions.stream().filter(x -> x.getMcc() == mcc).toList());
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, transactionRepository);

        Map<Month, Integer> excepted1 = new HashMap<>(Map.of(Month.APRIL, 100, Month.JULY, 100, Month.JANUARY, 100, Month.OCTOBER, 100));
        Map<Month, Integer> excepted2 = new HashMap<>(Map.of(Month.MAY, 100, Month.AUGUST, 100, Month.NOVEMBER, 100, Month.FEBRUARY, 100));
        excepted2.putAll(excepted1);
        Map<Month, Integer> exceptedEmpty = new HashMap<>();
        return Stream.of(
                Arguments.of(categoryService, categories.get(0), excepted1),
                Arguments.of(categoryService, categories.get(3), excepted2),
                Arguments.of(categoryService, categories.get(4), exceptedEmpty)
        );
    }
}
