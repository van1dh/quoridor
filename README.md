# Quoridor: Turn-Based Game Variant

## Overview

This project implements the classic board game **Quoridor** in Java, following a modular and extensible architecture that supports multiple turn-based game variants.

The current implementation provides:
- Full game logic (movement, wall placement, win conditions)
- CLI interface with real-time board visualization
- Error handling for illegal moves
- Turn-based gameplay loop
- Extensibility for future game variants

## How to Compile and Run

Ensure you have **Java 17+** and **Gradle** installed.

### Clone the project:
```bash
git clone <your-repo-url>
cd <your-repo-directory>
```

### Run Tests:
```bash
./gradlew test
```

### Run the CLI game:
```bash
./gradlew run
```

## How to Play

Players take turns entering moves.

Valid commands include:

- step x y — Move pawn to position (x, y)

- wall x y H — Place a horizontal wall at (x, y)

- wall x y V — Place a vertical wall at (x, y)

- resign — Concede the match

- quit — Exit the game without finishing

## File Structure

| Path                 | Description                                  |
|----------------------|----------------------------------------------|
| `core/`              | Shared game logic (interfaces, core classes) |
| `variants/quoridor/` | Game-specific logic for Quoridor             |
| `cli/`               | Command-line interface                       |
| `test/`              | Unit tests (JUnit 5)                         |
| `build.gradle`       | Gradle build configuration                   |
| `README.md`          | Project overview and setup instructions      |
| `DESIGN.md`          | Design documentation                         |


## Author
Yongyi Xie xyy0208@bu.edu
