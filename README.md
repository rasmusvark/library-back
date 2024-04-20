# Vue and Micronaut Library Management System

## Description
This project is the backend for a library management system built using the Micronaut framework. It manages books, handles operations like borrow and return.

## Features
- Book management (add, list, delete)
- Borrow and return functionalities
- RESTful API
- Automated CRUD operations for database management

## Technology Stack
- **Micronaut**: Framework for building microservices.
- **PostgreSQL**: Database for storing book records.
- **Hibernate**: ORM tool for database interaction.

## Installation

### Prerequisites
- JDK 11 or later
- PostgreSQL
- Gradle or compatible build tool

### Environment Setup
Set the following environment variables for your database connection:
shell
export DB_HOST="localhost"
export DB_USERNAME="library_user"
export DB_PASSWORD="librarytest"

### CORS Configuration

If you're running the frontend of the application on a different origin (domain, scheme, or port), you need to update the CORS configuration in the `application.properties` file to allow requests from the frontend. 

Find the following line in the `application.properties` file:

micronaut.server.cors.configurations.web.allowed-origins=http://localhost:8081

### Getting Started
Follow these instructions to get your application up and running on your local machine for development and testing purposes.

- Clone the repository: git clone https://github.com/rasmusvark/library_back.git

- Configure PostgreSQL: Ensure that PostgreSQL is running and the environment variables are set correctly as mentioned above.

- Run the application: ./gradlew run

This command will start the server on the default Micronaut port, typically http://localhost:8080.

### API Endpoints
The application provides various REST endpoints for managing books and their borrowal status. Here are some of the key endpoints:

- POST /books/: Add a new book to the system.
- GET /books/: Retrieve all books.
- PATCH /books/{id}/borrow: Mark a book as borrowed.
- PATCH /books/{id}/return: Mark a book as returned.
- DELETE /books/{id}: Delete a book from the system.

### Health Check
Access the health check endpoint at http://localhost:8080/health to see the health status of the application and connected services.

### Development Notes
Adjust configurations such as database details and server port in the application's application.properties file as necessary.
