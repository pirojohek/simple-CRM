package by.pirog.CRM.utils;

import by.pirog.CRM.enums.PaymentType;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.entity.TransactionEntity;

import java.time.LocalDateTime;

public class TransactionUtils {

    public static TransactionEntity getTransactionEntityTransient(SellerEntity seller){
        return TransactionEntity.builder()
                .amount(100.0)
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }

    public static TransactionEntity getTransactionEntityPersistent(SellerEntity seller){
        return TransactionEntity.builder()
                .id(1L)
                .amount(100.0)
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .seller(seller)
                .build();
    }
}
