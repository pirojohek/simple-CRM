package by.pirog.CRM.controller;

import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions")
@Tag(name="Transactions", description = "Управление транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(
            summary = "Получить список всех транзакций",
            description = "Возвращает полный список всех транзакций в системе"
    )
    @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions() {
        List<TransactionResponseDto> response = this.transactionService.getTransactionsList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId:\\d+}")
    @Operation(
            summary = "Получить транзакцию по ID",
            description = "Возвращает информацию о конкретной транзакции по её идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Транзакция найдена"),
            @ApiResponse(responseCode = "404", description = "Транзакция с указанным ID не найдена")
    })
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @Parameter(description = "Уникальный идентификатор транзакции", example = "1", required = true)
            @PathVariable Long transactionId) {
        var response = this.transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId:\\d+}")
    @Operation(
            summary = "Удалить транзакцию",
            description = "Удаляет транзакцию из системы по её идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Транзакция успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция с указанным ID не найдена")
    })
    public ResponseEntity<Void> deleteTransactionById(
            @Parameter(description = "Уникальный идентификатор транзакции", example = "1", required = true)
            @PathVariable("transactionId") Long transactionId){
        this.transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.noContent().build();
    }
}
