package com.example.stroy.controller.admin;

import com.example.stroy.dto.*;
import com.example.stroy.service.CategoryService;
import com.example.stroy.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

/** Kategoriya va mahsulotlarni boshqarish (faqat ADMIN). */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "6. Admin - Katalog boshqaruvi", description = "Kategoriya va mahsulotlarni qo'shish, o'zgartirish va o'chirish (Faqat ADMIN)")
public class AdminCatalogController {

    private final CategoryService categoryService;
    private final ProductService productService;

    // ---- categories ----

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Yangi kategoriya yaratish")
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "Kategoriyani tahrirlash")
    public CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Kategoriyani o'chirish", description = "Agar kategoriyada mahsulotlar bo'lsa, xatolik qaytaradi")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    }

    // ---- products ----

    @GetMapping("/products")
    @Operation(summary = "Barcha mahsulotlar (nofaol mahsulotlar ham ko'rinadi)")
    public PageResponse<ProductResponse> listProducts(
            @Parameter(description = "Kategoriya ID si") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv matni") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Saralash: newest | price_asc | price_desc | name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, false, true);
        return productService.search(filter, sort, page, size);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Yangi mahsulot yaratish")
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "Mahsulotni tahrirlash")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Mahsulotni nofaol qilish (Soft delete)", description = "Buyurtmalar tarixi saqlanishi uchun mahsulot o'chirilmaydi, faqat nofaol qilinadi")
    public void deactivateProduct(@PathVariable Long id) {
        productService.deactivate(id);
    }
}
