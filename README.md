# 🛒 ShopEasy - Microservices E-Commerce Platform

## 📖 What Is This Project?

ShopEasy is a **simplified e-commerce backend** built using **Microservices Architecture**. Instead of one big application (monolith), we split functionality into small, independent services that talk to each other.


---
## 🤔 Why Did We Build This?

### The Problem with Monolithic Applications:

Imagine you have ONE big application that handles:
- User registration/login
- Product catalog
- Order management
- Payments
- Notifications

**Problems:**
1. **One bug can crash everything** - A bug in payment code can bring down the entire app
2. **Hard to scale** - If orders are getting high traffic, you can't scale just orders - you scale the ENTIRE app
3. **Team bottlenecks** - All developers work on the same codebase, causing conflicts
4. **Technology lock-in** - Entire app must use same language/framework
5. **Slow deployments** - Small change = redeploy entire application

### The Microservices Solution:

```
┌─────────────────────────────────────────────────────────────────┐
│                         MONOLITH                                │
│  ┌─────────┬─────────┬─────────┬─────────┬─────────┐           │
│  │  Users  │ Products│ Orders  │ Payments│  Auth   │           │
│  │         │         │         │         │         │           │
│  └─────────┴─────────┴─────────┴─────────┴─────────┘           │
│              ONE BIG APPLICATION                                │
└─────────────────────────────────────────────────────────────────┘

                            ↓ SPLIT INTO ↓

┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
│   User   │  │ Product  │  │  Order   │  │ Payment  │  │   Auth   │
│ Service  │  │ Service  │  │ Service  │  │ Service  │  │ Service  │
│          │  │          │  │          │  │          │  │          │
│ Port:8082│  │ Port:8081│  │ Port:8080│  │ Port:8083│  │ Port:8085│
└──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘
     │              │             │             │             │
     └──────────────┴─────────────┴─────────────┴─────────────┘
                    INDEPENDENT SERVICES
```

---

## 🎯 Where Does Microservices Help?

| Scenario | Monolith | Microservices |
|----------|----------|---------------|
| **Black Friday Sale** | Scale entire app (expensive) | Scale only Order & Payment services |
| **Bug in User Profile** | Entire app may crash | Only User Service affected |
| **New Payment Method** | Redeploy entire app | Deploy only Payment Service |
| **Team Structure** | Everyone on same code | Each team owns their service |
| **Technology Choice** | One language for all | User Service in Java, Analytics in Python |

### Real-World Companies Using Microservices:
- **Netflix** - 700+ microservices
- **Amazon** - Thousands of services
- **Uber** - 2000+ microservices
- **Flipkart** - Hundreds of services

---

## 🏗️ ShopEasy Architecture

```
                                    ┌─────────────────┐
                                    │   EUREKA        │
                                    │   DISCOVERY     │
                                    │   SERVER        │
                                    │   (Port 8761)   │
                                    └────────┬────────┘
                                             │
                                    All services register here
                                             │
┌─────────┐                         ┌────────▼────────┐
│         │      HTTP Request       │                 │
│  USER   │ ────────────────────►   │   API GATEWAY   │
│(Browser/│                         │   (Port 8000)   │
│ Mobile) │ ◄────────────────────   │                 │
│         │      HTTP Response      │  • Routing      │
└─────────┘                         │  • JWT Auth     │
                                    │  • Load Balance │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
                    ▼                        ▼                        ▼
           ┌───────────────┐        ┌───────────────┐        ┌───────────────┐
           │ AUTH SERVICE  │        │PRODUCT SERVICE│        │ ORDER SERVICE │
           │  (Port 8085)  │        │  (Port 8081)  │        │  (Port 8080)  │
           │               │        │               │        │               │
           │ • Login       │        │ • Products    │        │ • Orders      │
           │ • Register    │        │ • Categories  │        │ • Order Items │
           │ • JWT Tokens  │        │ • Stock       │        │               │
           └───────┬───────┘        └───────┬───────┘        └───────┬───────┘
                   │                        │                        │
                   ▼                        ▼                        ▼
           ┌───────────────┐        ┌───────────────┐        ┌───────────────┐
           │   authdb      │        │  productdb    │        │   orderdb     │
           │  (PostgreSQL) │        │  (PostgreSQL) │        │  (PostgreSQL) │
           └───────────────┘        └───────────────┘        └───────────────┘

           ┌───────────────┐
           │ USER SERVICE  │
           │  (Port 8082)  │
           │               │
           │ • Users       │
           │ • Addresses   │
           └───────┬───────┘
                   │
                   ▼
           ┌───────────────┐
           │    userdb     │
           │  (PostgreSQL) │
           └───────────────┘
```

