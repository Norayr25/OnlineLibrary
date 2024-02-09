package com.Library.controllers;

import com.Library.services.SmartLibraryService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.Book;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for providing book suggestions to users.
 */
@RestController
@RequestMapping("/api/v1/library")
@PreAuthorize("hasRole('ROLE_ADMIN' or 'ROLE_SUPER_ADMIN')")
public class SmartLibraryController {
    private final SmartLibraryService smartLibraryService;

    public SmartLibraryController(SmartLibraryService smartLibraryService) {
        this.smartLibraryService = smartLibraryService;
    }

    /**
     * Suggests books to a user based on their past orders.
     *
     * @param email The email of the user for whom to suggest books.
     * @return A response containing the suggested books or an empty list if no books are found.
     */
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/suggest/books/{email}")
    public ResponseDTO<List<Book>> suggestBooksToUser(@PathVariable String email) {
        return new ResponseDTO<>(HttpStatus.OK.value(),
                "Books recommended to the user.", smartLibraryService.suggestBooksByGenre(email));
    }
}
