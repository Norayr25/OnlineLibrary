package com.Library.startup;

import com.Library.repositores.BooksRepository;
import com.Library.repositores.UserRepository;
import com.Library.services.dtos.ApiResponse;
import com.Library.services.entities.Book;
import com.Library.services.entities.User;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Initializes the database with sample data upon application startup.
 */
@Component
public class DatabaseInitializer implements ApplicationRunner {
    private static final Logger logger = Logger.getLogger(DatabaseInitializer.class.getCanonicalName());
    private static final String API_URL = "https://fakerapi.it/api/v1/books?_quantity=100&_locale=en_US";
    private static final String USERS_CSV_FILE_PATH = "users.csv";
    private final RestTemplate restTemplate;
    private final BooksRepository booksRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseInitializer(RestTemplate restTemplate,
                               BooksRepository booksRepository,
                               UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.booksRepository = booksRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        fetchBooksInventoryDataToDatabase();
        fetchUserDataToDatabase();
    }

    /**
     * Fetches book inventory data from an external API and saves it to the database.
     */
    public void fetchBooksInventoryDataToDatabase() {
        ApiResponse apiResponse = restTemplate.getForObject(API_URL, ApiResponse.class);
        if (apiResponse != null && apiResponse.getData() != null) {
            Book[] books = apiResponse.getData();
            Random random = new Random();
            for (Book book : books) {
                book.setPrice(random.nextInt(100) + 1);
                book.setQuantity(random.nextInt(20) + 1);
                booksRepository.save(book);
            }
            logger.info("Books imported successfully from the url " + API_URL);
        }
    }

    /**
     * Fetches user data from a CSV file and saves it to the database.
     */
    public void fetchUserDataToDatabase() {
        try (FileReader reader = new FileReader(USERS_CSV_FILE_PATH)) {
            List<User> users = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .build()
                    .parse();

            Random random = new Random();
            for (User user : users) {
                user.setMoney(random.nextInt(300) + 1);
                userRepository.save(user);
            }
            logger.info("Users imported successfully from the csv file " + USERS_CSV_FILE_PATH);
        } catch (Exception e) {
            logger.severe("Failed to import users from th csv file " + USERS_CSV_FILE_PATH);
        }
    }
}