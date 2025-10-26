package core;

import java.util.Optional;

public record MoveResult(Board newBoard, Optional<Player> winner, boolean terminal) {}