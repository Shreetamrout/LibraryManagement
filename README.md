
# 📚 Library Book Management System

A **Spring Boot REST API** for managing library books, borrowers, and borrowing operations — built with a layered architecture, validation, and strong business logic rules.

---

## 📋 How to Set Up and Run the Project

### 🧰 Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Any IDE** (IntelliJ IDEA, Eclipse, or VS Code)

---

### ⚙️ Installation Steps

#### **Step 1: Clone / Download the Project**
```
git clone <repository-url>
cd LibraryManagement
```
#### Step 2: Build the Project

- mvn clean install

#### Step 3: Run the Application

**Option 1: Using Maven**  
- mvn spring-boot:run


**Option 2: From IDE**

- Open the project in your IDE
- Run `LibraryBookApplication.java`

#### Step 4: Access the Application

Once started, the application will be available at:  
**API Base URL:** `http://localhost:8080`

## 🛠️ Tools, Libraries, and Frameworks Used

| Core Technologies  | Version | Purpose                  |
|--------------------|---------|--------------------------|
| Java               | 17      | Programming Language     |
| Spring Boot        | 3.5.7   | Application Framework    |
| Maven              | 3.6+    | Build & Dependency Management |
| H2 Database        | 2.x     | In-memory Database       |

## 🏗️ Overall Approach and Thought Process

### Architecture Design

I implemented a Layered Architecture pattern to ensure clean separation of concerns:


│ Controller Layer (REST API)      │ ← Handles HTTP requests/responses

│ Service Layer (Business)         │ ← Business logic & validations

│ Repository Layer (Data)          │ ← Database operations

│ Database (H2)                    │ ← Data storage



### Implementation Steps

- Analyzed Requirements - Identified entities, relationships, and business rules
- Database Design - Created entities with proper relationships
- Repository Layer - Built data access with custom JPQL queries
- Service Layer - Implemented business logic and validations
- Controller Layer - Exposed REST APIs following RESTful principles
- Exception Handling - Centralized error responses
- Bonus Features - Added caching, scheduler, and Swagger documentation

## 🚧 Challenges Faced and How I Solved Them

### Challenge 1: Auto-Updating Book Availability

**Problem:**  
`isAvailable` field needs to automatically update when `availableCopies` changes.

**Solution:**

Used JPA lifecycle callbacks (`@PrePersist` and `@PreUpdate`):

```@PrePersist
@PreUpdate
public void updateAvailability() {
this.isAvailable = this.availableCopies > 0;
}
```

- This method executes automatically before INSERT/UPDATE operations
- Eliminates need for manual updates, ensuring data consistency

### Challenge 2: Consistent Error Responses

**Problem:**  
Different exceptions were returning inconsistent error formats, making it hard for API consumers.

**Solution:**

Created `@RestControllerAdvice` with multiple `@ExceptionHandler` methods:

```
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleResourceNotFound(...) {
return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
}
```

- Standardized error response format with `ErrorResponse` DTO
- Included timestamp, status code, error message, and request path
- Added field-level validation error details

---
