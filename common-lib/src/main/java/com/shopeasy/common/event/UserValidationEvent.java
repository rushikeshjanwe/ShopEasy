package com.shopeasy.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationEvent {
    private String eventId;
    private String eventType;
    private Long orderId;
    private Long userId;
    private Long addressId;
    private String message;
    private String correlationId;
    private LocalDateTime timestamp;
    
    public enum EventType {
        VALIDATE_USER,
        USER_VALIDATED,
        USER_VALIDATION_FAILED
    }
}
