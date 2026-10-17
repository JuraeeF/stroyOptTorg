package com.example.stroy.util;

import com.example.stroy.dto.*;
import com.example.stroy.entity.*;

import java.util.Collections;

/** Entity -> DTO o'tkazgichlar. Lazy maydonlar uchun tranzaksiya ichida chaqiring. */
public final class Mappers {

    private Mappers() {
    }

    public static UserResponse toUser(User u) {
        if (u == null) return null;
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getPhone(), u.getCompanyName(), u.getRole());
    }

    public static CategoryResponse toCategory(Category c) {
        if (c == null) return null;
        return new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getDescription(), c.getImageUrl());
    }

    public static ProductResponse toProduct(Product p) {
        if (p == null) return null;
        Category c = p.getCategory();
        Long categoryId = (c != null) ? c.getId() : null;
        String categoryName = (c != null) ? c.getName() : null;
        return new ProductResponse(p.getId(), p.getName(), p.getSku(), p.getDescription(), p.getPrice(), p.getUnit(),
                p.getMinOrderQuantity(), p.getStock(), p.getImageUrl(), p.isActive(), categoryId, categoryName);
    }

    public static OrderItemResponse toOrderItem(OrderItem i) {
        if (i == null) return null;
        Long productId = (i.getProduct() != null) ? i.getProduct().getId() : null;
        return new OrderItemResponse(productId, i.getProductName(), i.getUnit(), i.getUnitPrice(),
                i.getQuantity(), i.getLineTotal());
    }

    public static OrderResponse toOrder(Order o) {
        if (o == null) return null;
        Long userId = (o.getUser() != null) ? o.getUser().getId() : null;
        String userEmail = (o.getUser() != null) ? o.getUser().getEmail() : null;
        var items = (o.getItems() != null)
                ? o.getItems().stream().map(Mappers::toOrderItem).toList()
                : Collections.<OrderItemResponse>emptyList();

        return new OrderResponse(o.getId(), o.getStatus(), o.getCustomerName(), o.getPhone(), o.getAddress(),
                o.getComment(), o.getTotalAmount(), o.getCreatedAt(), userId, userEmail, items);
    }

    public static ContactResponse toContact(ContactMessage m) {
        if (m == null) return null;
        return new ContactResponse(m.getId(), m.getName(), m.getPhone(), m.getMessage(), m.isProcessed(), m.getCreatedAt());
    }
}
