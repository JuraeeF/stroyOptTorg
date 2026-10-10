package com.example.stroy.util;

import com.example.stroy.dto.*;
import com.example.stroy.entity.*;

/** Entity -> DTO o'tkazgichlar. Lazy maydonlar uchun tranzaksiya ichida chaqiring. */
public final class Mappers {

    private Mappers() {
    }

    public static UserResponse toUser(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getCompanyName(), u.getRole());
    }

    public static CategoryResponse toCategory(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getDescription(), c.getImageUrl());
    }

    public static ProductResponse toProduct(Product p) {
        Category c = p.getCategory();
        return new ProductResponse(p.getId(), p.getName(), p.getSku(), p.getDescription(), p.getPrice(), p.getUnit(),
                p.getMinOrderQuantity(), p.getStock(), p.getImageUrl(), p.isActive(), c.getId(), c.getName());
    }

    public static OrderItemResponse toOrderItem(OrderItem i) {
        return new OrderItemResponse(i.getProduct().getId(), i.getProductName(), i.getUnit(), i.getUnitPrice(),
                i.getQuantity(), i.getLineTotal());
    }

    public static OrderResponse toOrder(Order o) {
        return new OrderResponse(o.getId(), o.getStatus(), o.getCustomerName(), o.getPhone(), o.getAddress(),
                o.getComment(), o.getTotalAmount(), o.getCreatedAt(), o.getUser().getId(), o.getUser().getEmail(),
                o.getItems().stream().map(Mappers::toOrderItem).toList());
    }

    public static ContactResponse toContact(ContactMessage m) {
        return new ContactResponse(m.getId(), m.getName(), m.getPhone(), m.getMessage(), m.isProcessed(), m.getCreatedAt());
    }
}
