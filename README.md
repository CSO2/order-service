# 📦 Order Service

> Order processing, checkout, and trade-in management for the CS02 E-Commerce Platform

## 📋 Overview

The Order Service handles all order-related operations including checkout processing, order management, trade-in program, and return merchandise authorization (RMA). It integrates with the Cart Service and Product Catalogue Service to validate and process orders.

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.0 |
| Database | PostgreSQL | 15 |
| Service Communication | Spring Cloud OpenFeign | Latest |
| Build Tool | Maven | 3.x |
| ORM | Spring Data JPA | Latest |

## 🚀 API Endpoints

### Orders

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/orders` | Yes | Create new order (checkout) |
| `GET` | `/api/orders` | Yes | Get user's orders |
| `GET` | `/api/orders/recent` | Yes | Get recent orders (limit 5) |
| `GET` | `/api/orders/all` | Admin | Get all orders (admin) |
| `GET` | `/api/orders/{id}` | Yes | Get order details |
| `PUT` | `/api/orders/{id}/status` | Admin | Update order status |
| `PUT` | `/api/orders/{id}/cancel` | Yes | Cancel order |
| `GET` | `/api/orders/{id}/invoice` | Yes | Download invoice PDF |

### Trade-In

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/trade-in/quote` | Yes | Calculate trade-in value |
| `POST` | `/api/trade-in/submit` | Yes | Submit trade-in request |
| `GET` | `/api/trade-in` | Yes | Get user's trade-in requests |
| `GET` | `/api/trade-in/{id}` | Yes | Get trade-in details |
| `PUT` | `/api/trade-in/{id}/status` | Admin | Update trade-in status |

### RMA (Returns)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/orders/{id}/rma` | Yes | Request return |
| `GET` | `/api/rma` | Yes | Get user's RMA requests |
| `PUT` | `/api/rma/{id}/status` | Admin | Update RMA status |

## 📊 Data Models

### Order

```java
{
  "id": "uuid",
  "userId": "uuid",
  "orderNumber": "CS02-20240115-001",
  "items": [OrderItem],
  "shippingAddress": ShippingAddress,
  "billingAddress": Address,
  "subtotal": 1299.99,
  "tax": 103.99,
  "shippingCost": 0.00,
  "discount": 50.00,
  "total": 1353.98,
  "status": "PENDING | CONFIRMED | PROCESSING | SHIPPED | DELIVERED | CANCELLED",
  "paymentMethod": "CREDIT_CARD | PAYPAL",
  "paymentStatus": "PENDING | PAID | REFUNDED",
  "trackingNumber": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### OrderItem

```java
{
  "id": "uuid",
  "productId": "string",
  "productName": "string",
  "quantity": 1,
  "price": 599.99,
  "imageUrl": "string"
}
```

### ShippingAddress

```java
{
  "name": "string",
  "street": "string",
  "city": "string",
  "state": "string",
  "zipCode": "string",
  "country": "string",
  "phone": "string"
}
```

### TradeInRequest

```java
{
  "id": "uuid",
  "userId": "uuid",
  "productType": "GPU | CPU | LAPTOP | DESKTOP",
  "brand": "string",
  "model": "string",
  "condition": "EXCELLENT | GOOD | FAIR | POOR",
  "estimatedValue": 250.00,
  "finalValue": 225.00,
  "status": "PENDING | APPROVED | REJECTED | COMPLETED",
  "createdAt": "datetime"
}
```

### RMARequest

```java
{
  "id": "uuid",
  "orderId": "uuid",
  "orderItemId": "uuid",
  "reason": "DEFECTIVE | WRONG_ITEM | NOT_AS_DESCRIBED | OTHER",
  "description": "string",
  "status": "PENDING | APPROVED | REJECTED | REFUNDED",
  "refundAmount": 599.99,
  "createdAt": "datetime"
}
```

## 🔧 Configuration

### Application Properties

```yaml
server:
  port: 8083

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/CSO2_order_service
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000

catalog-service:
  url: http://localhost:8082
cart-service:
  url: http://localhost:8084
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | No | `jdbc:postgresql://localhost:5432/CSO2_order_service` | Database URL |
| `SPRING_DATASOURCE_USERNAME` | No | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | No | `postgres` | Database password |
| `CATALOG_SERVICE_URL` | No | `http://localhost:8082` | Product service URL |
| `CART_SERVICE_URL` | No | `http://localhost:8084` | Cart service URL |
| `SERVER_PORT` | No | `8083` | Service port |

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## 🔌 Feign Clients

### CatalogServiceClient
```java
@FeignClient(name = "catalog-service", url = "${catalog-service.url}")
public interface CatalogServiceClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProduct(@PathVariable String id);
    
    @PostMapping("/api/products/validate-stock")
    StockValidationResult validateStock(@RequestBody List<StockRequest> items);
    
    @PostMapping("/api/products/reduce-stock")
    void reduceStock(@RequestBody List<StockReduction> items);
}
```

