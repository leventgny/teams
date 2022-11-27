Getting Started
====

Please find a Java application built by Gradle and Spring Boot technologies focusing on Team and Player entities.


# Technologies Involved
- Java 8 - runtime environment
- Gradle - build tool
- Spring Boot - web framework
- H2 database - in memory database
- Open API / Swagger UI - API docs
- Lombok - annotation based class utilities


# Running Application
``` $ gradle bootRun ```

# API Endpoints

You can access API endpoints by;
- ``` http://localhost:8080/api-docs (Open API specification)```
- ``` http://localhost:8080/swagger-ui/index.html ```
- You can also import Postman JSON
  - ./teams.postman_collection.json

# Running Tests
``` $ gradle test ```
- (!) There is one test failing over unloaded OneToMany relation

# Upcoming resolutions
- Fix outstanding OneToMany relation on loading
- Fix Swagger UI on docker
- Introduce MongoDB with docker
- Introduce GraphQL and WebFlux