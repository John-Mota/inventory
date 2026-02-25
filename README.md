# Autoflex Inventory API

## 📖 Project Overview

**Autoflex Inventory API** is a robust, scalable backend solution designed to manage inventory, product composition, and production planning. Built with **Java 21** and **Spring Boot 3**, this application adheres to modern software architecture principles, ensuring high cohesion, low coupling, and strict separation of concerns (SoC) through a layered architecture.

The system is engineered to handle complex business logic, such as the **Production Suggestion Algorithm (RF008)**, which optimizes manufacturing output based on current stock levels and product value prioritization.

---

## 🛠 Technologies & Stack

The architecture leverages a modern, cloud-native stack to ensure performance and maintainability:

*   **Language:** Java 21 (LTS)
*   **Framework:** Spring Boot 3.x
*   **ORM/Persistence:** Spring Data JPA, Hibernate
*   **Database:** PostgreSQL (hosted on Supabase)
*   **Build Tool:** Maven
*   **Containerization:** Docker
*   **Cloud Provider:** Render (PaaS)

---

## 🏗 Infrastructure & Resilience

### Database Connection Pooling (Supabase & PgBouncer)

To ensure scalability and handle a high volume of concurrent connections, the application connects to **Supabase** via **PgBouncer** configured in **Transaction Mode**.

### Critical Configuration: `prepareThreshold=0`

PgBouncer in Transaction Mode is incompatible with protocol-level prepared statements. To prevent connection instability and protocol errors, the JDBC connection string **must** include `prepareThreshold=0`. This forces the PostgreSQL driver to disable client-side prepared statements, ensuring stability within the connection pool.

**Example Configuration:**
```properties
spring.datasource.url=jdbc:postgresql://<HOST>:6543/postgres?prepareThreshold=0
```

---

## 🚀 Key Features

### 1. Raw Material Management
Full CRUD operations for raw materials, tracking stock quantities and definitions.

### 2. Product Composition
Management of products and their bill of materials (BOM). The system handles the complex relationships between products and their required raw materials.

### 3. Production Suggestion Algorithm (RF008)
A sophisticated algorithm located in the Service Layer that:
*   Analyzes current raw material stock levels.
*   Calculates the maximum possible production quantity for each product.
*   **Prioritization Strategy:** Automatically prioritizes products with higher market value to maximize potential revenue.
*   Simulates stock consumption in-memory to provide accurate, non-overlapping suggestions.

---

## ⚙️ Architecture & Design Principles

This project follows **SOLID** principles to ensure a clean and testable codebase:

*   **Single Responsibility Principle (SRP):** Each class has a distinct responsibility (e.g., `ProductionServiceImpl` handles only production logic, distinct from `ProductController`).
*   **Dependency Inversion:** Services are injected via interfaces, allowing for easy mocking and unit testing.
*   **DTO Pattern:** Data Transfer Objects are used to decouple the internal database entities from the external API contract, preventing over-fetching and accidental data exposure.

---

## 🔧 Environment Variables

The application requires the following environment variables to be set (either in `application.properties`, Docker environment, or Cloud provider settings):

| Variable | Description | Example |
| :--- | :--- | :--- |
| `DB_URL` | The JDBC URL for the PostgreSQL database. | `jdbc:postgresql://host:6543/db?prepareThreshold=0` |
| `DB_USERNAME` | Database user. | `postgres` |
| `DB_PASSWORD` | Database password. | `secret_password` |

---

## 🔌 API Endpoints

### Raw Materials
*   `GET /raw-materials` - List all raw materials.
*   `POST /raw-materials` - Create a new raw material.
*   `PUT /raw-materials/{id}` - Update stock or details.
*   `DELETE /raw-materials/{id}` - Remove a raw material.

### Products
*   `GET /products` - List all products.
*   `POST /products` - Create a new product.
*   `POST /products/{id}/raw-materials` - Associate raw materials with a product.

### Production
*   `GET /production/suggestions` - **(RF008)** Returns a prioritized list of products suggested for manufacturing based on available stock.

---

## ▶️ How to Run

### Prerequisites
*   Java 21 SDK
*   Maven

### Local Execution

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-org/inventory-back.git
    cd inventory-back
    ```

2.  **Configure Environment:**
    Set the required environment variables in your IDE or export them in your terminal.

3.  **Run the Application:**
    Use the Maven wrapper to start the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```

4.  **Access the API:**
    The server will start on port `8080` (default).
