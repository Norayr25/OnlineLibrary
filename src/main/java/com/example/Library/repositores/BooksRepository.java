package com.example.Library.repositores;

import com.example.Library.services.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> getByAuthor(String author);

    List<Book> getByGenre(String genre);

    List<Book> getByPublisher(String publisher);

    List<Book> getByTitle(String title);

    Book getByAuthorAndTitle(String author, String title);
}