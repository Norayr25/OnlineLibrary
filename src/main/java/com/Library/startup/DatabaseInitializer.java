package com.Library.startup;

import com.Library.repositores.BooksRepository;
import com.Library.repositores.UserRepository;
import com.Library.services.dtos.ApiResponse;
import com.Library.services.entities.Book;
import com.Library.services.entities.User;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.Nonnull;
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
    private static final String BOOKS_LOCATION_URL = "https://fakerapi.it/api/v1/books?_quantity=100&_locale=en_US";
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
        String usersCsvFilePath = args.containsOption("usersCsvFilePath") ?
                args.getOptionValues("usersCsvFilePath").getFirst() : USERS_CSV_FILE_PATH;
        fetchBooksInventoryDataToDatabase();
        fetchUserDataToDatabase(usersCsvFilePath);
    }

    /**
     * Fetches book inventory data from an external API and saves it to the database.
     */
    public void fetchBooksInventoryDataToDatabase() {
        ApiResponse apiResponse = restTemplate.getForObject(BOOKS_LOCATION_URL, ApiResponse.class);
        if (apiResponse != null && apiResponse.getData() != null) {
            Book[] books = apiResponse.getData();
            Random random = new Random();
            for (Book book : books) {
                book.setPrice(random.nextInt(100) + 1);
                book.setQuantity(random.nextInt(20) + 1);
                booksRepository.save(book);
            }
            logger.info("Books imported successfully from the url " + BOOKS_LOCATION_URL);
        }
    }

    /**
     * Fetches user data from a CSV file and saves it to the database.
     * @param usersCsvFilePath The file path of the CSV file containing user data.
     */
    public void fetchUserDataToDatabase(@Nonnull final String usersCsvFilePath) {
        try (FileReader reader = new FileReader(usersCsvFilePath)) {
            List<User> users = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .build()
                    .parse();

            Random random = new Random();
            for (User user : users) {
                user.setMoney(random.nextInt(400) + 1);
                userRepository.save(user);
            }
            logger.info("Users imported successfully from the csv file " + usersCsvFilePath);
        } catch (Exception e) {
            logger.severe("Failed to import users from th csv file " + usersCsvFilePath);
        }
    }
}