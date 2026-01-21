package by.pirog.CRM.service;


import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;
import by.pirog.CRM.enums.AnalyticsPeriod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {

    List<SellerAnalyticsResponseDto> findSellersWithSumLessThan(
            LocalDateTime from,
            LocalDateTime to,
            BigDecimal maxSum
    );

    SellerAnalyticsResponseDto getMostProductiveSeller(AnalyticsPeriod period);

}
