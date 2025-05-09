package edu.ainshams.cse332s.task3;
import java.util.Scanner;

public class TowerOfHanoi {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of disks: ");
        int numDisks = scanner.nextInt();

        if (numDisks <= 0) {
            System.out.println("Number of disks must be positive.");
            scanner.close();
            return;
        }

        TowerOfHanoiSolver solver = new TowerOfHanoiSolver(numDisks);

        System.out.println("\n--- Four-Peg Tower of Hanoi (Reve's Puzzle) ---");
        int minMovesFourPegs = solver.getMinMovesForFourPegs(numDisks);
        System.out.println("Minimum moves for " + numDisks + " disks and 4 pegs: " + minMovesFourPegs);

        if (numDisks > 1 && numDisks <= 10) { // Show optimal k for reasonable N
             System.out.println("Optimal initial split (k) for " + numDisks + " disks: " + solver.getOptimalKForFourPegs(numDisks));
        }
        
        // For n=8, the problem specifically asks if it can be solved in 33 moves.
        if (numDisks == 8) {
            if (minMovesFourPegs == 33) {
                System.out.println("Confirmed: 8 disks with 4 pegs can be solved in 33 moves.");
            } else {
                System.out.println("Calculated minimum moves for 8 disks is " + minMovesFourPegs + ", not 33 as per expectation. Check DP or problem statement.");
            }
        }

        // Optionally, print the sequence of moves for a small number of disks
        if (numDisks <= 4) { // Keep output manageable
            System.out.println("\nMoves sequence for " + numDisks + " disks using 4 pegs (A->B, C,D as aux):");
            solver.solveFrameStewartRecursive(numDisks, 'A', 'B', 'C', 'D');
        } else {
            System.out.println("\nMove sequence for " + numDisks + " disks is too long to display here.");
        }
        
        // Standard 3-peg comparison
        System.out.println("\n--- Standard Three-Peg Tower of Hanoi ---");
        long minMovesThreePegs = (1L << numDisks) - 1;
        System.out.println("Minimum moves for " + numDisks + " disks and 3 pegs: " + minMovesThreePegs);
        if (numDisks <= 4) { // Keep output manageable
             System.out.println("\nMoves sequence for " + numDisks + " disks using 3 pegs (A->B, C as aux):");
             solver.solveThreePegHanoiRecursive(numDisks, 'A', 'B', 'C');
        }

        scanner.close();
    }
}
