package by.pirog.CRM.controller;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sellers/{sellerId:\\d+}/transactions")
public class SellerTransactionsController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getSellerTransactions(@PathVariable Long sellerId) {
        var result = this.transactionService.getSellerTransactions(sellerId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction
            (@PathVariable Long sellerId, @Valid @RequestBody TransactionCreateRequestDto dto) {
        var result = this.transactionService.createTransaction(sellerId, dto);
        return ResponseEntity.created(URI.create("/api/transactions/%d".formatted(result.getId())))
                .body(result);
    }
}
