# 🏦 Loan Origination & Management API

This project is an enterprise-level backend service simulating the "Loan Origination" processes that form the foundation of modern financial systems and banking infrastructures.

Unlike traditional layered architectures, this project combines **Domain-Driven Design (DDD)**, **Hexagonal Architecture (Ports & Adapters)**, and **Event-Driven Architecture (EDA)** to create a system that is business-rule-centric, infrastructure-agnostic, testable, and highly scalable.

## Architectural Design

The system is built on **Hexagonal Architecture**, completely isolating the core business logic from the outside world (databases, web interfaces, message queues).

* **Domain Layer:** The heart of the system. Loans (`Loan`), Money (`Money`), and Events (`DomainEvent`) live here. It has zero external dependencies (not even Spring annotations) and is written in pure Java.
* **Application Layer:** Contains the Use-Cases. It handles orchestration and defines interfaces (Ports) to communicate with the outside world.
* **Infrastructure Layer:** Contains Adapters that communicate with the Database (PostgreSQL) and Message Broker (Kafka).
* **Presentation Layer:** The inbound adapter that handles HTTP (REST) requests from the outside world.

## Key Features

* **Tactical DDD Principles:** Rich Domain Model, Aggregate Root (`Loan`), Value Objects (`Money`), and immutable Domain Events (utilizing Java `record`).
* **Event-Driven Architecture (EDA):** When critical business processes occur (like loan approval), the system asynchronously fires and listens to events (e.g., `LoanApplicationSubmittedEvent`) via Kafka.
* **Persistence Ignorance:** Business rules (Domain) and database tables (JPA Entities) are strictly separated.
* **Modern Infrastructure:** Next-generation Kafka (KRaft) requiring no Zookeeper, integrated via Docker Compose.

## Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.x, Spring Data JPA, Spring Kafka
* **Database:** PostgreSQL
* **Message Broker:** Apache Kafka (confluent-local)
* **Containerization:** Docker & Docker Compose
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)

## Local Development Setup

Follow these steps to run the project on your local machine.

### Prerequisites
* Java 17 or higher
* Docker Desktop (to run Kafka)
* Maven

### 1. Clone the Repository
```bash
git clone https://github.com/Bengiilhan/loan-origination-api.git
cd loan-origination-api
```

### 2. Start the Infrastructure (Kafka)
Start the Kafka server in the background using the provided `docker-compose.yml` file:
```bash
docker-compose up -d
```
*(Note: Ensure your local PostgreSQL is running on `localhost:5432` with a database named `loan_db`. Adjust credentials in `application.yml` if necessary.)*

### 3. Run the Application
Run the `LoanApiApplication` class via your IDE or use Maven from the terminal:
```bash
mvn spring-boot:run
```

## API Documentation & Testing

Once the application is running, you can access the interactive API documentation (Swagger UI) via your browser:

👉 **http://localhost:8080/swagger-ui/index.html**

From there, you can use the `POST /api/v1/loans` endpoint to create a new loan application and observe the asynchronous messages (Events) fired to Kafka in your console:

```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "amount": 50000.00,
  "currency": "TRY"
}
```

*Sample Output:*
`📬 [NOTIFICATION SERVICE] NEW EVENT CAPTURED! ... LoanApplicationSubmittedEvent`
