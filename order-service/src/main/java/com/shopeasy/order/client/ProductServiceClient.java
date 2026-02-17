package com.shopeasy.order.client;

import com.shopeasy.common.dto.ApiResponse;
import com.shopeasy.common.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductServiceClient {

    @GetMapping("/api/v1/products/{id}")
    ApiResponse<ProductDTO> getProductById(@PathVariable Long id);
}