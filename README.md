
# LibLink – University Library Application

[![Android CI](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml/badge.svg?branch=master)](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml)

[**View GitHub Actions – CI Workflow**](https://github.com/LeeyahR/LibLinkApp/actions)

## 1. Project Overview

LibLink is a group-developed Android application designed to provide users with convenient access to library-related functionality through a mobile platform.

The application is developed using Kotlin and XML in Android Studio and communicates with an ASP.NET Core Web API. The backend is developed using C# and uses Entity Framework Core to interact with a SQL Server database.

The project forms part of the PROG7314 Programming 3E Portfolio of Evidence.

## 2. Project Objectives

The main objectives of LibLink are to:

- Develop an Android application with a user-friendly interface.
- Provide access to library-related functionality through a mobile device.
- Connect the Android application to a backend API.
- Implement authentication and additional security features.
- Explore offline functionality and additional application services.
- Apply collaborative software development practices.
- Use automated builds to support development and integration.

## 3. Application Features

### 3.1 Core Application

- Android application developed using Kotlin and XML.
- User login and registration functionality.
- Home screen and application navigation.
- Book browsing and search functionality.
- Add Book functionality.
- User profile screen.
- Communication with the backend API.

### 3.2 Authentication and Security

- Biometric authentication using AndroidX Biometric.
- Google Sign-In integration using Google authentication and Firebase Authentication.
- HTTPS communication with the hosted backend API.

### 3.3 Additional Group Features

The following features form part of the team's allocated development responsibilities. Their implementation and integration status should be confirmed against the latest project version:

- Offline data access and synchronisation using RoomDB.
- Application settings.
- Multilingual support.
- Firebase Cloud Messaging notifications.

## 4. Technologies Used

| Technology | Purpose |
|---|---|
| Android Studio | Android development environment |
| Kotlin | Android application programming language |
| XML | Android user interface layouts |
| C# | Backend programming language |
| ASP.NET Core Web API | Backend services |
| Entity Framework Core | Backend database access |
| Microsoft SQL Server | Backend database |
| Retrofit | Android-to-API communication |
| Firebase Authentication | Authentication integration |
| Firebase Cloud Messaging | Push notification functionality |
| RoomDB | Local database functionality |
| Git | Source code version control |
| GitHub | Repository hosting and collaboration |
| GitHub Actions | Automated build workflow |

## 5. System Architecture

LibLink follows a client-server approach.

The Android application communicates with the backend API using Retrofit. The backend processes requests and uses Entity Framework Core to access the SQL Server database.

General communication flow:

```text
Android Application
        |
        | Retrofit / HTTPS
        |
        v
ASP.NET Core Web API
        |
        | Entity Framework Core
        |
        v
Microsoft SQL Server
```

Firebase services are also used for authentication integration and are intended to support notification functionality.

## 6. Git and GitHub Workflow

Git and GitHub are used to manage the source code and coordinate development between group members.

The project uses a feature-branch workflow to allow team members to work on separate features without directly changing the main project branch.

### 6.1 Branch Structure

- `master` – Main project branch containing integrated code.
- `feature/biometric-auth` – Feature branch used for developing biometric authentication.
- `feature/*` – Additional feature branches used by team members for separate development tasks.

### 6.2 Development Workflow

1. Team members obtain the latest changes from `master`.
2. A feature branch is created for the assigned task.
3. Development takes place on the feature branch.
4. Changes are committed and pushed to GitHub.
5. A Pull Request is created to merge the feature branch into `master`.
6. Changes are reviewed and integrated into the main project.

### 6.3 Example Git Commands

The following commands demonstrate the workflow used for the biometric authentication feature:

```bash
git checkout master
git pull origin master
git checkout feature/biometric-auth

git add .
git commit -m "Update biometric authentication"
git push origin feature/biometric-auth
```

## 7. Continuous Integration – GitHub Actions

GitHub Actions is used to automate the Android application build process.

The workflow performs the following tasks:

1. Checks out the repository source code.
2. Sets up the required Java environment.
3. Configures the Android build environment.
4. Builds the Android application.
5. Uploads the generated APK as a workflow artifact.

The workflow provides an automated way to check whether the application builds successfully and allows the generated APK to be accessed from GitHub Actions.

A successful workflow run and APK artifact can be viewed through the repository's Actions section.

**Useful links:**

- [GitHub Actions – All Workflow Runs](https://github.com/LeeyahR/LibLinkApp/actions)
- [Android CI Workflow](https://github.com/LeeyahR/LibLinkApp/actions/workflows/android.yml)

## 8. Project Repository

GitHub repository:

[https://github.com/LeeyahR/LibLinkApp](https://github.com/LeeyahR/LibLinkApp)

The repository contains the Android application source code, project files and development workflow.

## 9. Demonstration

A video demonstration of the LibLink application is included in the project's README and repository documentation.

**YouTube demonstration:** Retain the existing YouTube video URL here.

## 10. Team Contributions

| Contributor | Responsibilities |
|---|---|
| Leeyah Rambaran | Biometric authentication, system integration, Git and DevOps, CI/CD |
| Saihil Narine | Google Single Sign-On (SSO) and offline synchronisation |
| Kiaano Ramsamy | Settings management, multilingual support and Firebase Cloud Messaging |
| Vianka Mahabeer | Documentation, testing coordination, demonstration and project submission |

## 11. Project Status

LibLink was developed as a collaborative academic software development project.

The repository reflects the team's implementation and integration work. Individual features may have different completion and testing statuses depending on their development progress.

The final project should be evaluated using the functionality available in the submitted application and the evidence provided in the Portfolio of Evidence.

---

**LibLink – PROG7314 Programming 3E**

Developed using Kotlin, XML, ASP.NET Core Web API, C#, Entity Framework Core and Microsoft SQL Server.
