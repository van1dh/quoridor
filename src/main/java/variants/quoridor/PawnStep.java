package variants.quoridor;

import core.Move;

public record PawnStep(int playerId, Pos to) implements Move {
    @Override
    public String describe() {
        return "move " + to.x() + " " + to.y();
    }
}
