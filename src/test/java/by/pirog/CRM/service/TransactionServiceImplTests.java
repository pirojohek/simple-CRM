package by.pirog.CRM.service;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.enums.PaymentType;
import by.pirog.CRM.exception.TransactionNotFoundException;
import by.pirog.CRM.mapper.TransactionMapper;
import by.pirog.CRM.storage.entity.TransactionEntity;
import by.pirog.CRM.storage.repository.TransactionRepository;
import by.pirog.CRM.utils.SellerUtils;
import by.pirog.CRM.utils.TransactionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTests {

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @Mock
    private SellerServiceImpl sellerService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Test get transaction by id functionality")
    void givenTransactionId_whenFindById_thenTransactionFound() {
        // given
        Long id = 1L;
        var seller = SellerUtils.getSellerEntityPersistent();
        var transaction = TransactionUtils.getTransactionEntityPersistent(seller);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));
        var expectedResult = mock(TransactionResponseDto.class);
        when(mapper.transactionEntityToResponseDto(any(TransactionEntity.class)))
                .thenReturn(expectedResult);

        // when
        var result = transactionService.getTransactionById(id);

        // then
        assertSame(expectedResult, result);
        verify(repository).findById(id);
        verify(mapper).transactionEntityToResponseDto(transaction);
    }

    @Test
    @DisplayName("Test get transaction by id not found functionality")
    void givenTransactionId_whenFindById_thenTransactionNotFoundException() {
        // given
        Long id = 1L;
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(id)
        );
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Test get all transactions functionality")
    void givenTwoTransactions_whenGetTransactionsList_thenSuccessResult() {
        // given
        var seller = SellerUtils.getSellerEntityPersistent();
        var transaction1 = TransactionUtils.getTransactionEntityPersistent(seller);
        var transaction2 = TransactionUtils.getTransactionSecondEntityPersistent(seller);
        var transactions = List.of(transaction1, transaction2);
        when(repository.findAll())
                .thenReturn(transactions);

        var transaction1Response = mock(TransactionResponseDto.class);
        var transaction2Response = mock(TransactionResponseDto.class);

        List<TransactionResponseDto> expectedResult =
                List.of(transaction1Response, transaction2Response);

        when(mapper.transactionsToListResponseDto(anyList()))
                .thenReturn(expectedResult);
        // when
        var response = transactionService.getTransactionsList();

        // then
        assertSame(expectedResult, response);
        verify(repository).findAll();
        verify(mapper).transactionsToListResponseDto(transactions);
    }

    @Test
    @DisplayName("Test delete transaction by id functionality")
    void givenTransactionId_whenDeleteTransactionById_thenTransactionDeleted() {
        // given
        Long id = 1L;
        var seller = SellerUtils.getSellerEntityPersistent();
        var transaction = TransactionUtils.getTransactionEntityPersistent(seller);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));

        // when
        transactionService.deleteTransactionById(id);

        // then
        verify(repository).findById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Test delete transaction by id transaction not found exception")
    void givenTransactionId_whenDeleteById_thenTransactionNotFoundException() {
        // given
        Long id = 1L;
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.deleteTransactionById(id)
        );
        verify(repository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Test create transaction functionality")
    void givenSellerIdAndRequest_whenCreateTransaction_thenTransactionIsCreated(){
        // given
        Long sellerId = 1L;
        var seller = SellerUtils.getSellerEntityPersistent();
        var createTransactionRequestDto = new TransactionCreateRequestDto(
                BigDecimal.valueOf(123.0),
                PaymentType.CARD
        );

        var expectedResult = mock(TransactionResponseDto.class);
        when(sellerService.getSellerEntityById(anyLong()))
                .thenReturn(seller);
        when(mapper.transactionEntityToResponseDto(any(TransactionEntity.class)))
                .thenReturn(expectedResult);

        // when
        var actualResult = transactionService.createTransaction(sellerId, createTransactionRequestDto);

        // then
        ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);

        verify(repository).save(captor.capture());

        TransactionEntity savedEntity = captor.getValue();

        assertSame(savedEntity.getSeller(), seller);
        assertThat(savedEntity.getAmount()).isEqualTo(createTransactionRequestDto.amount());
        assertThat(savedEntity.getPaymentType()).isEqualTo(createTransactionRequestDto.paymentType());
        verify(sellerService).getSellerEntityById(sellerId);
        assertSame(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Test get seller transactions functionality")
    void givenSellerId_whenGetSellerTransactions_thenReturnsSellerTransactions(){
        // given
        Long id = 1L;
        var seller = SellerUtils.getSellerEntityPersistent();
        var transaction1 = TransactionUtils.getTransactionEntityPersistent(seller);
        var transaction2 = TransactionUtils.getTransactionSecondEntityPersistent(seller);
        var listTransactions = List.of(transaction1, transaction2);
        when(repository.getTransactionEntitiesBySellerId(anyLong()))
                .thenReturn(listTransactions);

        var response1 = mock(TransactionResponseDto.class);
        var response2 = mock(TransactionResponseDto.class);

        var expectedResult = List.of(response1, response2);
        when(mapper.transactionsToListResponseDto(anyList()))
                .thenReturn(expectedResult);

        // when
        var result = transactionService.getSellerTransactions(id);

        // then
        assertSame(expectedResult, result);
        verify(repository).getTransactionEntitiesBySellerId(id);
        verify(mapper).transactionsToListResponseDto(listTransactions);
    }
}
