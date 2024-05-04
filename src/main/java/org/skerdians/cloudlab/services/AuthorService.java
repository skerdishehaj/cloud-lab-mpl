package org.skerdians.cloudlab.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skerdians.cloudlab.entities.Author;
import org.skerdians.cloudlab.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthorService {
    public static Logger LOGGER = LogManager.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        LOGGER.info("Creating AuthorService");
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(Author author) {
        LOGGER.info("{}: Adding author: {}", AuthorService.class.getName(), author);
        return authorRepository.save(author);
    }

    public Collection<Author> getAllAuthors() {
        LOGGER.info("{}: Getting all authors", AuthorService.class.getName());
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Long id) {
        LOGGER.info("{}: Getting author with id: {}", AuthorService.class.getName(), id);
        return authorRepository.findById(id);
    }

    public Collection<Author> findAuthorByName(String name) {
        LOGGER.info("{}: Finding author with name: {}", AuthorService.class.getName(), name);
        return authorRepository.findByName(name);
    }

    public Author updateAuthor(Long id, Author author) {
        LOGGER.info("{}: Updating author with id: {}", AuthorService.class.getName(), id);
        Optional<Author> currentAuthorOpt = authorRepository.findById(id);
        if (currentAuthorOpt.isPresent()) {
            LOGGER.info("{}: Author found: {}", AuthorService.class.getName(), currentAuthorOpt.get());
            Author currentAuthor = currentAuthorOpt.get();
            currentAuthor.setName(author.getName());
            currentAuthor.setEmail(author.getEmail());
            currentAuthor.setBiography(author.getBiography());
            currentAuthor.setAge(author.getAge());
            LOGGER.info("{}: Author updated: {}", AuthorService.class.getName(), currentAuthor);
            return authorRepository.save(currentAuthor);
        } else {
            LOGGER.info("{}: Author not found with id: {}", AuthorService.class.getName(), id);
            LOGGER.warn("{}: Author not found with id: {}", AuthorService.class.getName(), id);
            // Handle not found scenario
            return null;
        }
    }

    public void deleteAuthorById(Long id) {
        LOGGER.info("{}: Deleting author with id: {}", AuthorService.class.getName(), id);
        LOGGER.warn("{}: Deleting author with id: {}", AuthorService.class.getName(), id);
        authorRepository.deleteById(id);
    }

    public void deleteAllAuthors() {
        LOGGER.info("{}: Deleting all authors", AuthorService.class.getName());
        LOGGER.warn("{}: Deleting all authors", AuthorService.class.getName());
        authorRepository.deleteAll();
    }
}
