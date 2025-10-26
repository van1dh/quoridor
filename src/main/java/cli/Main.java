
package cli;

import core.*;
import core.errors.IllegalMoveException;
import variants.quoridor.*;

import java.util.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("Welcome to Quoridor (CLI Mode)");
    Scanner scanner = new Scanner(System.in);

    QuoridorRuleset ruleset = new QuoridorRuleset();
    QuoridorBoard board = QuoridorRuleset.initialBoard(2, 10, 9);
    Player[] players = new Player[] {
            new Player(0, "P1"),
            new Player(1, "P2")
    };

    int currentPlayer = 0;

    while (true) {
      Player player = players[currentPlayer];
      System.out.println(player.name() + ", your move (step x y OR wall x y H/V OR resign OR quit): ");
      board.printBoard();

      String line;
      try {
        line = scanner.nextLine().trim();
      } catch (NoSuchElementException e) {
        System.out.println("No input found. Exiting...");
        break;
      }

      if (line.equalsIgnoreCase("quit")) {
        System.out.println("Game terminated.");
        break;
      } else if (line.equalsIgnoreCase("resign")) {
        System.out.println(player.name() + " resigned. " + players[1 - currentPlayer].name() + " wins!");
        break;
      }

      try {
        Move move = parseMove(line, player.id());
        MoveResult result = ruleset.validateAndApply(board, move, player);
        board = (QuoridorBoard) result.newBoard();

        if (result.winner().isPresent()) {
          board.printBoard();
          System.out.println("Game Over! Winner: " + result.winner().get().name());
          break;
        }

        currentPlayer = 1 - currentPlayer;
      } catch (IllegalArgumentException | IllegalMoveException e) {
        System.out.println("Invalid move: " + e.getMessage());
      }
    }

    scanner.close();
  }

  private static Move parseMove(String input, int playerId) {
    String[] tokens = input.trim().split("\\s+");
    if (tokens.length == 3 && tokens[0].equalsIgnoreCase("step")) {
      int x = Integer.parseInt(tokens[1]);
      int y = Integer.parseInt(tokens[2]);
      return new PawnStep(playerId, new Pos(x, y));
    } else if (tokens.length == 4 && tokens[0].equalsIgnoreCase("wall")) {
      int x = Integer.parseInt(tokens[1]);
      int y = Integer.parseInt(tokens[2]);
      Orientation o = tokens[3].equalsIgnoreCase("H") ? Orientation.H : Orientation.V;
      return new PlaceWall(playerId, new Wall(x, y, o));
    } else {
      throw new IllegalArgumentException("Unknown move format.");
    }
  }
}

