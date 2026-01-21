package by.pirog.CRM.dto.sellerDto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SellerCreateRequestDto(

        @NotNull
        @Size(min = 3)
        String name,

        @Size(max = 2048)
        String contactInfo
) {
}
