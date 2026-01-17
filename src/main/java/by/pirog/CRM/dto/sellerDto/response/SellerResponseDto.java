package by.pirog.CRM.dto.sellerDto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDto {

    private Long id;

    private String name;

    private String contactInfo;

    private LocalDateTime registrationDate;
}
