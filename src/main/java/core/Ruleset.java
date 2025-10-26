package core;

import core.errors.IllegalMoveException;
import java.util.*;

public interface Ruleset {
    Move parseMove(String input, Board board, Player player) throws IllegalArgumentException;

    MoveResult validateAndApply(Board board, Move move, Player player) throws IllegalMoveException;

    boolean isTerminal(Board board);

    Optional<Player> winner(Board board);

    Player nextPlayer(Player current, List<Player> players, Board board);

    String help();
}
