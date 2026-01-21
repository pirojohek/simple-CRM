package by.pirog.CRM.service;

import by.pirog.CRM.dto.transactionDto.request.TransactionCreateRequestDto;
import by.pirog.CRM.dto.transactionDto.response.TransactionResponseDto;
import by.pirog.CRM.exception.TransactionNotFoundException;
import by.pirog.CRM.mapper.TransactionMapper;
import by.pirog.CRM.storage.entity.SellerEntity;
import by.pirog.CRM.storage.entity.TransactionEntity;
import by.pirog.CRM.storage.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final SellerService sellerService;

    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        TransactionEntity transaction = this.repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id: " + id + " not found"));
        return mapper.transactionEntityToResponseDto(transaction);
    }

    @Override
    public List<TransactionResponseDto> getTransactionsList(){
        List<TransactionEntity> transactions = this.repository.findAll();
        return mapper.transactionsToListResponseDto(transactions);
    }

    @Override
    public void deleteTransactionById(Long id) {
        this.repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id: " + id + " not found"));
        this.repository.deleteById(id);
    }

    @Override
    public TransactionResponseDto createTransaction(Long sellerId, TransactionCreateRequestDto requestDto) {
        SellerEntity seller = this.sellerService.getSellerEntityById(sellerId);

        TransactionEntity entity = TransactionEntity.builder()
                .seller(seller)
                .amount(requestDto.amount())
                .paymentType(requestDto.paymentType())
                .build();

        this.repository.save(entity);

        return this.mapper.transactionEntityToResponseDto(entity);
    }

    @Override
    public List<TransactionResponseDto> getSellerTransactions(Long sellerId) {
        List<TransactionEntity> entities = this.repository
                .getTransactionEntitiesBySellerId(sellerId);
        return mapper.transactionsToListResponseDto(entities);
    }


}
