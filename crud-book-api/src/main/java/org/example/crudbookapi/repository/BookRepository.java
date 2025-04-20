package org.example.crudbookapi.repository;

import org.example.crudbookapi.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find books by author (case-insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    // Find books by genre (case-insensitive)
    List<Book> findByGenreContainingIgnoreCase(String genre);
    
    // Find books by author with pagination
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    
    // Find books by genre with pagination
    Page<Book> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
}