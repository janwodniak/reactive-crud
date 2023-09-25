# Reactive-CRUD 🚀

---

Reactive-CRUD is a Spring Boot project utilizing reactive programming to offer robust CRUD endpoints. It combines
various technologies, ensuring efficiency, responsiveness, and resilience.

## 🛠 **Technologies Used**

- **Core:**
  - 🌐 Spring Boot WebFlux
  - R2DBC
- **Database:**
  - 📊 PostgreSQL
  - Liquibase (for migrations)
- **Utilities:**
  - 🔄 ModelMapper
  - 💼 Lombok
- **Testing:**
  - 🧪 Spring Boot Test
  - 📦 TestContainers
  - 🌐 WebTau
  - ⚙️ Reactor Test
- **Development Tools:**
  - 🧰 Spring Boot DevTools
  - 🐳 Docker Compose for Spring Boot

---

## Architecture Layers:

### 1. Controller Layer

This layer handles incoming HTTP requests, delegating business operations, and returning HTTP responses.

| Class/Interface                                                                             | Description                                                                                                                                                                        |
|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [NoteController](src/main/java/dev/janwodniak/reactivecrud/controlller/NoteController.java) | A RESTful controller that exposes CRUD operations for `Notes`. Operations are non-blocking, leveraging reactive types. Uses `ModelMapper` to map between DTOs and domain entities. |

### 2. Service Layer

Encapsulates the core business logic of the application.

| Class/Interface                                                                                                | Description                                                                                                              |
|----------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| [ReactiveNoteService](src/main/java/dev/janwodniak/reactivecrud/service/ReactiveNoteService.java)              | Interface defining core operations related to notes.                                                                     |
| [DefaultNoteService](src/main/java/dev/janwodniak/reactivecrud/service/implementation/DefaultNoteService.java) | Concrete implementation of `ReactiveNoteService`. Operations are transactional and handle scenarios like note not found. |

### 3. Repository Layer

Provides an abstraction over the database operations, facilitating CRUD operations on the domain entities.

| Class/Interface                                                                            | Description                                                                                                                                        |
|--------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| [NoteRepository](src/main/java/dev/janwodniak/reactivecrud/repository/NoteRepository.java) | Repository for `Note` domain entities. Extends `R2dbcRepository` for reactive database access. Offers custom query methods for specific use cases. |

---

## Testing 🧪

Reactive-CRUD strongly emphasizes testing, particularly integration tests focused on the `NoteController`.

### [BaseIntegrationTest](src/test/java/dev/janwodniak/reactivecrud/controlller/BaseIntegrationTest.java) 🌐

- Sets a uniform environment for all tests.
- Uses a Docker-contained PostgreSQL to emulate the production database.
- Dynamically adjusts R2DBC and Liquibase properties based on the container's setup.

### Utilities ⚙️

- **DatabaseCleaner**: Resets the database after each test.
- **Mocked Clock**: Provides a fixed reference time for predictable outcomes.

### Tools Used 🛠

- **WebTau**: Simplifies validation for HTTP calls and data assertions.
- **Testcontainers**: Uses Docker containers for real-world system testing.

### Report 📊

- For detailed metrics, check the [Test Report](docs/webtau.report.html).

To execute tests using Gradle:

```bash
./gradlew test
```

---

## Native Support 🚀

Reactive-CRUD harnesses the power of native execution, promising faster startup times and reduced runtime overhead.

### Key Features:

- **GraalVM**: Embracing native support for GraalVM, this application optimizes performance and resource utilization.
- **Docker Images**: Benefit from pre-configured Docker images, designed to work seamlessly across multiple
  architectures, ensuring broad compatibility.

### Configuration Insights:

For a smooth native compilation experience with GraalVM, I have implemented specific hints and configurations.

---

## 🚀 Running the Reactive-CRUD Application

### **1. IDE Setup**:

- **IDE Options**:
  - **IntelliJ IDEA**
  - **Eclipse**

**Instructions**:

1. Open the project in your preferred IDE.
2. Locate and run the `ReactiveCrudApplication` class to start the application.
3. With Spring Boot's Docker Compose integration, necessary databases and services (e.g., PostgreSQL) will initialize
   automatically.

### **2. Using Gradle Command**:

For those who'd rather utilize the terminal or do not have an IDE setup:

```bash
./gradlew bootRun
```

### **3. 🐳 Docker Compose for Native Image:**

To run the docker-compose file crafted explicitly for the native image:

```bash
docker-compose -f reactive-crud-native.yaml up
```

### **4. Execute the following command to run the JAR file:**

```bash
java -jar build/libs/reactive-crud-0.0.1-SNAPSHOT.jar
```

---

## 📄 Documentation Resources

- **🛠 JetBrains IDE**: Use the tailored `.http` for API requests:
  - [note-controller.http](docs/note-controller.http)

- **🌐 Postman**: Import collection for easy endpoint testing:
  - [note-controller.postman_collection.json](docs/note-controller.postman_collection.json)

- **📊 Test Report**: View detailed test outcomes:
  - [webtau.report.html](docs/webtau.report.html)
