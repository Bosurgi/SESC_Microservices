package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Mock
    private Model model;

    @MockBean
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(new Book("1234567890", "Title", "Author", 10, 5));
        when(bookService.findAllBooks()).thenReturn(books);

        // Act
        String viewName = bookController.getAllBooks(model);

        // Assert
        assertEquals("books", viewName);
        verify(bookService, times(1)).findAllBooks();
        verify(model, times(1)).addAttribute("books", books);
    }

    @Test
    void testGetBookByIsbn() {
        // Arrange
        String isbn = "1234567890";
        Book book = new Book(isbn, "Title", "Author", 2024, 5);
        when(bookService.findBookByIsbn(isbn)).thenReturn(book);

        // Act
        ResponseEntity<Book> responseEntity = bookController.getBookByIsbn(isbn);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book, responseEntity.getBody());
        verify(bookService, times(1)).findBookByIsbn(isbn);
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        // Arrange
        String isbn = "1234567890";
        when(bookService.findBookByIsbn(isbn)).thenReturn(null);

        // Act
        ResponseEntity<Book> responseEntity = bookController.getBookByIsbn(isbn);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(bookService, times(1)).findBookByIsbn(isbn);
    }

    @Test
    void testAddBook() {
        // Arrange
        Book book = new Book("1234567890", "Title", "Author", 2024, 5);
        when(bookService.saveBook(book)).thenReturn(book);

        // Act
        String viewName = bookController.addBook(book, model);

        // Assert
        assertEquals("admin/add-book", viewName);
        verify(bookService, times(1)).saveBook(book);
        verify(model, times(1)).addAttribute("success", "Book added successfully");
    }

    @Test
    void testAddBook_Failure() {
        // Arrange
        Book book = new Book("1234567890", "Title", "Author", 2024, 5);
        when(bookService.saveBook(book)).thenReturn(null);

        // Act
        String viewName = bookController.addBook(book, model);

        // Assert
        assertEquals("admin/add-book", viewName);
        verify(bookService, times(1)).saveBook(book);
        verify(model, times(1)).addAttribute("error", "Failed to add book");
    }

    @Test
    void testDeleteBookByIsbn() {
        // Arrange
        String isbn = "1234567890";
        Book book = new Book(isbn, "Title", "Author", 2024, 5);
        when(bookService.findBookByIsbn(isbn)).thenReturn(book);

        // Act
        ResponseEntity<Book> responseEntity = bookController.deleteBookByIsbn(isbn);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(bookService, times(1)).deleteBook(book.getId());
    }

    @Test
    void testDeleteBookByIsbn_NotFound() {
        // Arrange
        String isbn = "1234567890";
        when(bookService.findBookByIsbn(isbn)).thenReturn(null);

        // Act
        ResponseEntity<Book> responseEntity = bookController.deleteBookByIsbn(isbn);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(bookService, never()).deleteBook(anyLong());
    }
}