---

## 📁 Project Structure

```
ShopEasy/
├── discovery-server/     # Eureka - Service Registry
├── api-gateway/          # Single entry point + JWT Auth
├── auth-service/         # Login, Register, Token generation
├── product-service/      # Product CRUD operations
├── user-service/         # User management
├── order-service/        # Order management
├── common-lib/           # Shared code (DTOs, utilities)
└── saga-orchestrator/    # (Optional) For distributed transactions
```

---

## 🔧 Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java 21** | Programming language |
| **Spring Boot 3.2** | Application framework |
| **Spring Cloud** | Microservices patterns |
| **Netflix Eureka** | Service Discovery |
| **Spring Cloud Gateway** | API Gateway |
| **Spring Security + JWT** | Authentication |
| **OpenFeign** | Service-to-service communication |
| **PostgreSQL** | Database |
| **Maven** | Build tool |
| **Lombok** | Reduce boilerplate code |

---

## 🚀 How To Run

### Prerequisites:
- Java 21
- Maven
- PostgreSQL
- RabbitMQ (optional, for saga pattern)

### Step 1: Create Databases
```sql
CREATE DATABASE authdb;
CREATE DATABASE productdb;
CREATE DATABASE userdb;
CREATE DATABASE orderdb;
```

### Step 2: Start Services (In Order)
```bash
# Terminal 1: Discovery Server (FIRST - wait 30 seconds)
cd discovery-server
mvn spring-boot:run

# Terminal 2: API Gateway
cd api-gateway
mvn spring-boot:run

# Terminal 3: Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 4: Product Service
cd product-service
mvn spring-boot:run

# Terminal 5: User Service
cd user-service
mvn spring-boot:run

# Terminal 6: Order Service
cd order-service
mvn spring-boot:run
```

### Step 3: Test
```bash
# Register
curl -X POST http://localhost:8000/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"John","lastName":"Doe"}'

# Login
curl -X POST http://localhost:8000/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Get Products (Public - No Token)
curl http://localhost:8000/api/v1/products

# Create Order (Protected - Token Required)
curl -X POST http://localhost:8000/api/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"userId":1,"shippingAddressId":1,"items":[{"productId":1,"quantity":2}]}'
```

---

## 🔌 API Endpoints

### Public Endpoints (No Token Required):
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login and get token |
| GET | `/api/v1/products` | Get all products |
| GET | `/api/v1/products/{id}` | Get product by ID |

### Protected Endpoints (Token Required):
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| POST | `/api/v1/orders` | Create order |
| GET | `/api/v1/orders` | Get all orders |
| GET | `/api/v1/orders/{id}` | Get order by ID |

---

# 📚 MICROSERVICES CONCEPTS EXPLAINED

---

## 1️⃣ Service Discovery (Netflix Eureka)

### What Is It?
A **phone directory for services**. When services start, they register themselves. When a service needs to call another, it looks up the address from Eureka.

### Why Do We Need It?
In microservices:
- Services run on different ports
- Services can have multiple instances
- IP addresses can change (especially in cloud)

Without discovery, you'd have to **hardcode** addresses:
```java
// BAD - Hardcoded
String productServiceUrl = "http://192.168.1.100:8081";

// GOOD - With Eureka
String productServiceUrl = "http://PRODUCT-SERVICE";  // Eureka resolves this!
```

