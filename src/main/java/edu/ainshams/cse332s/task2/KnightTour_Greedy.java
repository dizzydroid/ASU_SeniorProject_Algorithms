package edu.ainshams.cse332s.task2;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

class KnightTour_Greedy {
    int n;
    int no_of_moves = 0;
    int[][] board;
    int startX, startY;

    /*      0    (-1,-2)  0  (1,-2)    0
     *    (-2,-1)    0    0    0     (2,-1)
     *      0        0    h    0       0 
     *   (-2,1)      0    0    0     (2,1)
     *     0      (-1,2)  0  (1,2)    0
     */
    
    //options of moves
    private final int[][] options = {
        {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
        {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
    };

    
 // constructor 
    KnightTour_Greedy(int size) {
        this.n = size;
        board = new int[n][n];
    }
    
    
    
     //if n is even --> ( n/2 , n/2 ) 
    //if n is odd  --> ( 0 , 0 )
    public void initializeBoard() {
        for (int[] row : board) Arrays.fill(row, -1);
        if (n % 2 == 0) {
            startX = n / 2;
            startY = n / 2;
        } else {
            startX = 0;
            startY = 0;
        }
        board[startY][startX] = 0;
    }
    
    

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    
    
    public int getno_of_move() {
        return no_of_moves - 1;
    }

    boolean accept_point (int x, int y) {
        return (x >= 0 && y >= 0 && x < n && y < n);
    }

    
    //check if all cells empty & index valid
    boolean is_empty(int x, int y) {
        return accept_point(x, y) && board[y][x] == -1;
    }

    int getDegree(int x, int y) {
        int count = 0;
        for (int[] option : options) { //search in row
            int nx = x + option[0], ny = y + option[1];
            if (is_empty(nx, ny)) count++;
        }
        return count;
    }

    
    
    Cell nextMove(Cell cell) {
        int min_deg_idx = -1, c, min_deg = 9;
        int start = ThreadLocalRandom.current().nextInt(8);

        for (int count = 0; count < 8; ++count) {
            int i = (start + count) % 8;
            int nx = cell.x + options[i][0];
            int ny = cell.y + options[i][1];

            if (is_empty(nx, ny) && (c = getDegree(nx, ny)) < min_deg) {
                min_deg_idx = i;
                min_deg = c;
            }
        }

        if (min_deg_idx == -1) return null;

        int nx = cell.x + options[min_deg_idx][0];
        int ny = cell.y + options[min_deg_idx][1];
        board[ny][nx] = board[cell.y][cell.x] + 1;
        cell.x = nx;
        cell.y = ny;
        return cell;
    }

    void printBoard() {
        no_of_moves = 0;
        for (int[] row : board) {
            for (int cell : row) {
                no_of_moves++;
                System.out.printf("%3d ", cell);
            }
            System.out.println();
        }
    }

    
    boolean arrived(int x, int y, int xx, int yy) {
        for (int[] option : options) {
            if ((x + option[0] == xx) && (y + option[1] == yy)) return true;
        }
        return false;
    }

    // check if find close tour 
    boolean findClosedTour() {
        initializeBoard();
        Cell cell = new Cell(startX, startY);

        for (int i = 0; i < n * n - 1; ++i) {
            if ((cell = nextMove(cell)) == null)
                return false;
        }

        return arrived(cell.x, cell.y, startX, startY);
    }

    
    // check if find open tour 
    boolean findOpenTour() {
        initializeBoard();
        Cell cell = new Cell(startX, startY);

        for (int i = 0; i < n * n - 1; ++i) {
            if ((cell = nextMove(cell)) == null)
                return false;
        }

        return true;
    }

   
}




class Cell {
    int x, y;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
