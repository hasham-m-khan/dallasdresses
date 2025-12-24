package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.request.ItemImageCreateRequest;
import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.services.ItemImageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${app.api.baseurl}/images")
public class ItemImageController {

    private final ItemImageServiceImpl imageService;

    public ItemImageController(ItemImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    @GetMapping("")
    public ApiResponse<List<ItemImageDto>> getImages() {
        log.info("🧲 Getting all images");

        List<ItemImageDto> itemImages = imageService.getAllImages();

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("filtered", false);
        metaData.put("filterType", "none");
        metaData.put("totalCount", itemImages.size());

        return ApiResponse.<List<ItemImageDto>>builder()
                .success(true)
                .data(itemImages)
                .metadata(metaData)
                .message("Images retrieved successfully")
                .build();
    }

    @PostMapping("")
    public ApiResponse<ItemImageDto> createImage(@RequestBody ItemImageCreateRequest request) {
        log.info("🔔 Creating Image: {}", request);

        return ApiResponse.<ItemImageDto>builder()
                .success(true)
                .data(imageService.createImage(request))
                .metadata(new HashMap<>())
                .message("Image created successfully")
                .build();
    }
}
