package com.sesc.libraryservice.service;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.repository.BookRepository;
import com.sesc.libraryservice.util.BookClient;
import com.sesc.libraryservice.util.BookParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    // Injecting the API Key to search for books
    private final String apiKey;

    public BookService(BookRepository bookRepository, @Value("${library.key}") String apiKey) {
        this.bookRepository = bookRepository;
        this.apiKey = apiKey;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book findBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * It returns a book object by querying the Google Book API.
     *
     * @param isbn the ISBN to look for
     * @return the Book Object
     */
    public Book findBookFromAPI(String isbn) {
        try {
            // Using the ISBN and the API KEY for Google Book Library
            String jsonResponse = BookClient.getBookByISBN(isbn, apiKey);
            return BookParser.parseBookResponse(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the book copies when a student borrows a book.
     *
     * @param bookId       the bookId of the book borrowed
     * @param borrowedCopy the number of copies borrowed
     */
    public void updateBookCopies(Long bookId, int borrowedCopy) {
        Book book = findBookById(bookId);
        book.setCopies(book.getCopies() - borrowedCopy);
        bookRepository.save(book);
    }
}
