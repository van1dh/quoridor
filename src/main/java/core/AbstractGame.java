package core;

import core.errors.IllegalMoveException;
import java.util.*;

public abstract class AbstractGame implements Game {
    protected final Ruleset rules;
    protected Board board;
    protected final List<Player> players;
    protected Player current;

    protected AbstractGame(GameVariant variant, List<Player> players){
        this.rules = variant.ruleset();
        this.board = variant.initialBoard(players.size());
        this.players = List.copyOf(players);
        this.current = players.get(0);
    }

    @Override
    public boolean isTerminal(){ return rules.isTerminal(board); }

    @Override
    public String prompt(){ return "[" + current.name() + "]> "; }

    @Override
    public String handle(String line) throws IllegalMoveException {
        Move m = rules.parseMove(line, board, current);
        MoveResult r = rules.validateAndApply(board, m, current);
        this.board = r.newBoard();
        if(r.terminal()) return "Game Over. Winner=" + r.winner().map(Player::name).orElse("none");
        this.current = rules.nextPlayer(current, players, board);
        return m.describe();
    }

    @Override
    public void printHelp(){ System.out.println(rules.help()); }

    @Override
    public void printResult(){
        rules.winner(board).ifPresentOrElse(
                w -> System.out.println("Winner: " + w.name()),
                () -> System.out.println("Draw/No winner.")
        );
    }
}
