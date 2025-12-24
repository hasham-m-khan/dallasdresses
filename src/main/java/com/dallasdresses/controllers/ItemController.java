package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.services.ItemServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${app.api.baseurl}/items")
public class ItemController {

    private final ItemServiceImpl itemService;

    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @GetMapping({"", "/"})
    public ApiResponse<List<ItemDto>> getItems() {
        log.info("🧲 Fetching all items");

        List<ItemDto> items = itemService.getAllItems();

        Map<String, Object> metadata =  new HashMap<>();
        metadata.put("filtered", false);
        metadata.put("totalCount", items.size());

        return ApiResponse.<List<ItemDto>>builder()
                .success(true)
                .data(items)
                .metadata(metadata)
                .message("Items retrieved successfully")
                .build();
    }

    @GetMapping("/{slug}")
    public ApiResponse<List<ItemDto>> getItems(@PathVariable String slug) {
        log.info("🧲 Fetching items by Slug {}", slug);

        List<ItemDto> items = itemService.getItemsByCategory(slug);

        Map<String, Object> metadata =  new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "category");
        metadata.put("filterValue", slug);
        metadata.put("totalCount", items.size());

        return ApiResponse.<List<ItemDto>>builder()
                .success(true)
                .data(items)
                .metadata(metadata)
                .message("Items retrieved successfully")
                .build();
    }
}
