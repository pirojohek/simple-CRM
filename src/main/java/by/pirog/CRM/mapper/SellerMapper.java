package by.pirog.CRM.mapper;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.storage.entity.SellerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerResponseDto sellerToResponseDto(SellerEntity seller);

    SellerEntity createSellerRequestToSellerEntity(SellerCreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contactInfo", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSellerEntityFromRequestDto(SellerUpdateRequestDto dto, @MappingTarget SellerEntity entity);

    default List<SellerResponseDto> toListResponseDto(List<SellerEntity> listEntities){
        if (listEntities == null || listEntities.isEmpty()){
            return List.of();
        }

        return listEntities.stream()
                .map(this::sellerToResponseDto)
                .toList();
    }
}
