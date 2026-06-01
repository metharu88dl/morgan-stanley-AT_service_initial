# 🏛️ Assignment: Account & Trade Services

This repository contains a complete, production-grade microservice solution consisting of two services:
1. **Account Service**: A Spring Boot RESTful service for managing accounts, persisted in an H2 in-memory database using Spring Data JPA.
2. **Trade Service**: A Spring Boot service that exposes a REST publishing endpoint (`POST /trades/publish`) and a Kafka Consumer that consumes XML trade messages, calls the Account Service to retrieve customer information, and logs the fully enriched details as a beautiful JSON document.

---

## 📂 Folder Structure

```
A_T_service/
│
├── account-service/                  # RESTful CRUD service (Port 8081)
│   ├── .mvn/                         # Maven Wrapper files
│   ├── src/main/java/.../
│   │   ├── entity/Account.java       # Account JPA Entity
│   │   ├── repository/AccountRepository.java
│   │   ├── service/AccountService.java
│   │   ├── controller/AccountController.java
│   │   └── exception/                # ResourceNotFound, InvalidRequest, GlobalExceptionHandler
│   ├── src/main/resources/
│   │   └── application.properties    # H2 DB & Server Port config
│   ├── mvnw.cmd                      # Windows Maven Wrapper
│   └── pom.xml                       # JPA, Web, H2, SpringDoc Swagger dependencies
│
├── trade-service/                    # Kafka REST Producer & Consumer service (Port 8082)
│   ├── .mvn/                         # Maven Wrapper files
│   ├── src/main/java/.../
│   │   ├── config/AppConfig.java     # RestTemplate declaration
│   │   ├── model/                    # TradeXml, AccountDto, EnrichedTradeDto
│   │   ├── controller/TradeController.java # POST /trades/publish REST endpoint
│   │   ├── service/TradeConsumerService.java # Kafka Listener & enrichment logic
│   │   └── exception/                # InvalidXml, KafkaPublish, AccountService, GlobalExceptionHandler
│   ├── src/main/resources/
│   │   └── application.properties    # Kafka consumer/producer & Server Port config
│   ├── mvnw.cmd                      # Windows Maven Wrapper
│   └── pom.xml                       # Kafka, XML, SpringDoc Swagger dependencies
│
├── docker-compose.yml                # Launches local single-node Kafka in KRaft mode
├── send_test_trade.py                # Python script to send XML trade message directly to broker
└── README.md                         # Project documentation
```

---

## 🛠️ Tech Stack & Requirements
- **Java**: Version 17+ (Java 24 fully supported)
- **Maven**: (Self-bootstrapped via included `mvnw.cmd` wrappers)
- **Spring Boot**: 3.2.5
- **Swagger / OpenAPI**: SpringDoc OpenAPI 2.5.0
- **Message Broker**: Kafka (KRaft mode)
- **Database**: H2 (In-memory)
- **Object Mapping**: Jackson XML & Standard clean Java DTOs

---

## 🚀 How to Run the Project

Follow these steps sequentially to run the entire flow locally.

### Step 1: Start local Kafka
Make sure Docker Desktop is running, then start the Kafka broker using:
```bash
docker compose up -d
```
*This starts a lightweight Kafka broker on `localhost:9092` in KRaft mode (no Zookeeper required!).*

### Step 2: Start the Account Service
Open a terminal in the `account-service/` directory and run:
```cmd
./mvnw.cmd spring-boot:run
```
*The service will start on **http://localhost:8081**. H2 Console is available at **http://localhost:8081/h2-console** (JDBC URL: `jdbc:h2:mem:accountdb`, User: `sa`, Password: `password`).*

### Step 3: Start the Trade Service
Open a second terminal in the `trade-service/` directory and run:
```cmd
./mvnw.cmd spring-boot:run
```
*The service will start on **http://localhost:8082** and begin listening to the `trade-topic` on your local Kafka broker.*

---

## 📖 Swagger / OpenAPI Documentation
Both services have full Swagger UI API documentation built in:
* 🏛️ **Account Service Swagger UI**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
* 📈 **Trade Service Swagger UI**: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

---

## 🧪 Verification & API Examples

### 1. Account Service REST API

Here are the sample PowerShell/Command Prompt commands to interact with the **Account Service**:

#### ➕ Create a New Account (`POST /accounts`)
```powershell
Invoke-RestMethod -Uri http://localhost:8081/accounts -Method Post -ContentType "application/json" -Body '{"accountNumber":"ACC1001","accountHolderName":"Tharun Tejas","currency":"USD","branch":"New York"}'
```

