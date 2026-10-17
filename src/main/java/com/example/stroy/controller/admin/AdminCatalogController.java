package com.example.stroy.controller.admin;

import com.example.stroy.dto.*;
import com.example.stroy.service.CategoryService;
import com.example.stroy.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/** Kategoriya va mahsulotlarni boshqarish (faqat ADMIN). */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin — Katalog", description = "Kategoriya va mahsulotlarni boshqarish (ADMIN)")
public class AdminCatalogController {

    private final CategoryService categoryService;
    private final ProductService productService;

    // ---- categories ----

    @Operation(summary = "Kategoriya yaratish", description = "Yangi kategoriya qo'shadi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kategoriya yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi"),
            @ApiResponse(responseCode = "403", description = "Faqat ADMIN ruxsati")
    })
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @Operation(summary = "Kategoriyani tahrirlash", description = "Mavjud kategoriyani yangilaydi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kategoriya yangilandi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(
            @Parameter(description = "Kategoriya ID raqami") @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @Operation(summary = "Kategoriyani o'chirish", description = "Kategoriyani o'chiradi. Ichida mahsulotlar bo'lsa xato qaytaradi.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Kategoriya o'chirildi"),
            @ApiResponse(responseCode = "400", description = "Kategoriyada mahsulotlar mavjud"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Parameter(description = "Kategoriya ID raqami") @PathVariable Long id) {
        categoryService.delete(id);
    }

    // ---- products ----

    @Operation(summary = "Mahsulotlar ro'yxati (admin)", description = "Nofaol mahsulotlar ham ko'rinadi. Qidirish va filtrlash imkoni mavjud.")
    @ApiResponse(responseCode = "200", description = "Sahifalangan mahsulotlar ro'yxati")
    /** Nofaol mahsulotlar ham ko'rinadi. */
    @GetMapping("/products")
    public PageResponse<ProductResponse> listProducts(
            @Parameter(description = "Kategoriya ID bo'yicha filtrlash") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv so'zi") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Sortlash: newest, price_asc, price_desc, name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "20") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, false, true);
        return productService.search(filter, sort, page, size);
    }

    @Operation(summary = "Mahsulot yaratish", description = "Yangi mahsulot qo'shadi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mahsulot yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @Operation(summary = "Mahsulotni tahrirlash", description = "Mavjud mahsulot ma'lumotlarini yangilaydi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mahsulot yangilandi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "404", description = "Mahsulot topilmadi")
    })
    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(
            @Parameter(description = "Mahsulot ID raqami") @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @Operation(summary = "Mahsulotni o'chirish", description = "Mahsulotni sotuvdan olib tashlaydi (soft delete — active=false)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mahsulot o'chirildi"),
            @ApiResponse(responseCode = "404", description = "Mahsulot topilmadi")
    })
    /** Mahsulotni sotuvdan olib tashlaydi (soft delete). */
    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@Parameter(description = "Mahsulot ID raqami") @PathVariable Long id) {
        productService.deactivate(id);
    }
}
