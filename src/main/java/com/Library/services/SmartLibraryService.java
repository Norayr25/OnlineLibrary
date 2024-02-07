package com.Library.services;

import com.Library.repositores.BooksRepository;
import com.Library.services.entities.Book;
import com.Library.services.entities.Cart;
import com.Library.services.entities.Item;
import com.Library.services.entities.Order;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for providing book suggestions to users based on their past orders.
 */
@Service
public class SmartLibraryService {
    private final OrderService orderService;
    private final BooksRepository booksRepository;

    public SmartLibraryService(OrderService orderService,
                               BooksRepository booksRepository) {
        this.orderService = orderService;
        this.booksRepository = booksRepository;
    }

    /**
     * Suggests books to a user based on their past orders.
     *
     * @param userId The ID of the user for whom to suggest books.
     * @return A list of suggested books based on the user's past orders. If the user has no past orders or no books are found,
     * an empty list is returned.
     */
    @Nonnull
    public List<Book> suggestBooksByGenre(@Nonnull final Long userId) {
        List<Order> orderList = orderService.getSpecificUserOrders(userId);
        if (orderList.isEmpty()) {
            return Collections.emptyList();
        }

        final Set<String> byGenre = new HashSet<>();
        for (Order order : orderList) {
            Cart cart = order.getCart();
            Set<Item> items = cart.getItems().keySet();
            for (Item item : items) {
                if (item instanceof Book book) {
                    byGenre.add(book.getGenre());
                }
            }
        }

        return byGenre.stream()
                .flatMap(genre -> booksRepository.getByGenre(genre).stream())
                .collect(Collectors.toList());
    }
}
