package by.pirog.CRM.controller;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.exception.SellerNotFoundException;
import by.pirog.CRM.service.SellerService;
import by.pirog.CRM.utils.SellerUtils;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
public class SellerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SellerService sellerService;

    @Test
    @DisplayName("Test get seller by id functionality")
    void getSeller_shouldReturnSellerResponse() throws Exception {
        // given
        Long id = 1L;
        var sellerDto = SellerUtils.getSellerResponseDto();
        when(sellerService.findSellerById(anyLong()))
                .thenReturn(sellerDto);

        // when
        ResultActions result = mockMvc.perform(get("/api/sellers/%d".formatted(id)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(sellerDto.getName()))
                .andExpect(jsonPath("$.contactInfo").value(sellerDto.getContactInfo()));

    }

    @Test
    @DisplayName("Test get seller by id functionality seller not found")
    void givenSellerId_whenGetSeller_thenErrorNotFoundResponse() throws Exception {
        // given
        Long id = 999L;
        when(sellerService.findSellerById(anyLong()))
                .thenThrow(new SellerNotFoundException("Seller with id " + id + " not found"));

        // when
        ResultActions result = mockMvc.perform(get("/api/sellers/%d".formatted(id)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d".formatted(id)))
                .andExpect(jsonPath("$.method").value(HttpMethod.GET.name()));

    }

    @Test
    @DisplayName("Test get sellers functionality")
    void getSellers_shouldReturnCollectionSellers() throws Exception {
        // given
        var firstSeller = SellerUtils.getSellerResponseDto();
        var secondSeller = SellerUtils.getSecondSellerResponseDto();

        when(sellerService.getAllSellers())
                .thenReturn(List.of(firstSeller, secondSeller));

        // when
        ResultActions result = mockMvc.perform(get("/api/sellers"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].name").value(firstSeller.getName()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").value(secondSeller.getName()));

    }

    @Test
    @DisplayName("Test create seller functionality")
    void givenSellerDto_whenCreateSeller_thenCreatedResponse() throws Exception {
        // given
        var createSellerRequest = new SellerCreateRequestDto(
                "Seller",
                "Contact Info"
        );
        var createdResponse = SellerUtils.getSellerResponseDto();
        when(sellerService.createNewSeller(any(SellerCreateRequestDto.class)))
                .thenReturn(createdResponse);
        // when
        ResultActions result = mockMvc.perform(post("/api/sellers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSellerRequest)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/sellers/%d".formatted(createdResponse.getId())))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(createdResponse.getName()))
                .andExpect(jsonPath("$.contactInfo").value(createdResponse.getContactInfo()));
    }

    @Test
    @DisplayName("Test delete seller functionality")
    void givenIdSeller_whenDeleteSeller_thenNoContentResponse() throws Exception {
        // given
        Long sellerId = 1L;

        // when
        mockMvc.perform(delete("/api/sellers/%d".formatted(sellerId)))
                .andExpect(status().isNoContent());

        // then
        verify(sellerService, times(1)).deleteById(sellerId);
    }

    @Test
    @DisplayName("Test delete seller functionality seller not found")
    void givenSellerId_whenDeleteSeller_thenNotFoundException() throws Exception {
        // given
        Long sellerId = 999L;
        doThrow(new SellerNotFoundException("Seller with id " + sellerId + " not found"))
                .when(sellerService).deleteById(sellerId);

        // when
        ResultActions result = mockMvc.perform(delete("/api/sellers/%d".formatted(sellerId)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d".formatted(sellerId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.DELETE.name()));

    }


    @Test
    @DisplayName("Test update seller functionality")
    void givenSellerIdAndSellerUpdateRequest_whenUpdateSeller_thenSuccessResult() throws Exception {
        // given
        Long sellerId = 1L;
        var updateSellerRequest = new SellerUpdateRequestDto(
                "New Seller",
                null
        );
        var sellerResponseDto = SellerUtils.getSellerResponseDto();
        when(sellerService.updateSeller(anyLong(), any(SellerUpdateRequestDto.class)))
                .thenReturn(sellerResponseDto);

        // when
        ResultActions result = mockMvc.perform(patch("/api/sellers/%d".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSellerRequest)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(sellerResponseDto.getName()))
                .andExpect(jsonPath("$.contactInfo").value(sellerResponseDto.getContactInfo()));
    }

    @Test
    @DisplayName("Test update seller functionality not found exception")
    void givenSellerIdAndUpdateSellerRequest_whenUpdateSeller_thenNotFoundException() throws Exception{
        // given
        Long sellerId = 1L;
        var updateSellerRequest = new SellerUpdateRequestDto(
                "New Seller",
                null
        );
        when(sellerService.updateSeller(anyLong(), any(SellerUpdateRequestDto.class)))
                .thenThrow(new SellerNotFoundException("Seller with id " + sellerId + " not found"));

        // when
        ResultActions result = mockMvc.perform(patch("/api/sellers/%d".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSellerRequest)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d".formatted(sellerId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.PATCH.name()));
    }

    @Test
    @DisplayName("Test replace seller functionality")
    void givenSellerIdAndUpdateSellerRequest_whenUpdateSeller_thenSuccessResult() throws Exception{
        // given
        Long sellerId = 1L;
        var updateSellerRequest = new SellerUpdateRequestDto(
                "New Seller",
                null
        );
        var sellerResponseDto = SellerUtils.getSellerResponseDto();
        when(sellerService.replaceSeller(anyLong(), any(SellerUpdateRequestDto.class)))
                .thenReturn(sellerResponseDto);

        // when
        ResultActions result = mockMvc.perform(put("/api/sellers/%d".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSellerRequest)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(sellerResponseDto.getName()))
                .andExpect(jsonPath("$.contactInfo").value(sellerResponseDto.getContactInfo()));
    }

    @Test
    @DisplayName("Test replace seller functionality not found exception")
    void givenSellerIdAndUpdateSellerRequest_whenReplaceSeller_thenNotFoundException() throws Exception{
        // given
        Long sellerId = 1L;
        var updateSellerRequest = new SellerUpdateRequestDto(
                "New Seller",
                null
        );
        when(sellerService.replaceSeller(anyLong(), any(SellerUpdateRequestDto.class)))
                .thenThrow(new SellerNotFoundException("Seller with id " + sellerId + " not found"));

        // when
        ResultActions result = mockMvc.perform(put("/api/sellers/%d".formatted(sellerId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSellerRequest)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.instance").value("/api/sellers/%d".formatted(sellerId)))
                .andExpect(jsonPath("$.method").value(HttpMethod.PUT.name()));
    }
}
