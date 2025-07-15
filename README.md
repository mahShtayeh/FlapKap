# FlapKap Vending Machine
Manage FlapKap vending machine.

## Documentation
### Design Decisions
1. Database Choice: Used PostgreSQL database from docker compose for simplicity in development, 
using a properties-configurable DB using Docker Compose.
2. Transaction Management: Used Spring's @Transactional annotation to ensure data consistency during deposits and buys.
3. UUID for Entities PKs: guarantee uniqueness across different systems without requiring a central coordinator, 
enhance security by being non-sequential and unpredictable, and simplify data migration and replication.
4. RESTful Design: Followed REST conventions for API endpoints with appropriate HTTP methods.

### Challenges
1. Transaction Isolation: Ensuring that concurrent deposits/buys don't lead to race conditions. Addressed with @Transactional and proper service layer design.
2. Error Handling: Implementing comprehensive error handling for business rules like insufficient funds.
3. Testing: Setting up integration tests that maintain state between test methods required careful ordering.

## Running the Application
- Clone the repository
```bash
    https://github.com/mahShtayeh/FlapKap.git
```
### Local Development
1. Run PostgreSQL DB on port: 5433, on localhost
2. Run the application
```bash
    mvn spring-boot:run
```

### Docker
1. Up PostgreSQL DB, PGAdmin, and Latest pushed image, using Docker Compose
```bash
    docker compose up -d
```

## API Documentation
- After starting the application, Swagger UI will be available at:
```http request
http://localhost:8080/swagger-ui/index.html
```