package by.pirog.CRM.controller;

import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;
import by.pirog.CRM.enums.AnalyticsPeriod;
import by.pirog.CRM.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    // http://localhost:8080/api/analytics/sellers/transactions/sum-less-than?from=2026-01-01T00:00:00&to=2026-01-10T23:59:59&maxSum=1000
    @GetMapping("/sellers/transactions/sum-less-than")
    public ResponseEntity<List<SellerAnalyticsResponseDto>> findSellersWithSumLessThan(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            @RequestParam BigDecimal maxSum
    ) {
        return ResponseEntity.ok(
                analyticsService.findSellersWithSumLessThan(from, to, maxSum)
        );
    }

    @GetMapping("/top-seller")
    public ResponseEntity<SellerAnalyticsResponseDto> getMostProductiveSeller(
            @RequestParam("period") AnalyticsPeriod period) {
        return ResponseEntity.ok(analyticsService.getMostProductiveSeller(period));
    }
}
