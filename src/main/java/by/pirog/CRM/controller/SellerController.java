package by.pirog.CRM.controller;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sellers")
@Tag(name = "Sellers", description = "Управление продавцами")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping
    @Operation(summary = "Получить список всех продавцов")
    @ApiResponse(responseCode = "200", description = "Список продавцов успешно получен")
    public ResponseEntity<List<SellerResponseDto>> getSellersCollection() {
        var sellers = this.sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{sellerId:\\d+}")
    @Operation(summary = "Получить продавца по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продавец найден"),
            @ApiResponse(responseCode = "404", description = "Продавец не найден")
    })
    public ResponseEntity<SellerResponseDto> getSeller(
            @Parameter(description = "ID продавца", example = "1")
            @PathVariable Long sellerId) {
        var seller = this.sellerService.findSellerById(sellerId);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    @Operation(summary = "Создать нового продавца")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Продавец создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<SellerResponseDto> createSeller(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового продавца",
                    required = true
            )
            @Valid @RequestBody SellerCreateRequestDto dto) {
        SellerResponseDto response = this.sellerService.createNewSeller(dto);
        return ResponseEntity.created(URI.create("/api/sellers/%d".formatted(response.getId())))
                .body(response);
    }

    @DeleteMapping("/{sellerId:\\d+}")
    @Operation(summary = "Удалить продавца")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продавец успешно удален"),
            @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден")
    })
    public ResponseEntity<Void> deleteSeller(
            @Parameter(description = "ID продавца")
            @PathVariable Long sellerId) {
        this.sellerService.deleteById(sellerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{sellerId:\\d+}")
    @Operation(
            summary = "Частично обновить данные продавца",
            description = "Обновляет только указанные поля продавца (PATCH запрос)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные продавца успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public ResponseEntity<SellerResponseDto> updateSeller(
            @Parameter(description = "Уникальный идентификатор продавца", example = "1", required = true)
            @PathVariable Long sellerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления (только измененные поля)",
                    required = true
            )
            @RequestBody SellerUpdateRequestDto dto) {
        var response = this.sellerService.updateSeller(sellerId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{sellerId:\\d+}")
    @Operation(
            summary = "Полностью заменить данные продавца",
            description = "Заменяет все данные продавца новыми значениями (PUT запрос)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные продавца успешно заменены"),
            @ApiResponse(responseCode = "404", description = "Продавец с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public ResponseEntity<SellerResponseDto> replaceSeller(
            @Parameter(description = "Уникальный идентификатор продавца", example = "1", required = true)
            @PathVariable Long sellerId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Полный набор данных продавца для замены",
                    required = true
            )
            @RequestBody SellerUpdateRequestDto dto) {
        var response = this.sellerService.replaceSeller(sellerId, dto);
        return ResponseEntity.ok(response);
    }
}
