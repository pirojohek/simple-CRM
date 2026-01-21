package by.pirog.CRM.repository;

import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.entity.TransactionEntity;
import by.pirog.CRM.storage.repository.TransactionRepository;
import by.pirog.CRM.utils.SellerUtils;
import by.pirog.CRM.utils.TransactionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Test save transaction functionality")
    void givenTransactionEntityToSave_whenSave_thenReturnSavedTransactionEntity() {
        // given
        SellerEntity sellerEntity = SellerUtils.getSellerEntityTransient();
        entityManager.persist(sellerEntity);

        TransactionEntity transactionToSave = TransactionUtils.getTransactionEntityTransient(sellerEntity);

        // when
        var savedTransaction = transactionRepository.save(transactionToSave);
        entityManager.flush();
        entityManager.clear();
        // then
        var foundTransaction = entityManager.find(TransactionEntity.class, savedTransaction.getId());
        assertThat(foundTransaction).isNotNull();
        assertThat(foundTransaction.getId()).isEqualTo(savedTransaction.getId());
    }

    @Test
    @DisplayName("Test find transaction by id functionality")
    void givenTransactionId_whenFindById_thenTransactionIsFound() {
        // given
        SellerEntity sellerEntity = SellerUtils.getSellerEntityTransient();
        entityManager.persist(sellerEntity);

        TransactionEntity transaction = TransactionUtils.getTransactionEntityTransient(sellerEntity);
        var savedTransaction = entityManager.persist(transaction);
        entityManager.flush();
        entityManager.clear();

        // when
        var foundTransaction = transactionRepository.findById(savedTransaction.getId()).orElse(null);

        // then
        assertThat(foundTransaction).isNotNull();
        assertThat(foundTransaction.getId()).isEqualTo(savedTransaction.getId());
    }

    @Test
    @DisplayName("Test delete transaction by id functionality")
    void givetTransactionId_whenDeleteById_thenTransactionIsRemovedFromDb(){
        // given
        SellerEntity sellerEntity = SellerUtils.getSellerEntityTransient();
        entityManager.persist(sellerEntity);

        TransactionEntity transaction = TransactionUtils.getTransactionEntityTransient(sellerEntity);
        entityManager.persist(transaction);
        entityManager.flush();
        entityManager.clear();

        // when
        transactionRepository.deleteById(transaction.getId());

        // then
        var deletedTransaction = entityManager.find(TransactionEntity.class, 1L);

        assertThat(deletedTransaction).isNull();
    }
}
