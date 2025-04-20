package org.example.crudbookapi.service;

import org.example.crudbookapi.exception.BookNotFoundException;
import org.example.crudbookapi.model.Book;
import org.example.crudbookapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get all books with pagination
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // Get book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Create a new book
    public Book createBook(Book book) {
        // Set id to null to ensure a new book is created
        book.setId(null);
        return bookRepository.save(book);
    }

    // Update an existing book
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id); // This will throw BookNotFoundException if book doesn't exist
        
        // Update book details
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublishedYear(bookDetails.getPublishedYear());
        book.setGenre(bookDetails.getGenre());
        
        return bookRepository.save(book);
    }

    // Delete a book
    public void deleteBook(Long id) {
        Book book = getBookById(id); // This will throw BookNotFoundException if book doesn't exist
        bookRepository.delete(book);
    }

    // Find books by author
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    // Find books by genre
    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    // Find books by author with pagination
    public Page<Book> findBooksByAuthor(String author, Pageable pageable) {
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }

    // Find books by genre with pagination
    public Page<Book> findBooksByGenre(String genre, Pageable pageable) {
        return bookRepository.findByGenreContainingIgnoreCase(genre, pageable);
    }
}