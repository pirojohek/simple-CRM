package by.pirog.CRM.dto.analytics;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SellerAnalyticsResponseDto(
        Long sellerId,
        String sellerName,
        BigDecimal totalAmount
) {

}
