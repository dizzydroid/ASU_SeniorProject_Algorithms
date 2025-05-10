package edu.ainshams.cse332s.task2;


import java.util.*;

public class KnightTourSolver {

    public static void run_Greedy_task2(int n) {
        KnightTour_Greedy kt_greedy = new KnightTour_Greedy(n);
        kt_greedy.initializeBoard();

        long startTime = System.currentTimeMillis();

        if (n < 5) {
            if (n == 1) {
                System.out.println("Trivial case: 1 x 1 board has only one cell.");
            } else {
                System.out.println("No closed knight's tour exists for n = " + n);
                kt_greedy.printBoard();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Time Taken: " + (endTime - startTime) + " ms");
            return;
        }

        if (n % 2 == 0) {
            System.out.println("Finding a closed knight's tour.");
            while (!kt_greedy.findClosedTour()) {
                // retry until found
            }
        } else {
            System.out.println("Odd-sized board: closed knight's tour is impossible.");
            System.out.println("Finding an open knight's tour.");
            while (!kt_greedy.findOpenTour()) {
                // retry until found
            }
        }

        kt_greedy.printBoard();
        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("Minimum number of moves: " + kt_greedy.getno_of_move());
    }

    public static void run_Greedy_backtracking_task2(int n) {
        KnightTour solver = new KnightTour(n);
        solver.initializeBoard();

        System.out.println("Starting from position (" + solver.getStartX() + ", " + solver.getStartY() + ")");

        long startTime = System.currentTimeMillis();

        if (n < 5) {
            if (n == 1) {
                System.out.println("Trivial case: 1 x 1 board has only one cell.");
            } else {
                System.out.println("No closed knight's tour exists for n = " + n);
                solver.printBoard();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Time Taken: " + (endTime - startTime) + " ms");
            return;
        }

        if (n % 2 == 1) {
            System.out.println("Odd-sized board: closed knight's tour is impossible.");
            boolean allVisited = solver.knightOpenTour(solver.getStartX(), solver.getStartY(), 1);
            if (allVisited) {
                System.out.println("All cells visited (open tour)");
            } else {
                System.out.println("Could not visit all cells");
            }
            solver.printBoard();
        } else {
            if (solver.knightClosedTour(solver.getStartX(), solver.getStartY(), 1)) {
                System.out.println("A CLOSED knight's tour is found");
            } else {
                System.out.println("Closed tour failed");

                solver.resetBoard();
                boolean allVisited = solver.knightOpenTour(solver.getStartX(), solver.getStartY(), 1);
                if (allVisited) {
                    System.out.println("All cells visited (open tour)");
                } else {
                    System.out.println("Could not visit all cells");
                }
            }
            solver.printBoard();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("Minimum number of moves the chess knight needs: " + solver.getno_of_move());
    }

    public static void run(Scanner scanner) {
        System.out.print("Enter board size n: ");
        int n = Integer.parseInt(scanner.nextLine());
        
        System.out.println("Using Greedy algorithm only:");
        run_Greedy_task2(n);
        
        System.out.println("----------------------------------------");
        
        System.out.println("Using Greedy algorithm and Backtracking:");
        run_Greedy_backtracking_task2(n);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter board size n : ");
        int n = input.nextInt();

        System.out.println("Using Greedy algorithm only:");
        run_Greedy_task2(n);

        System.out.println("----------------------------------------");

        System.out.println("Using Greedy algorithm and Backtracking:");
        run_Greedy_backtracking_task2(n);

        input.close();
    }
}
