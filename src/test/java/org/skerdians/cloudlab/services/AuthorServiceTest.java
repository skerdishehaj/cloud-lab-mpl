package org.skerdians.cloudlab.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skerdians.cloudlab.entities.Author;
import org.skerdians.cloudlab.repositories.AuthorRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @InjectMocks
    AuthorService authorService;

    @Mock
    AuthorRepository authorRepository;

    @Test
    public void testAddAuthor() {
        Author author = new Author();
        author.setId(1L);

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author addedAuthor = authorService.addAuthor(author);

        assertNotNull(addedAuthor);
        assertEquals(1L, addedAuthor.getId());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testGetAllAuthors() {
        Author author1 = new Author();
        Author author2 = new Author();
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        Collection<Author> allAuthors = authorService.getAllAuthors();

        assertNotNull(allAuthors);
        assertEquals(2, allAuthors.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void testGetAuthorById() {
        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Optional<Author> retrievedAuthor = authorService.getAuthorById(1L);

        assertTrue(retrievedAuthor.isPresent());
        assertEquals(1L, retrievedAuthor.get().getId());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindAuthorByName() {
        Author author1 = new Author();
        Author author2 = new Author();
        when(authorRepository.findByName("John")).thenReturn(Arrays.asList(author1, author2));

        Collection<Author> foundAuthors = authorService.findAuthorByName("John");

        assertNotNull(foundAuthors);
        assertEquals(2, foundAuthors.size());
        verify(authorRepository, times(1)).findByName("John");
    }

    @Test
    public void testUpdateAuthor() {
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setName("John");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(1L);
        updatedAuthor.setName("John Doe");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        Author result = authorService.updateAuthor(1L, updatedAuthor);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testUpdateAuthorNotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Author result = authorService.updateAuthor(1L, new Author());

        assertNull(result);
        verify(authorRepository, times(1)).findById(anyLong());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthorById() {
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthorById(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAllAuthors() {
        doNothing().when(authorRepository).deleteAll();

        authorService.deleteAllAuthors();

        verify(authorRepository, times(1)).deleteAll();
    }
}
