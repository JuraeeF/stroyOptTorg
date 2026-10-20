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

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

/** Kategoriya va mahsulotlarni boshqarish (faqat ADMIN). */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "6. Admin — Katalog boshqaruvi", description = "Kategoriya va mahsulotlarni qo'shish, o'zgartirish va o'chirish (Faqat ADMIN)")
public class AdminCatalogController {

    private final CategoryService categoryService;
    private final ProductService productService;

    // ---- categories ----

    @Operation(summary = "Yangi kategoriya yaratish", description = "Admin tomonidan yangi mahsulot toifasi qo'shiladi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Kategoriya muvaffaqiyatli yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "403", description = "Ruxsat yo'q (faqat ADMIN)")
    })
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.create(request);
    }

    @Operation(summary = "Kategoriyani tahrirlash", description = "Mavjud kategoriya nomini, tavsifini yoki rasmini yangilash")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Kategoriya yangilandi"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(
            @Parameter(description = "Kategoriya ID raqami") @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @Operation(summary = "Kategoriyani o'chirish", description = "Kategoriyani butunlay o'chirish (agar unda mahsulotlar bo'lsa xatolik beradi)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Kategoriya muvaffaqiyatli o'chirildi"),
            @ApiResponse(responseCode = "400", description = "Kategoriyada mahsulotlar mavjud, o'chirib bo'lmaydi"),
            @ApiResponse(responseCode = "404", description = "Kategoriya topilmadi")
    })
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Parameter(description = "Kategoriya ID raqami") @PathVariable Long id) {
        categoryService.delete(id);
    }

    // ---- products ----

    @Operation(summary = "Barcha mahsulotlar (nofaol mahsulotlar bilan birga)", description = "Admin uchun to'liq ro'yxat, hatto sotuvdan olingan (nofaol) mahsulotlar ham ko'rinadi")
    @ApiResponse(responseCode = "200", description = "Sahifalangan mahsulotlar ro'yxati")
    @GetMapping("/products")
    public PageResponse<ProductResponse> listProducts(
            @Parameter(description = "Kategoriya ID si") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Qidiruv so'zi") @RequestParam(required = false) String q,
            @Parameter(description = "Minimal narx") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maksimal narx") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Saralash: newest | price_asc | price_desc | name") @RequestParam(defaultValue = "newest") String sort,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        var filter = new ProductService.Filter(categoryId, q, minPrice, maxPrice, false, true);
        return productService.search(filter, sort, page, size);
    }

    @Operation(summary = "Yangi mahsulot yaratish", description = "Katalogga yangi tovar qo'shadi (artikul, narx, o'lchov birligi, minimal buyurtma va qoldiq)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mahsulot yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "404", description = "Ko'rsatilgan kategoriya topilmadi")
    })
    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @Operation(summary = "Mahsulotni tahrirlash", description = "Mavjud tovar ma'lumotlarini (narx, qoldiq, tavsif, rasm) yangilash")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mahsulot yangilandi"),
            @ApiResponse(responseCode = "404", description = "Mahsulot topilmadi")
    })
    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(
            @Parameter(description = "Mahsulot ID raqami") @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @Operation(summary = "Mahsulotni sotuvdan olish (Soft delete)", description = "Buyurtmalar tarixi saqlanishi uchun mahsulot bazadan butunlay o'chirilmaydi, balki active=false qilinadi")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mahsulot nofaol holatga o'tkazildi"),
            @ApiResponse(responseCode = "404", description = "Mahsulot topilmadi")
    })
    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateProduct(@Parameter(description = "Mahsulot ID raqami") @PathVariable Long id) {
        productService.deactivate(id);
    }
}
