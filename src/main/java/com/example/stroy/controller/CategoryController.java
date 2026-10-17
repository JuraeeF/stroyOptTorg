package com.example.stroy.controller;

import com.example.stroy.dto.CategoryResponse;
import com.example.stroy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "2. Kategoriyalar", description = "Katalog kategoriyalarini ko'rish (ommaviy)")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Barcha kategoriyalar ro'yxati", description = "Faol kategoriyalar ro'yxatini alifbo tartibida olish")
    public List<CategoryResponse> list() {
        return categoryService.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Kategoriya ma'lumotlari", description = "ID bo'yicha bitta kategoriyani olish")
    public CategoryResponse get(@PathVariable Long id) {
        return categoryService.get(id);
    }
}
