package com.shopeasy.order.client;

import com.shopeasy.common.dto.ApiResponse;
import com.shopeasy.common.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class ProductClientFallbackFactory implements FallbackFactory<ProductServiceClient> {

    @Override
    public ProductServiceClient create(Throwable cause) {
        log.error("========================================");
        log.error("CIRCUIT BREAKER ACTIVATED!");
        log.error("Product Service is DOWN: {}", cause.getMessage());
        log.error("========================================");

        return new ProductServiceClient() {
            @Override
            public ApiResponse<ProductDTO> getProductById(Long id) {
                log.warn("Returning fallback for product ID: {}", id);

                ProductDTO fallbackProduct = ProductDTO.builder()
                        .id(id)
                        .name("Product Unavailable")
                        .description("Product service is currently unavailable")
                        .price(BigDecimal.ZERO)
                        .stockQuantity(0)
                        .build();

                return ApiResponse.success(fallbackProduct);
            }
        };
    }
}