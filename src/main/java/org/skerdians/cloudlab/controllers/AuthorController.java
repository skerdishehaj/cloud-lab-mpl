package org.skerdians.cloudlab.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skerdians.cloudlab.entities.Author;
import org.skerdians.cloudlab.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private static final Logger LOGGER = LogManager.getLogger(AuthorController.class);
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody Author author) {
        LOGGER.info("{}: Adding author: {}", AuthorController.class.getName(), author);
        Author addedAuthor = authorService.addAuthor(author);
        LOGGER.info("{}: Author added: {}", AuthorController.class.getName(), addedAuthor);
        LOGGER.debug("{}: Author added: {}", AuthorController.class.getName(), addedAuthor);
        return new ResponseEntity<>(addedAuthor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Author>> getAllAuthors() {
        LOGGER.info("{}: Getting all authors", AuthorController.class.getName());
        Collection<Author> allAuthors = authorService.getAllAuthors();
        LOGGER.info("{}: All authors: {}", AuthorController.class.getName(), allAuthors);
        LOGGER.debug("{}: All authors: {}", AuthorController.class.getName(), allAuthors);
        return new ResponseEntity<>(allAuthors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorWithId(@PathVariable Long id) {
        LOGGER.info("{}: Getting author with id: {}", AuthorController.class.getName(), id);
        Optional<Author> authorById = authorService.getAuthorById(id);
        LOGGER.info("{}: Author with id {}: {}", AuthorController.class.getName(), id, authorById);
        LOGGER.debug("{}: Author with id {}: {}", AuthorController.class.getName(), id, authorById);
        return authorById.map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Author>> findAuthorWithName(@RequestParam(value = "name") String name) {
        LOGGER.info("{}: Finding author with name: {}", AuthorController.class.getName(), name);
        Collection<Author> authorsByName = authorService.findAuthorByName(name);
        LOGGER.info("{}: Authors with name {}: {}", AuthorController.class.getName(), name, authorsByName);
        LOGGER.debug("{}: Authors with name {}: {}", AuthorController.class.getName(), name, authorsByName);
        return new ResponseEntity<>(authorsByName, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthorFromDB(@PathVariable("id") long id, @RequestBody Author author) {
        LOGGER.info("{}: Updating author with id: {}", AuthorController.class.getName(), id);
        Author updatedAuthor = authorService.updateAuthor(id, author);
        if (updatedAuthor != null) {
            LOGGER.info("{}: Author with id {} updated: {}", AuthorController.class.getName(), id, updatedAuthor);
            LOGGER.debug("{}: Author with id {} updated: {}", AuthorController.class.getName(), id, updatedAuthor);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } else {
            LOGGER.error("{}: Author with id {} not found", AuthorController.class.getName(), id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorWithId(@PathVariable Long id) {
        LOGGER.warn("{}: Deleting author with id: {}", AuthorController.class.getName(), id);
        LOGGER.info("{}: Deleting author with id: {}", AuthorController.class.getName(), id);
        authorService.deleteAuthorById(id);
        LOGGER.info("{}: Author with id {} deleted", AuthorController.class.getName(), id);
        LOGGER.debug("{}: Author with id {} deleted", AuthorController.class.getName(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAuthors() {
        LOGGER.warn("{}: Deleting all authors", AuthorController.class.getName());
        authorService.deleteAllAuthors();
        LOGGER.info("{}: All authors deleted", AuthorController.class.getName());
        LOGGER.debug("{}: All authors deleted", AuthorController.class.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
