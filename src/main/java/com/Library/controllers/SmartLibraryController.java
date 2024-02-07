package com.Library.controllers;

import com.Library.services.SmartLibraryService;
import com.Library.services.dtos.ResponseDTO;
import com.Library.services.entities.Book;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for providing book suggestions to users.
 */
@RestController
@RequestMapping("/v1/library")
public class SmartLibraryController {
    private final SmartLibraryService smartLibraryService;

    public SmartLibraryController(SmartLibraryService smartLibraryService) {
        this.smartLibraryService = smartLibraryService;
    }

    /**
     * Suggests books to a user based on their past orders.
     *
     * @param userId The ID of the user for whom to suggest books.
     * @return A response containing the suggested books or an empty list if no books are found.
     */
    @GetMapping("/suggest/books/{userId}")
    public ResponseDTO<List<Book>> suggestBooksToUser(@PathVariable Long userId) {
        return new ResponseDTO<>(HttpStatus.OK.value(),
                "Books recommended to the user.", smartLibraryService.suggestBooksByGenre(userId));
    }
}
