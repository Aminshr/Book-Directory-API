package org.example.crudbookapi.controller;

import jakarta.validation.Valid;
import org.example.crudbookapi.model.Book;
import org.example.crudbookapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /books - List all books (with optional pagination and search)
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre) {

        // If pagination parameters are provided
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> bookPage;

            // If search parameters are provided
            if (author != null) {
                bookPage = bookService.findBooksByAuthor(author, pageable);
            } else if (genre != null) {
                bookPage = bookService.findBooksByGenre(genre, pageable);
            } else {
                bookPage = bookService.getAllBooks(pageable);
            }
            return ResponseEntity.ok(bookPage);
        }

        // If only search parameters are provided (without pagination)
        if (author != null) {
            return ResponseEntity.ok(bookService.findBooksByAuthor(author));
        }
        if (genre != null) {
            return ResponseEntity.ok(bookService.findBooksByGenre(genre));
        }

        // Default: return all books without pagination
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // GET /books/{id} - Get a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // POST /books - Add a new book
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    // PUT /books/{id} - Update book info
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    // DELETE /books/{id} - Delete a book
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted successfully"));
    }
}