package by.pirog.CRM.mapper;

import by.pirog.CRM.storage.entity.TransactionEntity;
import by.pirog.CRM.utils.SellerUtils;
import by.pirog.CRM.utils.TransactionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionMapperTests {

    private final TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    @DisplayName("Test entity to dto functionality")
    void givenTransactionEntity_whenTransactionEntityToResponseDto_thenCreateResponseDto() {
        // given
        var seller = SellerUtils.getSellerEntityPersistent();
        var transaction = TransactionUtils.getTransactionEntityPersistent(seller);

        // when
        var dto = mapper.transactionEntityToResponseDto(transaction);

        // then
        assertEquals(transaction.getId(), dto.getId());
        assertEquals(transaction.getSeller().getId(), dto.getSellerId());
        assertEquals(transaction.getTransactionDate(), dto.getTransactionDate());
        assertEquals(transaction.getPaymentType(), dto.getPaymentType());
        assertEquals(transaction.getAmount(), dto.getAmount());
    }
}
