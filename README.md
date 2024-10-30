# Task Management System

---

### Introduction

---

This is the backend part of the "Skill Share" project, which is a web application created to help you find new knowledge.
This app allows you to search for courses you are interested in, sorted by categories, price, etc. Also, if you are interested in some new information related to different topics every day, the articles section is for you :nerd_face:

---

### Technologies and tools

---

- Java 21
- Spring Boot 3.3.4, Spring Security, Spring Data JPA
- MySQL 9, Liquibase
- JWT
- Maven, Docker
- Lombok, Mapstruct, Swagger
- JUnit, Mockito, Testcontainers

---

### How to run:

---

1. Clone the repository.
2. Install and run Docker.
3. Create a Google account and app to enable authentication through a Google account ([Guide](https://developers.google.com/identity/protocols/oauth2)).
4. Configure access parameters in the `.env` file (refer to the required fields in `.env.sample`).
5. Open a terminal and navigate to the root directory of the project on your machine.
6. Run the application using Docker Compose: `docker-compose up`
7. Voilà! :tada:

Feel free to test the available functionality using endpoints with tools like Postman or via Swagger UI, which you can find below ⬇️

---

### Application functionality

---

**Base URL**: - `http://localhost:8080/api`

**Authentication** - Endpoints for managing authentication:
- `POST: /auth/register` - Register a new user
- `POST: /auth/login` - Log in
- `POST: /auth/forgotPassword` - Request a link to reset your password

**User** - Endpoints for managing users:
- `GET: /users/me` - Get the current user's profile info
- `PUT: /users/me` - Update the current user's profile info
- `PATCH: /users/me/password` - Update the user's password
- `PATCH: /users/me/email` - Update the user's email
- `POST: /users/me/favourites/{courseId}` - Add a course to favourites
- `DELETE: /users/me/favourites/{courseId}` - Remove a course from favourites

**Category** - Endpoints for managing categories:
- `POST: /categories` - Create a new category
- `GET: /categories` - Retrieve all categories
- `DELETE: /categories/{id}` - Delete a category by ID

**Course** - Endpoints for managing courses:
- `POST: /courses` - Create a new course
- `GET: /courses` - Retrieve all courses
- `GET: /courses/search` - Search for specific courses by requested parameters
- `GET: /courses/{id}` - Get a course by ID
- `PUT: /courses/{id}` - Update a course by ID
- `DELETE: /courses/{id}` - Delete a course by ID

**Review** - Endpoints for managing reviews of courses:
- `POST: /reviews/course/{courseId}` - Create a new review
- `GET: /reviews/course/{courseId}` - Retrieve all reviews for a course
- `PATCH: /reviews/{id}` - Update a review by ID
- `DELETE: /reviews/{id}` - Delete a review by ID

**Article** - Endpoints for managing articles:
- `POST: /articles` - Create a new article
- `GET: /articles` - Retrieve all articles
- `GET: /articles/{id}` - Get an article by ID
- `PUT: /articles/{id}` - Update an article by ID
- `DELETE: /articles/{id}` - Delete an article by ID

**Comment** - Endpoints for managing comments on articles:
- `POST: /comments/article/{articleId}` - Create a new comment
- `GET: /comments/article/{articleId}` - Retrieve all comments for an article
- `PATCH: /comments/{id}` - Update a comment by ID
- `DELETE: /comments/{id}` - Delete a comment by ID

For examples of request bodies and additional endpoints, refer to the Swagger documentation.

---

#### [SWAGGER UI](http://localhost:8080/api/swagger-ui/index.html#/)

---