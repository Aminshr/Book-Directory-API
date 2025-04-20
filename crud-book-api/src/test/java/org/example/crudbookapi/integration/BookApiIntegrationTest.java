package org.example.crudbookapi.integration;

import org.example.crudbookapi.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/books";
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpEntity<Object> createHttpEntity(Object body) {
        return new HttpEntity<>(body, createHeaders());
    }

    @Test
    public void testCrudOperations() {
        // Set up authentication
        restTemplate = restTemplate.withBasicAuth("user", "password");

        // 1. Create a new book
        Book newBook = new Book();
        newBook.setTitle("Integration Test Book");
        newBook.setAuthor("Test Author");
        newBook.setPublishedYear(2023);
        newBook.setGenre("Test");

        ResponseEntity<Book> createResponse = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.POST,
                createHttpEntity(newBook),
                Book.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Book createdBook = createResponse.getBody();
        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertEquals("Integration Test Book", createdBook.getTitle());

        Long bookId = createdBook.getId();

        // 2. Get the book by ID
        ResponseEntity<Book> getResponse = restTemplate.exchange(
                getBaseUrl() + "/" + bookId,
                HttpMethod.GET,
                createHttpEntity(null),
                Book.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Book retrievedBook = getResponse.getBody();
        assertNotNull(retrievedBook);
        assertEquals(bookId, retrievedBook.getId());
        assertEquals("Integration Test Book", retrievedBook.getTitle());

        // 3. Update the book
        retrievedBook.setTitle("Updated Book Title");
        retrievedBook.setAuthor("Updated Author");

        ResponseEntity<Book> updateResponse = restTemplate.exchange(
                getBaseUrl() + "/" + bookId,
                HttpMethod.PUT,
                createHttpEntity(retrievedBook),
                Book.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        Book updatedBook = updateResponse.getBody();
        assertNotNull(updatedBook);
        assertEquals(bookId, updatedBook.getId());
        assertEquals("Updated Book Title", updatedBook.getTitle());
        assertEquals("Updated Author", updatedBook.getAuthor());

        // 4. Get all books and verify our book is in the list
        ResponseEntity<List<Book>> getAllResponse = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                createHttpEntity(null),
                new ParameterizedTypeReference<List<Book>>() {}
        );

        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        List<Book> allBooks = getAllResponse.getBody();
        assertNotNull(allBooks);
        assertTrue(allBooks.stream().anyMatch(b -> b.getId().equals(bookId)));

        // 5. Delete the book
        ResponseEntity<Map<String, String>> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + bookId,
                HttpMethod.DELETE,
                createHttpEntity(null),
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        Map<String, String> deleteResult = deleteResponse.getBody();
        assertNotNull(deleteResult);
        assertEquals("Book deleted successfully", deleteResult.get("message"));

        // 6. Verify the book is deleted
        ResponseEntity<Object> getDeletedResponse = restTemplate.exchange(
                getBaseUrl() + "/" + bookId,
                HttpMethod.GET,
                createHttpEntity(null),
                Object.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }

    @Test
    public void testUnauthorizedAccess() {
        // Try to access without authentication
        ResponseEntity<Object> response = new TestRestTemplate()
                .exchange(
                        getBaseUrl(),
                        HttpMethod.GET,
                        createHttpEntity(null),
                        Object.class
                );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}