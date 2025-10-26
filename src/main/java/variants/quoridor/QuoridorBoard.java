package variants.quoridor;

import core.Board;

import java.util.Arrays;

final class QuoridorBoard implements Board {
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
    Pos getPawn(int pid) { return pawnPos[pid]; }

    void setPawn(int pid, Pos p) { pawnPos[pid] = p; }

    int getWallsLeft(int pid) { return wallsLeft[pid]; }

    void decWallsLeft(int pid) { wallsLeft[pid]--; }

    boolean hasH(int x, int y) { return hWalls[x][y]; }

    boolean hasV(int x, int y) { return vWalls[x][y]; }

    void putH(int x, int y) { hWalls[x][y] = true; }

    void putV(int x, int y) { vWalls[x][y] = true; }

    public void setWallsLeft(int pid, int count) {
        wallsLeft[pid] = count;
    }
}
