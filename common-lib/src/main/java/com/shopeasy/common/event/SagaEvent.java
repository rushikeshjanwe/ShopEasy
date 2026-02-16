package com.shopeasy.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SagaEvent {
    private String sagaId;
    private String eventType;
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private List<OrderItemEvent> items;
    private String status;
    private String message;
    private String correlationId;
    private LocalDateTime timestamp;
    
    public enum SagaStatus {
        STARTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        COMPENSATING
    }
}
