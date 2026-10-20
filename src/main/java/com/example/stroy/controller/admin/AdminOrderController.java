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

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = BEARER_AUTH)
@Tag(name = "7. Admin — Buyurtmalar boshqaruvi", description = "Barcha buyurtmalar, holat o'zgartirish va statistika (Faqat ADMIN)")
public class AdminOrderController {

    private final OrderService orderService;

    @Operation(summary = "Barcha buyurtmalar ro'yxati", description = "Admin uchun barcha mijozlarning buyurtmalari. Holat (status) bo'yicha filtrlash imkoni mavjud.")
    @ApiResponse(responseCode = "200", description = "Buyurtmalar ro'yxati olindi")
    @GetMapping("/orders")
    public PageResponse<OrderResponse> list(
            @Parameter(description = "Holat bo'yicha filter: NEW | CONFIRMED | SHIPPED | DELIVERED | CANCELLED") @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Sahifa raqami") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sahifa hajmi") @RequestParam(defaultValue = "20") int size) {
        return orderService.adminList(status, page, size);
    }

    @Operation(summary = "Buyurtma tafsilotlari (admin)", description = "ID bo'yicha istalgan mijozning buyurtmasini ko'rish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Buyurtma topildi"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @GetMapping("/orders/{id}")
    public OrderResponse get(@Parameter(description = "Buyurtma ID raqami") @PathVariable Long id) {
        return orderService.get(id);
    }

    @Operation(summary = "Buyurtma holatini yangilash", description = """
            Buyurtma bosqichini o'zgartirish. Ruxsat etilgan o'tishlar:
            - **NEW** → `CONFIRMED` yoki `CANCELLED`
            - **CONFIRMED** → `SHIPPED` yoki `CANCELLED`
            - **SHIPPED** → `DELIVERED`
            - `DELIVERED` va `CANCELLED` holatlarini o'zgartirib bo'lmaydi.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Holat muvaffaqiyatli yangilandi"),
            @ApiResponse(responseCode = "400", description = "Ushbu holatga o'tish taqiqlangan"),
            @ApiResponse(responseCode = "404", description = "Buyurtma topilmadi")
    })
    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateStatus(
            @Parameter(description = "Buyurtma ID raqami") @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @Operation(summary = "Platforma umumiy statistikasi", description = "Jami buyurtmalar, yangi buyurtmalar, umumiy tushum, faol tovarlar va mijozlar soni")
    @ApiResponse(responseCode = "200", description = "Statistika ma'lumotlari olindi")
    @GetMapping("/stats")
    public StatsResponse stats() {
        return orderService.stats();
    }
}
