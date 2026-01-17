package by.pirog.CRM.utils;

import by.pirog.CRM.enums.PaymentType;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.entity.TransactionEntity;

public class TransactionUtils {

    public static TransactionEntity getTransactionEntityTransient(SellerEntity seller){
        return TransactionEntity.builder()
                .amount(100.0)
                .paymentType(PaymentType.CARD)
                .seller(seller)
                .build();
    }
}
