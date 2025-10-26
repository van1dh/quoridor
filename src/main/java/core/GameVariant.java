package core;

public interface GameVariant {
    Board initialBoard(int players);

    Ruleset ruleset();

    String name();
}
