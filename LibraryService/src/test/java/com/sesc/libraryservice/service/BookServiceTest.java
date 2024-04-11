package com.sesc.libraryservice.service;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.repository.BookRepository;
import com.sesc.libraryservice.util.BookClient;
import com.sesc.libraryservice.util.BookParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @Value("${library.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBooks() {
        // Arrange
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(new Book());
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookService.findAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks.get(0), actualBooks.get(0));
    }

    @Test
    void testFindBookById_ExistingId() {
        // Arrange
        long bookId = 1;
        Book expectedBook = new Book();
        expectedBook.setId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        // Act
        Book actualBook = bookService.findBookById(bookId);

        // Assert
        assertNotNull(actualBook);
        assertEquals(expectedBook.getId(), actualBook.getId());
    }

    @Test
    void testFindBookById_NonExistingId() {
        // Arrange
        long nonExistingId = 999;
        when(bookRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act
        Book actualBook = bookService.findBookById(nonExistingId);

        // Assert
        assertNull(actualBook);
    }

    @Test
    void testFindBookByIsbn_ExistingIsbn() {
        // Arrange
        String isbn = "1234567890";
        Book expectedBook = new Book();
        expectedBook.setIsbn(isbn);
        when(bookRepository.findBookByIsbn(isbn)).thenReturn(expectedBook);

        // Act
        Book actualBook = bookService.findBookByIsbn(isbn);

        // Assert
        assertNotNull(actualBook);
        assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
    }

    @Test
    void testFindBookByIsbn_NonExistingIsbn() {
        // Arrange
        String nonExistingIsbn = "0000000000";
        when(bookRepository.findBookByIsbn(nonExistingIsbn)).thenReturn(null);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> bookService.findBookByIsbn(nonExistingIsbn));
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book bookToSave = new Book();
        when(bookRepository.save(bookToSave)).thenReturn(bookToSave);

        // Act
        Book savedBook = bookService.saveBook(bookToSave);

        // Assert
        assertNotNull(savedBook);
        assertEquals(bookToSave, savedBook);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        long bookId = 1;

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testFindBookFromAPI_ExistingIsbn() throws IOException {
        // Arrange
        String isbn = "1234567890";
        String jsonResponse = "{\"isbn\": \"" + isbn + "\", \"title\": \"Test Book\"}";
        Book expectedBook = new Book();
        expectedBook.setIsbn(isbn);

        try (MockedStatic<BookClient> mockedClient = mockStatic(BookClient.class);
             MockedStatic<BookParser> mockedParser = mockStatic(BookParser.class)) {
            mockedClient.when(() -> BookClient.getBookByISBN(isbn, apiKey)).thenReturn(jsonResponse);
            mockedParser.when(() -> BookParser.parseBookResponse(jsonResponse)).thenReturn(expectedBook);

            // Act
            Book actualBook = bookService.findBookFromAPI(isbn);

            // Assert
            assertNotNull(actualBook);
            assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
        }
    }

    @Test
    void testFindBookFromAPI_NonExistingIsbn() throws IOException {
        // Arrange
        String nonExistingIsbn = "0000000000";
        try (MockedStatic<BookClient> mockedStatic = mockStatic(BookClient.class)) {
            mockedStatic.when(() -> BookClient.getBookByISBN(nonExistingIsbn, apiKey))
                    .thenThrow(new IOException("Failed to fetch book information: 400"));

            // Act
            Book actualBook = bookService.findBookFromAPI(nonExistingIsbn);

            // Assert
            assertNull(actualBook);
        }
    }

    @Test
    void testUpdateBookCopies() {
        // Arrange
        long bookId = 1;
        int borrowedCopy = 1;
        Book book = new Book();
        book.setId(bookId);
        book.setCopies(5);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        bookService.updateBookCopies(bookId, borrowedCopy);

        // Assert
        verify(bookRepository, times(1)).save(book);
        assertEquals(4, book.getCopies());
    }
}
