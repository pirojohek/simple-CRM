package by.pirog.CRM.storage.repository;

import by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyticsRepositoryImpl implements AnalyticsRepository {

    private final EntityManager em;

    @Override
    public List<SellerAnalyticsResponseDto> findSellersWithSumLessThan(LocalDateTime from, LocalDateTime to, BigDecimal maxSum) {

        return em.createQuery("""
                            SELECT new by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto(
                                s.id,
                                s.name,
                                SUM(t.amount)
                            )
                            FROM SellerEntity s
                            LEFT JOIN s.transactions t
                                ON t.transactionDate BETWEEN :from AND :to
                            GROUP BY s.id, s.name
                            HAVING COALESCE(SUM(t.amount), :zero) < :maxSum
                        """, SellerAnalyticsResponseDto.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("maxSum", maxSum)
                .setParameter("zero", BigDecimal.ZERO)
                .getResultList();
    }

    @Override
    public SellerAnalyticsResponseDto findMostProductiveSeller(LocalDateTime start, LocalDateTime end) {
        List<SellerAnalyticsResponseDto> list =  em.createQuery("""
                            SELECT new by.pirog.CRM.dto.analytics.SellerAnalyticsResponseDto(
                                s.id,
                                s.name,
                                SUM(t.amount)
                            )
                            FROM SellerEntity s
                            JOIN s.transactions t
                            WHERE t.transactionDate BETWEEN :start AND :end
                            GROUP BY s.id, s.name
                            ORDER BY SUM(t.amount) DESC
                        """, SellerAnalyticsResponseDto.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
}
