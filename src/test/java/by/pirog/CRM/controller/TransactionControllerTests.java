package by.pirog.CRM.controller;

import by.pirog.CRM.exception.TransactionNotFoundException;
import by.pirog.CRM.service.TransactionService;
import by.pirog.CRM.utils.SellerUtils;
import by.pirog.CRM.utils.TransactionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTests {

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test get list transactions functionality")
    void getTransactions_shouldReturnCollectionTransactions() throws Exception{
        // given
        var transaction1 = TransactionUtils.getTransactionResponseDto(1L);
        var transaction2 = TransactionUtils.getSecondTransactionResponseDto(2L);

        when(transactionService.getTransactionsList())
                .thenReturn(List.of(transaction1, transaction2));
        // when
        ResultActions result = mockMvc.perform(get("/api/transactions"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Test get transaction by id functionality")
    void getTransactionById_shouldReturnTransaction() throws Exception{
        // given
        var transaction1 = TransactionUtils.getTransactionResponseDto(1L);
        when(transactionService.getTransactionById(anyLong()))
                .thenReturn(transaction1);

        // when
        ResultActions result = mockMvc.perform(get("/api/transactions/%d"
                .formatted(transaction1.getId())));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(transaction1.getAmount()))
                .andExpect(jsonPath("$.paymentType").value(transaction1.getPaymentType().toString()))
                .andExpect(jsonPath("$.sellerId").value(transaction1.getSellerId()));

        verify(transactionService).getTransactionById(transaction1.getId());
    }
    @Test
    @DisplayName("Test get transaction by id not found functionality")
    void givenTransactionById_transactionNotFound_shouldReturnTransactionNotFoundStatus() throws Exception{
        // given
        Long transactionId = 1L;
        when(transactionService.getTransactionById(anyLong()))
                .thenThrow(new TransactionNotFoundException("Transaction not found"));
        // when
        ResultActions result = mockMvc
                .perform(get("/api/transactions/%d".formatted(transactionId)));
        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/transactions/%d".formatted(transactionId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.GET.name()));
        verify(transactionService).getTransactionById(transactionId);
    }

    @Test
    @DisplayName("Test delete transaction by id functionality")
    void givenTransactionId_whenDeleteTransaction_thenSuccessResult()throws Exception{
        // given
        Long transactionId = 1L;

        // when
        ResultActions result = mockMvc
                .perform(delete("/api/transactions/%d".formatted(transactionId)));
        // then
        result.andExpect(status().isNoContent());

        verify(transactionService).deleteTransactionById(transactionId);
    }

    @Test
    @DisplayName("Test delete transaction by id transaction not found exception")
    void givenTransactionId_whenDeleteTransaction_thenNotFoundStatus() throws Exception{
        // given
        Long transactionId = 1L;

        doThrow(new TransactionNotFoundException("Transaction not found"))
                .when(transactionService).deleteTransactionById(anyLong());

        // when
        ResultActions result = mockMvc
                .perform(delete("/api/transactions/%d".formatted(transactionId)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/transactions/%d".formatted(transactionId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.DELETE.name()));
        verify(transactionService).deleteTransactionById(transactionId);
    }
}
