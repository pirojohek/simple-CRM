package by.pirog.CRM.service;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;

import java.util.List;

public interface SellerService {

    SellerResponseDto createNewSeller(SellerCreateRequestDto request);

    SellerResponseDto findSellerById(Long id);

    void deleteById(Long id);

    List<SellerResponseDto> getAllSellers();

}
