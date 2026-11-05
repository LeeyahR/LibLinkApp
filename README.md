# LibLink

[![LibLink Android CI](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml/badge.svg)](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml)

## 1. Project Overview

LibLink is a mobile Library Management System designed for university students and library administrators.

The application provides students with a mobile platform to access library services such as browsing and searching for books, viewing book details, borrowing and returning books, reserving books, managing reading lists, submitting reviews, and managing their profiles.

Administrators can manage the library catalogue and monitor borrowing activity.

## 2. Purpose and Scope

The purpose of LibLink is to provide a mobile-based library management solution that makes library resources accessible through an Android application.

### Student Features

Students can:

- Register for an account
- Log in securely using email and password
- Authenticate using Google Sign-In
- Use biometric authentication to unlock a saved session
- Browse and search the library catalogue
- View book details
- Borrow and return books
- Reserve unavailable books
- Manage reading lists
- Submit reviews and ratings
- View their profile
- Manage application settings

### Administrator Features

Administrators can:

- Add books
- Update books
- Delete books
- Manage book copies
- Monitor borrowing activity

## 3. Technologies

### Android Application

- Android Studio
- Kotlin
- XML
- Retrofit
- AndroidX Biometric

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
- Biometric authentication for local session unlocking

### Development and Version Control

- Git
- GitHub
- GitHub Actions

## 4. Architecture

LibLink uses a client-server architecture.

The Android application communicates with the ASP.NET Core Web API through HTTPS REST API requests. Retrofit is used in the Android application to communicate with the backend.

The backend processes requests and uses Entity Framework Core to communicate with the SQL Server database.

```text
Android Application
       |
       | HTTPS REST API requests
       | Retrofit
       v
ASP.NET Core Web API
       |
       | Entity Framework Core
       v
SQL Server Database
```

## 5. Continuous Integration (CI)

LibLink uses GitHub Actions to automatically build and test the Android application.

The CI workflow is located at:

```text
.github/workflows/android.yml
```

### Workflow Triggers

The workflow runs when code is:

- Pushed to `main` or `master`
- Submitted through a pull request targeting `main` or `master`

### CI Process

The workflow performs the following steps:

1. Checks out the repository
2. Sets up JDK 17
3. Sets up Gradle
4. Builds the Android debug application
5. Runs unit tests
6. Uploads the generated debug APK as a GitHub Actions artifact

### Debug APK Artifact

After a successful workflow run, the generated debug APK is available to download from the workflow run’s **Artifacts** section.

The artifact is named:

```text
LibLink-debug-apk
```

Artifacts are retained for 14 days and can be used by team members to install and test the application.

### CI Workflow

[View LibLink Android CI runs](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml)

## 6. Demonstration

YouTube Link: https://youtu.be/uzDr4IPr1ns
