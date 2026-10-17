package com.example.stroy.controller.admin;

import com.example.stroy.dto.OrderResponse;
import com.example.stroy.dto.OrderStatusRequest;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.StatsResponse;
import com.example.stroy.entity.OrderStatus;
import com.example.stroy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Admin — Buyurtmalar", description = "Buyurtmalarni boshqarish va statistika (ADMIN)")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "Barcha buyurtmalar", description = "Barcha buyurtmalarni holat bo'yicha filtrlash va sahifalash")
    @ApiResponse(responseCode = "200", description = "Buyurtmalar ro'yxati")
    @GetMapping("/orders")
    public PageResponse<OrderResponse> list(
            @Parameter(description = "Holat bo'yicha filtrlash: NEW, CONFIRMED, SHIPPED, DELIVERED, CANCELLED") @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifadagi elementlar soni") @RequestParam(defaultValue = "20") int size) {
        return orderService.adminList(status, page, size);
    }

    @Operation(summary = "Buyurtma tafsilotlari (admin)", description = "ID bo'yicha istalgan buyurtmani ko'rish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma topildi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @GetMapping("/orders/{id}")
    public OrderResponse get(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.get(id);
    }

    @Operation(summary = "Buyurtma holatini o'zgartirish", description = """
            Buyurtma holatini yangilaydi. Ruxsat etilgan o'tishlar:
            - NEW → CONFIRMED, CANCELLED
            - CONFIRMED → SHIPPED, CANCELLED
            - SHIPPED → DELIVERED
            - DELIVERED, CANCELLED → o'zgartib bo'lmaydi
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holat yangilandi"),
            @ApiResponse(responseCode = "400", description = "Noto'g'ri holat o'tishi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateStatus(
            @Parameter(description = "Buyurtma ID raqami") @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @Operation(summary = "Statistika", description = "Umumiy buyurtmalar soni, yangi buyurtmalar, daromad, faol mahsulotlar va mijozlar soni")
    @ApiResponse(responseCode = "200", description = "Statistika ma'lumotlari")
    @GetMapping("/stats")
    public StatsResponse stats() {
        return orderService.stats();
    }
}
