package com.example.Library.services;

import com.example.Library.repositores.BooksRepository;
import com.example.Library.services.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final BooksRepository booksRepository;

    @Autowired
    public InventoryServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    public boolean isAvailable(Book book, int quantity) {
        Book currentBook = booksRepository.getByAuthorAndTitle(book.getAuthor(), book.getTitle());
        return currentBook != null && currentBook.getQuantity() >= quantity;
    }

    @Override
    public void updateInventory(Book book, int quantity) {
        Book currentBook = booksRepository.getByAuthorAndTitle(book.getAuthor(), book.getTitle());
        if (currentBook != null) {
            int newQuantity = currentBook.getQuantity() - quantity;
            currentBook.setQuantity(newQuantity);
            booksRepository.save(currentBook);
        }
    }

    @Override
    public Book getBookByAuthorAndTitle(String author, String title) {
        return booksRepository.getByAuthorAndTitle(author, title);
    }

    @Override
    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }
}