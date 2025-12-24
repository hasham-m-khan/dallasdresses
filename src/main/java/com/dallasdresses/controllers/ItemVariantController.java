package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.request.ItemVariantCreateRequest;
import com.dallasdresses.dtos.request.ItemVariantUpdateRequest;
import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.services.ItemVariantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${app.api.baseurl}/items/{itemId}/variants")
public class ItemVariantController {

    private final ItemVariantService variantService;

    public ItemVariantController(ItemVariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping({"", "/"})
    public ApiResponse<Page<ItemVariantDto>> getItemVariants (
            @PathVariable Long itemId,
            Pageable pageable) {
        log.info("🧲 Fetching ItemVariants by item id: {}", itemId);

        Page<ItemVariantDto> variantDtos = variantService.getVariantsByItem(itemId, pageable);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemId", itemId);
        metadata.put("createdAt", LocalDateTime.now());

        return ApiResponse.<Page<ItemVariantDto>>builder()
                .success(true)
                .data(variantDtos)
                .metadata(metadata)
                .message("Item variants retrieved successfully")
                .build();
    }

    @PostMapping({"", "/"})
    public ApiResponse<ItemVariantDto> createItemVariant(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemVariantCreateRequest req) {
        log.info("🔔 Creating new Item Variant- ItemId: {}, color: {}, size: {}",
                itemId, req.getColor(), req.getSize());

        if (itemId == null) {
            throw new RuntimeException("itemId is null");
        }

        ItemVariantDto variantDto = variantService.createItemVariant(itemId, req);

        log.info("🔔 Created Item Variant ({}) for item ({})", variantDto.getId(), itemId);

        return ApiResponse.<ItemVariantDto>builder()
                .success(true)
                .data(variantDto)
                .metadata(new HashMap<>())
                .message("Item variant created successfully")
                .build();
    }

    @PostMapping("/{variantId}")
    public ApiResponse<ItemVariantDto> updateItemVariant(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @RequestBody ItemVariantUpdateRequest req) {
        log.info("🔔 Updating Item Variant- Item id: {}, Variant id: {}, Request: {}",
                itemId, variantId, req);

        ItemVariantDto updatedVariant = variantService.updateItemVariant(itemId, variantId, req);

        log.info("🧶 Updated Item Variant ({}) for item ({})", updatedVariant.getId(), itemId);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemId", itemId);
        metadata.put("createdAt", LocalDateTime.now());

        return ApiResponse.<ItemVariantDto>builder()
                .success(true)
                .data(updatedVariant)
                .metadata(metadata)
                .message("Item variant updated successfully")
                .build();
    }

    @DeleteMapping("/{variantId}")
    public ApiResponse<String> deleteItemVariant(@PathVariable Long itemId, @PathVariable Long variantId) {
        log.info("🔔 Delete item variant with id '{}'",  variantId);

        variantService.deleteItemVariant(variantId);

        log.info("💀 Item variant with id '{}' deleted successfully", variantId);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemId", itemId);
        metadata.put("variantId", variantId);
        metadata.put("updatedAt", LocalDateTime.now());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Item variant deleted successfully")
                .metadata(metadata)
                .build();
    }
}
