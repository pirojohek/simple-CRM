package by.pirog.CRM.storage.repository;

import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsRepository {

    List<SellerAnalyticsResponseDto> findSellersWithSumLessThan(
            LocalDateTime from,
            LocalDateTime to,
            BigDecimal maxSum
    );

    SellerAnalyticsResponseDto findMostProductiveSeller(LocalDateTime start, LocalDateTime end);
}