### How It Works:
```
┌─────────────────────────────────────────────────────────────────┐
│                        EUREKA SERVER                            │
│                                                                 │
│   Service Registry:                                             │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │ SERVICE NAME      │ INSTANCES                           │  │
│   ├───────────────────┼─────────────────────────────────────┤  │
│   │ AUTH-SERVICE      │ 192.168.1.10:8085                   │  │
│   │ PRODUCT-SERVICE   │ 192.168.1.11:8081, 192.168.1.12:8081│  │
│   │ ORDER-SERVICE     │ 192.168.1.13:8080                   │  │
│   │ USER-SERVICE      │ 192.168.1.14:8082                   │  │
│   └───────────────────┴─────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

1. Service starts → Registers with Eureka
2. Service needs another service → Asks Eureka for address
3. Eureka returns IP:Port
4. Service makes the call
```

### Code Example:
```yaml
# application.yml - Service registering with Eureka
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### Dashboard:
Open `http://localhost:8761` to see all registered services!

---

## 2️⃣ API Gateway

### What Is It?
A **single entry point** for all client requests. Instead of clients calling each service directly, they call the Gateway, which routes to the correct service.

### Why Do We Need It?

**Without Gateway:**
```
Client → http://192.168.1.10:8085/auth/login
Client → http://192.168.1.11:8081/products
Client → http://192.168.1.13:8080/orders

Problems:
- Client needs to know all service addresses
- No centralized security
- Hard to add cross-cutting concerns (logging, rate limiting)
```

**With Gateway:**
```
Client → http://api.shopeasy.com/auth/login     → Gateway routes to Auth
Client → http://api.shopeasy.com/products       → Gateway routes to Product
Client → http://api.shopeasy.com/orders         → Gateway routes to Order

Benefits:
- Single URL for clients
- Centralized authentication
- Load balancing
- Rate limiting
- Request logging
```

