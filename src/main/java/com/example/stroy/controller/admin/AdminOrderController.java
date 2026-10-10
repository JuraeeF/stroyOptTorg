package com.example.stroy.controller.admin;

import com.example.stroy.dto.OrderResponse;
import com.example.stroy.dto.OrderStatusRequest;
import com.example.stroy.dto.PageResponse;
import com.example.stroy.dto.StatsResponse;
import com.example.stroy.entity.OrderStatus;
import com.example.stroy.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public PageResponse<OrderResponse> list(@RequestParam(required = false) OrderStatus status,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return orderService.adminList(status, page, size);
    }

    @GetMapping("/orders/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.get(id);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @GetMapping("/stats")
    public StatsResponse stats() {
        return orderService.stats();
    }
}
