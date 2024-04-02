package com.sesc.libraryservice.controller;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Get all books from the Database
     *
     * @return List of Books and HTTP Status code
     */
    @GetMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    /**
     * Get a book by the ISBN
     *
     * @param isbn the ISBN to search for
     * @return the book entity along with HTTP Status code.
     */
    @GetMapping("/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findBookByIsbn(isbn);
        return ResponseEntity.ok(book);
    }

    /**
     * Post request endpoint to add a book to the Database
     *
     * @param book the book to add
     * @return the created book entity and HTTP status code.
     */
    @PostMapping("/admin/add-book")
    public String addBook(@ModelAttribute Book book, Model model) {
        Book newBook = bookService.saveBook(book);
        if (newBook != null) {
            model.addAttribute("success", "Book added successfully");
        } else {
            model.addAttribute("error", "Failed to add book");
        }
        return "admin/add-book";
    }

    /**
     * Get the add book page
     *
     * @return the add book page
     */
    @GetMapping("/admin/add-book")
    public String addBook() {
        return "admin/add-book";
    }

    @PostMapping("/admin/search-book")
    public String searchBook(@RequestParam String bookIsbn, Model model) {
        // Calling the Google API
        Book book = bookService.findBookFromAPI(bookIsbn);
        if (book != null) {
            model.addAttribute("book", book);
            return "admin/add-book-details";
        } else {
            model.addAttribute("error", "Book not found");
            return "admin/add-book";
        }
    }

    /**
     * Delete a book by the ISBN
     *
     * @param isbn the ISBN to search for
     * @return the book entity along with HTTP Status code.
     */
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Book> deleteBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findBookByIsbn(isbn);
        bookService.deleteBook(book.getId());
        return ResponseEntity.ok(book);
    }
}
