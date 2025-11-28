# Bowling Game Scorer (Multi-Player)

A simple multi-player bowling score tracker built using **Spring Boot** and **Thymeleaf**. The app lets users add players, submit rolls, view scores, and track a live leaderboard—all managed in-memory.

## Features

- Add, delete, and reset players
- Submit rolls and track scores based on standard bowling rules
- Detect strikes and spares
- Auto-rotate turns between players
- Live leaderboard sorted by score

## Technologies Used

- **Java 17+**
- **Spring Boot**
- **Thymeleaf (HTML templating)**
- **In-memory state** using `Map` and `List`
- **Basic HTML/CSS** for layout

## Architecture

The app uses a standard **MVC (Model-View-Controller)** structure:

- **Controller (`BowlingController`)**: Manages players, routes requests, and updates the model.
- **Model (`BowlingGame`)**: Contains the scoring logic based on bowling rules.
- **View (`index.html`)**: Thymeleaf template renders the UI and updates dynamically with user interactions.

## How to Run

### Prerequisites

- Java 17
- Gradle

### Using Maven

```bash
# Clone the repository
git clone https://github.com/your-username/bowling-game.git
cd bowling-game

# Build and run the app
./gradlew bootRun