package edu.ainshams.cse332s.task6;

import java.util.List;
import java.util.Scanner;

public class LatticeCoverSolver {
    
    public static void run(Scanner scanner) {
        try {
            System.out.print("Enter the lattice size n: ");
            int n = Integer.parseInt(scanner.nextLine());
            
            if (n <= 2) {
                System.out.println("n must be greater than 2");
                return;
            }
            
            LatticeCover latticeCover = new LatticeCover(n);
            System.out.println("Solving... (this may take a moment)");
            
            long startTime = System.currentTimeMillis();
            List<?> solution = latticeCover.solve();
            long endTime = System.currentTimeMillis();
            
            if (solution != null) {
                System.out.println("Solution found for n=" + n + " with " + solution.size() + " lines:");
                for (Object segment : solution) {
                    System.out.println("  " + segment.toString());
                }
                
                System.out.println("Time taken: " + (endTime - startTime) + " ms");
            } else {
                System.out.println("No solution found for n=" + n);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
