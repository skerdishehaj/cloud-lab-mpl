package org.skerdians.cloudlab.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skerdians.cloudlab.entities.Book;
import org.skerdians.cloudlab.repositories.BookRepository;
import org.skerdians.cloudlab.services.BookService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    BookRepository bookRepository;

    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book addedBook = bookService.addBook(book);

        assertNotNull(addedBook);
        assertEquals(1L, addedBook.getId());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        Collection<Book> allBooks = bookService.getAllBooks();

        assertNotNull(allBooks);
        assertEquals(2, allBooks.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> retrievedBook = bookService.getBookById(1L);

        assertTrue(retrievedBook.isPresent());
        assertEquals(1L, retrievedBook.get().getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindBookByName() {
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findByName("Book")).thenReturn(Arrays.asList(book1, book2));

        Collection<Book> foundBooks = bookService.findBookByName("Book");

        assertNotNull(foundBooks);
        assertEquals(2, foundBooks.size());
        verify(bookRepository, times(1)).findByName("Book");
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setName("Book");

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setName("Updated Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Book", result.getName());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Book result = bookService.updateBook(1L, new Book());

        assertNull(result);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void testDeleteBookById() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAllBooks() {
        doNothing().when(bookRepository).deleteAll();

        bookService.deleteAllBooks();

        verify(bookRepository, times(1)).deleteAll();
    }
}
