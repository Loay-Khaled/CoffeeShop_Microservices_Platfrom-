# ☕ CoffeeShop Microservices Platform

A full-stack microservices-based coffee shop application with modern technologies including Spring Boot, React, Keycloak authentication, Kafka messaging, and PostgreSQL database.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-green)
![React](https://img.shields.io/badge/React-18.2.0-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![Keycloak](https://img.shields.io/badge/Keycloak-25.0-red)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Technologies Used](#-technologies-used)
- [Prerequisites](#-prerequisites)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Running the Application](#-running-the-application)
- [Default Users](#-default-users)
- [API Endpoints](#-api-endpoints)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Overview

CoffeeShop Microservices Platform is a complete e-commerce solution for a coffee shop business. It demonstrates a microservices architecture with the following capabilities:

- **Product Catalog Management** - Browse and manage coffee products
- **Order Management** - Create, track, and manage customer orders
- **Payment Processing** - Process payments for orders
- **Notifications** - Event-driven notifications via Kafka
- **Admin Dashboard** - Full admin panel for product and order management
- **User Authentication** - Secure authentication via Keycloak OAuth2/OpenID Connect

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Frontend (React)                         │
│                    http://localhost:3000                         │
└─────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Keycloak (Auth Server)                      │
│                    http://localhost:8081                         │
└─────────────────────────────────────────────────────────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        ▼                        ▼                        ▼
┌───────────────┐      ┌───────────────┐      ┌───────────────┐
│ Catalog       │      │ Order         │      │ Payment       │
│ Service       │      │ Service       │      │ Service       │
│ :8082         │      │ :8084         │      │ :8085         │
└───────────────┘      └───────────────┘      └───────────────┘
        │                        │                        │
        └────────────────────────┼────────────────────────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
            ┌───────────────┐        ┌───────────────┐
            │ PostgreSQL    │        │ Kafka         │
            │ :5433         │        │ :9092         │
            └───────────────┘        └───────────────┘
                                            │
                                            ▼
                                    ┌───────────────┐
                                    │ Notification  │
                                    │ Service       │
                                    │ :8086         │
                                    └───────────────┘
```

---

## 🛠 Technologies Used

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Programming Language |
| **Spring Boot** | 3.5.9 | Backend Framework |
| **Spring Security** | - | Security & OAuth2 Resource Server |
| **Spring Data JPA** | - | Database ORM |
| **Spring Kafka** | - | Message Broker Integration |
| **Flyway** | - | Database Migration |
| **Maven** | 3.x | Build Tool |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18.2.0 | Frontend Framework |
| **React Router** | 6.20.0 | Client-side Routing |
| **Axios** | 1.6.2 | HTTP Client |
| **Keycloak JS** | 25.0.0 | Authentication |
| **React Hot Toast** | 2.4.1 | Notifications |

### Infrastructure
| Technology | Version | Purpose |
|------------|---------|---------|
| **Docker** | Latest | Containerization |
| **Docker Compose** | Latest | Container Orchestration |
| **PostgreSQL** | 16 | Relational Database |
| **Keycloak** | 25.0 | Identity & Access Management |
| **Apache Kafka** | 7.6.1 | Message Broker |
| **Zookeeper** | 7.6.1 | Kafka Coordination |
| **RabbitMQ** | 3 | Message Queue (Optional) |

---

## ✅ Prerequisites

Before you begin, ensure you have the following installed:

### Required Software

1. **Java Development Kit (JDK) 17+**
   - Download: https://adoptium.net/temurin/releases/
   - Verify: `java -version`

2. **Apache Maven 3.6+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **Node.js 18+ & npm**
   - Download: https://nodejs.org/
   - Verify: `node -v` and `npm -v`

4. **Docker Desktop**
   - Download: https://www.docker.com/products/docker-desktop/
   - Verify: `docker --version` and `docker-compose --version`

5. **Git**
   - Download: https://git-scm.com/downloads
   - Verify: `git --version`

### System Requirements
- **RAM**: Minimum 8GB (16GB recommended)
- **Disk Space**: At least 10GB free space
- **OS**: Windows 10/11, macOS, or Linux

---

## 📁 Project Structure

```
CoffeeShop-Microservices-Platform/
├── 📂 frontend/
│   └── 📂 web-frontend/           # React Frontend Application
│       ├── 📂 public/
│       ├── 📂 src/
│       │   ├── 📂 components/     # Reusable UI Components
│       │   ├── 📂 pages/          # Page Components
│       │   ├── 📂 services/       # API Services
│       │   ├── 📂 styles/         # CSS Stylesheets
│       │   ├── 📂 context/        # React Context
│       │   ├── App.js
│       │   ├── index.js
│       │   └── keycloak.js        # Keycloak Configuration
│       └── package.json
│
├── 📂 services/
│   ├── 📂 catalog-service/        # Product Catalog Microservice
│   │   ├── 📂 src/main/java/
│   │   ├── 📂 src/main/resources/
│   │   ├── pom.xml
│   │   └── Dockerfile
│   │
│   ├── 📂 order-service/          # Order Management Microservice
│   │   ├── 📂 src/main/java/
│   │   ├── 📂 src/main/resources/
│   │   ├── pom.xml
│   │   └── Dockerfile
│   │
│   ├── 📂 payment-service/        # Payment Processing Microservice
│   │   ├── 📂 src/main/java/
│   │   ├── 📂 src/main/resources/
│   │   ├── pom.xml
│   │   └── Dockerfile
│   │
│   └── 📂 notification-service/   # Notification Microservice
│       ├── 📂 src/main/java/
│       ├── 📂 src/main/resources/
│       ├── pom.xml
│       └── Dockerfile
│
├── 📂 infra/
│   ├── docker-compose.yml         # Docker Compose Configuration
│   ├── init-db.sh                 # Database Initialization Script
│   └── 📂 keycloak/
│       └── realm-export.json      # Keycloak Realm Configuration
│
└── README.md
```

---

## 🚀 Installation & Setup

### Step 1: Clone the Repository

```bash
git clone https://github.com/Loay-Khaled/CoffeeShop_Microservices_Platfrom-.git
cd CoffeeShop_Microservices_Platfrom-
```

### Step 2: Start Infrastructure Services (Docker)

Navigate to the infrastructure folder and start Docker containers:

```bash
cd infra
docker-compose up -d
```

This will start:
- **PostgreSQL** on port `5433`
- **Keycloak** on port `8081`
- **Kafka** on port `9092`
- **Zookeeper** on port `2181`
- **RabbitMQ** on ports `5672` and `15672`

Wait for all containers to be healthy (approximately 1-2 minutes):

```bash
docker-compose ps
```

### Step 3: Build Backend Services

Open a new terminal and build each microservice:

```bash
# Build Catalog Service
cd services/catalog-service
mvn clean package -DskipTests

# Build Order Service
cd ../order-service
mvn clean package -DskipTests

# Build Payment Service
cd ../payment-service
mvn clean package -DskipTests

# Build Notification Service
cd ../notification-service
mvn clean package -DskipTests
```

### Step 4: Install Frontend Dependencies

```bash
cd frontend/web-frontend
npm install
```

---

## ▶️ Running the Application

### Option 1: Run Services Individually (Development Mode)

#### Terminal 1 - Catalog Service:
```bash
cd services/catalog-service
java -jar target/catalog-service-0.0.1-SNAPSHOT.jar
```

#### Terminal 2 - Order Service:
```bash
cd services/order-service
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

#### Terminal 3 - Payment Service:
```bash
cd services/payment-service
java -jar target/payment-service-0.0.1-SNAPSHOT.jar
```

#### Terminal 4 - Notification Service:
```bash
cd services/notification-service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar
```

#### Terminal 5 - Frontend:
```bash
cd frontend/web-frontend
npm start
```

### Option 2: Run with Docker Compose (All Services)

```bash
cd infra
docker-compose --profile all up -d
```

### Verify Services are Running

| Service | URL | Health Check |
|---------|-----|--------------|
| Frontend | http://localhost:3000 | Open in browser |
| Keycloak | http://localhost:8081 | Admin Console |
| Catalog Service | http://localhost:8082 | API responds |
| Order Service | http://localhost:8084 | API responds |
| Payment Service | http://localhost:8085 | API responds |
| Notification Service | http://localhost:8086 | Logs show Kafka connection |
| RabbitMQ Console | http://localhost:15672 | Login with admin/admin |

---

## 👤 Default Users

The following users are pre-configured in Keycloak:

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin1` | `123456` | Admin | Full access to admin dashboard, product & order management |
| `customer1` | `123456` | Customer | Browse products, place orders, view order history |

### Keycloak Admin Console
- **URL**: http://localhost:8081
- **Username**: `admin`
- **Password**: `admin`

---

## 🔌 API Endpoints

### Catalog Service (Port 8082)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/catalog/items` | Get all products | Yes |
| GET | `/api/catalog/items/{id}` | Get product by ID | Yes |
| POST | `/api/catalog/items` | Create new product | Yes (Admin) |
| PUT | `/api/catalog/items/{id}` | Update product | Yes (Admin) |
| DELETE | `/api/catalog/items/{id}` | Delete product | Yes (Admin) |

### Order Service (Port 8084)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/orders/me` | Get current user's orders | Yes |
| GET | `/api/orders/all` | Get all orders (Admin) | Yes (Admin) |
| GET | `/api/orders/{id}` | Get order by ID | Yes |
| POST | `/api/orders` | Create new order | Yes |
| POST | `/api/orders/{id}/items` | Add item to order | Yes |
| PUT | `/api/orders/{id}/status` | Update order status | Yes (Admin) |
| PUT | `/api/orders/{id}/cancel` | Cancel order | Yes |
| DELETE | `/api/orders/{id}` | Delete order | Yes (Admin) |

### Payment Service (Port 8085)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/payments/process/{orderId}` | Process payment | Yes |
| GET | `/api/payments/status/{orderId}` | Get payment status | Yes |

---

## ✨ Features

### Customer Features
- 🔐 **Secure Authentication** - Login/Logout via Keycloak
- 🛍️ **Browse Products** - View available coffee products
- 🛒 **Shopping Cart** - Add/remove items from cart
- 📦 **Place Orders** - Checkout and create orders
- 💳 **Pay for Orders** - Process payments
- 📋 **Order History** - View past orders
- ❌ **Cancel Orders** - Cancel pending orders

### Admin Features
- 📊 **Dashboard** - Overview of products, orders, and revenue
- 📝 **Product Management** - Create, edit, delete products
- 📦 **Order Management** - View all orders, update status, delete orders
- 📈 **Statistics** - Total revenue, pending orders, completed orders

---

## 📸 Screenshots

### Home Page
The landing page welcomes users to the Coffee Shop.

### Catalog Page
Browse all available coffee products with add-to-cart functionality.

### Cart Page
Review items in cart before checkout.

### Orders Page
View order history with payment and cancellation options.

### Admin Dashboard
Comprehensive admin panel for managing products and orders.

---

## 🔧 Troubleshooting

### Common Issues

#### 1. Docker containers not starting
```bash
# Check container logs
docker-compose logs -f

# Restart containers
docker-compose down
docker-compose up -d
```

#### 2. Port already in use
```bash
# Windows - Find process using port
netstat -ano | findstr :8082

# Kill process
taskkill /F /PID <PID>
```

#### 3. Keycloak not ready
Wait for Keycloak to fully initialize (check logs):
```bash
docker-compose logs -f keycloak
```

#### 4. Database connection issues
Ensure PostgreSQL is running and healthy:
```bash
docker-compose ps postgres
```

#### 5. Frontend can't connect to backend
- Ensure all backend services are running
- Check if CORS is properly configured
- Verify the API URLs in `frontend/web-frontend/src/services/api.js`

#### 6. Maven build fails
```bash
# Clean and rebuild
mvn clean install -DskipTests
```

#### 7. Node modules issues
```bash
# Remove and reinstall
rm -rf node_modules
npm install
```

---

## 📝 Environment Configuration

### Backend Services (application.yml)

Each service uses the following default configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/<service>_db
    username: admin
    password: admin123
  
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/realms/coffeeshop
```

### Frontend (.env)

```env
REACT_APP_KEYCLOAK_URL=http://localhost:8081
REACT_APP_KEYCLOAK_REALM=coffeeshop
REACT_APP_KEYCLOAK_CLIENT_ID=coffeeshop-client
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Loay Khaled**

- GitHub: [@Loay-Khaled](https://github.com/Loay-Khaled)

---

## 🙏 Acknowledgments

- Spring Boot Team
- React Team
- Keycloak Team
- Apache Kafka Team
- Docker Team

---

**Happy Coding! ☕**
