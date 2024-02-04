package com.example.Library.services;

import com.example.Library.repositores.BooksRepository;
import com.example.Library.services.entities.Book;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BooksService {
    private static final Logger logger = Logger.getLogger(BooksService.class.getCanonicalName());
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Nullable
    public Book getBookByID(@Nonnull final Long id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }
    public List<Book> getAllByAuthor(@Nonnull final String author) {
        return booksRepository.getByAuthor(author);
    }

    public List<Book> getAllByGenre(@Nonnull final String genre) {
        return booksRepository.getByGenre(genre);
    }

    public List<Book> getAllByPublisher(@Nonnull final String publisher) {
        return booksRepository.getByPublisher(publisher);
    }

    public List<Book> getAllByTitle(@Nonnull final String title) {
        return booksRepository.getByTitle(title);
    }

    public Book addToLibrary(@Nonnull final Book book) {
        return booksRepository.save(book);
    }

    public void removeFromLibrary(@Nonnull final Long id) {
        booksRepository.deleteById(id);
    }
}
