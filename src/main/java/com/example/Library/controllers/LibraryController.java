package com.example.Library.controllers;

import com.example.Library.services.BooksService;
import com.example.Library.services.LibraryService;
import com.example.Library.services.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {
    private final BooksService booksService;
    private final LibraryService libraryService;


    @Autowired
    public LibraryController(BooksService booksService, LibraryService libraryService) {
        this.booksService = booksService;
        this.libraryService = libraryService;
    }

    @GetMapping("/books/{id}")
    public Book getBookByID(@PathVariable Long id) {
        return booksService.getBookByID(id);
    }

    @GetMapping("/books/author/{author}")
    public List<Book> getAllByAuthor(@PathVariable String author) {
        return booksService.getAllByAuthor(author);
    }

    @GetMapping("/books/genre/{genre}")
    public List<Book> getAllByGenre(@PathVariable String genre) {
        return booksService.getAllByGenre(genre);
    }

    @GetMapping("/books/publisher/{publisher}")
    public List<Book> getAllByPublisher(@PathVariable String publisher) {
        return booksService.getAllByPublisher(publisher);
    }

    @GetMapping("/books/title/{title}")
    public List<Book> getAllByTitle(@PathVariable String title) {
        return booksService.getAllByTitle(title);
    }

    @PostMapping("/books/add")
    public Book addToLibrary(@RequestBody Book book) {
        return booksService.addToLibrary(book);
    }

    @DeleteMapping("/books/remove/{id}")
    public void removeFromLibrary(@PathVariable Long id) {
        booksService.removeFromLibrary(id);
    }

    @PostMapping("/add/{userId}/{bookId}")
    public void addToCart(@PathVariable Long userId, @PathVariable Long bookId) {
        libraryService.addBookToCart(userId, bookId);
    }
}