package by.pirog.CRM.mapper;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.utils.SellerUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SellerMapperTests {

    private final SellerMapper mapper = Mappers.getMapper(SellerMapper.class);

    @Test
    @DisplayName("sellerToResponseDto should map entity to Dto")
    void givenSellerEntity_whenSellerToResponseDto_shouldCreateResponseDto() {
        // given
        LocalDateTime registrationTime = LocalDateTime.now();
        SellerEntity seller = SellerEntity.builder()
                .id(1L)
                .name("Name")
                .contactInfo("Contact Info")
                .registrationDate(registrationTime)
                .build();

        // when
        SellerResponseDto dto = mapper.sellerToResponseDto(seller);

        // then
        assertEquals(seller.getId(), dto.getId());
        assertEquals(seller.getName(), dto.getName());
        assertEquals(seller.getContactInfo(), dto.getContactInfo());
        assertEquals(seller.getRegistrationDate(), dto.getRegistrationDate());
    }

    @Test
    @DisplayName("createSellerRequestToSellerEntity should map create request to New Entity")
    void givenSellerRequest_whenCreateSellerRequestToSellerEntity_thenCreateNewEntity() {
        // given
        SellerCreateRequestDto dto = new SellerCreateRequestDto("Name", "info");

        // when
        SellerEntity entity = mapper.createSellerRequestToSellerEntity(dto);

        // then
        assertNull(entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.contactInfo(), entity.getContactInfo());
        assertNull(entity.getRegistrationDate());
    }

    @Test
    @DisplayName("updateSellerEntityFromRequestDto should map new fields to entity")
    void givenSellerUpdateRequest_whenUpdateSellerEntityFromRequestDto_thenUpdateFields() {
        // given
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                "Name",
                "Contact"
        );
        Long id = 1L;
        LocalDateTime date = LocalDateTime.now();

        SellerEntity entity = SellerEntity.builder()
                .name("Seller")
                .contactInfo("Contact Info")
                .registrationDate(date)
                .id(id)
                .build();

        // when
        mapper.updateSellerEntityFromRequestDto(dto, entity);

        // then
        assertEquals(id, entity.getId());
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.contactInfo(), entity.getContactInfo());
        assertEquals(date, entity.getRegistrationDate());
    }

    @Test
    @DisplayName("updateSellerEntityFromRequestDto with null fields should not update entity")
    void givenSellerUpdateRequestWithNullFields_whenUpdateSellerEntityFromRequestDto_thenNoUpdatesFields() {
        // given
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                null,
                null
        );
        Long id = 1L;
        LocalDateTime date = LocalDateTime.now();
        String name = "Seller";
        String contactInfo = "Contact Info";
        SellerEntity entity = SellerEntity.builder()
                .name(name)
                .contactInfo(contactInfo)
                .registrationDate(date)
                .id(id)
                .build();

        // when
        mapper.updateSellerEntityFromRequestDto(dto, entity);

        // then
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(date, entity.getRegistrationDate());
    }

    @Test
    @DisplayName("toListResponseDto should return List Dtos from List Entities")
    void givenListEntities_whenToListResponseDto_thenReturnListResponseDto() {
        // given
        var entity1 = SellerUtils.getSellerEntityPersistent();
        var entity2 = SellerUtils.getSellerSecondEntityPersistent();

        // when
        List<SellerResponseDto> sellerResponseDtoList =
                mapper.toListResponseDto(List.of(entity1, entity2));
        // then
        assertThat(sellerResponseDtoList).hasSize(2);
    }
}