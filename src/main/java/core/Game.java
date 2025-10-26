package core;

import core.errors.IllegalMoveException;

public interface Game {
    boolean isTerminal();
    String prompt();
    String handle(String input) throws IllegalMoveException;
    void printHelp();
    void printResult();
}
