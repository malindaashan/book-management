# Book Management

#### Step 1

You need to have java 11 configured. Please configure java 11 based on you OS.

#### Step 2
Checkout the project from the git link.

#### Step 3
Go to the project downloaded folder and open a terminal run ./gradlew build

#### Step 4
Once built go inside the build/libs folder.

#### Step 5

And open a terminal there and run java -jar book-management-0.0.1-SNAPSHOT.jar to start the spring boot app

#### Step 6

This build file or jar file is main entry point to the programme. Its good if you can copy it to a fresh location. Rather than keeping it inside your project.

### Description Project Structure.
#### Package Breakdown

##### Controller Layer (controller package)
BookController → Acts as the entry point for the API requests.
It handles HTTP requests and calls the BookService interface for business logic.

##### Service Layer (service package)
BookService → Defines the business logic contract.
BookServiceImpl → Implements BookService and follows SOLID principles to ensure separation of concerns.

##### Repository Layer (repository package)
BookRepository → Interface that abstracts the data layer.
BookCSVRepositoryImpl → Implements BookRepository and handles CSV-based data storage.
This can be easily swapped with a database repository in the future due to the interface-based design.

##### Entity Layer (entity package)
Book → Represents the Book entity, which is stored in CSV.

#####  DTO Layer (dto package)
BookDto → Used for handling API requests and responses, ensuring clean separation between API models and persistence models.

##### Exception Handling Layer (exception package)
BookNotFoundException → Custom exception for handling book-related errors.