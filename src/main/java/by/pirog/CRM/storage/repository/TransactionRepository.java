package by.pirog.CRM.storage.repository;

import by.pirog.CRM.storage.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    @Query("SELECT t from TransactionEntity t " +
            "where t.seller.id = :sellerId")
    List<TransactionEntity> getTransactionEntitiesBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT s FROM SellerEntity s " +
            "JOIN s.transactions t " +
            "WHERE t.transactionDate BETWEEN :from AND :to " +
            "GROUP BY s " +
            "HAVING SUM(t.amount) < :limit")
    List<TransactionEntity> findSellersWithTransactionSumLessThan(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("limit") BigDecimal limit
    );
}
