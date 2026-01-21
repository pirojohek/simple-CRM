package by.pirog.CRM.dto.transactionDto.response;

import by.pirog.CRM.enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponseDto {
    private Long id;

    private Long sellerId;

    private BigDecimal amount;

    private PaymentType paymentType;

    private LocalDateTime transactionDate;
}