### How It Works:
```
┌─────────────────────────────────────────────────────────────────┐
│                        API GATEWAY                              │
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                    ROUTING RULES                        │  │
│   ├─────────────────────────────────────────────────────────┤  │
│   │ /api/v1/auth/**     →  AUTH-SERVICE                     │  │
│   │ /api/v1/products/** →  PRODUCT-SERVICE                  │  │
│   │ /api/v1/orders/**   →  ORDER-SERVICE                    │  │
│   │ /api/v1/users/**    →  USER-SERVICE                     │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                    FILTERS                              │  │
│   ├─────────────────────────────────────────────────────────┤  │
│   │ 1. JWT Authentication Filter                            │  │
│   │ 2. Logging Filter                                       │  │
│   │ 3. Rate Limiting Filter                                 │  │
│   └─────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Code Example:
```yaml
# API Gateway routes configuration
spring:
  cloud:
    gateway:
      routes:
        - id: product-service
          uri: lb://PRODUCT-SERVICE    # lb = load balanced via Eureka
          predicates:
            - Path=/api/v1/products/**
          filters:
            - AuthenticationFilter     # Apply JWT check
```

---

## 3️⃣ Feign Client

### What Is It?
A **declarative HTTP client** that makes calling other services as easy as calling a local method.

### Why Do We Need It?

**Without Feign (using RestTemplate):**
```java
// Complicated and verbose
RestTemplate restTemplate = new RestTemplate();
String url = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
ResponseEntity<ProductDTO> response = restTemplate.getForEntity(url, ProductDTO.class);
ProductDTO product = response.getBody();
```

**With Feign:**
```java
// Clean and simple - looks like calling a local method!
ProductDTO product = productClient.getProductById(productId);
```

### How It Works:
```
┌─────────────────┐         ┌──────────────────┐         ┌─────────────────┐
│  ORDER SERVICE  │         │    FEIGN CLIENT  │         │ PRODUCT SERVICE │
│                 │         │                  │         │                 │
│ Need product    │  ──►    │ @FeignClient     │  ──►    │ GET /products/1 │
│ details for     │         │ ("PRODUCT-       │         │                 │
│ order           │  ◄──    │  SERVICE")       │  ◄──    │ Returns product │
│                 │         │                  │         │ JSON            │
└─────────────────┘         └──────────────────┘         └─────────────────┘
```

### Code Example:
```java
// Define the Feign Client interface
@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {
    
    @GetMapping("/api/v1/products/{id}")
    ProductDTO getProductById(@PathVariable Long id);
    
    @GetMapping("/api/v1/products")
    List<ProductDTO> getAllProducts();
}

// Use it in your service
@Service
public class OrderService {
    
    private final ProductClient productClient;
    
    public OrderDTO createOrder(OrderRequest request) {
        // Calling Product Service is now just one line!
        ProductDTO product = productClient.getProductById(request.getProductId());
        
        // Use product details...
    }
}
```

---

## 4️⃣ Config Server (Not Implemented - Future Enhancement)

### What Is It?
A **centralized configuration management** service. All configuration files are stored in one place (usually Git), and services fetch their config from there.

### Why Do We Need It?
```
WITHOUT Config Server:
├── auth-service/
│   └── application.yml       # DB config, ports, secrets
├── product-service/
│   └── application.yml       # Same DB config repeated!
├── order-service/
│   └── application.yml       # Same DB config repeated!
└── user-service/
    └── application.yml       # Same DB config repeated!

Problems:
- Change DB password = Edit 4 files, redeploy 4 services
- Secrets in code repository
- No environment-specific configs
```

```
WITH Config Server:
┌─────────────────────────────────────────────┐
│              CONFIG SERVER                  │
│                                             │
│   Git Repository:                           │
│   ├── application.yml      (common config) │
│   ├── auth-service.yml                      │
│   ├── product-service.yml                   │
│   └── product-service-prod.yml              │
└─────────────────────────────────────────────┘
              │
              ▼ Services fetch config at startup
┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
│   Auth   │ │ Product  │ │  Order   │ │   User   │
│ Service  │ │ Service  │ │ Service  │ │ Service  │
└──────────┘ └──────────┘ └──────────┘ └──────────┘

Benefits:
- Change once, apply everywhere
- Environment-specific configs (dev, staging, prod)
- Secrets stored securely
- Hot reload without restart
```

---

## 5️⃣ Circuit Breaker (Resilience4j) - Not Implemented

### What Is It?
A **fault tolerance pattern** that prevents cascading failures. If a service is down, the circuit "opens" and fails fast instead of waiting.

### Why Do We Need It?
```
WITHOUT Circuit Breaker:

User → Order Service → Product Service (DOWN!)
                            │
                            ▼
                      Timeout (30 seconds)
                            │
                            ▼
                      Order Service stuck
                            │
                            ▼
                      More requests pile up
                            │
                            ▼
                      Order Service crashes too!
                            │
                            ▼
                      ENTIRE SYSTEM DOWN! 💥
```

```
WITH Circuit Breaker:

User → Order Service → Circuit Breaker → Product Service (DOWN!)
                            │
                            ▼
                      Circuit OPENS after 3 failures
                            │
                            ▼
                      Returns fallback response immediately
                            │
                            ▼
                      "Product details unavailable, but order saved"
                            │
                            ▼
                      System continues working! ✅
```

### States:
```
┌─────────────────────────────────────────────────────────────────┐
│                    CIRCUIT BREAKER STATES                       │
│                                                                 │
│   ┌──────────┐         ┌──────────┐         ┌──────────┐       │
│   │  CLOSED  │ ──────► │   OPEN   │ ──────► │HALF-OPEN │       │
│   │          │ Failures│          │  Timer  │          │       │
│   │ Normal   │ exceed  │ Fail fast│ expires │ Test few │       │
│   │ operation│ threshold│ Return   │         │ requests │       │
│   │          │         │ fallback │         │          │       │
│   └──────────┘         └──────────┘         └──────────┘       │
│        ▲                                          │             │
│        │              Success                     │             │
│        └──────────────────────────────────────────┘             │
└─────────────────────────────────────────────────────────────────┘
```

### Code Example:
```java
@CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
public ProductDTO getProduct(Long id) {
    return productClient.getProductById(id);  // May fail
}

// Fallback when circuit is open
public ProductDTO getProductFallback(Long id, Exception e) {
    return ProductDTO.builder()
        .id(id)
        .name("Product Unavailable")
        .price(BigDecimal.ZERO)
        .build();
}
```

---

## 6️⃣ Saga Pattern (Choreography vs Orchestration)

### What Is It?
A pattern for managing **distributed transactions** across multiple services.

### The Problem:
```
Traditional Monolith (Single Database Transaction):
BEGIN TRANSACTION
  1. Create Order
  2. Reduce Stock
  3. Charge Payment
  4. Send Notification
COMMIT  ← All succeed or all rollback. Simple!

Microservices (Multiple Databases):
Order Service    → orderdb
Product Service  → productdb  
Payment Service  → paymentdb
Notification     → notificationdb

Problem: How to rollback if Payment fails after Stock is reduced?
```

### Solution 1: Choreography (Event-Driven)
Each service publishes events, others react. No central coordinator.

```
┌─────────────────────────────────────────────────────────────────┐
│                    CHOREOGRAPHY                                 │
│                                                                 │
│   Order        Product       Payment       Notification         │
│   Service      Service       Service       Service              │
│      │            │             │              │                │
│      │ OrderCreated            │              │                │
│      │──────────►│             │              │                │
│      │            │             │              │                │
│      │            │ StockReserved              │                │
│      │            │────────────►│              │                │
│      │            │             │              │                │
│      │            │             │ PaymentDone  │                │
│      │            │             │─────────────►│                │
│      │            │             │              │                │
│      │◄───────────┴─────────────┴──────────────┤                │
│      │         OrderConfirmed                  │                │
│                                                                 │
│   Pros: Loose coupling, no single point of failure              │
│   Cons: Hard to track, complex debugging                        │
└─────────────────────────────────────────────────────────────────┘
```

### Solution 2: Orchestration (Central Coordinator)
A Saga Orchestrator controls the flow and handles rollbacks.

```
┌─────────────────────────────────────────────────────────────────┐
│                    ORCHESTRATION                                │
│                                                                 │
│                  ┌──────────────────┐                          │
│                  │ SAGA ORCHESTRATOR│                          │
│                  │                  │                          │
│                  │ 1. Create Order  │                          │
│                  │ 2. Reserve Stock │                          │
│                  │ 3. Process Pay   │                          │
│                  │ 4. Send Notif    │                          │
│                  └────────┬─────────┘                          │
│                           │                                     │
│         ┌─────────────────┼─────────────────┐                  │
│         ▼                 ▼                 ▼                  │
│   ┌──────────┐     ┌──────────┐     ┌──────────┐              │
│   │  Order   │     │ Product  │     │ Payment  │              │
│   │ Service  │     │ Service  │     │ Service  │              │
│   └──────────┘     └──────────┘     └──────────┘              │
│                                                                 │
│   If Payment fails:                                             │
│   Orchestrator calls: Product.releaseStock()                    │
│   Orchestrator calls: Order.cancelOrder()                       │
│                                                                 │
│   Pros: Easy to track, clear flow, centralized rollback         │
│   Cons: Single point of failure, orchestrator complexity        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7️⃣ Kafka Basics (Not Implemented - For Learning)

### What Is It?
A **distributed message streaming platform** for high-throughput, real-time data pipelines.

### RabbitMQ vs Kafka:

| Feature | RabbitMQ | Kafka |
|---------|----------|-------|
| **Type** | Message Broker | Event Streaming |
| **Pattern** | Push (broker sends to consumers) | Pull (consumers fetch) |
| **Message Retention** | Deleted after consumed | Retained for configurable time |
| **Throughput** | Thousands/sec | Millions/sec |
| **Use Case** | Task queues, RPC | Event sourcing, logs, analytics |
| **Ordering** | Per queue | Per partition |

### When To Use Kafka:
```
┌─────────────────────────────────────────────────────────────────┐
│                        KAFKA USE CASES                          │
│                                                                 │
│   1. Event Sourcing - Store all events, replay anytime          │
│   2. Log Aggregation - Collect logs from all services           │
│   3. Real-time Analytics - Process millions of events/sec       │
│   4. Activity Tracking - User clicks, page views                │
│   5. Data Pipelines - Connect databases, apps, analytics        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Kafka Architecture:
```
┌─────────────────────────────────────────────────────────────────┐
│                        KAFKA CLUSTER                            │
│                                                                 │
│   TOPIC: orders                                                 │
│   ┌───────────────────────────────────────────────────────┐    │
│   │ Partition 0: [msg1] [msg4] [msg7] [msg10]             │    │
│   │ Partition 1: [msg2] [msg5] [msg8] [msg11]             │    │
│   │ Partition 2: [msg3] [msg6] [msg9] [msg12]             │    │
│   └───────────────────────────────────────────────────────┘    │
│                                                                 │
│   Producers ──────► Kafka ──────► Consumer Groups               │
│   (Order Service)              (Analytics, Notifications)       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8️⃣ Observability (Logs, Metrics, Tracing)

### The Three Pillars:

```
┌─────────────────────────────────────────────────────────────────┐
│                    OBSERVABILITY                                │
│                                                                 │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│   │    LOGS      │  │   METRICS    │  │   TRACING    │         │
│   │              │  │              │  │              │         │
│   │ What happened│  │ How is it    │  │ Where did    │         │
│   │ (events,     │  │ performing?  │  │ request go?  │         │
│   │  errors)     │  │ (CPU, memory,│  │ (across      │         │
│   │              │  │  requests/s) │  │  services)   │         │
│   └──────────────┘  └──────────────┘  └──────────────┘         │
│         │                  │                  │                 │
│         ▼                  ▼                  ▼                 │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│   │     ELK      │  │  Prometheus  │  │    Jaeger    │         │
│   │   Stack      │  │  + Grafana   │  │   Zipkin     │         │
│   └──────────────┘  └──────────────┘  └──────────────┘         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### 1. Logging (ELK Stack)
```
Service logs → Logstash → Elasticsearch → Kibana (Dashboard)

Example log:
{
  "timestamp": "2024-01-15T10:30:45",
  "service": "order-service",
  "level": "ERROR",
  "message": "Failed to create order",
  "orderId": "ORD-123",
  "userId": 456,
  "error": "Product not found"
}
```

### 2. Metrics (Prometheus + Grafana)
```
Metrics collected:
- HTTP requests per second
- Response times (p50, p95, p99)
- Error rates
- CPU/Memory usage
- Active connections

Grafana Dashboard shows graphs and alerts!
```

### 3. Distributed Tracing (Jaeger/Zipkin)
```
One request travels through multiple services:

User → Gateway → Order → Product → User → Payment
         │         │        │        │        │
     TraceID: abc-123 (same for entire request)
         │         │        │        │        │
     SpanID: 1  SpanID: 2 SpanID: 3 SpanID: 4 SpanID: 5

In Jaeger UI, you see the entire journey and time spent in each service!
```

---

## 🎯 Design Patterns Used

| Pattern | Where Used | Why |
|---------|------------|-----|
| **API Gateway** | api-gateway | Single entry point, security |
| **Service Registry** | discovery-server | Dynamic service discovery |
| **Database per Service** | Each service has own DB | Loose coupling, independence |
| **DTO Pattern** | All services | Separate internal entities from API |
| **Repository Pattern** | All services | Abstract data access |
| **Builder Pattern** | DTOs, Entities | Clean object construction |
| **Dependency Injection** | All services | Loose coupling, testability |

---

## 📝 Interview Questions & Answers

### Q1: Why microservices over monolith?
**A:** Microservices provide independent deployment, technology flexibility, team autonomy, and better scalability. Each service can be scaled, deployed, and developed independently.

### Q2: How do services find each other?
**A:** Using Eureka Service Discovery. Services register themselves at startup, and when they need to communicate, they query Eureka for the address.

### Q3: How is security handled?
**A:** JWT-based authentication at the API Gateway level. Users login via Auth Service, receive a JWT token, and include it in subsequent requests. Gateway validates the token before routing.

### Q4: What if a service goes down?
**A:** With Circuit Breaker (Resilience4j), the system fails gracefully. Instead of cascading failures, it returns fallback responses and retries when the service recovers.

### Q5: How are distributed transactions handled?
**A:** Using Saga Pattern. Either Choreography (event-driven) or Orchestration (central coordinator) ensures data consistency across services without distributed locks.

---

## 🚧 Future Enhancements

- [ ] Config Server for centralized configuration
- [ ] Circuit Breaker with Resilience4j
- [ ] Distributed Tracing with Zipkin/Jaeger
- [ ] Kafka for event streaming
- [ ] Docker containerization
- [ ] Kubernetes deployment
- [ ] CI/CD Pipeline
- [ ] API Documentation with Swagger

---

## 👨‍💻 Author

**Rushikesh** - Built for learning microservices architecture

---

## 📄 License

This project is for educational purposes.
