package com.example.Library.startup;

import com.example.Library.repositores.BooksRepository;
import com.example.Library.repositores.UsersRepository;
import com.example.Library.services.dtos.ApiResponse;
import com.example.Library.services.entities.Book;
import com.example.Library.services.entities.User;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.util.List;
import java.util.logging.Logger;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getCanonicalName());
    private static final String API_URL = "https://fakerapi.it/api/v1/books?_quantity=100&_locale=en_US";
    private static final String USERS_CSV_PATH = "users.csv";
    private final RestTemplate restTemplate;
    private final BooksRepository booksRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public DatabaseInitializer(RestTemplate restTemplate, BooksRepository booksRepository, UsersRepository usersRepository) {
        this.restTemplate = restTemplate;
        this.booksRepository = booksRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        fetchBooksInventoryDataToDatabase();
        fetchUserDataToDatabase();
    }

    public void fetchBooksInventoryDataToDatabase() {
        ApiResponse apiResponse = restTemplate.getForObject(API_URL, ApiResponse.class);
        if (apiResponse != null && apiResponse.getData() != null) {
            Book[] books = apiResponse.getData();
            for (Book book : books) {
                // Check if a book with the same title and author exists in the database.
                Book existingBook = booksRepository.getByAuthorAndTitle(book.getAuthor(), book.getTitle());
                if (existingBook != null) {
                    // If the book already exists, update its quantity.
                    existingBook.setQuantity(existingBook.getQuantity() + 1);
                    booksRepository.save(existingBook);
                } else {
                    // If the book doesn't exist, save it with a quantity of 1.
                    book.setQuantity(1);
                    booksRepository.save(book);
                }
            }
        }
    }

    public void fetchUserDataToDatabase() {
        try (FileReader reader = new FileReader(USERS_CSV_PATH)) {
            List<User> users = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .build()
                    .parse();

            usersRepository.saveAll(users);
            logger.severe("Users imported successfully from the csv file " + USERS_CSV_PATH);
        } catch (Exception e) {
            logger.severe("Failed to import users from th csv file " + USERS_CSV_PATH);
        }
    }
}