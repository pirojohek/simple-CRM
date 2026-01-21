package by.pirog.CRM.service;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;

import java.util.List;

public interface TransactionService {

    TransactionResponseDto getTransactionById(Long id);

    List<TransactionResponseDto> getTransactionsList();

    void deleteTransactionById(Long id);

    TransactionResponseDto createTransaction(Long sellerId, TransactionCreateRequestDto requestDto);

    List<TransactionResponseDto> getSellerTransactions(Long sellerId);
}
