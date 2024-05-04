package org.skerdians.cloudlab.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skerdians.cloudlab.entities.Book;
import org.skerdians.cloudlab.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @InjectMocks
    BookController bookController;

    @Mock
    BookService bookService;

    @Test
    public void testGetAllBooks() {
        // Arrange
        Book book1 = new Book(
                1L,
                "Book 1",
                "Description 1",
                Arrays.asList("Tag 1", "Tag 2")
        );
        Book book2 = new Book(
                2L,
                "Book 2",
                "Description 2",
                Arrays.asList("Tag 3", "Tag 4")
        );
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        // Act
        ResponseEntity<Collection<Book>> response = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Collection<Book> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        Book retrievedBook1 = responseBody.stream().filter(b -> b.getId().equals(1L)).findFirst().orElse(null);
        assertNotNull(retrievedBook1);
        assertEquals("Book 1", retrievedBook1.getName());
        assertEquals("Description 1", retrievedBook1.getDescription());
        assertEquals(Arrays.asList("Tag 1", "Tag 2"), retrievedBook1.getTagsList());

        Book retrievedBook2 = responseBody.stream().filter(b -> b.getId().equals(2L)).findFirst().orElse(null);
        assertNotNull(retrievedBook2);
        assertEquals("Book 2", retrievedBook2.getName());
        assertEquals("Description 2", retrievedBook2.getDescription());
        assertEquals(Arrays.asList("Tag 3", "Tag 4"), retrievedBook2.getTagsList());
    }

    @Test
    public void testGetBookWithId() {
        Book book = new Book();
        book.setId(1L);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookWithId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void testGetBookWithInvalidId() {
        Long invalidId = 100L; // Assuming 100 is not a valid ID
        when(bookService.getBookById(invalidId)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookWithId(invalidId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddBook() {
        Book book = new Book(1L, "Book 1", "Description 1", Arrays.asList("Tag 1", "Tag 2"));
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(book.getId(), response.getBody().getId());
        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    public void testFindBookWithName() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Test Book 1", "Description 1", Arrays.asList("Tag 1", "Tag 2")),
                new Book(2L, "Test Book 2", "Description 2", Arrays.asList("Tag 3", "Tag 4"))
        );
        when(bookService.findBookByName(anyString())).thenReturn(books);

        ResponseEntity<Collection<Book>> response = bookController.findBookWithName("Test");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(books, response.getBody());
    }

    @Test
    public void testUpdateBookFromDB() {
        Book book = new Book(1L, "Book 1", "Description 1", Arrays.asList("Tag 1", "Tag 2"));
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.updateBookFromDB(1L, book);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).updateBook(anyLong(), any(Book.class)); // Verify method call
    }

    @Test
    public void testUpdateBookFromDBWithInvalidId() {
        long invalidId = 999L;
        Book book = new Book();
        book.setId(invalidId);
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(null);

        ResponseEntity<Book> response = bookController.updateBookFromDB(invalidId, book);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, times(1)).updateBook(anyLong(), any(Book.class));
    }

    @Test
    public void testDeleteBookWithId() {
        ResponseEntity<Void> response = bookController.deleteBookWithId(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBookById(1L); // Verify method call
    }

    @Test
    public void testDeleteAllBooks() {
        ResponseEntity<Void> response = bookController.deleteAllBooks();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteAllBooks(); // Verify method call
    }
}
