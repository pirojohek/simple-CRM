package by.pirog.CRM.repository;

import by.pirog.CRM.storage.repository.AnalyticsRepository;
import by.pirog.CRM.storage.repository.AnalyticsRepositoryImpl;
import by.pirog.CRM.utils.SellerUtils;
import by.pirog.CRM.utils.TransactionUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(AnalyticsRepositoryImpl.class)
public class AnalyticsRepositoryImplTests {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Test Most productive seller")
    void findMostProductiveSeller_returnsCorrectSeller() {
        // given
        var seller1 = SellerUtils.getSellerEntityTransient();
        var seller2 = SellerUtils.getSellerSecondEntityTransient();
        entityManager.persist(seller1);
        entityManager.persist(seller2);

        var now = LocalDateTime.now();

        var transaction1 = TransactionUtils.getTransactionEntityTransient(seller1);
        var transaction2 = TransactionUtils.getTransactionSecondEntityTransient(seller2);
        transaction1.setAmount(BigDecimal.valueOf(1000.00));
        transaction1.setTransactionDate(now);

        transaction2.setAmount(new BigDecimal("1.00"));
        transaction2.setTransactionDate(now);

        entityManager.persist(transaction1);
        entityManager.persist(transaction2);
        entityManager.flush();
        entityManager.clear();

        // when
        var result = analyticsRepository.findMostProductiveSeller(
                LocalDate.now().atStartOfDay(),
                LocalDate.now().atTime(LocalTime.MAX));

        // then
        assertThat(result).isNotNull();
        assertThat(result.sellerId()).isEqualTo(seller1.getId());
    }


}
