package by.pirog.CRM.dto.sellerDto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание нового продавца")
public record SellerCreateRequestDto(
        @Schema(description = "Имя продавца", example="Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @NotBlank
        @Size(min = 3)
        String name,

        @Schema(description = "Контактная информация")
        @Size(max = 2048)
        String contactInfo
) {
}
