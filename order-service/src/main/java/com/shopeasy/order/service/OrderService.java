package com.shopeasy.order.service;

import com.shopeasy.common.dto.*;
import com.shopeasy.common.exception.ResourceNotFoundException;
import com.shopeasy.order.client.ProductServiceClient;
import com.shopeasy.order.entity.Order;
import com.shopeasy.order.entity.OrderItem;
import com.shopeasy.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;

    @Transactional(readOnly = true)
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders");
        return orderRepository.findByDeletedFalse(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByUserId(Long userId, Pageable pageable) {
        log.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserIdAndDeletedFalse(userId, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return toDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .userId(request.getUserId())
                .status(Order.OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            ApiResponse<ProductDTO> response = productServiceClient.getProductById(itemRequest.getProductId());
            ProductDTO product = response.getData();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            order.getItems().add(orderItem);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        log.info("Order created: {}", order.getOrderNumber());
        return toDTO(order);
    }

    public PageInfo createPageInfo(Page<?> page) {
        return PageInfo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    private OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .userEmail(order.getUserEmail())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .items(order.getItems().stream()
                        .map(item -> OrderItemDTO.builder()
                                .id(item.getId())
                                .productId(item.getProductId())
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
