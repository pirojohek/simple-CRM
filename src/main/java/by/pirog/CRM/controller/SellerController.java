package by.pirog.CRM.controller;

import by.pirog.CRM.dto.sellerDto.request.SellerCreateRequestDto;
import by.pirog.CRM.dto.sellerDto.request.SellerUpdateRequestDto;
import by.pirog.CRM.dto.sellerDto.response.SellerResponseDto;
import by.pirog.CRM.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sellers")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<SellerResponseDto>> getSellersCollection() {
        var sellers = this.sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{sellerId:\\d+}")
    public ResponseEntity<SellerResponseDto> getSeller(@PathVariable Long sellerId) {
        var seller = this.sellerService.findSellerById(sellerId);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<SellerResponseDto> createSeller(@RequestBody SellerCreateRequestDto dto) {
        SellerResponseDto response = this.sellerService.createNewSeller(dto);
        return ResponseEntity.created(URI.create("/api/sellers/%d".formatted(response.getId())))
                .body(response);
    }

    @DeleteMapping("/{sellerId:\\d+}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long sellerId) {
        this.sellerService.deleteById(sellerId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{sellerId:\\d+}")
    public ResponseEntity<SellerResponseDto> updateSeller(@PathVariable Long sellerId, @RequestBody SellerUpdateRequestDto dto) {
        var response = this.sellerService.updateSeller(sellerId, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{sellerId:\\d+}")
    public ResponseEntity<SellerResponseDto> replaceSeller(@PathVariable Long sellerId, @RequestBody SellerUpdateRequestDto dto) {
        var response = this.sellerService.replaceSeller(sellerId, dto);
        return ResponseEntity.ok(response);
    }
}
