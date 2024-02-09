package com.Library.services;

import com.Library.repositores.BooksRepository;
import com.Library.services.entities.Book;
import com.Library.services.entities.Cart;
import com.Library.services.entities.Item;
import com.Library.services.entities.Order;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.*;
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
     * @param userEmail The email of the user for whom to suggest books.
     * @return A list of suggested books based on the user's past orders. If the user has no past orders or no books are found,
     * an empty list is returned.
     */
    @Nonnull
    public List<Book> suggestBooksByGenre(@Nonnull final String userEmail) {
        List<Order> orderList = orderService.getSpecificUserOrders(userEmail);
        if (orderList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> byGenre = orderList.stream().map(order -> {
            Cart cart = order.getCart();
            Set<Item> items = cart.getItems().keySet();
            return items
                    .stream()
                    .filter(item -> item instanceof Book)
                    .map(item -> ((Book) item).getGenre())
                    .collect(Collectors.toList());
        }).flatMap(Collection::stream).toList();

        return byGenre.stream()
                .flatMap(genre -> booksRepository.getByGenre(genre).stream())
                .collect(Collectors.toList());
    }
}
