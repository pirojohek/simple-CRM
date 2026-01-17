package by.pirog.CRM.service;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.mapper.SellerMapper;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    private final SellerMapper sellerMapper;


    @Override
    public List<SellerResponseDto> getAllSellers() {
        List<SellerEntity> sellers = this.sellerRepository.findAll();

        return this.sellerMapper.toListResponseDto(sellers);
    }

    @Override
    public SellerResponseDto createNewSeller(SellerCreateRequestDto request) {
        SellerEntity entity = this.sellerMapper.createSellerRequestToSellerEntity(request);

        var savedSeller = this.sellerRepository.save(entity);

        return this.sellerMapper.sellerToResponseDto(savedSeller);
    }
}
