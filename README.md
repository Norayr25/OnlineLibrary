📚
Welcome to OnlineLibrary! This is an online library application where users can browse books, manage their carts, and make purchases. The application has three user roles: Super-Admin, Admin, and User.

## Features
#### User Authentication
User Sign In: Users can sign in to the library using basic authentication (email and password) via POST request to /api/v1/auth/signIn. Upon signing in, the user is assigned the role of USER.
User Sign-Up: New users can register into the library system by making a POST request to /api/v1/auth/signUp. The required request body schema is the same as for users already imported during startup.
User Sign-Out: Users can sign out from the library using basic authentication via POST request to /api/v1/auth/signOut. This operation removes the USER role from the user but does not delete the user from the library.

#### Data importing
During application startup, users are imported from a CSV file called users.csv located in the current directory. 
Books are loaded from the URL https://fakerapi.it/api/v1/books?_quantity=100&_locale=en_US.
Note: The service will run on port _8000_ by default and can be accessed via that port.
The database chosen for this application is SQLite.

#### Book Management 
Admin users can manage books in the library, including adding, updating, and deleting books.
#### User Management
Admin users can manage user data, including updating user information and assigning roles.
#### Search and Purchase
Users can search for books and make purchases using the cart functionality.
#### Order Management
The library system provides APIs for managing orders, including retrieving order history for all users and specific users.
#### Smart Library Features
The SmartLibraryController provides endpoints for suggesting books to users based on their past orders.

#### Startup Data Loading
During application startup, the following data is loaded:

#### Database Schema
The application's database schema includes the following entities:

#### User
Represents a user in the library system. Includes fields such as name, email, address, phone, country, password, money (new field), and user role.
#### Super admin user
A super admin user is treated based on the login credentials used:
If a user tries to authenticate with the email/password credentials of super_admin/super_admin,
they will be treated as a super admin user, regardless of whether a user with the username "super_admin" exists in the database.
This super admin user has all privileges in the library system and is the only user with super admin rights.
The super admin has the ability to:
Change the roles of any user in the system, including granting or revoking super admin privileges.
#### Book
Represents a book in the library system. Includes fields such as title, author, genre, description, ISBN, image, published date, publisher, price (new field), and quantity (new field).
#### Cart
Represents a user's cart in the library system. Contains a collection of cart items.
#### Order
Represents an order made by a user. Contains information about the user, cart, and total price.
#### Item
An abstract class representing an item in the library system. Includes common fields such as price and quantity.

#### Roles
Super-Admin: Can add new Admins and access sales reports.
Admin: Can manage books and user data.
User: Can search for books and make purchases.

#### Controller Classes

#### SmartLibraryController
Provides endpoints for suggesting books to users based on their past orders.
#### UserManagementController
Handles user management operations such as retrieving users by ID, email, or name, deleting users, and changing user roles.
#### PurchaseController
Manages purchase operations, allowing users to initiate purchases for specific items.
#### OrderHistoryController
Retrieves order history, allowing SUPER_ADMIN to view all orders or orders made by a specific user.
#### LibraryController
Handles library operations such as retrieving books, adding books, and removing books from the library.
#### CartController
Manages cart operations such as adding items to the cart and removing items from the cart. 
Items (in this case books) are counted by IDs starting with 1.
Here is an example of request body to add/remove an item:
{"email": "dui.lectus@google.ca", "itemId": 1,"count": 2 }
#### AuthController
Manages user authentication operations such as signing in, signing up, and signing out.


#### Using the JAR file
You can run the application using the provided JAR file (Library-0.0.1-SNAPSHOT.jar). To execute the JAR file, use the following command:
**java -jar Library-0.0.1-SNAPSHOT.jar**

Providing Users CSV Location
If you want to specify the location of the users.csv file, you can pass it as an argument when running the JAR file. Use the --usersCsvFilePath option followed by the path to the CSV file. For example:

**java -jar Library-0.0.1-SNAPSHOT.jar --usersCsvFilePath=/path/to/users.csv**
If you don't provide the --usersCsvFilePath argument, the application will attempt to locate the users.csv file in the current directory.








