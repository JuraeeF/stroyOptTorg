package com.example.stroy.controller;

import com.example.stroy.dto.OrderRequest;
import com.example.stroy.dto.OrderResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.service.OrderService;
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

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "4. Buyurtmalar", description = "Mijoz buyurtmalarini yaratish, ko'rish va bekor qilish")
@SecurityRequirement(name = BEARER_AUTH)
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Yangi buyurtma rasmiylashtirish", description = "Savatchadagi mahsulotlar va yetkazib berish ma'lumotlari bilan yangi buyurtma yaratadi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Buyurtma muvaffaqiyatli yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi yoki omborda mahsulot yetarli emas"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiyadan o'tilmagan")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @Operation(summary = "Mening buyurtmalarim ro'yxati", description = "Joriy mijozning barcha buyurtmalarini sahifalab ko'rsatadi")
    @ApiResponse(responseCode = "200", description = "Buyurtmalar ro'yxati olindi")
    @GetMapping
    public PageResponse<OrderResponse> mine(
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi buyurtmalar soni") @RequestParam(defaultValue = "10") int size) {
        return orderService.myOrders(page, size);
    }

    @Operation(summary = "Buyurtma tafsilotlari", description = "ID bo'yicha buyurtma ma'lumotlarini olish (faqat buyurtma egasi yoki admin ko'ra oladi)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma topildi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @GetMapping("/{id}")
    public OrderResponse get(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.get(id);
    }

    @Operation(summary = "Buyurtmani bekor qilish", description = "Mijoz o'z buyurtmasini faqat NEW (yangi) yoki CONFIRMED (tasdiqlangan) holatda bekor qilishi mumkin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma bekor qilindi"),
            @ApiResponse(responseCode = "400", description = "Buyurtma ushbu holatda bekor qilinishi mumkin emas"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.cancelMine(id);
    }
}
