package org.skerdians.cloudlab.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skerdians.cloudlab.entities.Book;
import org.skerdians.cloudlab.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private static final Logger LOGGER = LogManager.getLogger(BookController.class);
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        LOGGER.info("{}: Adding book: {}", BookController.class.getName(), book);
        Book addedBook = bookService.addBook(book);
        LOGGER.info("{}: Book added: {}", BookController.class.getName(), addedBook);
        LOGGER.debug("{}: Book added: {}", BookController.class.getName(), addedBook);
        return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        LOGGER.info("{}: Getting all books", BookController.class.getName());
        Collection<Book> allBooks = bookService.getAllBooks();
        LOGGER.info("{}: All books: {}", BookController.class.getName(), allBooks);
        LOGGER.debug("{}: All books: {}", BookController.class.getName(), allBooks);
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookWithId(@PathVariable Long id) {
        LOGGER.info("{}: Getting book with id: {}", BookController.class.getName(), id);
        Optional<Book> bookById = bookService.getBookById(id);
        LOGGER.info("{}: Book with id {}: {}", BookController.class.getName(), id, bookById);
        LOGGER.debug("{}: Book with id {}: {}", BookController.class.getName(), id, bookById);
        return bookById.map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Book>> findBookWithName(@RequestParam(value = "name") String name) {
        LOGGER.info("{}: Finding book with name: {}", BookController.class.getName(), name);
        Collection<Book> booksByName = bookService.findBookByName(name);
        LOGGER.info("{}: Books with name {}: {}", BookController.class.getName(), name, booksByName);
        LOGGER.debug("{}: Books with name {}: {}", BookController.class.getName(), name, booksByName);
        return new ResponseEntity<>(booksByName, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookFromDB(@PathVariable("id") long id, @RequestBody Book book) {
        LOGGER.info("{}: Updating book with id: {}", BookController.class.getName(), id);
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            LOGGER.info("{}: Book with id {} updated: {}", BookController.class.getName(), id, updatedBook);
            LOGGER.debug("{}: Book with id {} updated: {}", BookController.class.getName(), id, updatedBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            LOGGER.error("{}: Book with id {} not found", BookController.class.getName(), id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookWithId(@PathVariable Long id) {
        LOGGER.warn("{}: Deleting book with id: {}", BookController.class.getName(), id);
        LOGGER.info("{}: Deleting book with id: {}", BookController.class.getName(), id);
        bookService.deleteBookById(id);
        LOGGER.info("{}: Book with id {} deleted", BookController.class.getName(), id);
        LOGGER.debug("{}: Book with id {} deleted", BookController.class.getName(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllBooks() {
        LOGGER.warn("{}: Deleting all books", BookController.class.getName());
        bookService.deleteAllBooks();
        LOGGER.info("{}: All books deleted", BookController.class.getName());
        LOGGER.debug("{}: All books deleted", BookController.class.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
