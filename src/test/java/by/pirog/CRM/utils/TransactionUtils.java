package by.pirog.CRM.utils;

import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.enums.PaymentType;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.entity.TransactionEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionUtils {

    public static TransactionEntity getTransactionEntityTransient(SellerEntity seller){
        return TransactionEntity.builder()
                .amount(BigDecimal.valueOf(100.0))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }

    public static TransactionEntity getTransactionEntityPersistent(SellerEntity seller){
        return TransactionEntity.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100.0))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }

    public static TransactionEntity getTransactionSecondEntityTransient(SellerEntity seller){
        return TransactionEntity.builder()
                .amount(BigDecimal.valueOf(125.0))
                .paymentType(PaymentType.CASH)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }

    public static TransactionEntity getTransactionSecondEntityPersistent(SellerEntity seller){
        return TransactionEntity.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(125.0))
                .paymentType(PaymentType.CASH)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }

    public static TransactionResponseDto getTransactionResponseDto(Long sellerId){
        return TransactionResponseDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(125.00))
                .paymentType(PaymentType.CARD)
                .sellerId(sellerId)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    public static TransactionResponseDto getSecondTransactionResponseDto(Long sellerId){
        return TransactionResponseDto.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(250.00))
                .paymentType(PaymentType.CASH)
                .sellerId(sellerId)
                .transactionDate(LocalDateTime.now())
                .build();
    }
}
