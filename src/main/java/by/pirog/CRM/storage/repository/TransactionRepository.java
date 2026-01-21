package by.pirog.CRM.storage.repository;

import by.pirog.CRM.storage.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


    @Query("SELECT t from TransactionEntity t " +
            "where t.seller.id = :sellerId" )
    List<TransactionEntity> getTransactionEntitiesBySellerId(@Param("sellerId") Long sellerId);
}
