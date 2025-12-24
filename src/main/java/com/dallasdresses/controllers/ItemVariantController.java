package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.services.ItemVariantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ApiResponse.<Page<ItemVariantDto>>builder()
                .success(true)
                .data(variantDtos)
                .metadata(metadata)
                .message("Item variants retrieved successfully")
                .build();
    }
}
