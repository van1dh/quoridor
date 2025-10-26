package variants.quoridor;

import core.*;

public final class QuoridorVariant implements GameVariant {
    private final int players;
    private final int wallsPerPlayer;
    private final int size;

    public QuoridorVariant(int players, int wallsPerPlayer, int size) {
        if (players != 2)
            throw new IllegalArgumentException("Only 2-player mode is supported");
        this.players = players;
        this.wallsPerPlayer = wallsPerPlayer;
        this.size = size;
    }

    @Override
    public Board initialBoard(int ignored) {
        return QuoridorRuleset.initialBoard(players, wallsPerPlayer, size);
    }

    @Override
    public Ruleset ruleset() {
        return new QuoridorRuleset();
    }

    @Override
    public String name() {
        return "Quoridor";
    }
}
