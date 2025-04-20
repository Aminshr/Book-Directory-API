package org.example.crudbookapi.config;

import org.example.crudbookapi.model.Book;
import org.example.crudbookapi.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(BookRepository bookRepository) {
        return args -> {
            // Check if the database is empty
            if (bookRepository.count() == 0) {
                // Create sample books
                Book book1 = new Book();
                book1.setTitle("To Kill a Mockingbird");
                book1.setAuthor("Harper Lee");
                book1.setPublishedYear(1960);
                book1.setGenre("Classic");

                Book book2 = new Book();
                book2.setTitle("1984");
                book2.setAuthor("George Orwell");
                book2.setPublishedYear(1949);
                book2.setGenre("Dystopian");

                Book book3 = new Book();
                book3.setTitle("The Great Gatsby");
                book3.setAuthor("F. Scott Fitzgerald");
                book3.setPublishedYear(1925);
                book3.setGenre("Classic");

                Book book4 = new Book();
                book4.setTitle("The Hobbit");
                book4.setAuthor("J.R.R. Tolkien");
                book4.setPublishedYear(1937);
                book4.setGenre("Fantasy");

                Book book5 = new Book();
                book5.setTitle("Pride and Prejudice");
                book5.setAuthor("Jane Austen");
                book5.setPublishedYear(1813);
                book5.setGenre("Romance");

                // Save all books to the database
                bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5));
                
                System.out.println("Sample data initialized!");
            }
        };
    }
}