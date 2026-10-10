package com.example.stroy.repository;

import com.example.stroy.entity.Order;
import com.example.stroy.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    long countByStatus(OrderStatus status);

    /** Bekor qilinmagan buyurtmalar summasi (buyurtma bo'lmasa null qaytadi). */
    @Query("select sum(o.totalAmount) from Order o where o.status <> :status")
    BigDecimal sumRevenueExcluding(@Param("status") OrderStatus status);
}
