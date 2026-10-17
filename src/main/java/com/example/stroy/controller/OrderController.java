package com.example.stroy.controller;

import com.example.stroy.dto.OrderRequest;
import com.example.stroy.dto.OrderResponse;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "4. Buyurtmalar (Mijoz)", description = "Mijozning buyurtmalarini boshqarish")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Yangi buyurtma rasmiylashtirish",
            description = "Savatdagi mahsulotlar uchun buyurtma yaratish. Mahsulotlar ombordan band qilinadi.")
    public OrderResponse create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping
    @Operation(summary = "Mening buyurtmalarim ro'yxati",
            description = "Tizimga kirgan mijozning barcha buyurtmalarini sahifalab olish")
    public PageResponse<OrderResponse> mine(
            @Parameter(description = "Sahifa raqami (0 dan boshlanadi)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "10") int size) {
        return orderService.myOrders(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buyurtma tafsilotlari",
            description = "Faqat buyurtma egasi yoki admin ko'ra oladi")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.get(id);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Buyurtmani bekor qilish",
            description = "Faqat YANGI (NEW) holatidagi buyurtmani bekor qilish mumkin. Mahsulotlar omborga qaytariladi.")
    public OrderResponse cancel(@PathVariable Long id) {
        return orderService.cancelMine(id);
    }
}
