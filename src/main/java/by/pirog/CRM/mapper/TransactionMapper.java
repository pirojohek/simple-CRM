package by.pirog.CRM.mapper;


import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.storage.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "seller.id", target = "sellerId")
    TransactionResponseDto transactionEntityToResponseDto(TransactionEntity entity);

    default List<TransactionResponseDto> transactionsToListResponseDto(List<TransactionEntity> entities){
        if (entities == null || entities.isEmpty()){
            return List.of();
        }

        return entities.stream()
                .map(this::transactionEntityToResponseDto)
                .toList();
    }

}
