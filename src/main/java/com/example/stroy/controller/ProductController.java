package com.example.stroy.controller;

import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.ProductResponse;
import com.example.stroy.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "3. Mahsulotlar", description = "Mahsulotlar katalogi, qidirish, filtr va saralash")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Mahsulotlar katalogi (qidiruv, filtr, sahifalash)", description = """
            Qurilish mollari katalogini qidirish, filtrlash va saralash imkoniyati:
            - **sort parametrlari:** `newest` (eng yangi), `price_asc` (narx o'sish), `price_desc` (narx kamayish), `name` (nom bo'yicha)
            - **inStock:** omborda faqat mavjud mahsulotlarni ko'rsatish
            """)
    @ApiResponse(responseCode = "200", description = "Sahifalangan mahsulotlar ro'yxati")
    @GetMapping
    public PageResponse<ProductResponse> list(
            @Parameter(description = "Kategoriya ID bo'yicha filtrlash") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv so'zi (nom yoki SKU/artikul bo'yicha)") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Faqat omborda mavjud mahsulotlar (true/false)") @RequestParam(defaultValue = "false") boolean inStock,
            @Parameter(description = "Saralash tartibi: newest | price_asc | price_desc | name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "12") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, inStock, false);
        return productService.search(filter, sort, page, size);
    }

    @Operation(summary = "Mahsulot tafsilotlari", description = "ID bo'yicha bitta mahsulot haqida to'liq ma'lumot olish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mahsulot topildi"),
            @ApiResponse(responseCode = "404", description = "Bunday ID li mahsulot topilmadi")
    })
    @GetMapping("/{id}")
    public ProductResponse get(@Parameter(description = "Mahsulot ID raqami") @PathVariable Long id) {
        return productService.get(id);
    }
}
