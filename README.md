# LibLink

## 1. Project Overview

LibLink is a mobile Library Management System designed for university students and library administrators.

The application provides students with a mobile platform to access library services such as browsing books, searching for books, viewing book details, borrowing books, returning books, reserving books, managing reading lists, submitting reviews and managing their profiles.

Administrators can manage the library catalogue and monitor borrowing activity.

---

## 2. Purpose and Scope

The purpose of LibLink is to provide a mobile-based library management solution that makes library resources accessible through an Android application.

The application allows students to:

- Register for an account
- Log in securely
- Browse the library catalogue
- Search for books
- View book details
- Borrow books
- Return borrowed books
- Reserve unavailable books
- Manage reading lists
- Submit reviews and ratings
- View their profile
- Manage application settings

Administrators can:

- Add books
- Update books
- Delete books
- Manage book copies
- Monitor borrowing activity

---

## 3. Technologies

### Android Application

- Android Studio
- Kotlin
- XML
- Retrofit

### Backend

- ASP.NET Core Web API
- C#
- Entity Framework Core

### Database

- SQL Server

### Security

- JWT authentication
- Password hashing
- Role-based authorization
- HTTPS

### Development and Version Control

- Git
- GitHub
- GitHub Actions

---

## 4. Architecture

LibLink uses a client-server architecture.

The Android application communicates with the ASP.NET Core Web API through HTTPS REST API requests. Retrofit is used in the Android application to communicate with the backend.

The backend processes requests and uses Entity Framework Core to communicate with the SQL Server database.
