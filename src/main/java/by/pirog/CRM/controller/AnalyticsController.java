package by.pirog.CRM.controller;

import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;
import by.pirog.CRM.enums.AnalyticsPeriod;
import by.pirog.CRM.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@Tag(name = "Analytics", description = "Аналитика по продавцам и транзакциям")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // http://localhost:8080/api/analytics/sellers/transactions/sum-less-than?from=2026-01-01T00:00:00&to=2026-01-10T23:59:59&maxSum=1000
    @GetMapping("/sellers/transactions/sum-less-than")
    @Operation(
            summary = "Получить продавцов с суммой транзакций ниже порога",
            description = "Возвращает список продавцов, у которых общая сумма транзакций за указанный период меньше заданного значения"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список продавцов успешно получен"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса (например, дата 'from' позже даты 'to')")
    })
    public ResponseEntity<List<SellerAnalyticsResponseDto>> findSellersWithSumLessThan(
            @Parameter(
                    description = "Начало периода анализа",
                    example = "2024-01-01T00:00:00",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(
                    description = "Конец периода анализа",
                    example = "2024-12-31T23:59:59",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @Parameter(
                    description = "Максимальная сумма транзакций (порог)",
                    example = "10000.00",
                    required = true
            )
            @RequestParam
            BigDecimal maxSum
    ) {
        return ResponseEntity.ok(
                analyticsService.findSellersWithSumLessThan(from, to, maxSum)
        );
    }

    @GetMapping("/top-seller")
    @Operation(
            summary = "Получить самого продуктивного продавца",
            description = "Возвращает продавца с наибольшей суммой транзакций за указанный период (день, месяц, квартал, год)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Самый продуктивный продавец найден"),
            @ApiResponse(responseCode = "404", description = "Нет данных за указанный период")
    })
    public ResponseEntity<SellerAnalyticsResponseDto> getMostProductiveSeller(
            @Parameter(
                    description = "Период анализа",
                    example = "DAY",
                    required = true,
                    schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = AnalyticsPeriod.class,
                            allowableValues = {"DAY", "MONTH", "QUARTER", "YEAR"}
                    )
            )
            @RequestParam("period") AnalyticsPeriod period) {
        return ResponseEntity.ok(analyticsService.getMostProductiveSeller(period));
    }
}
