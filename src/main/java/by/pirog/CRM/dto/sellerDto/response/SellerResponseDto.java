package by.pirog.CRM.dto.sellerDto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SellerResponseDto {

    private Long id;

    private String name;

    private String contactInfo;

    private LocalDateTime registrationDate;
}
