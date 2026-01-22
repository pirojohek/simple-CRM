package by.pirog.CRM.controller;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sellers/{sellerId:\\d+}/transactions")
@Tag(name="Seller Transactions", description = "Управление транзакциями конкретного продавца")
public class SellerTransactionsController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(
            summary = "Получить все транзакции продавца",
            description = "Возвращает список всех транзакций, совершенных указанным продавцом"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден")
    })
    public ResponseEntity<List<TransactionResponseDto>> getSellerTransactions(
            @Parameter(description = "Уникальный идентификатор продавца", example = "1", required = true)
            @PathVariable Long sellerId) {
        var result = this.transactionService.getSellerTransactions(sellerId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(
            summary = "Создать новую транзакцию для продавца",
            description = "Регистрирует новую транзакцию для указанного продавца"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Транзакция успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден")
    })
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Parameter(description = "Уникальный идентификатор продавца", example = "1", required = true)
            @PathVariable Long sellerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные новой транзакции",
                    required = true
            )
            @Valid @RequestBody TransactionCreateRequestDto dto) {
        var result = this.transactionService.createTransaction(sellerId, dto);
        return ResponseEntity.created(URI.create("/api/transactions/%d".formatted(result.getId())))
                .body(result);
    }
}