#### 🔍 Get Account Details (`GET /accounts/{accountNumber}`)
```powershell
Invoke-RestMethod -Uri http://localhost:8081/accounts/ACC1001 -Method Get
```

#### 📋 Get All Accounts (`GET /accounts`)
```powershell
Invoke-RestMethod -Uri http://localhost:8081/accounts -Method Get
```

#### ✏️ Update an Account (`PUT /accounts/{accountNumber}`)
```powershell
Invoke-RestMethod -Uri http://localhost:8081/accounts/ACC1001 -Method Put -ContentType "application/json" -Body '{"accountHolderName":"Tharun Tejas Suresh","currency":"EUR","branch":"London"}'
```

#### ❌ Delete an Account (`DELETE /accounts/{accountNumber}`)
```powershell
Invoke-RestMethod -Uri http://localhost:8081/accounts/ACC1001 -Method Delete
```

#### ⚠️ Global JSON Exception Handling Examples
If you search for a non-existent account (`GET /accounts/ACC-999`), it returns a structured JSON payload:
```json
{
  "timestamp": "2026-05-28T21:18:24.12512",
  "status": 404,
  "error": "Not Found",
  "message": "Account 'ACC-999' not found",
  "path": "/accounts/ACC-999"
}
```

If you try to create an account with a missing or empty name (`POST /accounts`), it returns a `400 Bad Request` JSON:
```json
{
  "timestamp": "2026-05-28T21:19:12.56234",
  "status": 400,
  "error": "Bad Request",
  "message": "Account holder name is required",
  "path": "/accounts"
}
```

---

### 2. Trade Service Integration (Kafka to JSON)

Once you have created the account `ACC1001` in the Account Service, trigger the XML consumer flow:

#### Option A: Publish XML via the New REST Endpoint (`POST /trades/publish`)
This endpoint accepts XML in the request body, validates it, produces it to the Kafka topic via `KafkaTemplate`, and returns a clean demo-friendly JSON response:

```powershell
Invoke-RestMethod -Uri http://localhost:8082/trades/publish -Method Post -ContentType "application/xml" -Body '<trade><tradeId>TR-555</tradeId><time>2026-05-28T12:00:00Z</time><amount>25000.00</amount><accountNumber>ACC1001</accountNumber></trade>'
```
**JSON Response Output:**
```json
{
  "message": "Trade message published successfully",
  "tradeId": "TR-555",
  "topic": "trade-topic"
}
```

.
*If you publish malformed or invalid XML, the REST API intercepts it and returns a clean `400 Bad Request` JSON:*
```json
{
  "timestamp": "2026-05-28T21:20:10.12456",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid or malformed XML syntax: Unexpected end-of-input...",
  "path": "/trades/publish"
}
```

#### Option B: Use the Python Helper
Run the python script from the root folder to push directly to Kafka (bypassing REST):
```bash
python send_test_trade.py
```

### 🎯 Expected Console Output (Trade Service)
When the Trade Service consumes the message from Kafka, it fetches `ACC1001` details from the Account Service, enriches the trade details, and logs the beautiful JSON block to the console:

```json
{
  "tradeId" : "TR-555",
  "time" : "2026-05-28T12:00:00Z",
  "amount" : 25000.0,
  "accountNumber" : "ACC1001",
  "accountHolderName" : "Tharun Tejas Suresh",
  "currency" : "EUR",
  "branch" : "London"
}
```

---

## 📬 Postman Request & Payload Samples

### 1. Create/Update Account (POST / PUT `/accounts`)
**URL**: `http://localhost:8081/accounts` (or `http://localhost:8081/accounts/ACC1001` for PUT)  
**Headers**: `Content-Type: application/json`  
**Body (JSON)**:
```json
{
  "accountNumber": "ACC1001",
  "accountHolderName": "Tharun Tejas Kurubarahalli Suresh Murthy",
  "currency": "USD",
  "branch": "Morgan Stanley London Branch"
}
```

### 2. Publish REST XML Trade Message (POST `/trades/publish`)
**URL**: `http://localhost:8082/trades/publish`  
**Headers**: `Content-Type: application/xml`  
**Body (XML)**:
```xml
<trade>
    <tradeId>TR-75489</tradeId>
    <time>2026-05-28T09:15:30.450Z</time>
    <amount>1245000.00</amount>
    <accountNumber>ACC1001</accountNumber>
</trade>
```
