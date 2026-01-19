package by.pirog.CRM.service;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.exception.SellerNotFoundException;
import by.pirog.CRM.mapper.SellerMapper;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.repository.SellerRepository;
import by.pirog.CRM.utils.SellerUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceImplTests {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @Test
    @DisplayName("Test get all sellers functionality")
    void givenTwoSellers_whenGetAll_thenReturnResponseSellerDtoList(){
        // given
        var seller1 = SellerUtils.getSellerEntityPersistent();
        var seller2 = SellerUtils.getSellerSecondEntityPersistent();

        List<SellerEntity> sellers = List.of(seller1, seller2);
        when(sellerRepository.findAll()).thenReturn(sellers);

        var response1 = mock(SellerResponseDto.class);
        var response2 = mock(SellerResponseDto.class);

        List<SellerResponseDto> responseDtoList = List.of(response1, response2);
        when(sellerMapper.toListResponseDto(anyList())).thenReturn(responseDtoList);

        // when
        var response = sellerService.getAllSellers();

        // then
        verify(sellerRepository).findAll();
        verify(sellerMapper).toListResponseDto(sellers);
        assertSame(responseDtoList, response);
    }

    @Test
    @DisplayName("Test create new seller functionality")
    void givenCreateRequestDto_whenCreateNewSeller_thenCreatedNewEntity(){
        // given
        SellerCreateRequestDto request = new SellerCreateRequestDto(
                "Best Seller",
                "Contact info"
        );
        var sellerToSave = SellerUtils.getSellerEntityTransient();
        when(sellerMapper.createSellerRequestToSellerEntity(any(SellerCreateRequestDto.class)))
                .thenReturn(sellerToSave);
        when(sellerRepository.save(any(SellerEntity.class)))
                .thenReturn(SellerUtils.getSellerEntityPersistent());

        var response = mock(SellerResponseDto.class);
        when(sellerMapper.sellerToResponseDto(any(SellerEntity.class)))
                .thenReturn(response);
        // when
        var result = sellerService.createNewSeller(request);

        // then
        verify(sellerRepository, times(1)).save(any(SellerEntity.class));
        assertSame(response, result);
    }

    @Test
    @DisplayName("Test seller find by id functionality")
    void givenSellerId_whenFindById_thenReturnResponseDto(){
        // given
        Long sellerId = 1L;
        SellerEntity seller = SellerUtils.getSellerEntityPersistent();

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.of(seller));

        var expectedResult = mock(SellerResponseDto.class);
        when(sellerMapper.sellerToResponseDto(any(SellerEntity.class)))
                .thenReturn(expectedResult);

        // when
        var actualResult = sellerService.findSellerById(sellerId);
        // then
        assertSame(expectedResult, actualResult);
        verify(sellerRepository).findById(sellerId);
    }

    @Test
    @DisplayName("Test seller find by id functionality seller not found")
    void givenSellerId_whenFindById_thenReturnNotFoundException(){
        // given
        Long sellerId = 1L;

        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        assertThrows(SellerNotFoundException.class, () -> {
           sellerService.findSellerById(sellerId);
        });
    }


    @Test
    @DisplayName("Test delete by id functionality")
    void givenSellerId_whenDeleteById_thenSellerIsRemoved(){
        // given
        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.of(SellerUtils.getSellerEntityPersistent()));
        // when
        sellerService.deleteById(1L);

        // then
        verify(sellerRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    @DisplayName("Test delete by id functionality seller not found exception")
    void givenSellerId_whenDeleteById_thenSellerNotFoundException(){
        // given
        when(sellerRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        //
        assertThrows(SellerNotFoundException.class, () -> {
            sellerService.deleteById(1L);
        });
    }

    @Test
    @DisplayName("Test update seller functionality")
    void givenUpdateSellerDto_whenUpdateSeller_thenRepositoryIsCalled(){
        // given
        Long sellerId = 1L;
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                "Seller",
                null
        );
        var seller = SellerUtils.getSellerEntityPersistent();

        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));
        var expectedResult = mock(SellerResponseDto.class);
        when(sellerMapper.sellerToResponseDto(any(SellerEntity.class)))
                .thenReturn(expectedResult);

        // when
        var actualResult = sellerService.updateSeller(sellerId, dto);

        // then
        assertSame(expectedResult, actualResult);
        verify(sellerMapper).updateSellerEntityFromRequestDto
                (any(SellerUpdateRequestDto.class), any(SellerEntity.class));
        verify(sellerRepository).save(seller);
    }

    @Test
    @DisplayName("Test seller not found when update seller functionality")
    void givenUpdateSellerDto_whenUpdateSeller_thenSellerNotFound(){
        // given
        Long sellerId = 1L;
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                "Seller",
                null
        );
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(SellerNotFoundException.class, () -> {
            sellerService.updateSeller(sellerId, dto);
        });
    }

    @Test
    @DisplayName("Test replace seller functionality")
    void givenUpdateSellerDtoWithNullFields_whenReplaceSeller_thenSellerReplaceFields(){
        // given
        Long sellerId = 1L;
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                null,
                null
        );
        SellerEntity seller = SellerUtils.getSellerEntityPersistent();

        when(sellerRepository.findById(anyLong())).thenReturn(Optional.of(seller));

        var expectedResult = mock(SellerResponseDto.class);
        when(sellerMapper.sellerToResponseDto(any(SellerEntity.class)))
                .thenReturn(expectedResult);

        // when
        var actualResult = sellerService.replaceSeller(sellerId, dto);
        // then
        assertSame(expectedResult, actualResult);
        assertThat(seller.getContactInfo()).isNull();
        assertThat(seller.getName()).isNull();
        verify(sellerRepository).save(seller);
    }

    @Test
    @DisplayName("Test seller not found when replace seller functionality")
    void givenUpdateSellerDto_whenReplaceSeller_thenSellerNotFound(){
        // given
        Long sellerId = 1L;
        SellerUpdateRequestDto dto = new SellerUpdateRequestDto(
                "Seller",
                null
        );
        when(sellerRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(SellerNotFoundException.class, () -> {
            sellerService.replaceSeller(sellerId, dto);
        });
    }
}
