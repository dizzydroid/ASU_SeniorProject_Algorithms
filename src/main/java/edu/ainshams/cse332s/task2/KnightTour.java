package edu.ainshams.cse332s.task2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
public class KnightTour {
    private final int n;
    private int startX, startY;
    private final int[][] board;
    private int no_of_move=0;
    private final Random random = new Random();
    /*      0    (-1,-2)  0  (1,-2)    0
     *    (-2,-1)    0    0    0     (2,-1)
     *      0        0    h    0       0 
     *   (-2,1)      0    0    0     (2,1)
     *     0      (-1,2)  0  (1,2)    0
     */
    private final int[][] options = {
        {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
        {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
    };

    
    
    public KnightTour(int size) {
        this.n = size;
        this.board = new int[n][n];
    }

    public void initializeBoard() {
        for (int[] row : board) Arrays.fill(row, -1);
        if (n % 2 == 0) {
            startX = n / 2;
            startY = n / 2;
        } else {
            startX = 0;
            startY = 0;
        }
        board[startX][startY] = 0;
    }

    public void resetBoard() {
        for (int[] row : board) Arrays.fill(row, -1);
        board[startX][startY] = 0;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    
    
    public int getno_of_move() {
        return no_of_move;
    }

    
    public boolean knightClosedTour(int x, int y, int moveCount) {
        if (moveCount == n * n) {
        	no_of_move=moveCount-1;
            return isOneKnightMoveAway(x, y, startX, startY);
        }

        List<Move> nextMoves = getNextMoves(x, y, true, moveCount);
        for (Move move : nextMoves) {
            board[move.x][move.y] = moveCount;
            if (knightClosedTour(move.x, move.y, moveCount + 1)) return true;
            board[move.x][move.y] = -1;
        }
        return false;
    }
    
    
    

    public boolean knightOpenTour(int x, int y, int moveCount) {
        if (moveCount == n * n) {
        	no_of_move=moveCount-1;
        	return true;}

        List<Move> nextMoves = getNextMoves(x, y, false, moveCount);
        for (Move move : nextMoves) {
            board[move.x][move.y] = moveCount;
            if (knightOpenTour(move.x, move.y, moveCount + 1)) return true;
            board[move.x][move.y] = -1;
        }
        return false;
    }

    private List<Move> getNextMoves(int x, int y, boolean closed, int moveCount) {
        List<Move> moves = new ArrayList<>();
        for (int[] option : options) {
            int nx = x + option[0];
            int ny = y + option[1];
            if (isSafe(nx, ny)) {
                if (closed && moveCount == n * n - 1 && !isOneKnightMoveAway(nx, ny, startX, startY)) continue;
                int degree = countPossibleMoves(nx, ny);
                moves.add(new Move(nx, ny, degree));
            }
        }
        moves.sort(Comparator.comparingInt(m -> m.degree));
        shuffleSameDegreeMoves(moves);
        return moves;
    }

    
    
    
    
    public int countVisitedCells() {
        int count = 0;
        for (int[] row : board)
            for (int cell : row)
                if (cell != -1) count++;
        return count;
    }
    
    

    public void printBoard() {
        System.out.println("Board state:");
        for (int[] row : board) {
            for (int cell : row)
                System.out.printf("%3d ", cell);
            System.out.println();
        }
    }

    
    
    private boolean isSafe(int x, int y) {
        return (x >= 0 && y >= 0 && x < n && y < n && board[x][y] == -1);
    }
    
    

    private int countPossibleMoves(int x, int y) {
        int count = 0;
        for (int[] option : options) {
            int nx = x + option[0];
            int ny = y + option[1];
            if (isSafe(nx, ny)) count++;
        }
        return count;
    }

    private boolean isOneKnightMoveAway(int x1, int y1, int x2, int y2) {
        for (int[] option : options)
            if (x1 + option[0] == x2 && y1 + option[1] == y2) return true;
        return false;
    }

    private void shuffleSameDegreeMoves(List<Move> moves) {
        int i = 0;
        while (i < moves.size()) {
            int j = i;
            while (j < moves.size() && moves.get(j).degree == moves.get(i).degree) j++;
            Collections.shuffle(moves.subList(i, j), random);
            i = j;
        }
    }

    static class Move {
        int x, y, degree;
        Move(int x, int y, int degree) {
            this.x = x;
            this.y = y;
            this.degree = degree;
        }
    }
}
