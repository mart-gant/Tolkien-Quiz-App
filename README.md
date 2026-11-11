# Tolkien Quiz App

A native Android quiz application focused on the world of J.R.R. Tolkien.

The project was created as a practical Android/Kotlin project to explore modern Android development, clean separation of responsibilities, dependency injection, local data persistence, and Jetpack Compose UI.

## Features

* Multiple-choice questions based on Tolkien's legendarium
* Questions grouped into categories such as:

  * Lore
  * Language
  * History
  * Geography
  * Characters
  * Creatures
* Immediate answer validation
* Score tracking
* Local question data
* Modern declarative UI built with Jetpack Compose
* Separation of presentation, domain, and data layers
* Dependency injection with Hilt
* Local persistence with Room
* Preferences storage with DataStore

## Screenshots

*Add screenshots of the application here.*

## Architecture

The application is organized into three main modules:

```text
Tolkien-Quiz-App
│
├── presentation
│   └── Android application and UI
│
├── domain
│   └── Business logic and domain models
│
├── data
│   └── Data sources, repositories and persistence
│
└── questions.json
    └── Quiz question data
```

### Presentation

The `presentation` module contains the Android application and user interface.

It uses:

* Jetpack Compose
* Material 3
* ViewModel
* Kotlin Coroutines
* Hilt for dependency injection

### Domain

The `domain` module contains the core business logic and domain models.

Keeping this layer independent from Android-specific implementation details makes the application easier to test and maintain.

### Data

The `data` module is responsible for providing and persisting application data.

It uses:

* Room
* DataStore Preferences
* Retrofit
* Moshi
* OkHttp

The data layer depends on the domain layer, while the presentation layer consumes both the domain and data modules.

## Tech Stack

| Technology        | Purpose                           |
| ----------------- | --------------------------------- |
| Kotlin            | Primary programming language      |
| Android           | Target platform                   |
| Jetpack Compose   | Declarative UI                    |
| Material 3        | UI components and design system   |
| Android ViewModel | UI state and lifecycle management |
| Kotlin Coroutines | Asynchronous operations           |
| Hilt              | Dependency injection              |
| Room              | Local database                    |
| DataStore         | Persistent preferences            |
| Retrofit          | HTTP client                       |
| Moshi             | JSON serialization                |
| OkHttp            | HTTP networking                   |
| Gradle            | Build system                      |

The project currently targets Android API 34, supports Android API 21 and above, and uses Java/Kotlin JVM target 17.

## Project Structure

```text
presentation/
├── UI
├── ViewModels
└── Android application layer

domain/
├── Models
├── Use cases
└── Repository contracts

data/
├── Repository implementations
├── Local data sources
├── Room
├── DataStore
└── Network layer
```

The project is configured as a multi-module Gradle project with separate `presentation`, `data`, and `domain` modules.

## Question Data

Quiz questions are stored as structured JSON data.

Each question contains:

```json
{
  "text": "What is the translation of 'Mellon' from Sindarin?",
  "options": [
    "Friend",
    "King",
    "Gate",
    "Star"
  ],
  "correctAnswerIndex": 0,
  "category": "Language"
}
```

The current question set covers several aspects of Tolkien's world, including lore, languages, history, geography, characters, and creatures.

## Getting Started

### Requirements

Before running the project, make sure you have:

* Android Studio
* JDK 17
* Android SDK 34
* An Android emulator or a physical Android device

### Clone the repository

```bash
git clone https://github.com/mart-gant/Tolkien-Quiz-App.git
cd Tolkien-Quiz-App
```

### Open the project

Open the project in Android Studio and allow Gradle to synchronize the dependencies.

### Run the application

Select an Android emulator or connected physical device and run the `presentation` module.

Alternatively, the project can be built from the command line:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

## Testing

The project includes unit and Android instrumentation testing dependencies.

Run the unit tests with:

```bash
./gradlew test
```

For Windows:

```powershell
.\gradlew.bat test
```

## Build Configuration

The application uses:

* Android Gradle Plugin 8.2.2
* Kotlin 1.9.22
* Hilt 2.50
* KSP
* Compile SDK 34
* Minimum SDK 21
* Java 17

Release builds are configured with code shrinking and resource shrinking enabled.

## What I Practiced

This project was developed to strengthen practical skills in:

* Kotlin and modern Android development
* Jetpack Compose
* Multi-module project organization
* Separation of concerns
* Clean architecture principles
* Dependency injection
* Repository pattern
* Local data persistence
* JSON data modelling
* Asynchronous programming with Kotlin Coroutines
* Unit and instrumentation testing
* Gradle-based Android builds

## Future Improvements

Possible future improvements include:

* More questions and categories
* Difficulty levels
* Randomized question order
* Persistent high scores
* Improved accessibility
* Dark/light theme customization
* More comprehensive test coverage
* Improved UI animations and feedback
* Localization
* Offline-first quiz experience

## License

This project is intended as a personal learning and portfolio project.

Please check the repository for the applicable license information.

## Author

**Marcin Gantkowski**

GitHub: [mart-gant](https://github.com/mart-gant)

---

> This project is not affiliated with or endorsed by J.R.R. Tolkien, his estate, or the rights holders of Tolkien's works.
