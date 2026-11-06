**LibLink - University Library Management System**
**Overview**
Using a cutting-edge mobile application, LibLink is a native Android library management system created in Kotlin to assist university students in finding, borrowing, reserving, and managing library materials.

The application provides a centralised platform that allows students to:
- Look up and peruse the library's offerings.
- Check out and hold onto books
- View the history of borrowing
- Send in reviews and ratings.
- Get alerts about events at the library.
- Access library resources both offline and online.
- Use biometric authentication and Single Sign-On (SSO) to securely authenticate
The project uses MVVM architecture, Room Database, Retrofit networking, Firebase services, and GitHub CI/CD automation, among other contemporary Android development techniques.

**Project Objectives**
The primary objectives of LibLink are:
- Expand the availability of university library materials
- Decrease reliance on in-person library visits
- Give information on the availability of resources in real time.
- Encourage offline access by using local caching
- Boost user interaction with ratings and reviews
- Provide a safe and convenient experience

**Features**
**Core Features**

**Authentication**

- Single Sign-On (SSO) on Google
- Firebase Verification
- Management of user sessions
- Persistence of secure login

**Library Catalogue**

- View the complete book collection.
- Find books by title.
- Find books by author name.
- Browse books by category.
- Display only books that are currently available.

**Borrowing System**

- Borrow books from the library.
- View currently borrowed books.
- Access previous borrowing records.
- Manage and track borrowed books.

**Reservation System**

- Reserve books that are currently unavailable.
- View the reservation waiting list.
- Check the status of book reservations.

**User Profile**

- View personal account details.
- Update and manage account settings.
- Securely log out of the system.

**Custom Feature 1: Ratings and Reviews**
Students can provide feedback by writing book reviews and assigning ratings based on their reading experience.

**Functionality**

- Rate books using a 1–5 star scale.
- Submit written feedback and comments.
- Browse reviews submitted by other users.
- Modify previously submitted reviews.
- Remove existing reviews.

**Validation**

The system prevents:
- Prevent submission of blank reviews.
- Validate ratings to ensure they fall within the accepted range.
- Reject incorrectly formatted or incomplete submissions.

**Logging**

Review-related activities and user interactions are monitored and recorded through Android Logcat for debugging and tracking purposes.
Example:
Log.d("ReviewViewModel", "Review submitted")

**Custom Feature 2: Offline Catalogue Browsing**
The application enables offline functionality by using Room Database to locally store and manage data, allowing users to access information without an internet connection.

**Online Mode**
REST API
    ↓
Book Repository
    ↓
Room Database Cache
    ↓
User Interface

**Offline Mode**
Room Database
    ↓
Repository
    ↓
User Interface

**Benefits**

- Access the book catalogue without requiring an internet connection.
- Improve application performance through faster data retrieval.
- Minimise network usage by reducing unnecessary server requests.

**POE Feature: Offline Synchronisation**
LibLink enables users to access selected features and perform supported operations without an active internet connection.

**Example Workflow**
No Internet
      ↓
Submit Review
      ↓
Stored in Room Database
      ↓
Marked as Pending
      ↓
Internet Restored
      ↓
WorkManager Sync
      ↓
REST API
      ↓
Cloud Database

**Technologies**

- Room Database
- WorkManager
- Retrofit
- Coroutines

**Real-Time Notifications**
Firebase Cloud Messaging (FCM) enables the application to send and receive push notifications, keeping users informed about important updates and activities.

**Notification Examples**

- Notify users when a reserved book becomes available.
- Send reminders about approaching book return deadlines.
- Inform users when their book reservations are approved.
- Deliver important library news and announcements.

**Biometric Authentication**
The application supports:
- Fingerprint Verification: Allow users to securely access their accounts using fingerprint authentication.
- Facial Recognition: Enable face-based authentication on compatible devices.
The Android Biometric API provides an additional layer of security by allowing users to authenticate their identity through supported biometric methods before accessing the application after logging in.

**Flow**
Google Login
      ↓
Biometric Verification
      ↓
Dashboard

**Multi-Language Support**
LibLink offers multilingual support, allowing users to access and navigate the application in various South African languages.

Currently implemented:
- English
- isiZulu

Language translations and text resources are organised and maintained using Android's resource files to support multiple languages within the application.

Example:
res/values/strings.xml
res/values-zu/strings.xml

**Architecture**
The application is structured using the Model-View-ViewModel (MVVM) architectural pattern to promote code organisation, maintainability, and separation of concerns.
UI Layer
     ↓
ViewModels
     ↓
Repositories
     ↓
─────────────────
│               │
REST API     Room DB
│               │
Remote Data  Local Data

**Advantages**

