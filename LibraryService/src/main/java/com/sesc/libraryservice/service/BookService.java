package com.sesc.libraryservice.service;

import com.sesc.libraryservice.model.Book;
import com.sesc.libraryservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
