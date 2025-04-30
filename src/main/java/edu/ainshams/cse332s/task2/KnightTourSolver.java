package edu.ainshams.cse332s.task2;

import java.util.*;

public class KnightTourSolver {

    public static void run_task2(int n) {
        KnightTour solver = new KnightTour(n);
        solver.initializeBoard();

        System.out.println("Starting from position (" + solver.getStartX() + ", " + solver.getStartY() + ")");

        long startTime = System.currentTimeMillis();

        if (n < 5) {
            System.out.println("For n < 5, using special handling...");
            if (n == 1) {
                System.out.println("Trivial case: 1×1 board has only one cell.");
                solver.printBoard();
            }
            // Early exit for boards too small
            long endTime = System.currentTimeMillis();
            System.out.println("Time Taken: " + (endTime - startTime) + " ms");
            return;
        }

        if (n % 2 == 1) {
            System.out.println("Odd-sized board: closed knight's tour is impossible.");
         

            boolean allVisited = solver.knightOpenTour(solver.getStartX(), solver.getStartY(), 1);
            if (allVisited) {
                System.out.println(" All cells visited (open tour)");
            } else {
                System.out.println(" Could not visit all cells");
            }
            solver.printBoard();

        } else {
            if (solver.knightClosedTour(solver.getStartX(), solver.getStartY(), 1)) {
                System.out.println("A CLOSED knight's tour is found");
            } else {
                System.out.println(" Closed tour failed");

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
}


/* main 
import java.util.Scanner;
@SuppressWarnings("unused")
public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter board size n : ");
        int n = sc.nextInt();
        KnightTourSolver . run_task2(n);  // Calls the runner
    }
}
*/