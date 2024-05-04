package org.skerdians.cloudlab.services;

import org.skerdians.cloudlab.entities.Book;
import org.skerdians.cloudlab.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Collection<Book> findBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public Book updateBook(Long id, Book book) {
        Optional<Book> currentBookOpt = bookRepository.findById(id);
        if (currentBookOpt.isPresent()) {
            Book currentBook = currentBookOpt.get();
            currentBook.setName(book.getName());
            currentBook.setDescription(book.getDescription());
            currentBook.setTagsList(book.getTagsList());
            return bookRepository.save(currentBook);
        } else {
            // Handle not found scenario
            return null;
        }
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }
}
