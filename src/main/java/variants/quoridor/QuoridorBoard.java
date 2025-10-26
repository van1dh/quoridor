package variants.quoridor;

import core.Board;

import java.util.Arrays;

public final class QuoridorBoard implements Board {
    final int N;
    private final Pos[] pawnPos;
    private final int[] wallsLeft;
    private final boolean[][] hWalls;
    private final boolean[][] vWalls;

    QuoridorBoard(int players, int size) {
        this.N = size;
        this.pawnPos = new Pos[players];
        this.wallsLeft = new int[players];
        this.hWalls = new boolean[N - 1][N - 1];
        this.vWalls = new boolean[N - 1][N - 1];

        int totalWalls = 20;
        int wallsPerPlayer = totalWalls / players;
        for (int i = 0; i < players; i++) {
            this.wallsLeft[i] = wallsPerPlayer;
        }    }

    public void printBoard() {
        int N = this.size();

        // Print column headers
        System.out.print("   ");
        for (int x = 0; x < N; x++) {
            System.out.print(x + " ");
        }
        System.out.println();

        for (int y = 0; y < N; y++) {
            // Print row number
            System.out.print(y + "  ");

            // Print pawn positions and vertical walls
            for (int x = 0; x < N; x++) {
                boolean hasP1 = this.getPawn(0).equals(new Pos(x, y));
                boolean hasP2 = this.getPawn(1).equals(new Pos(x, y));

                if (hasP1) System.out.print("1");
                else if (hasP2) System.out.print("2");
                else System.out.print(".");

                // Print vertical wall between cells (x,y) and (x+1,y)
                if (x < N - 1) {
                    System.out.print(this.hasV(x, y) ? "|" : " ");
                }
            }
            System.out.println();

            // Print horizontal walls below this row (only up to N-2)
            if (y < N - 1) {
                System.out.print("   ");
                for (int x = 0; x < N; x++) {
                    if (x < N - 1) {
                        System.out.print(this.hasH(x, y) ? "--" : "  ");
                    }
                }
                System.out.println();
            }
        }

        // Print remaining walls per player
        System.out.println("\nWalls left: P1=" + this.getWallsLeft(0) + ", P2=" + this.getWallsLeft(1));
    }

    private QuoridorBoard(int N, Pos[] pp, int[] wl, boolean[][] h, boolean[][] v) {
        this.N = N;
        this.pawnPos = pp;
        this.wallsLeft = wl;
        this.hWalls = h;
        this.vWalls = v;
    }

    @Override
    public Board copy() {
        Pos[] pp = Arrays.copyOf(pawnPos, pawnPos.length);
        int[] wl = Arrays.copyOf(wallsLeft, wallsLeft.length);
        boolean[][] h = new boolean[N - 1][N - 1];
        boolean[][] v = new boolean[N - 1][N - 1];
        for (int i = 0; i < N - 1; i++) {
            System.arraycopy(hWalls[i], 0, h[i], 0, N - 1);
            System.arraycopy(vWalls[i], 0, v[i], 0, N - 1);
        }
        return new QuoridorBoard(N, pp, wl, h, v);
    }

    // Getters and setters
    public int size() { return N; }

    Pos getPawn(int pid) { return pawnPos[pid]; }

    void setPawn(int pid, Pos p) { pawnPos[pid] = p; }

    int getWallsLeft(int pid) { return wallsLeft[pid]; }

    void decWallsLeft(int pid) { wallsLeft[pid]--; }

    public boolean hasH(int x, int y) {
        if (x < 0 || x >= N || y < 0 || y >= N - 1) return false;
        if (x >= N - 1) return false;
        return hWalls[x][y];
    }


    public boolean hasV(int x, int y) {
        if (x < 0 || x >= N - 1 || y < 0 || y >= N) return false;
        if (y >= N - 1) return false;
        return vWalls[x][y];
    }

    void putH(int x, int y) { hWalls[x][y] = true; }

    void putV(int x, int y) { vWalls[x][y] = true; }

    public void setWallsLeft(int pid, int count) {
        wallsLeft[pid] = count;
    }
}
