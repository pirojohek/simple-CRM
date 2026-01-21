package by.pirog.CRM.service;

import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;

import java.util.List;

public interface SellerTransactionsService {

    List<TransactionResponseDto> getSellerTransactions(Long sellerId);
}
