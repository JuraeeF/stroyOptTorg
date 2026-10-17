package com.example.stroy.controller;

import com.example.stroy.dto.CategoryResponse;
import com.example.stroy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Kategoriyalar", description = "Mahsulot toifalarini ko'rish")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Barcha kategoriyalar", description = "Barcha faol kategoriyalar ro'yxatini qaytaradi")
    @ApiResponse(responseCode = "200", description = "Kategoriyalar ro'yxati")
    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.list();
    }

    @Operation(summary = "Kategoriya tafsilotlari", description = "ID bo'yicha bitta kategoriyani qaytaradi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kategoriya topildi"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @GetMapping("/{id}")
    public CategoryResponse get(@Parameter(description = "Kategoriya ID raqami") @PathVariable Long id) {
        return categoryService.get(id);
    }
}