### CartServiceClient
```java
@FeignClient(name = "cart-service", url = "${cart-service.url}")
public interface CartServiceClient {
    @GetMapping("/api/cart")
    CartDTO getCart(@RequestHeader("X-User-Id") String userId);
    
    @DeleteMapping("/api/cart/clear")
    void clearCart(@RequestHeader("X-User-Id") String userId);
}
```

## 🏃 Running the Service

### Local Development

```bash
cd backend/order-service

# Using Maven Wrapper
./mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

### Docker

```bash
cd backend/order-service

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t cs02/order-service .

# Run container
docker run -p 8083:8083 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/CSO2_order_service \
  -e CATALOG_SERVICE_URL=http://product-catalogue-service:8082 \
  -e CART_SERVICE_URL=http://shoppingcart-wishlist-service:8084 \
  cs02/order-service
```

## 🗄️ Database Requirements

- **PostgreSQL** running on port `5432`
- Database: `CSO2_order_service`
- Tables auto-created via JPA (ddl-auto: update)

### Database Schema

```sql
-- orders table
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    subtotal DECIMAL(10,2),
    tax DECIMAL(10,2),
    shipping_cost DECIMAL(10,2),
    discount DECIMAL(10,2),
    total DECIMAL(10,2),
    status VARCHAR(20),
    payment_method VARCHAR(20),
    payment_status VARCHAR(20),
    tracking_number VARCHAR(100),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- order_items table
CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(id),
    product_id VARCHAR(100),
    product_name VARCHAR(255),
    quantity INTEGER,
    price DECIMAL(10,2),
    image_url VARCHAR(500)
);
```

## ✅ Features - Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| Order creation (checkout) | ✅ Complete | Cart-to-order conversion |
| Order history | ✅ Complete | User order listing |
| Order details | ✅ Complete | Full order information |
| Order status updates | ✅ Complete | Admin status management |
| Order cancellation | ✅ Complete | User-initiated cancel |
| Trade-in quotes | ✅ Complete | Value estimation |
| Trade-in submission | ✅ Complete | Request processing |
| Feign client integration | ✅ Complete | Cart & Catalog services |
| Stock validation | ✅ Complete | Pre-checkout validation |
| Stock reduction | ✅ Complete | Post-checkout deduction |

### **Overall Completion: 80%** ✅

## ❌ Not Implemented / Future Enhancements

| Feature | Priority | Notes |
|---------|----------|-------|
| Payment gateway integration | High | Stripe, PayPal integration |
| Invoice PDF generation | Medium | Currently placeholder |
| RMA controller endpoints | Medium | Entity exists, needs controller |
| Order email notifications | Medium | Via Notifications Service |
| Shipping carrier integration | Low | UPS, FedEx APIs |
| Order splitting | Low | Multiple shipments |
| Promo code validation | Medium | Discount codes |
| Tax calculation service | Low | Dynamic tax rates |

## 📁 Project Structure

```
order-service/
├── src/
│   ├── main/
│   │   ├── java/com/cs02/order/
│   │   │   ├── OrderServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── OrderController.java
│   │   │   │   └── TradeInController.java
│   │   │   ├── model/
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── ShippingAddress.java
│   │   │   │   ├── TradeInRequest.java
│   │   │   │   └── RMARequest.java
│   │   │   ├── repository/
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── TradeInRepository.java
│   │   │   ├── service/
│   │   │   │   ├── OrderService.java
│   │   │   │   └── TradeInService.java
│   │   │   ├── client/
│   │   │   │   ├── CatalogServiceClient.java
│   │   │   │   └── CartServiceClient.java
│   │   │   └── dto/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Test endpoints
curl -H "X-User-Id: user123" http://localhost:8083/api/orders
curl -H "X-User-Id: user123" http://localhost:8083/api/orders/recent
curl -X POST -H "X-User-Id: user123" http://localhost:8083/api/trade-in/quote \
  -H "Content-Type: application/json" \
  -d '{"productType": "GPU", "brand": "NVIDIA", "model": "RTX 3080", "condition": "GOOD"}'
```

## 🔗 Related Services

- [API Gateway](../api-gateway/README.md) - Routes `/api/orders/*` and `/api/trade-in/*`
- [Product Catalogue Service](../product-catalogue-service/README.md) - Product info and stock
- [Shopping Cart Service](../shoppingcart-wishlist-service/README.md) - Cart retrieval
- [Notifications Service](../notifications-service/README.md) - Order notifications

## 📝 Notes

- Service runs on port **8083**
- Uses **PostgreSQL** for transactional data
- Integrates with Cart and Catalog services via **Feign**
- Order numbers are auto-generated: `CS02-YYYYMMDD-XXX`
- Stock is validated before checkout and reduced after
