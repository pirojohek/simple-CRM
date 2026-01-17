package by.pirog.CRM.repository;

import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.repository.SellerRepository;
import by.pirog.CRM.utils.SellerUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SellerRepositoryTests {

    @Autowired
    private SellerRepository sellerRepository;

    @BeforeEach
    void setup() {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test save seller functionality")
    void givenSellerEntity_whenSave_thenSellerIsCreated() {
        // given
        SellerEntity sellerToSave = SellerUtils.getSellerEntityTransient();

        // when
        var savedSeller = sellerRepository.save(sellerToSave);
        System.out.println("Saved seller: " + savedSeller);
        // then
        assertThat(savedSeller).isNotNull();
        assertThat(savedSeller.getId()).isNotNull();
        assertThat(savedSeller.getRegistrationDate()).isNotNull();
        System.out.println(savedSeller.getRegistrationDate());
    }

    @Test
    @DisplayName("Test find seller by id functionality")
    void givenSellerId_whenFindById_thenSellerIsFound() {
        // given
        SellerEntity sellerToCreate = SellerUtils.getSellerEntityTransient();
        var savedSeller = sellerRepository.save(sellerToCreate);
        Long sellerId = savedSeller.getId();
        // when
        var foundSeller = sellerRepository.findById(sellerId)
                .orElse(null);

        // then
        assertThat(foundSeller).isNotNull();
        assertThat(foundSeller.getId()).isEqualTo(sellerId);
    }

    @Test
    @DisplayName("Test update seller functionality")
    void givenSellerToUpdate_whenSave_thenNameIsChanged() {
        // given
        String updatedName = "Updated Seller";

        SellerEntity sellerToCreate = SellerUtils.getSellerEntityTransient();
        var savedSeller = sellerRepository.save(sellerToCreate);
        // when
        SellerEntity sellerToUpdate = sellerRepository.findById(savedSeller.getId())
                .orElse(null);
        sellerToUpdate.setName(updatedName);

        var updatedSeller = sellerRepository.save(sellerToUpdate);

        // then
        assertThat(updatedSeller).isNotNull();
        assertThat(updatedSeller.getName()).isEqualTo(updatedName);

    }
}
