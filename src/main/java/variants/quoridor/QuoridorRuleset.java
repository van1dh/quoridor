package variants.quoridor;

import core.*;
import core.errors.IllegalMoveException;

import java.util.*;

public final class QuoridorRuleset implements Ruleset {

    public QuoridorRuleset() {
    }

    public static QuoridorBoard initialBoard(int players, int wallsPerPlayer, int size) {
        QuoridorBoard b = new QuoridorBoard(players, size);
        int mid = size / 2;
        b.setPawn(0, new Pos(mid, 0));
        b.setPawn(1, new Pos(mid, size - 1));
        for (int i = 0; i < players; i++) {
            b.setWallsLeft(i, wallsPerPlayer);
        }
        return b;
    }


    @Override
    public Move parseMove(String in, Board B, Player p) {
        String[] s = in.trim().split("\\s+");
        if (s.length == 3 && s[0].equalsIgnoreCase("move")) {
            return new PawnStep(p.id(), new Pos(Integer.parseInt(s[1]), Integer.parseInt(s[2])));
        }
        if (s.length == 4 && s[0].equalsIgnoreCase("wall")) {
            Orientation o = Orientation.valueOf(s[1].toUpperCase());
            return new PlaceWall(p.id(), new Wall(Integer.parseInt(s[2]), Integer.parseInt(s[3]), o));
        }
        throw new IllegalArgumentException("用法: move x y | wall H|V x y");
    }

    @Override
    public MoveResult validateAndApply(Board B, Move M, Player p) throws IllegalMoveException {
        QuoridorBoard b = (QuoridorBoard) B;
        if (M instanceof PawnStep ps) return applyPawn(b, ps, p);
        if (M instanceof PlaceWall pw) return applyWall(b, pw, p);
        throw new IllegalMoveException("Unknown move");
    }

    private MoveResult applyPawn(QuoridorBoard b, PawnStep ps, Player player) throws IllegalMoveException {
        int id = player.id();
        Set<Pos> legal = legalPawnDestinations(b, id);
        if (!legal.contains(ps.to())) throw new IllegalMoveException("Illegal move");
        QuoridorBoard nb = (QuoridorBoard) b.copy();
        nb.setPawn(id, ps.to());
        Optional<Player> w = winner(nb);
        return new MoveResult(nb, w, w.isPresent());
    }

    private MoveResult applyWall(QuoridorBoard b, PlaceWall pw, Player player) throws IllegalMoveException {
        int id = player.id();
        if (b.getWallsLeft(id) <= 0) throw new IllegalMoveException("No walls left");
        if (!canPlaceWall(b, pw.wall())) throw new IllegalMoveException("Illegal place wall");
        QuoridorBoard nb = (QuoridorBoard) b.copy();
        placeWallInPlace(nb, pw.wall());
        nb.decWallsLeft(id);
        return new MoveResult(nb, Optional.empty(), false);
    }

    @Override
    public boolean isTerminal(Board B) {
        return winner(B).isPresent();
    }

    @Override
    public Optional<Player> winner(Board B) {
        QuoridorBoard b = (QuoridorBoard) B;
        Pos p0 = b.getPawn(0), p1 = b.getPawn(1);
        if (p0.y() == b.N - 1) return Optional.of(new Player(0, "P1"));
        if (p1.y() == 0) return Optional.of(new Player(1, "P2"));
        return Optional.empty();
    }

    @Override
    public Player nextPlayer(Player cur, List<Player> ps, Board b) {
        return ps.get((cur.id() + 1) % ps.size());
    }

    @Override
    public String help() {
        return "Quoridor: move x y | wall H|V x y  (0-based). Blocking all paths is not allowed.";
    }

