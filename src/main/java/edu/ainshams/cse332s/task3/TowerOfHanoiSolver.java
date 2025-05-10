package edu.ainshams.cse332s.task3;

import java.util.Scanner;

public class TowerOfHanoiSolver {

    private int[] dpMoves;     // Stores R(i, 4) for 4 pegs
    private int[] optimalK;    // Stores optimal k for i disks for 4 pegs

    public TowerOfHanoiSolver(int maxDisks) {
        if (maxDisks < 0) throw new IllegalArgumentException("Number of disks cannot be negative.");
        this.dpMoves = new int[maxDisks + 1];
        this.optimalK = new int[maxDisks + 1];
        calculateFrameStewartOptimalMovesDP(maxDisks);
    }

    private void calculateFrameStewartOptimalMovesDP(int numDisks) {
        dpMoves[0] = 0;
        if (numDisks >= 1) {
            dpMoves[1] = 1;
            // optimalK[1] could be 0 or 1; not critical for base recursion.
            // For R(n,p) where p=3, k is not used.
        }

        for (int i = 2; i <= numDisks; i++) {
            dpMoves[i] = Integer.MAX_VALUE; // Initialize with a large value
            for (int k = 1; k < i; k++) {
                // Moves for (i-k) disks using 3 pegs (standard Hanoi)
                long movesForThreePegs = (1L << (i - k)) - 1; // 2^(i-k) - 1
                
                // Total moves: 2 * (moves for k disks on 4 pegs) + moves for (i-k) disks on 3 pegs
                long currentTotalMoves = 2L * dpMoves[k] + movesForThreePegs;
                
                if (currentTotalMoves < dpMoves[i]) {
                    dpMoves[i] = (int) currentTotalMoves;
                    optimalK[i] = k;
                }
            }
        }
    }

    public int getMinMovesForFourPegs(int n) {
        if (n < 0 || n >= dpMoves.length) {
            throw new IllegalArgumentException("Number of disks out of precomputed range or invalid.");
        }
        return dpMoves[n];
    }

    public int getOptimalKForFourPegs(int n) {
        if (n <= 1 || n >= optimalK.length) { // optimalK[0] and optimalK[1] are not typically used for splitting
             throw new IllegalArgumentException("Optimal k not applicable or out of range for " + n + " disks.");
        }
        return optimalK[n];
    }
    
    // Recursive function for standard 3-peg Tower of Hanoi
    public void solveThreePegHanoiRecursive(int n, char source, char destination, char auxiliary) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + source + " to " + destination);
            return;
        }
        solveThreePegHanoiRecursive(n - 1, source, auxiliary, destination);
        System.out.println("Move disk " + n + " from " + source + " to " + destination);
        solveThreePegHanoiRecursive(n - 1, auxiliary, destination, source);
    }

    // Recursive function for 4-peg Frame-Stewart algorithm
    public void solveFrameStewartRecursive(int n, char source, char destination, char aux1, char aux2) {
        if (n == 0) {
            return;
        }
        if (n == 1) {
            // For consistency, you might want a "Move disk X from..." format
            // where X is the actual disk number, not just "disk 1".
            // This requires passing down the current disk being moved or its offset.
            // For simplicity in this example, we keep it as "disk 1" relative to the subproblem.
            System.out.println("Move disk 1 (relative) from " + source + " to " + destination + " (FS)");
            return;
        }

        int k = optimalK[n]; // Get pre-calculated optimal k for n disks

        // 1. Move top k disks from source to aux1, using 4 pegs
        solveFrameStewartRecursive(k, source, aux1, destination, aux2);

        // 2. Move remaining n-k disks from source to destination, using only 3 pegs (S, D, aux2)
        // The aux1 peg is occupied by the k disks.
        // System.out.println("--- Switching to 3-peg for " + (n-k) + " disks (actual disks " + (k+1) + " to " + n + ") ---");
        solveThreePegHanoiRecursive(n - k, source, destination, aux2); // Note: aux2 is the auxiliary here
        // System.out.println("--- Back to 4-peg ---");

        // 3. Move k disks from aux1 to destination, using 4 pegs
        solveFrameStewartRecursive(k, aux1, destination, source, aux2);
    }

    public static void run(Scanner scanner) {
        System.out.print("Enter the number of disks: ");
        int numDisks = Integer.parseInt(scanner.nextLine());
        
        if (numDisks <= 0) {
            System.out.println("Number of disks must be positive.");
            return;
        }
        
        TowerOfHanoiSolver solver = new TowerOfHanoiSolver(numDisks);
        
        System.out.println("\n--- Four-Peg Tower of Hanoi (Reve's Puzzle) ---");
        int minMovesFourPegs = solver.getMinMovesForFourPegs(numDisks);
        System.out.println("Minimum moves for " + numDisks + " disks and 4 pegs: " + minMovesFourPegs);
        
        if (numDisks > 1 && numDisks <= 10) {
            System.out.println("Optimal initial split (k) for " + numDisks + " disks: " + solver.getOptimalKForFourPegs(numDisks));
        }
        
        if (numDisks == 8) {
            if (minMovesFourPegs == 33) {
                System.out.println("Confirmed: 8 disks with 4 pegs can be solved in 33 moves.");
            } else {
                System.out.println("Calculated minimum moves for 8 disks is " + minMovesFourPegs + ", not 33 as per expectation.");
            }
        }
        
        if (numDisks <= 4) {
            System.out.println("\nMoves sequence for " + numDisks + " disks using 4 pegs (A->B, C,D as aux):");
            solver.solveFrameStewartRecursive(numDisks, 'A', 'B', 'C', 'D');
        } else {
            System.out.println("\nMove sequence for " + numDisks + " disks is too long to display here.");
        }
        
        System.out.println("\n--- Standard Three-Peg Tower of Hanoi ---");
        long minMovesThreePegs = (1L << numDisks) - 1;
        System.out.println("Minimum moves for " + numDisks + " disks and 3 pegs: " + minMovesThreePegs);
        
        if (numDisks <= 4) {
            System.out.println("\nMoves sequence for " + numDisks + " disks using 3 pegs (A->B, C as aux):");
            solver.solveThreePegHanoiRecursive(numDisks, 'A', 'B', 'C');
        }
    }
}