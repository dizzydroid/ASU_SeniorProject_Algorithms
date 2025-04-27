package edu.ainshams.cse332s.task1;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class TrominoTiling {
    private int[][] board;
    private int[][] trominoId;
    private int size;
    private int missingRow;
    private int missingCol;
    private List<Tromino> trominoes;
    private Scanner scanner;
    
    // Represents a single tromino piece that consists of 3 cells
    public static class Tromino {
        private int id;
        private List<int[]> cells;
        
        public Tromino(int id) {
            this.id = id;
            this.cells = new ArrayList<>();
        }
        
        public void addCell(int row, int col) {
            cells.add(new int[]{row, col});
        }
        
        public int getId() {
            return id;
        }
        
        public List<int[]> getCells() {
            return cells;
        }
    }
    
    public TrominoTiling() {
        scanner = new Scanner(System.in);
    }
    
    public TrominoTiling(int n, int missingRow, int missingCol) {
        this.size = (int) Math.pow(2, n);
        this.board = new int[size][size];
        this.trominoId = new int[size][size];
        this.missingRow = missingRow - 1;
        this.missingCol = missingCol - 1;
        this.trominoes = new ArrayList<>();
    }

    // Getters and setters
    
    public int[][] getBoard() {
        return board;
    }
    
    public void setBoard(int[][] board) {
        this.board = board;
    }
    
    public int[][] getTrominoId() {
        return trominoId;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getMissingRow() {
        return missingRow;
    }
    
    public int getMissingCol() {
        return missingCol;
    }
    
    public List<Tromino> getTrominoes() {
        return trominoes;
    }
    
    public void addTromino(Tromino tromino) {
        trominoes.add(tromino);
    }
    
    public void run() {
        System.out.print("Enter value of n (board will be 2^n x 2^n): ");
        int n = scanner.nextInt();

        System.out.print("Enter the missing row: ");
        int missingRow = scanner.nextInt();

        System.out.print("Enter the missing column: ");
        int missingCol = scanner.nextInt();

        // Initialize the tiling
        this.size = (int) Math.pow(2, n);
        this.board = new int[size][size];
        this.trominoId = new int[size][size];
        this.missingRow = missingRow - 1;
        this.missingCol = missingCol - 1;
        this.trominoes = new ArrayList<>();

        TrominoTilingSolver solver = new TrominoTilingSolver();
        solver.solve(this);   
        printBoard();
    }
    
    //Check if position is within board boundaries
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    
    public void printBoard() {
        String reset = "\u001B[0m";
        String[] colors = {
            "\u001B[40m", // 0 - Black for missing tile
            "\u001B[41m", // 1 - Red
            "\u001B[42m", // 2 - Green
            "\u001B[44m"  // 3 - Blue
        };
    
        for (int[] row : board) {
            for (int cell : row) {
                int colorIndex = Math.min(Math.max(cell, 0), colors.length - 1);
                System.out.print(colors[colorIndex] + "  " + reset);
            }
            System.out.println();
        }
        
        System.out.println("\nTotal trominoes placed: " + trominoes.size());
    }
    
    
    public static void main(String[] args) {
        TrominoTiling trominoGame = new TrominoTiling();
        trominoGame.run();
    }
}