    Set<Pos> legalPawnDestinations(QuoridorBoard b, int pid) {
        Set<Pos> out = new HashSet<>();
        Pos me = b.getPawn(pid), opp = b.getPawn(1 - pid);
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] d : dirs) {
            int nx = me.x() + d[0], ny = me.y() + d[1];
            if (!inBoard(b, nx, ny)) continue;
            if (!edgeOpen(b, me.x(), me.y(), nx, ny)) continue;
            Pos n = new Pos(nx, ny);
            if (!n.equals(opp)) {
                out.add(n);
            } else {
                int jx = nx + d[0], jy = ny + d[1];
                if (inBoard(b, jx, jy) && edgeOpen(b, nx, ny, jx, jy)) {
                    out.add(new Pos(jx, jy));
                } else {
                    int[][] diags = (d[0] == 0) ? new int[][]{{1, 0}, {-1, 0}} : new int[][]{{0, 1}, {0, -1}};
                    for (int[] dd : diags) {
                        int dx = nx + dd[0], dy = ny + dd[1];
                        if (inBoard(b, dx, dy) && edgeOpen(b, nx, ny, dx, dy))
                            out.add(new Pos(dx, dy));
                    }
                }
            }
        }
        return out;
    }

    private boolean inBoard(QuoridorBoard b, int x, int y) {
        return 0 <= x && x < b.N && 0 <= y && y < b.N;
    }

    private boolean edgeOpen(QuoridorBoard b, int x1, int y1, int x2, int y2) {
        if (x1 == x2 && Math.abs(y1 - y2) == 1) {
            int y = Math.min(y1, y2);
            return !(x1 < b.N - 1 && y < b.N - 1 && b.hasH(x1, y));
        }
        if (y1 == y2 && Math.abs(x1 - x2) == 1) {
            int x = Math.min(x1, x2);
            return !(x < b.N - 1 && y1 < b.N - 1 && b.hasV(x, y1));
        }
        return false;
    }

    private boolean canPlaceWall(QuoridorBoard b, Wall w) {
        if (!wallInBounds(b, w)) return false;
        if (conflicts(b, w)) return false;
        QuoridorBoard nb = (QuoridorBoard) b.copy();
        placeWallInPlace(nb, w);
        return hasPath(nb, 0) && hasPath(nb, 1);
    }

    private boolean wallInBounds(QuoridorBoard b, Wall w) {
        return 0 <= w.x && w.x < b.N - 1 && 0 <= w.y && w.y < b.N - 1;
    }

    private boolean conflicts(QuoridorBoard b, Wall w) {
        if (w.o == Orientation.H) {
            if (b.hasH(w.x, w.y)) return true;
            if (w.x < b.N - 2 && b.hasH(w.x + 1, w.y)) return false;
            if (b.hasV(w.x, w.y)) return true;
            return w.x > 0 && b.hasV(w.x - 1, w.y);
        } else {
            if (b.hasV(w.x, w.y)) return true;
            if (w.y < b.N - 2 && b.hasV(w.x, w.y + 1)) return false;
            if (b.hasH(w.x, w.y)) return true;
            return w.y > 0 && b.hasH(w.x, w.y - 1);
        }
    }



    private void placeWallInPlace(QuoridorBoard b, Wall w) {
        if (w.o == Orientation.H) b.putH(w.x, w.y);
        else b.putV(w.x, w.y);
    }

    private boolean hasPath(QuoridorBoard b, int pid) {
        Pos s = b.getPawn(pid);
        boolean[][] vis = new boolean[b.N][b.N];
        ArrayDeque<Pos> q = new ArrayDeque<>();
        q.add(s);
        vis[s.x()][s.y()] = true;
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!q.isEmpty()) {
            Pos p = q.poll();
            if (goalFor(b, pid, p)) return true;
            for (int[] d : dirs) {
                int nx = p.x() + d[0], ny = p.y() + d[1];
                if (!inBoard(b, nx, ny) || vis[nx][ny]) continue;
                if (edgeOpen(b, p.x(), p.y(), nx, ny)) {
                    vis[nx][ny] = true;
                    q.add(new Pos(nx, ny));
                }
            }
        }
        return false;
    }

    private boolean goalFor(QuoridorBoard b, int pid, Pos p) {
        return (pid == 0 && p.y() == b.N - 1) || (pid == 1 && p.y() == 0);
    }
}
