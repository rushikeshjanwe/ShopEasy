package com.shopeasy.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
    private String eventId;
    private String eventType;
    private Long orderId;
    private String orderNumber;
    private Map<Long, Integer> productQuantities;
    private String message;
    private String correlationId;
    private LocalDateTime timestamp;
    
    public enum EventType {
        RESERVE_INVENTORY,
        RELEASE_INVENTORY,
        INVENTORY_RESERVED,
        INVENTORY_RELEASED,
        INVENTORY_RESERVATION_FAILED
    }
}
