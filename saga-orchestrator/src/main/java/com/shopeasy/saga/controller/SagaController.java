package com.shopeasy.saga.controller;

import com.shopeasy.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/saga")
@Slf4j
public class SagaController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        log.info("GET /api/v1/saga/health");
        return ResponseEntity.ok(ApiResponse.success("Saga Orchestrator is running"));
    }
}
