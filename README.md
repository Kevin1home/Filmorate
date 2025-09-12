# Filmorate Application

**Filmorate** is a Spring Boot-based application that allows users to manage films, likes, genres, ratings, and friendships.<br>
Users can create, update, and retrieve films and users, add likes to films, manage genres and ratings, and handle friendships.<br>
The application provides RESTful APIs for all operations.

---

## Table of Contents

- [Features](#features)  
- [Project Structure](#project-structure)  
- [Technologies](#technologies)  
- [Getting Started](#getting-started)  
- [Running Tests](#running-tests)  
- [API Endpoints](#api-endpoints)  

---

## Features

- **User Management**: Create, update, and list users; manage friendships.  
- **Film Management**: Add, update, and list films; manage likes.  
- **Genres & Ratings**: Retrieve and assign genres and ratings to films.  
- **Popular Films**: Fetch most liked/popular films.  
- **Exception Handling & Validation**: Handles invalid inputs, duplicates, and not-found resources.  

---

## Project Structure

```
Filmorate/src
├── main
│   ├── java/com/filmorate
│   │   ├── FilmorateApplication.java
│   │   ├── exception/
│   │   │   ├── ErrorHandler.java
│   │   │   ├── NotFoundException.java
│   │   │   └── ValidationException.java
│   │   ├── model/
│   │   │   ├── Film.java
│   │   │   ├── FilmLike.java
│   │   │   ├── GenreType.java
│   │   │   ├── RatingType.java
│   │   │   ├── User.java
│   │   │   └── Friendship.java
│   │   ├── storage/
│   │   │   ├── film/
│   │   │   │   ├── FilmDbStorage.java
│   │   │   │   ├── InMemoryFilmStorage.java
│   │   │   │   └── FilmStorage.java
│   │   │   └── user/
│   │   │       ├── UserDbStorage.java
│   │   │       ├── InMemoryUserStorage.java
│   │   │       └── UserStorage.java
│   └── resources/
│       ├── application.properties
│       └── schema.sql
└── test/java/ru/yandex/practicum/filmorate
    ├── film/
    │   ├── FilmDbStorageTest.java
    │   └── InMemoryFilmStorageTest.java
    └── user/
        ├── UserDbStorageTest.java
        └── InMemoryUserStorageTest.java
```

---

## Technologies

- Java 17  
- Spring Boot 3.x  
- Spring JDBC / H2 Database  
- Lombok  
- JUnit 5  

---

## Getting Started

1. **Clone repository**  
```bash
git clone <repository_url>
cd filmorate
```

2. **Build project**  
```bash
./mvnw clean install
```

3. **Run application**  
```bash
./mvnw spring-boot:run
```

4. **Access API** at `http://localhost:8080/`  

---

## Configuration

Database configuration is stored in `application.properties`:

```properties
# Database Initialization
spring.sql.init.mode=always

# DataSource
spring.datasource.url=jdbc:h2:file:./db/filmorate
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

# Optional H2 Console for development
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console
```

---

## Running Tests

```bash
./mvnw test
```

Includes unit tests for in-memory storage and DB storage implementations for both films and users.

---

## API Endpoints

### Users
- `POST /users` – Create a new user  
- `GET /users/{id}` – Get user by ID  
- `GET /users` – List all users  
- `PATCH /users/{id}` – Update user  

### Films
- `POST /films` – Create a new film  
- `GET /films/{id}` – Get film by ID  
- `GET /films` – List all films  
- `PATCH /films/{id}` – Update film  
- `POST /films/{id}/like/{userId}` – Add like to a film  
- `DELETE /films/{id}/like/{userId}` – Remove like  

### Genres
- `GET /genres` – List all genres  
- `GET /genres/{id}` – Get genre by ID  
- `GET /films/{id}/genres` – Get genres for a film  
- `POST /films/{id}/genres` – Add genres to a film  

### Ratings
- `GET /ratings` – List all ratings  
- `GET /ratings/{id}` – Get rating by ID  
- `GET /films/{id}/rating` – Get rating of a film  

### Popular Films
- `GET /films/popular?count={count}` – Get top liked/popular films  

### Friendships
- `POST /users/{id}/friends/{friendId}` – Add friend  
- `DELETE /users/{id}/friends/{friendId}` – Remove friend  
- `GET /users/{id}/friends` – List friends of user  

---

## License

Released under the **MIT License**.

