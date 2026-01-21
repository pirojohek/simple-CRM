package by.pirog.CRM.service;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.exception.SellerNotFoundException;
import by.pirog.CRM.mapper.SellerMapper;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public SellerResponseDto findSellerById(Long id) {
        Optional<SellerEntity> entity = this.sellerRepository.findById(id);

        if (entity.isEmpty()){
            throw new SellerNotFoundException("Seller with id: " + id + "not found");
        }

        return sellerMapper.sellerToResponseDto(entity.get());
    }

    @Override
    public void deleteById(Long id) {
        this.sellerRepository.findById(id)
                .orElseThrow(() -> new SellerNotFoundException("Seller with id: " + id + "not found"));
        this.sellerRepository.deleteById(id);
    }

    @Override
    public SellerResponseDto updateSeller(Long sellerId, SellerUpdateRequestDto dto) {
        SellerEntity entity = this.sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller with id: " + sellerId + "not found"));

        sellerMapper.updateSellerEntityFromRequestDto(dto, entity);
        sellerRepository.save(entity);

        return sellerMapper.sellerToResponseDto(entity);
    }

    @Override
    public SellerResponseDto replaceSeller(Long sellerId, SellerUpdateRequestDto dto) {
        SellerEntity entity = this.sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller with id: " + sellerId + "not found"));

        entity.setContactInfo(dto.contactInfo());
        entity.setName(dto.name());
        sellerRepository.save(entity);

        return sellerMapper.sellerToResponseDto(entity);
    }

    @Override
    public SellerEntity getSellerEntityById(Long id) {
        return this.sellerRepository.findById(id)
                .orElseThrow(() -> new SellerNotFoundException("Seller with id: " + id + "not found"));
    }

}
