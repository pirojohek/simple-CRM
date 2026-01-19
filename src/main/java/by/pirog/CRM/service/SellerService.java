package by.pirog.CRM.service;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;

import java.util.List;

public interface SellerService {

    SellerResponseDto createNewSeller(SellerCreateRequestDto request);

    SellerResponseDto findSellerById(Long id);

    void deleteById(Long id);

    List<SellerResponseDto> getAllSellers();

    // PATCH
    SellerResponseDto updateSeller(Long sellerId, SellerUpdateRequestDto dto);

    // PUT
    SellerResponseDto replaceSeller(Long sellerId, SellerUpdateRequestDto dto);
}