- Clear separation of application components and responsibilities.
- Simplified testing and debugging processes.
- Improved ability to expand and accommodate new features.
- Easier code management, updates, and maintenance.

**Project Structure**com.example.liblink
│
├── data
│   ├── local
│   │   ├── dao
│   │   ├── entities
│   │   └── LibLinkDatabase.kt
│   │
│   └── remote
│       ├── RetrofitInstance.kt
│       ├── BookApiService.kt
│       └── ReviewApiService.kt
│
├── repository
│   ├── BookRepository.kt
│   └── ReviewRepository.kt
│
├── viewmodel
│   ├── BookViewModel.kt
│   └── ReviewViewModel.kt
│
├── workers
│   └── SyncReviewWorker.kt
│
├── ui
│   ├── screens
│   ├── components
│   └── navigation
│
└── utils

**Technology Stack**
**Mobile Development**
- Kotlin
- Android Studio
- Jetpack Compose

**Architecture**
- MVVM
- Repository Pattern

**Local Storage**
- Room Database
- DataStore

**Networking**
- Retrofit
- Gson

**Concurrency**
- Kotlin Coroutines
- Flow

**Authentication**
- Firebase Authentication
- Google Sign-In

**Notifications**
- Firebase Cloud Messaging

**Offline Sync**
- WorkManager

**Version Control**
- Git
- GitHub

**REST API Architecture**
Android Application
        ↓
REST API
        ↓
Business Logic Layer
        ↓
Database

**Main Endpoints**

**Authentication**
POST /api/auth/login
POST /api/auth/register

**Books**
GET /api/books
GET /api/books/{id}

**Reviews**
GET /api/books/{id}/reviews
POST /api/reviews
PUT /api/reviews/{id}
DELETE /api/reviews/{id}

**Reservations**
POST /api/reservations
GET /api/reservations

**Database Schema**
**User**
userId : Int
name : String
email : String
role : String

**Book**
bookId : Int
title : String
author : String
isbn : String
category : String
availableCopies : Int

**Review**
reviewId : Int
bookId : Int
userId : String
rating : Int
comment : String
synced : Boolean


**Reservation**
reservationId : Int
bookId : Int
userId : String
status : String

**Testing Strategy**
The project includes:

**Unit Tests**
- Login validation
- Book search functionality
- Review validation
- Repository operations

**Integration Tests**
- API communication
- Room database operations
- Offline synchronisation

**UI Testing**
- Navigation flows
- Form validation
- Authentication flow

**GitHub Actions CI/CD**
The repository uses GitHub Actions to automate:
- Project builds
- Unit testing
- Build verification
The automated workflow is triggered whenever new code changes are pushed to the repository.
Example workflow:
on:
  push:
    branches:
      - main
Automated validation processes verify that the application compiles and builds correctly in an environment independent of the developer's local machine.

**Screenshots**
Login Screen
Dashboard
Browse Books
Book Details
Review Screen
Offline Mode
Profile Screen
Settings Screen

**Version Control Workflow**
The project follows a structured Git workflow.
Example commit history:
Initial project setup
Implemented authentication
Added catalogue feature
Implemented review system
Added offline caching
Implemented synchronisation
Added notifications
Fixed validation issues
Prepared final release

**Final POE Release**
The final project release is tagged:
git tag -a FinalPOE -m "Final POE Submission"
git push origin FinalPOE
This tag identifies the final assessed submission.

**Contributors**
| **Contributor** | **Responsibilities** |
|---|---|
| Leeyah | Biometric Authentication, System Integration and DevOps |
| Saihil | Google Single Sign-On (SSO) and Offline Synchronisation |
| Kiaano | Settings Management, Multilingual Support and Notifications |
| Vianka | Documentation, Testing Coordination and Project Submission |

**References**
Android Developers. (2026). Android Developer Documentation. https://developer.android.com

Firebase. (2026). Firebase Authentication Documentation. https://firebase.google.com/docs/auth

Firebase. (2026). Firebase Cloud Messaging Documentation. https://firebase.google.com/docs/cloud-messaging

Google. (2026). Android Biometric Authentication Documentation. https://developer.android.com/training/sign-in/biometric-auth

JetBrains. (2026). Kotlin Documentation. https://kotlinlang.org/docs

Square. (2026). Retrofit Documentation. https://square.github.io/retrofit

Android Developers. (2026). Room Persistence Library Documentation. https://developer.android.com/training/data-storage/room

Android Developers. (2026). WorkManager Documentation. https://developer.android.com/topic/libraries/architecture/workmanager

**LibLink v1.0 - University Library Management System**
**Built with Kotlin, Jetpack Compose, Room, Retrofit, Firebase, and MVVM Architecture.**
