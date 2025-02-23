package com.management.book;

import com.management.book.dto.BookDto;
import com.management.book.entity.Book;
import com.management.book.exception.BookNotFoundException;
import com.management.book.repository.impl.BookCSVRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookCSVRepositoryImplTest {

    @Mock
    private BookCSVRepositoryImpl bookCSVRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        // Arrange
        BookDto bookDto = new BookDto(1L, "Title", "Author", new Date(), "1234", "Description");
        Book book = new Book(1L, "Title", "Author", new Date(), "1234", "Description");

        when(bookCSVRepository.save(bookDto)).thenReturn(book); // Mock the save method to return a book

        // Act
        Book savedBook = bookCSVRepository.save(bookDto);

        // Assert
        assertNotNull(savedBook);
        assertEquals("Title", savedBook.getTitle());
        assertEquals("Author", savedBook.getAuthor());
    }

    @Test
    public void testFindById_BookNotFound() throws BookNotFoundException {
        // Arrange
        Long bookId = 1L;

        // Simulate that the book is not found in the CSV file
        when(bookCSVRepository.findById(bookId)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookCSVRepository.findById(bookId));
    }

    @Test
    public void testFindById_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", new Date(), "1234", "Description");

        when(bookCSVRepository.findById(bookId)).thenReturn(book);

        // Act
        Book foundBook = bookCSVRepository.findById(bookId);

        // Assert
        assertNotNull(foundBook);
        assertEquals(bookId, foundBook.getId());
    }

    @Test
    public void testDeleteById_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book(bookId, "Title", "Author", new Date(), "1234", "Description");

        // Simulate that the book is deleted successfully
        when(bookCSVRepository.deleteById(bookId)).thenReturn(true);

        // Act
        boolean isDeleted = bookCSVRepository.deleteById(bookId);

        // Assert
        assertTrue(isDeleted);
    }

    @Test
    public void testDeleteById_BookNotFound() throws Exception {
        // Arrange
        Long bookId = 1L;

        // Simulate that the book to be deleted doesn't exist
        when(bookCSVRepository.deleteById(bookId)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookCSVRepository.deleteById(bookId));
    }

    @Test
    public void testUpdateById_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        BookDto updatedBookDto = new BookDto(bookId, "Updated Title", "Updated Author", new Date(), "5678", "Updated Description");
        Book updatedBook = new Book(bookId, "Updated Title", "Updated Author", new Date(), "5678", "Updated Description");

        when(bookCSVRepository.updateById(bookId, updatedBookDto)).thenReturn(updatedBook);

        // Act
        Book result = bookCSVRepository.updateById(bookId, updatedBookDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
    }

    @Test
    public void testUpdateById_BookNotFound() throws Exception {
        // Arrange
        Long bookId = 1L;
        BookDto updatedBookDto = new BookDto(bookId, "Updated Title", "Updated Author", new Date(), "5678", "Updated Description");

        when(bookCSVRepository.updateById(bookId, updatedBookDto)).thenThrow(new BookNotFoundException("Book not found with id: " + bookId));

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookCSVRepository.updateById(bookId, updatedBookDto));
    }
}