package org.skerdians.cloudlab.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skerdians.cloudlab.entities.Book;
import org.skerdians.cloudlab.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookService {
    public static Logger LOGGER = LogManager.getLogger(BookService.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        LOGGER.info("Creating BookService");
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        // First param should be  but it is not working
        LOGGER.info("{}: Adding book: {}", BookService.class.getName(), book);
        return bookRepository.save(book);
    }

    public Collection<Book> getAllBooks() {
        LOGGER.info("{}: Getting all books", BookService.class.getName());
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        LOGGER.info("{}: Getting book with id: {}", BookService.class.getName(), id);
        return bookRepository.findById(id);
    }

    public Collection<Book> findBookByName(String name) {
        LOGGER.info("{}: Finding book with name: {}", BookService.class.getName(), name);
        return bookRepository.findByName(name);
    }

    public Book updateBook(Long id, Book book) {
        LOGGER.info("{}: Updating book with id: {}", BookService.class.getName(), id);
        Optional<Book> currentBookOpt = bookRepository.findById(id);
        if (currentBookOpt.isPresent()) {
            LOGGER.info("{}: Book found: {}", BookService.class.getName(), currentBookOpt.get());
            Book currentBook = currentBookOpt.get();
            currentBook.setName(book.getName());
            currentBook.setDescription(book.getDescription());
            currentBook.setTagsList(book.getTagsList());
            LOGGER.info("{}: Book updated: {}", BookService.class.getName(), currentBook);
            return bookRepository.save(currentBook);
        } else {
            LOGGER.info("{}: Book not found with id: {}", BookService.class.getName(), id);
            LOGGER.warn("{}: Book not found with id: {}", BookService.class.getName(), id);
            // Handle not found scenario
            return null;
        }
    }

    public void deleteBookById(Long id) {
        LOGGER.info("{}: Deleting book with id: {}", BookService.class.getName(), id);
        LOGGER.warn("{}: Deleting book with id: {}", BookService.class.getName(), id);
        bookRepository.deleteById(id);
    }

    public void deleteAllBooks() {
        LOGGER.info("{}: Deleting all books", BookService.class.getName());
        LOGGER.warn("{}: Deleting all books", BookService.class.getName());
        bookRepository.deleteAll();
    }
}
