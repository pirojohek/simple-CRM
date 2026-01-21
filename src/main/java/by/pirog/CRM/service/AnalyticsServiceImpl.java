package by.pirog.CRM.service;

import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;
import by.pirog.CRM.enums.AnalyticsPeriod;
import by.pirog.CRM.storage.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{

    private final AnalyticsRepository analyticsRepository;

    public List<SellerAnalyticsResponseDto> findSellersWithSumLessThan(
            LocalDateTime from,
            LocalDateTime to,
            BigDecimal maxSum
    ) {
        return analyticsRepository.findSellersWithSumLessThan(from, to, maxSum);
    }

    @Override
    public SellerAnalyticsResponseDto getMostProductiveSeller(AnalyticsPeriod period) {
        LocalDateTime start;
        LocalDateTime end;
        LocalDateTime now = LocalDateTime.now();

        switch (period) {
            case DAY -> {
                start = now.toLocalDate().atStartOfDay();
                end = now.toLocalDate().atTime(LocalTime.MAX);
            }
            case MONTH -> {
                YearMonth month = YearMonth.now();
                start = month.atDay(1).atStartOfDay();
                end = month.atEndOfMonth().atTime(LocalTime.MAX);
            }
            case QUARTER -> {
                int currentMonth = now.getMonthValue();
                int startMonth = 3 * ((currentMonth - 1) / 3) + 1;

                LocalDate startDate = LocalDate.of(now.getYear(), startMonth, 1);
                LocalDate endDate = startDate.plusMonths(3).minusDays(1);

                start = startDate.atStartOfDay();
                end = endDate.atTime(LocalTime.MAX);
            }
            case YEAR -> {
                start = LocalDate.of(now.getYear(), 1, 1).atStartOfDay();
                end = LocalDate.of(now.getYear(), 12, 31).atTime(LocalTime.MAX);
            }
            default -> throw new IllegalArgumentException("Unsupported period: " + period);
        }

        return analyticsRepository.findMostProductiveSeller(start, end);
    }
}
