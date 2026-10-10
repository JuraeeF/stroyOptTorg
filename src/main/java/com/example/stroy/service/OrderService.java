package com.example.stroy.service;

import com.example.stroy.dto.*;
import com.example.stroy.entity.*;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.OrderRepository;
import com.example.stroy.repository.ProductRepository;
import com.example.stroy.repository.UserRepository;
import com.example.stroy.security.CurrentUserService;
import com.example.stroy.util.Mappers;
import com.example.stroy.util.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orders;
    private final ProductRepository products;
    private final UserRepository users;
    private final CurrentUserService currentUser;
    private final MailService mailService;

    @Transactional
    public OrderResponse create(OrderRequest req) {
        User user = currentUser.require();

        // Bir xil mahsulot bir necha marta kelsa, miqdorlar qo'shiladi. TreeMap = bloklash tartibi doimiy.
        Map<Long, Integer> quantities = new TreeMap<>();
        for (OrderItemRequest item : req.items()) {
            quantities.merge(item.productId(), item.quantity(), Integer::sum);
        }

        List<Product> found = products.findAllForUpdate(quantities.keySet());
        if (found.size() != quantities.size()) {
            throw ApiException.badRequest("Ba'zi mahsulotlar topilmadi");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .customerName(req.customerName().trim())
                .phone(req.phone().trim())
                .address(req.address().trim())
                .comment(req.comment())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (Product p : found) {
            int qty = quantities.get(p.getId());
            if (!p.isActive()) {
                throw ApiException.badRequest("«" + p.getName() + "» hozir sotuvda yo'q");
            }
            if (qty < p.getMinOrderQuantity()) {
                throw ApiException.badRequest("«" + p.getName() + "» uchun minimal buyurtma: "
                        + p.getMinOrderQuantity() + " " + p.getUnit());
            }
            if (qty > p.getStock()) {
                throw ApiException.badRequest("«" + p.getName() + "» omborda yetarli emas (qoldiq: "
                        + p.getStock() + " " + p.getUnit() + ")");
            }
            p.setStock(p.getStock() - qty);
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(qty));
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .product(p)
                    .productName(p.getName())
                    .unit(p.getUnit())
                    .unitPrice(p.getPrice())
                    .quantity(qty)
                    .lineTotal(line)
                    .build());
            total = total.add(line);
        }
        order.setTotalAmount(total);
        Order saved = orders.save(order);

        mailService.send("Yangi buyurtma #" + saved.getId(),
                "Mijoz: " + saved.getCustomerName() + "\nTelefon: " + saved.getPhone()
                        + "\nManzil: " + saved.getAddress() + "\nSumma: " + saved.getTotalAmount());
        return Mappers.toOrder(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> myOrders(int page, int size) {
        User user = currentUser.require();
        Page<Order> result = orders.findByUserId(user.getId(),
                Paging.of(page, size, Sort.by("createdAt").descending().and(Sort.by("id").descending())));
        return PageResponse.from(result.map(Mappers::toOrder));
    }

    /** Buyurtmani egasi yoki admin ko'ra oladi; boshqalarga 404 qaytadi. */
    @Transactional(readOnly = true)
    public OrderResponse get(Long id) {
        User user = currentUser.require();
        Order order = orders.findById(id).orElseThrow(() -> ApiException.notFound("Buyurtma topilmadi"));
        if (user.getRole() != Role.ADMIN && !order.getUser().getId().equals(user.getId())) {
            throw ApiException.notFound("Buyurtma topilmadi");
        }
        return Mappers.toOrder(order);
    }

    /** Mijoz faqat o'zining YANGI buyurtmasini bekor qila oladi. */
    @Transactional
    public OrderResponse cancelMine(Long id) {
        User user = currentUser.require();
        Order order = orders.findById(id).orElseThrow(() -> ApiException.notFound("Buyurtma topilmadi"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw ApiException.notFound("Buyurtma topilmadi");
        }
        if (order.getStatus() != OrderStatus.NEW) {
            throw ApiException.badRequest("Faqat yangi buyurtmani bekor qilish mumkin");
        }
        applyStatus(order, OrderStatus.CANCELLED);
        return Mappers.toOrder(order);
    }

    // ---------------- admin ----------------

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> adminList(OrderStatus status, int page, int size) {
        var pageable = Paging.of(page, size, Sort.by("createdAt").descending().and(Sort.by("id").descending()));
        Page<Order> result = status == null ? orders.findAll(pageable) : orders.findByStatus(status, pageable);
        return PageResponse.from(result.map(Mappers::toOrder));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus next) {
        Order order = orders.findById(id).orElseThrow(() -> ApiException.notFound("Buyurtma topilmadi"));
        if (!order.getStatus().canTransitionTo(next)) {
            throw ApiException.badRequest("Holatni " + order.getStatus() + " dan " + next + " ga o'zgartirib bo'lmaydi");
        }
        applyStatus(order, next);
        return Mappers.toOrder(order);
    }

    @Transactional(readOnly = true)
    public StatsResponse stats() {
        BigDecimal revenue = orders.sumRevenueExcluding(OrderStatus.CANCELLED);
        return new StatsResponse(
                orders.count(),
                orders.countByStatus(OrderStatus.NEW),
                revenue == null ? BigDecimal.ZERO : revenue,
                products.countByActiveTrue(),
                users.countByRole(Role.CUSTOMER));
    }

    /** Holatni o'zgartiradi; bekor qilinsa ombor qoldig'ini qaytaradi. */
    private void applyStatus(Order order, OrderStatus next) {
        if (next == OrderStatus.CANCELLED) {
            List<Long> ids = order.getItems().stream().map(i -> i.getProduct().getId()).distinct().sorted().toList();
            Map<Long, Product> locked = new TreeMap<>();
            products.findAllForUpdate(ids).forEach(p -> locked.put(p.getId(), p));
            for (OrderItem item : order.getItems()) {
                Product p = locked.get(item.getProduct().getId());
                p.setStock(p.getStock() + item.getQuantity());
            }
        }
        order.setStatus(next);
    }
}
