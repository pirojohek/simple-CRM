package by.pirog.CRM.utils;

import by.pirog.CRM.storage.entity.SellerEntity;

public class SellerUtils {

    public static SellerEntity getSellerEntityTransient(){
        return SellerEntity.builder()
                .name("Seller")
                .contactInfo("Contact Info")
                .build();
    }

    public static SellerEntity getSellerSecondEntityTransient(){
        return SellerEntity.builder()
                .name("Second Seller")
                .contactInfo("Contact Info Second Seller")
                .build();
    }

    public static SellerEntity getSellerEntityPersistent(){
        return SellerEntity.builder()
                .id(1L)
                .name("Seller")
                .contactInfo("Contact Info")
                .build();
    }

    public static SellerEntity getSellerSecondEntityPersistent(){
        return SellerEntity.builder()
                .id(2L)
                .name("Second Seller")
                .contactInfo("Contact Info Second Seller")
                .build();
    }
}
