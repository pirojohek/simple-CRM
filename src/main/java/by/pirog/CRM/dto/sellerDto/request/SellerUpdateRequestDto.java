package by.pirog.CRM.dto.sellerDto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление продавца")
public record SellerUpdateRequestDto(

        @NotNull
        @NotBlank
        @Size(min = 3)
        String name,

        @Size(max = 2048)
        String contactInfo
) {
}
