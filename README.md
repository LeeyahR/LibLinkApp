# LibLinkApp

## 1. Project Overview

LibLink is a mobile Library Management System designed for university students and library administrators.

## 2. Purpose

The application allows students to browse, search, borrow and reserve library books through an Android mobile application.

## 3. Technologies

- Android Studio
- Kotlin
- XML
- Retrofit
- ASP.NET Core Web API
- C#
- Entity Framework Core
- SQL Server
- JWT
- GitHub
- GitHub Actions

## 4. Architecture

LibLink uses the following architecture:

- Android application
- Retrofit HTTP client
- HTTPS REST API
- ASP.NET Core Web API
- Entity Framework Core
- SQL Server database

The Android application communicates with the backend through REST API endpoints using Retrofit.

## 5. Features

### Student

- Registration
- Login
- Book catalogue
- Book search
- Book details
- Borrowing
- Returning books
- Reservations
- Reading lists
- Reviews and ratings
- Profile
- Settings

### Administrator

- Add books
- Update books
- Delete books
- Manage book copies
- Monitor borrowing

## 6. Authentication

LibLink uses authenticated API requests and role-based authorization to control access to application functionality.

## 7. REST API

### Authentication

- POST /api/auth/register`
- POST /api/auth/login`

### Books

- GET /api/books`
- GET /api/books/{id}`
- GET /api/books/search`
- POST /api/books`
- PUT /api/books/{id}`
- DELETE /api/books/{id}`

### Borrowing

- POST /api/borrowings`
- PUT /api/borrowings/{id}/return`
- GET /api/borrowings/user/{userId}`

### Reservations

- POST /api/reservations`
- GET /api/reservations/user/{userId}`

### Reviews

- GET /api/reviews/book/{bookId}`
- POST /api/reviews`

### Reading Lists

- POST /api/readingLists`
- GET /api/readingLists/user/{userId}`

## 8. Validation

The application validates user input before sending requests to the API.

## 9. Logging

Android logging utilities are used during development to record important application states and API operations.

## 10. Testing

Unit tests are used to verify application validation and logic where applicable.

## 11. GitHub Actions

GitHub Actions automatically builds the Android project and runs the project's automated tests when changes are pushed to the repository.

## 12. Database

SQL Server stores application data including:

- Users
- Books
- Borrowing records
- Reservations
- Reviews
- Reading lists

## 13. Security

The system uses:

- HTTPS
- Authentication
- Password hashing
- Authorization

These mechanisms help protect user accounts and application data.

## 14. Installation

1. Clone the repository.
2. Open the Android project in Android Studio.
3. Open the API project in Visual Studio.
4. Configure the SQL Server database.
5. Run the required database migrations.
6. Start the API.
7. Configure the Android application with the API URL.
8. Build and run the Android application.



