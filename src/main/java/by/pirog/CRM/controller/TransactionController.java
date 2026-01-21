package by.pirog.CRM.controller;

import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getTransactions() {
        List<TransactionResponseDto> response = this.transactionService.getTransactionsList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId:\\d+}")
    public ResponseEntity<TransactionResponseDto> getTransactionById
            (@PathVariable Long transactionId) {
        var response = this.transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{transactionId:\\d+}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable("transactionId") Long transactionId){
        this.transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.noContent().build();
    }
}
