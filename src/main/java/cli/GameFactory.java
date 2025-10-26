
package cli; import core.*; import variants.quoridor.*; import java.util.*;
public final class GameFactory {
  public static Game create(CliArgs a){
    List<Player> ps = List.of(new Player(0,"P1"), new Player(1,"P2"));
    return new AbstractGame(new QuoridorVariant(2, a.wallsPerPlayer(), a.size()), ps){};
  }
}
