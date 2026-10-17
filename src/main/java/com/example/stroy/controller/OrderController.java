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

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Buyurtmalar", description = "Buyurtma yaratish va kuzatish")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Buyurtma yaratish", description = "Yangi buyurtma yaratadi. Mahsulotlar ro'yxati va yetkazish manzilini kiriting")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Buyurtma muvaffaqiyatli yaratildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @Operation(summary = "Mening buyurtmalarim", description = "Joriy foydalanuvchining barcha buyurtmalarini sahifalab qaytaradi")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtmalar ro'yxati"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi")
    })
    @GetMapping
    public PageResponse<OrderResponse> mine(
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "10") int size) {
        return orderService.myOrders(page, size);
    }

    @Operation(summary = "Buyurtma tafsilotlari", description = "ID bo'yicha buyurtma tafsilotlarini ko'rish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma topildi"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @GetMapping("/{id}")
    public OrderResponse get(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.get(id);
    }

    @Operation(summary = "Buyurtmani bekor qilish", description = "O'z buyurtmangizni bekor qilish (faqat NEW yoki CONFIRMED holatda)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma bekor qilindi"),
            @ApiResponse(responseCode = "400", description = "Buyurtmani bekor qilib bo'lmaydi"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.cancelMine(id);
    }
}
