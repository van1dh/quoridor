package variants.quoridor;

import core.*;
import core.errors.IllegalMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class QuoridorRulesetTest {

    private QuoridorBoard board;
    private QuoridorRuleset ruleset;
    private Player p1;
    private Player p2;

    @BeforeEach
    public void setup() {
        board = new QuoridorBoard(2, 9);
        board.setPawn(0, new Pos(4, 0));
        board.setPawn(1, new Pos(4, 8));

        // 手动设置墙数
        board.setWallsLeft(0, 10);
        board.setWallsLeft(1, 10);

        ruleset = new QuoridorRuleset();
        p1 = new Player(0, "P1");
        p2 = new Player(1, "P2");
    }

    @Test
    public void testValidPawnMoveForward() throws IllegalMoveException {
        Move move = new PawnStep(0, new Pos(4, 1));
        MoveResult result = ruleset.validateAndApply(board, move, p1);
        assertEquals(new Pos(4, 1), ((QuoridorBoard) result.newBoard()).getPawn(0));
    }

    @Test
    public void testIllegalPawnMoveBlocked() {
        Move move = new PawnStep(0, new Pos(4, 3)); // Too far
        assertThrows(IllegalMoveException.class, () -> ruleset.validateAndApply(board, move, p1));
    }

    @Test
    public void testPlaceValidWall() throws IllegalMoveException {
        Move move = new PlaceWall(0, new Wall(5, 5, Orientation.H));
        MoveResult result = ruleset.validateAndApply(board, move, p1);
        QuoridorBoard updated = (QuoridorBoard) result.newBoard();
        assertTrue(updated.hasH(5, 5));
        assertEquals(9, updated.getWallsLeft(0));
    }


    @Test
    public void testPlaceInvalidWallOverlap() throws IllegalMoveException {
        // Place first wall
        Move move1 = new PlaceWall(0, new Wall(5, 5, Orientation.H));
        MoveResult result1 = ruleset.validateAndApply(board, move1, p1);
        Board updatedBoard = result1.newBoard();

        // Try placing overlapping wall on updated board
        Move move2 = new PlaceWall(1, new Wall(5, 5, Orientation.H));
        assertThrows(IllegalMoveException.class, () -> ruleset.validateAndApply(updatedBoard, move2, p2));
    }


    @Test
    public void testWinCondition() {
        QuoridorBoard custom = QuoridorRuleset.initialBoard(2, 10, 9);
        custom.setPawn(0, new Pos(4, 8)); // Win position
        Optional<Player> winner = ruleset.winner(custom);
        assertTrue(winner.isPresent());
        assertEquals(0, winner.get().id());
    }

    @Test
    public void testSanity() {
        assertEquals(1 + 1, 2);
    }
    @Test
    public void testPawnJumpOverOpponent() throws IllegalMoveException {
        board.setPawn(0, new Pos(4, 4));
        board.setPawn(1, new Pos(4, 5));
        Move move = new PawnStep(0, new Pos(4, 6)); // Jump over
        MoveResult result = ruleset.validateAndApply(board, move, p1);
        assertEquals(new Pos(4, 6), ((QuoridorBoard) result.newBoard()).getPawn(0));
    }

    @Test
    public void testPawnDiagonalJumpWhenBlocked() throws IllegalMoveException {
        board.setPawn(0, new Pos(4, 4));
        board.setPawn(1, new Pos(4, 5));
        board.putH(4, 5); // Place horizontal wall behind opponent
        Move move = new PawnStep(0, new Pos(3, 5)); // Diagonal left
        MoveResult result = ruleset.validateAndApply(board, move, p1);
        assertEquals(new Pos(3, 5), ((QuoridorBoard) result.newBoard()).getPawn(0));
    }

    @Test
    public void testWallBlocksPathForOnePlayerOnly() {
        board.setPawn(0, new Pos(4, 0));
        board.setPawn(1, new Pos(4, 8));
        // Block P1 completely
        for (int y = 0; y < 8; y++) {
            board.putH(4, y);
        }
        board.putH(3, 7);  // Final block

        Move move = new PlaceWall(1, new Wall(3, 7, Orientation.H));
        assertThrows(IllegalMoveException.class, () -> ruleset.validateAndApply(board, move, p2));
    }

    @Test
    public void testLegalPawnDestinations() {
        board.setPawn(0, new Pos(4, 4));
        Set<Pos> legal = ruleset.legalPawnDestinations(board, 0);
        System.out.println("Legal destinations: " + legal);
        assertTrue(legal.contains(new Pos(4, 5)));
    }

}
