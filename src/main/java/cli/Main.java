
package cli; import core.*; import core.errors.IllegalMoveException; import java.util.*;
public final class Main {
  public static void main(String[] args){
    CliArgs a = CliArgs.from(args);
    Game game = GameFactory.create(a);
    game.printHelp();
    try(Scanner sc=new Scanner(System.in)){
      while(!game.isTerminal()){
        System.out.print(game.prompt());
        if(!sc.hasNextLine()) break; String line=sc.nextLine().trim();
        if(line.isEmpty()) continue;
        if(line.equalsIgnoreCase("quit")){ System.out.println("Bye!"); return; }
        if(line.equalsIgnoreCase("help")){ game.printHelp(); continue; }
        try{ System.out.println(game.handle(line)); }
        catch(IllegalMoveException ime){ System.out.println("Illegal move"+ime.getMessage()); }
        catch(Exception e){ System.out.println("Input error "+e.getMessage()); }
      }
      game.printResult();
    }
  }
}
