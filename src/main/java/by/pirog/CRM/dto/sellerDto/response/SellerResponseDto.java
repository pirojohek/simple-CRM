package by.pirog.CRM.dto.sellerDto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Информация о продавце")
public class SellerResponseDto {

    @Schema(description = "ID продавца", example = "1")
    private Long id;

    @Schema(description = "Имя продавца", example = "Иван Иванов")
    private String name;

    @Schema(description = "Контактная информация")
    private String contactInfo;

    @Schema(description = "Дата регистрации", example = "2024-01-15T10:30:00")
    private LocalDateTime registrationDate;
}
