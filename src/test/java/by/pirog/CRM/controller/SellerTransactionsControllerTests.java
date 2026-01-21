package by.pirog.CRM.controller;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.enums.PaymentType;
import by.pirog.CRM.exception.SellerNotFoundException;
import by.pirog.CRM.exception.TransactionNotFoundException;
import by.pirog.CRM.service.TransactionService;
import by.pirog.CRM.utils.TransactionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerTransactionsController.class)
public class SellerTransactionsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    @DisplayName("Test get transactions by sellerId functionality")
    void givenSellerId_whenGetSellerTransactions_thenSuccessResult() throws Exception {
        // given
        Long sellerId = 1L;
        var transaction1 = TransactionUtils.getTransactionResponseDto(sellerId);
        var transaction2 = TransactionUtils.getSecondTransactionResponseDto(sellerId);
        when(transactionService.getSellerTransactions(anyLong()))
                .thenReturn(List.of(transaction1, transaction2));

        // when
        ResultActions result = mockMvc.perform
                (get("/api/sellers/%d/transactions".formatted(sellerId)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(transactionService).getSellerTransactions(sellerId);
    }

    @Test
    @DisplayName("Test get transactions by seller id not found functionality")
    void givenSellerId_sellerNotFoundException_thenStatusNotFound() throws Exception {
        // given
        Long sellerId = 1L;

        when(transactionService.getSellerTransactions(anyLong()))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));

        // when
        ResultActions result = mockMvc.perform
                (get("/api/sellers/%d/transactions".formatted(sellerId)));
        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d/transactions".formatted(sellerId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.GET.name()));
        verify(transactionService).getSellerTransactions(sellerId);
    }

    @Test
    @DisplayName("Test create transaction functionality")
    void givenSellerIdAndCreateRequest_whenCreateTransaction_thenSuccessResponse() throws Exception {
        // given
        Long sellerId = 1L;
        var createdTransaction = TransactionUtils.getTransactionResponseDto(sellerId);
        when(transactionService.createTransaction(anyLong(), any(TransactionCreateRequestDto.class)))
                .thenReturn(createdTransaction);

        TransactionCreateRequestDto request = new TransactionCreateRequestDto(
                BigDecimal.valueOf(125.00),
                PaymentType.CARD
        );

        // when
        ResultActions result = mockMvc.perform(post("/api/sellers/%d/transactions".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/transactions/%d".formatted(createdTransaction.getId())))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(createdTransaction.getAmount()))
                .andExpect(jsonPath("$.paymentType").value(createdTransaction.getPaymentType().toString()))
                .andExpect(jsonPath("$.sellerId").value(createdTransaction.getSellerId()));
    }

    @Test
    @DisplayName("Test create transaction functionality seller not found")
    void givenSellerIdAndCreateRequest_whenCreateTransaction_thenSellerNotFound() throws Exception{
        // given
        Long sellerId = 1L;
        TransactionCreateRequestDto request = new TransactionCreateRequestDto(
                BigDecimal.valueOf(125.00),
                PaymentType.CARD
        );

        when(transactionService.createTransaction(anyLong(), any(TransactionCreateRequestDto.class)))
                .thenThrow(new SellerNotFoundException("Seller with id " + sellerId + " not found"));

        // when
        ResultActions result = mockMvc.perform(post("/api/sellers/%d/transactions".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d/transactions".formatted(sellerId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.POST.name()));
    }


    @Test
    @DisplayName("Test create transaction functionality transaction not valid ")
    void givenSellerIdAndCreateRequest_transactionNotValid_thenReturnValidationErrorStatus() throws Exception{
        // given
        Long sellerId = 1L;
        TransactionCreateRequestDto request = new TransactionCreateRequestDto(
                BigDecimal.valueOf(1255555555545.00),
                null
        );
        // when
        ResultActions result = mockMvc.perform(post("/api/sellers/%d/transactions".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        verify(transactionService, never()).createTransaction(anyLong(), any(TransactionCreateRequestDto.class));

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.method").value(HttpMethod.POST.name()))
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d/transactions".formatted(sellerId)))
                .andExpect(jsonPath("$.errors.length()").value(2));
    }
}
