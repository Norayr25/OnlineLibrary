package com.example.Library.services;

import com.example.Library.repositores.BooksRepository;
import com.example.Library.repositores.UsersRepository;
import com.example.Library.services.entities.Book;
import com.example.Library.services.entities.Cart;
import com.example.Library.services.entities.CartItem;
import com.example.Library.services.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final PurchaseService purchaseService;

    private final UsersRepository usersRepository;

    private final BooksRepository booksRepository;

    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    @Autowired
    public LibraryService(PurchaseService purchaseService, UsersRepository usersRepository, BooksRepository booksRepository, PaymentService paymentService, InventoryService inventoryService) {
        this.purchaseService = purchaseService;
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }

//    public PurchaseResponse purchaseBooks(Long userId) {
//        Optional<User> userOpt = usersRepository.findById(userId);
//
//        if (userOpt.isEmpty()) {
//            return new PurchaseResponse("User not found", false);
//        }
//
//        User user = userOpt.get();
//        Cart cart = user.getCart();
//
//        // Step 1: Check inventory availability
//        boolean isAvailable = inventoryService.checkAvailability(cart.getItems());
//
//        if (!isAvailable) {
//            return new PurchaseResponse("Some books are out of stock", false);
//        }
//
//        // Step 2: Process payment
//        boolean isPaymentSuccessful = paymentService.processPayment(user, cart.getTotalPrice());
//
//        if (!isPaymentSuccessful) {
//            return new PurchaseResponse("Payment failed", false);
//        }
//
//        // Step 3: Process purchase
//        PurchaseResponse purchaseResponse = purchaseService.processPurchase(user, cart.getItems());
//
//        if (!purchaseResponse.isSuccess()) {
//            // Rollback payment if purchase failed
//            paymentService.rollbackPayment(user, cart.getTotalPrice());
//            return purchaseResponse;
//        }
//
//        // Clear the user's cart after successful purchase
//        user.clearCart();
//
//        return purchaseResponse;
//    }


    public void addBookToCart(Long userId, Long bookId) {
        Optional<User> user = usersRepository.findById(userId);
        Optional<Book> book = booksRepository.findById(bookId);

        if (user.isEmpty() || book.isEmpty()) {
            return;
        }


        CartItem cartItem = new CartItem(book.get());
        purchaseService.addToCart(user.get(), cartItem);
    }

    public void removeBookFromCart(Long userId, Long bookId) {
        Optional<User> user = usersRepository.findById(userId);
        Optional<Book> book = booksRepository.findById(bookId);

        if (user.isEmpty() || book.isEmpty()) {
            return;
        }


        CartItem cartItem = new CartItem(book.get());

        purchaseService.removeFromCart(user.get(), cartItem);
    }

    // Utility method to calculate total price of books
    private double calculateTotalPrice(List<Book> books) {
        return books.stream().mapToDouble(Book::getPrice).sum();
    }
}
