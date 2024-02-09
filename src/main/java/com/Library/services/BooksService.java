package com.Library.services;

import com.Library.repositores.BooksRepository;
import com.Library.services.entities.Book;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for managing book-related operations.
 */
@Service
public class BooksService {
    private static final Logger log = Logger.getLogger(BooksService.class.getCanonicalName());

    private static final String BOOK_NOT_FOUND = "There isn't a book with ID %s in the library";
    private final BooksRepository booksRepository;
    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book to retrieve.
     * @return The book if found, otherwise null.
     */
    @Nullable
    public Book getBookByID(@Nonnull final Long id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    /**
     * Retrieves all books in the library.
     *
     * @return A list of all books.
     */
    @Nonnull
    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    /**
     * Retrieves all books by a specific author.
     *
     * @param author The author's name.
     * @return A list of books by the specified author.
     */
    @Nonnull
    public List<Book> getAllByAuthor(@Nonnull final String author) {
        return booksRepository.getByAuthor(author);
    }

    /**
     * Retrieves all books of a specific genre.
     *
     * @param genre The genre of the books.
     * @return A list of books of the specified genre.
     */
    @Nonnull
    public List<Book> getAllByGenre(@Nonnull final String genre) {
        return booksRepository.getByGenre(genre);
    }

    /**
     * Retrieves all books by a specific publisher.
     *
     * @param publisher The publisher's name.
     * @return A list of books published by the specified publisher.
     */
    @Nonnull
    public List<Book> getAllByPublisher(@Nonnull final String publisher) {
        return booksRepository.getByPublisher(publisher);
    }

    /**
     * Retrieves all books with a specific title.
     *
     * @param title The title of the books.
     * @return A list of books with the specified title.
     */
    @Nonnull
    public List<Book> getAllByTitle(@Nonnull final String title) {
        return booksRepository.getByTitle(title);
    }

    /**
     * Adds a new book to the library.
     *
     * @param book The book to add.
     */
    public void addBookToLibrary(@Nonnull final Book book) {
        booksRepository.save(book);
    }

    /**
     * Removes a book from the library.
     *
     * @param id The ID of the book to remove.
     * @return The removed book if found, otherwise null.
     */
    @Nullable
    public Book removeBookFromLibrary(@Nonnull final Long id) {
        Optional<Book> bookOpt = booksRepository.findById(id);
        if (bookOpt.isEmpty()) {
            log.severe(String.format(BOOK_NOT_FOUND, id));
            return null;
        }
        booksRepository.deleteById(id);
        return bookOpt.get();
    }
}
