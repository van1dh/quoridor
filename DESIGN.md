
# Design Documentation

## 1. High-Level Architecture

This project follows a **modular MVC-like design**, separating game logic, rule validation, board state, and user interaction.

### Core Components:

| Component         | Description                                                       |
|------------------|-------------------------------------------------------------------|
| `Board`          | Interface for representing game state (positions, walls, etc.)    |
| `Move`           | Represents a player action (e.g., moving pawn, placing wall)      |
| `Player`         | Represents each player (id, name)                                 |
| `Ruleset`        | Validates and applies moves, enforces turn-based rules            |
| `MoveResult`     | Encapsulates result of a move (updated board, winner, terminal)   |

### Variant Layer:

The `variants.quoridor` package defines Quoridor-specific implementations:
- `QuoridorBoard`
- `QuoridorRuleset`
- `PlaceWall`, `PawnStep`, `Wall`, `Orientation`
- `Conflicts` (used to prevent illegal wall placements)

---

## 2. Extensibility and Scalability

### Design for Variants

The key to extensibility lies in **abstracting turn-based behavior** via the `Ruleset` and `Board` interfaces:

- **New turn-based games** can be added by implementing:
  - a new `Board` type
  - a new `Ruleset` logic
  - game-specific `Move` classes

For example, to implement a variant like **TicTacToe** or **Chess**, only game-specific logic is needed without modifying core logic.

### Plug-and-Play Ruleset

The CLI driver reads a single `Ruleset` object and executes game turns via:
```java
MoveResult result = ruleset.validateAndApply(board, move, player);
```
This enables hot-swapping different rulesets with minimal code change.

3. Refactorings Made for Scalability
Originally, Quoridor game logic was hardcoded in procedural functions. We refactored by:

- Extracting MoveResult to capture side effects cleanly.

- Encapsulating board state in QuoridorBoard, with clear wall and pawn APIs.

- Introducing Conflicts class to centralize wall conflict logic.

- Moving printBoard() into the board itself for encapsulated rendering.

These changes allow new variants (e.g., turn-based grid games, two-player duels) to be added quickly without modifying existing files.

## 4. Evaluation of Starting Designs
I evaluated three options:

| Design Approach | Pros | Cons                             |                                         
|------------------------------|----------------------------------------|----------------------------------|
| Flat procedural|	Fast to implement | 	Difficult to test, not scalable | 
| Strategy pattern|	Easy to swap logic | 	Too complex for simple games    |
| Interface + Variant Packages | Clean separation, extensible, testable | Slightly verbose setup           |

I chose Design 3 for its long-term flexibility. It also mirrors typical engine-based architectures used in professional game engines.

## 5. Partner Contributions
Team Member	Contribution Areas
Yongyi Xie All stuffs

## 6. Testing Strategy
I used JUnit 5 for unit testing:

QuoridorRulesetTest.java: covers valid/invalid moves, wall placement, and win condition.

Future variants can re-use the same testing structure by extending Board and Ruleset.

To run:

```bash
./gradlew test
```
## 7. Conclusion
My architecture successfully supports modular turn-based game development. Adding new games (e.g., Chess, Checkers, Hex) requires minimal effort.