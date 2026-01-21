package by.pirog.CRM.dto.transactionDto.request;

import by.pirog.CRM.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionCreateRequestDto(
        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        BigDecimal amount,

        @NotNull
        PaymentType paymentType
) {
}
