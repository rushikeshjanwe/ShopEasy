package com.shopeasy.order.repository;

import com.shopeasy.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByDeletedFalse(Pageable pageable);
    Page<Order> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
    Optional<Order> findByIdAndDeletedFalse(Long id);
    Optional<Order> findByOrderNumberAndDeletedFalse(String orderNumber);
}
