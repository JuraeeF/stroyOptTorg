package com.example.stroy.controller;

import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.ProductResponse;
import com.example.stroy.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "3. Mahsulotlar", description = "Qurilish materiallari katalogi, qidiruv va filtrlash (ommaviy)")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Mahsulotlar katalogini qidirish va saralash",
            description = "Kategoriya, qidiruv so'zi, narx oralig'i va ombor qoldig'i bo'yicha filtrlash")
    public PageResponse<ProductResponse> list(
            @Parameter(description = "Kategoriya ID si") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv matni (nomi yoki artikul/SKU)") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Faqat omborda borlari") @RequestParam(defaultValue = "false") boolean inStock,
            @Parameter(description = "Saralash: newest | price_asc | price_desc | name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar limiti") @RequestParam(defaultValue = "12") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, inStock, false);
        return productService.search(filter, sort, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Mahsulot ma'lumotlari", description = "ID bo'yicha bitta mahsulot tafsilotlarini olish")
    public ProductResponse get(@PathVariable Long id) {
        return productService.get(id);
    }
}
