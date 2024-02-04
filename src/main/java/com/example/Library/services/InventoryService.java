package com.example.Library.services;

import com.example.Library.services.entities.Book;

import java.util.List;

public interface InventoryService {
    boolean isAvailable(Book book, int quantity);
    void updateInventory(Book book, int quantity);
    Book getBookByAuthorAndTitle(String author, String title);
    List<Book> getAllBooks();
}
