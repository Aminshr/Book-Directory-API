# 📚 Book Directory API - Spring Boot REST Service

A comprehensive REST API for managing a book collection, built with Java and Spring Boot. This application demonstrates modern API development practices including proper endpoint design, data validation, authentication, and documentation.

This project serves as both a functional book management system and a reference implementation for RESTful API best practices.

## Features

- CRUD operations for books (Create, Read, Update, Delete)
- Pagination support
- Search by author or genre
- Basic authentication
- Swagger UI documentation
- Error handling with appropriate HTTP status codes

## API Endpoints

### Books

- `GET /books` - List all books
  - Optional query parameters:
    - `page` - Page number (0-based)
    - `size` - Page size
    - `author` - Filter by author
    - `genre` - Filter by genre

- `GET /books/{id}` - Get a book by ID

- `POST /books` - Add a new book
  - Request body: Book JSON

- `PUT /books/{id}` - Update book info
  - Request body: Book JSON

- `DELETE /books/{id}` - Delete a book

## Book Model

```json
{
  "id": 1,
  "title": "Book Title",
  "author": "Author Name",
  "publishedYear": 2023,
  "genre": "Fiction"
}
```

## Authentication

The API uses basic authentication. Default credentials:

- Username: `user`
- Password: `password`

Admin credentials:

- Username: `admin`
- Password: `admin`

## Error Handling

- `400 Bad Request` - Invalid input (e.g., empty title)
- `401 Unauthorized` - Authentication required
- `404 Not Found` - Book not found
- `500 Internal Server Error` - Server error

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run `./mvnw spring-boot:run`
4. Access the API at `http://localhost:8080/books`
5. Access Swagger UI at `http://localhost:8080/swagger-ui.html`
6. Access H2 Console at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:bookdb`, Username: `sa`, Password: `password`)

## Testing with Curl

### Get all books
```bash
curl -u user:password http://localhost:8080/books
```

### Get a book by ID
```bash
curl -u user:password http://localhost:8080/books/1
```

### Create a new book
```bash
curl -u user:password -X POST -H "Content-Type: application/json" -d '{"title":"New Book","author":"New Author","publishedYear":2023,"genre":"Fiction"}' http://localhost:8080/books
```

### Update a book
```bash
curl -u user:password -X PUT -H "Content-Type: application/json" -d '{"title":"Updated Book","author":"Updated Author","publishedYear":2023,"genre":"Non-Fiction"}' http://localhost:8080/books/1
```

### Delete a book
```bash
curl -u user:password -X DELETE http://localhost:8080/books/1
```

### Search books by author
```bash
curl -u user:password "http://localhost:8080/books?author=Tolkien"
```

### Get books with pagination
```bash
curl -u user:password "http://localhost:8080/books?page=0&size=10"
```
