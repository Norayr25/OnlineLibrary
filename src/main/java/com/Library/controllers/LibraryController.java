package com.Library.controllers;

import com.Library.services.BooksService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling library operations.
 */
@RestController
@RequestMapping("/api/v1/library")
public class LibraryController {
    private final BooksService booksService;

    @Autowired
    public LibraryController(BooksService booksService) {
        this.booksService = booksService;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return A response containing the retrieved book or an error message if the book is not found.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books/{id}")
    public ResponseDTO<Book> getBookByID(@PathVariable Long id) {
        Book book = booksService.getBookByID(id);
        if (book == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "The book with specified id " +
                    id + " not found.", null);
        }
        return new ResponseDTO<>(HttpStatus.FOUND.value(), "The book with specified id " +
                id + " was successfully found.", book);
    }

    /**
     * Retrieves all books.
     * @return A response containing a list of all books.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books")
    public ResponseDTO<List<Book>> getAllBooks() {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Retrieved all books.",
                booksService.getAllBooks());
    }

    /**
     * Retrieves all books written by a specific author.
     * @param author The name of the author.
     * @return A response containing a list of books written by the specified author.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books/author/{author}")
    public ResponseDTO<List<Book>> getAllByAuthor(@PathVariable String author) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Retrieved all books of the author " +
                author + ".", booksService.getAllByAuthor(author));
    }

    /**
     * Retrieves all books of a specific genre.
     * @param genre The genre of the books.
     * @return A response containing a list of books belonging to the specified genre.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books/genre/{genre}")
    public ResponseDTO<List<Book>> getAllByGenre(@PathVariable String genre) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Retrieved all books of the genre " +
                genre + ".", booksService.getAllByGenre(genre));
    }

    /**
     * Retrieves all books published by a specific publisher.
     * @param publisher The name of the publisher.
     * @return A response containing a list of books published by the specified publisher.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books/publisher/{publisher}")
    public ResponseDTO<List<Book>> getAllByPublisher(@PathVariable String publisher) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Retrieved all books of the publisher " +
                publisher + ".", booksService.getAllByPublisher(publisher));
    }

    /**
     * Retrieves all books with a specific title.
     * @param title The title of the books.
     * @return A response containing a list of books with the specified title.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/books/title/{title}")
    public ResponseDTO<List<Book>> getAllByTitle(@PathVariable String title) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "Retrieved all books with the title " +
                title + ".", booksService.getAllByTitle(title));
    }

    /**
     * Adds a book to the library.
     * @param book The book to add to the library.
     * @return A response containing the added book.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/books/add")
    public ResponseDTO<Book> addBookToLibrary(@RequestBody Book book) {
        booksService.addBookToLibrary(book);
        return new ResponseDTO<>(HttpStatus.OK.value(), "The book successfully added to the library.", book);
    }

    /**
     * Removes a book from the library.
     * @param id The ID of the book to remove.
     * @return A response containing the removed book or an error message if the book is not found.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/books/{id}")
    public ResponseDTO<Book> removeBookFromLibrary(@PathVariable Long id) {
        Book book = booksService.removeBookFromLibrary(id);
        if (book == null) {
            return new ResponseDTO<>(HttpStatus.NOT_FOUND.value(),
                    "The book with specified id " + id + " not found.", null);
        }

        return new ResponseDTO<>(HttpStatus.OK.value(),
                "The book with specified id " + id + " was removed.", book);
    }
}