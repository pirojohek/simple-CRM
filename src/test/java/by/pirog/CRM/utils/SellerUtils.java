package by.pirog.CRM.utils;

import by.pirog.CRM.storage.entity.SellerEntity;

public class SellerUtils {

    public static SellerEntity getSellerEntityTransient(){
        return SellerEntity.builder()
                .name("Seller")
                .contactInfo("Contact Info")
                .build();
    }
}
