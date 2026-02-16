package com.shopeasy.common.config;

public class RabbitMQConfig {
    
    // Order Exchange
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_CREATED_QUEUE = "order.created.queue";
    public static final String ORDER_UPDATED_QUEUE = "order.updated.queue";
    public static final String ORDER_CREATED_KEY = "order.created";
    public static final String ORDER_UPDATED_KEY = "order.updated";
    
    // Inventory Exchange
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String INVENTORY_RESERVE_QUEUE = "inventory.reserve.queue";
    public static final String INVENTORY_RELEASE_QUEUE = "inventory.release.queue";
    public static final String INVENTORY_RESPONSE_QUEUE = "inventory.response.queue";
    public static final String INVENTORY_RESERVE_KEY = "inventory.reserve";
    public static final String INVENTORY_RELEASE_KEY = "inventory.release";
    public static final String INVENTORY_RESPONSE_KEY = "inventory.response";
    
    // User Exchange
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_VALIDATE_QUEUE = "user.validate.queue";
    public static final String USER_RESPONSE_QUEUE = "user.response.queue";
    public static final String USER_VALIDATE_KEY = "user.validate";
    public static final String USER_RESPONSE_KEY = "user.response";
    
    // Saga Exchange
    public static final String SAGA_EXCHANGE = "saga.exchange";
    public static final String SAGA_START_QUEUE = "saga.start.queue";
    public static final String SAGA_RESPONSE_QUEUE = "saga.response.queue";
    public static final String SAGA_START_KEY = "saga.start";
    public static final String SAGA_RESPONSE_KEY = "saga.response";
}
