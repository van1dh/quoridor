package variants.quoridor;

import core.Move;

public record PlaceWall(int playerId, Wall wall) implements Move {
    @Override
    public String describe() {
        return "wall " + wall.o + " " + wall.x + " " + wall.y;
    }
}
