# LibLink

## 1. Project Overview
LibLink is a mobile Library Management System designed for
university students and library administrators.

## 2. Purpose
The application allows students to search, borrow and reserve
library resources through a mobile Android application.

## 3. Technologies
- Android Studio
- Kotlin
- XML
- Retrofit
- MVVM
- ASP.NET Core Web API
- C#
- Entity Framework Core
- SQL Server
- JWT
- GitHub
- GitHub Actions

## 4. Architecture
- Android App
- Retrofit
- HTTPS REST API
- Entity Framework Core
- SQL Server

## 5. Features
### Student
- Registration
- Login
- Book catalogue
- Search
- Book details
- Borrowing
- Returning
- Reservations
- Reading lists
- Reviews
- Profile
- Settings

### Administrator
- Add books
- Update books
- Delete books
- Manage copies
- Monitor borrowing

## 6. Authentication
LibLink uses authenticated API requests and role-based
authorization.

## 7. REST API
### Authentication
- POST /api/auth/register
- POST /api/auth/login

### Books
- GET /api/books
- GET /api/books/{id}
- GET /api/books/search
- POST /api/books
- PUT /api/books/{id}
- DELETE /api/books/{id}

### Borrowing
- POST /api/borrowings
- PUT /api/borrowings/{id}/return
- GET /api/borrowings/user/{userId}

### Reservations
- POST /api/reservations
- GET /api/reservations/user/{userId}

### Reviews
- GET /api/reviews/book/{bookId}
- POST /api/reviews

### Reading Lists
- POST /api/readingLists
- GET /api/readingLists/user/{userId}












