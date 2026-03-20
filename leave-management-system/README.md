# Employee Leave Management System

REST API built with Java Spring Boot + MySQL + Docker + Azure DevOps CI/CD.

---

## How to Run — End to End

### Prerequisites
- Java 17
- Maven
- Docker Desktop
- Postman (API testing ke liye)

---

### Option 1 — Docker Compose (Recommended)

Ye sabse easy way hai. MySQL aur App dono ek saath start ho jaate hain.

```bash
# Step 1: Project folder mein jao
cd leave-management-system

# Step 2: Docker Compose se start karo
# --build = pehli baar ya code change hone pe image rebuild karo
docker-compose up --build

# App start ho gayi check karo — ye line console mein dikhegi:
# Started LeaveApplication in X seconds

# Step 3: Browser mein check karo
# http://localhost:8080/api/employees

# Step 4: Band karna ho toh
docker-compose down

# Data bhi delete karna ho toh (MySQL volume bhi hata do)
docker-compose down -v
```

---

### Option 2 — Without Docker (Direct Run)

```bash
# Step 1: MySQL mein database banao
mysql -u root -p
CREATE DATABASE leavedb;
exit;

# Step 2: application.properties mein apna MySQL password daalo
# spring.datasource.password=YOUR_PASSWORD

# Step 3: Run karo
mvn spring-boot:run
```

---

### API Testing — Postman se karo

Postman open karo aur ye requests chalao order mein:

**1. Pehle employee banao**
```
POST http://localhost:8080/api/employees
Content-Type: application/json

{
  "name": "Khushbu Agrawal",
  "email": "khushbu@jio.com",
  "department": "DevOps",
  "totalLeaves": 18,
  "usedLeaves": 0
}
```

**2. Leave apply karo (id=1 wale employee ke liye)**
```
POST http://localhost:8080/api/employees/1/leaves
Content-Type: application/json

{
  "startDate": "2026-04-01",
  "endDate": "2026-04-03",
  "reason": "Medical"
}
```

**3. Pending leaves dekho**
```
GET http://localhost:8080/api/leaves/pending
```

**4. Leave approve karo (id=1 wali leave)**
```
PUT http://localhost:8080/api/leaves/1/status
Content-Type: application/json

{
  "status": "APPROVED"
}
```

**5. Leave balance check karo**
```
GET http://localhost:8080/api/employees/1/balance
```

---

## Project Structure

```
leave-management-system/
├── src/main/java/com/leaveapp/
│   ├── LeaveApplication.java        ← App entry point
│   ├── controller/
│   │   └── LeaveController.java     ← REST API endpoints
│   ├── model/
│   │   ├── Employee.java            ← Employee DB table
│   │   └── Leave.java               ← Leave DB table
│   ├── repository/
│   │   ├── EmployeeRepository.java  ← Employee DB queries
│   │   └── LeaveRepository.java     ← Leave DB queries
│   └── service/
│       └── LeaveService.java        ← Business logic
├── src/main/resources/
│   └── application.properties       ← DB + server config
├── Dockerfile                        ← Docker image build
├── docker-compose.yml               ← Multi-container setup
└── azure-pipelines.yml              ← CI/CD pipeline
```

---

## Concepts Used

| Concept | Kahan Use Kiya |
|---------|---------------|
| Spring Boot | Embedded Tomcat server, auto-configuration |
| REST API | GET, POST, PUT endpoints via @RestController |
| Spring Data JPA | Database operations bina SQL likhe |
| Hibernate ORM | Java objects ↔ MySQL tables mapping |
| Lombok | @Data se getter/setter auto-generate |
| @Transactional | Leave approve pe employee balance bhi ek saath update |
| Docker Multi-stage Build | Build stage alag, run stage alag — image chhoti hoti hai |
| Docker Compose | MySQL + App ek network pe ek saath run |
| Dependency Injection | Service aur Repository constructor se inject |
| Azure DevOps Pipeline | Push pe auto build → test → Docker image push |

---

## GitHub Setup

```bash
git init
git add .
git commit -m "Initial commit - Leave Management System"
git remote add origin https://github.com/khushbu2610/leave-management-system.git
git push -u origin main
```
