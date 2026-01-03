package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.services.ItemServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "${app.frontend.base.url}")
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

    @GetMapping({"/{id}"})
    public ApiResponse<ItemDto> getItem(@PathVariable Long id) {
        log.info("🧲 Fetching item with id: {}", id);

        ItemDto item =  itemService.getItemById(id);

        Map<String, Object> metadata =  new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "id");
        metadata.put("filterValue", id);

        return ApiResponse.<ItemDto>builder()
                .success(true)
                .data(item)
                .metadata(metadata)
                .message("Items retrieved successfully")
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<List<ItemDto>> getItemsByCategory(@PathVariable String slug) {
        log.info("🧲 Fetching items by category slug: {}", slug);

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
