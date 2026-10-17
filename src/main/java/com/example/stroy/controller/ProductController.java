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
@Tag(name = "Mahsulotlar", description = "Mahsulotlar katalogi, qidirish va filtrlash")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Mahsulotlar ro'yxati", description = """
            Mahsulotlarni qidirish va filtrlash. Sortlash turlari:
            - **newest** — eng yangilari (standart)
            - **price_asc** — narx bo'yicha o'sish tartibida
            - **price_desc** — narx bo'yicha kamayish tartibida
            - **name** — nom bo'yicha alifbo tartibida
            """)
    @ApiResponse(responseCode = "200", description = "Sahifalangan mahsulotlar ro'yxati")
    @GetMapping
    public PageResponse<ProductResponse> list(
            @Parameter(description = "Kategoriya ID bo'yicha filtrlash") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv so'zi (nom yoki SKU bo'yicha)") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Faqat omborda mavjud") @RequestParam(defaultValue = "false") boolean inStock,
            @Parameter(description = "Sortlash: newest, price_asc, price_desc, name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "12") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, inStock, false);
        return productService.search(filter, sort, page, size);
    }

    @Operation(summary = "Mahsulot tafsilotlari", description = "ID bo'yicha bitta mahsulotni qaytaradi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mahsulot topildi"),
            @ApiResponse(responseCode = "404", description = "Mahsulot topilmadi")
    })
    @GetMapping("/{id}")
    public ProductResponse get(@Parameter(description = "Mahsulot ID raqami") @PathVariable Long id) {
        return productService.get(id);
    }
}
