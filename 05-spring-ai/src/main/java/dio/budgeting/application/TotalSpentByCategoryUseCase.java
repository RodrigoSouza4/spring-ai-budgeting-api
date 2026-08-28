package dio.budgeting.application;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TotalSpentByCategoryUseCase {

    private final TransactionRepository transactionRepository;

    public TotalSpentByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(
            name = "total-spent-by-category",
            description = "Calcula o valor total gasto em uma categoria"
    )
    public double execute(
            @ToolParam(description = "Categoria das transações") Category category
    ) {

        long total = transactionRepository
                .findAllByCategory(category)
                .stream()
                .mapToLong(transaction -> transaction.getAmount())
                .sum();

        return BigDecimal
                .valueOf(total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}