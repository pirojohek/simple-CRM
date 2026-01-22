package by.pirog.CRM.dto.transactionDto.request;

import by.pirog.CRM.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Запрос на создание новой транзакции")
public record TransactionCreateRequestDto(

        @Schema(description = "Сумма транзакции", example = "1500.50", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @DecimalMin(value = "0.00", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        @Positive
        BigDecimal amount,

        @Schema(
                description = "Тип оплаты",
                example = "CARD",
                allowableValues = {"CASH", "CARD", "TRANSFER"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        PaymentType paymentType
) {
}
