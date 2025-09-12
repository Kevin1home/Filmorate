# Filmorate Application

**Filmorate** is a movie selection service where the main criterion is popularity, determined by the number of user likes.<br>
Users can view detailed characteristics of each movie, including genre, rating, popularity, description, duration, release date, and title.<br>
In addition users can actively engage by liking movies and connecting with other users by adding them as friends, making the experience more interactive and social.<br>
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
│   │   ├── controller
│   │   │   ├── FilmController.java
│   │   │   ├── UserController.java
│   │   │   └── util
│   │   │       ├── ErrorHandler.java
│   │   │       └── ResponseTransfer.java
│   │   ├── excepsion
│   │   │   ├── IncorrectParameterException.java
│   │   │   ├── NotFoundException.java
│   │   │   ├── ValidationException.java
│   │   │   ├── film
│   │   │   │   ├── FilmAlreadyExistException.java
│   │   │   │   └── FilmNotFoundException.java
│   │   │   ├── filmLike
│   │   │   │   ├── FilmLikeAlreadyExistException.java
│   │   │   │   └── FilmLikeNotFoundException.java
│   │   │   ├── friendship
│   │   │   │   ├── FriendshipAlreadyExistException.java
│   │   │   │   └── FriendshipNotFoundException.java
│   │   │   ├── gender
│   │   │   │   ├── GenreAlreadyExistException.java
│   │   │   │   └── GenreNotFoundException.java
│   │   │   ├── rating
│   │   │   │   └── RatingNotFoundException.java
│   │   │   └── user
│   │   │       ├── UserAlreadyExistException.java
│   │   │       └── UserNotFoundException.java
│   │   ├── model
│   │   │   ├── Film.java
│   │   │   ├── FilmLike.java
│   │   │   ├── Friendship.java
│   │   │   ├── Genre.java
│   │   │   ├── GenreType.java
│   │   │   ├── RatingType.java
│   │   │   └── User.java
│   │   ├── service
│   │   │   ├── FilmService.java
│   │   │   ├── UserService.java
│   │   │   └── util
│   │   │       └── Validator.java
│   │   └── storage
│   │       ├── film
│   │       │   ├── FilmDbStorage.java
│   │       │   ├── FilmStorage.java
│   │       │   └── InMemoryFilmStorage.java
│   │       └── user
│   │           ├── InMemoryUserStorage.java
│   │           ├── UserDbStorage.java
│   │           └── UserStorage.java
│   └── resources
│       ├── application.properties
│       └── schema.sql
└── test
    └── java
        └── com
            └── filmorate
                └── FilmorateApplicationTests.java
```

---

## Technologies

- Java 21
- Spring Boot 3.2.4 
- Spring JDBC / H2 Database  
- Lombok

---

## Getting Started

1. **Clone repository**  
```bash
git clone git@github.com:Kevin1home/Filmorate.git
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

## Running Tests

```bash
./mvnw test
```

---

# API Endpoints

## Users
- `POST /users` – Create a new user
- `GET /users/{id}` – Get user by ID
- `GET /users` – List all users
- `PUT /users` – Update user

## Friendships
- `PUT /users/{id}/friends/{friendId}` – Add friend
- `DELETE /users/{id}/friends/{friendId}` – Remove friend
- `GET /users/{id}/friends` – List friends of user
- `GET /users/{id}/friends/common/{otherId}` – Get common friends between two users

## Films
- `POST /films` – Create a new film
- `GET /films/{id}` – Get film by ID
- `GET /films` – List all films
- `PUT /films` – Update film

## Film Likes
- `GET /films/{id}/like` – List likes for a film
- `POST /films/{id}/like/{userId}` – Add like to a film
- `DELETE /films/{id}/like/{userId}` – Remove like

## Popular Films
- `GET /films/popular?count={count}` – Get top liked/popular films

## Genres
- `GET /films/genres` – List all genres
- `GET /films/genres/{id}` – Get genre by ID
- `GET /films/{id}/genres` – Get genres for a film
- `POST /films/{id}/genres` – Add genres to a film

## Ratings (MPA)
- `GET /films/mpa` – List all ratings
- `GET /films/mpa/{id}` – Get rating by ID
- `GET /films/{id}/mpa` – Get rating of a film

---

## License

Released under the **MIT License**.
