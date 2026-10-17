package com.example.stroy.controller.admin;

import com.example.stroy.dto.OrderResponse;
import com.example.stroy.dto.OrderStatusRequest;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.StatsResponse;
import com.example.stroy.entity.OrderStatus;
import com.example.stroy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "7. Admin - Buyurtmalar va Statistika", description = "Buyurtmalarni monitoring qilish, statusni o'zgartirish va umumiy hisobotlar (Faqat ADMIN)")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    @Operation(summary = "Barcha buyurtmalar ro'yxati", description = "Status bo'yicha saralab olish imkoniyati bilan")
    public PageResponse<OrderResponse> list(
            @Parameter(description = "Buyurtma holati bo'yicha filter (NEW, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)")
            @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        return orderService.adminList(status, page, size);
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Buyurtma tafsilotlari")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.get(id);
    }

    @PatchMapping("/orders/{id}/status")
    @Operation(summary = "Buyurtma holatini yangilash",
            description = "Masalan NEW -> CONFIRMED -> SHIPPED -> DELIVERED yoki CANCELLED")
    public OrderResponse updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @GetMapping("/stats")
    @Operation(summary = "Tizim statistikasi",
            description = "Jami buyurtmalar, yangi buyurtmalar, umumiy tushum, faol tovarlar va mijozlar soni")
    public StatsResponse stats() {
        return orderService.stats();
    }
}
