package com.shopeasy.common.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
    private final String errorCode;
    private final Long productId;
    private final Integer requested;
    private final Integer available;

    public InsufficientStockException(Long productId, Integer requested, Integer available) {
        super(String.format("Insufficient stock for product %d. Requested: %d, Available: %d", 
                productId, requested, available));
        this.errorCode = "INSUFFICIENT_STOCK";
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }
}
