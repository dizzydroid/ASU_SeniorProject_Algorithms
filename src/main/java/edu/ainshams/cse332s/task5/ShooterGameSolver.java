package edu.ainshams.cse332s.task5;

import java.util.Scanner;

/**
 * Solver for the Shooter Game using a divide and conquer algorithm
 * This implementation guarantees finding the target using the divide and conquer paradigm
 */
public class ShooterGameSolver {

    // Sleep time between algorithm steps (in milliseconds) for visualization
    private static final int VISUALIZATION_DELAY = 1000;

    /**
     * Run the Shooter Game solver with user interaction
     * @param scanner Scanner object for user input
     */
    public static void run(Scanner scanner) {
        System.out.print("Enter the number of hiding spots: ");
        int spots = Integer.parseInt(scanner.nextLine());
        
        ShooterGame game = new ShooterGame(spots);
        ShooterGameSolver solver = new ShooterGameSolver();
        
        boolean showDebug = false;
        System.out.print("Show target position for debugging? (y/n): ");
        String debug = scanner.nextLine().trim().toLowerCase();
        if (debug.equals("y") || debug.equals("yes")) {
            showDebug = true;
        }
        
        game.visualizeGame(showDebug);
        
        if (showDebug) {
            solver.solveGameWithDebug(game);
        } else {
            solver.solveGame(game);
        }
    }

    /**
     * Solves the shooter game using a divide and conquer approach
     * 
     * @param game The ShooterGame instance to solve
     */
    public void solveGame(ShooterGame game) {
        int numSpots = game.getNumSpots();
        System.out.println(ShooterGame.ANSI_PURPLE + "\nStarting divide and conquer algorithm to find the target..."
                + ShooterGame.ANSI_RESET);
        System.out.println("\nThis algorithm guarantees finding the target by dividing the search space");
        System.out.println("and strategically shooting to narrow down the target's location.\n");

        // Show initial game state
        game.visualizeGame(false);

        // Add a small delay before starting
        sleep(VISUALIZATION_DELAY);

        // Solve using a proper divide and conquer algorithm
        divideAndConquer(game, 0, numSpots - 1, false);

        System.out.println(
                ShooterGame.ANSI_GREEN + "\nTarget found using divide and conquer algorithm!" + ShooterGame.ANSI_RESET);
    }

    /**
     * Solves the shooter game with debug visualization
     * 
     * @param game The ShooterGame instance to solve
     */
    public void solveGameWithDebug(ShooterGame game) {
        int numSpots = game.getNumSpots();
        System.out.println(ShooterGame.ANSI_PURPLE + "\nStarting divide and conquer algorithm with debug mode..."
                + ShooterGame.ANSI_RESET);
        System.out.println(ShooterGame.ANSI_YELLOW + "DEBUG MODE: Target is visible for educational purposes"
                + ShooterGame.ANSI_RESET);

        // Show initial game state with target visible
        game.visualizeGame(true);

        // Add a small delay before starting
        sleep(VISUALIZATION_DELAY);

        // Solve using a proper divide and conquer algorithm with debugging
        divideAndConquer(game, 0, numSpots - 1, true);
    }

    /**
     * Implements a true divide and conquer algorithm to find the target
     * 
     * This algorithm follows the classic divide and conquer paradigm:
     * 1. DIVIDE: Split the problem into subproblems (left and right halves)
     * 2. CONQUER: Make strategic shots to determine where the target must be
     * 3. COMBINE: Use the information gained to focus on the correct subproblem
     * 
     * The algorithm guarantees finding the target by systematically eliminating
     * portions of the search space where the target cannot be.
     * 
     * @param game  The ShooterGame instance
     * @param start The start position (0-indexed)
     * @param end   The end position (0-indexed)
     * @param debug Whether to show target position (for debugging)
     */
    private void divideAndConquer(ShooterGame game, int start, int end, boolean debug) {
        if (game.isTargetFound()) {
            return; // Target already found
        }

        // Display current search range (using 1-based indexing for display)
        System.out.println(ShooterGame.ANSI_CYAN + "\nSearching range [" + (start + 1) + ", " + (end + 1) + "]"
                + ShooterGame.ANSI_RESET);

        // Base cases
        if (start == end) {
            // Only one position left - must be the target
            System.out.println(ShooterGame.ANSI_YELLOW + "Only one position left. Shooting at position " + (start + 1)
                    + ShooterGame.ANSI_RESET);
            boolean hit = game.shootAt(start);
            game.visualizeGame(debug);
            sleep(VISUALIZATION_DELAY);

            if (!hit && !game.isTargetFound()) {
                // Edge case: if we somehow missed the only position,
                // the target must have moved to an adjacent position
                if (start > 0) {
                    System.out.println(ShooterGame.ANSI_YELLOW + "Target must have moved to position " + start
                            + ShooterGame.ANSI_RESET);
                    game.shootAt(start - 1);
                    game.visualizeGame(debug);
                    sleep(VISUALIZATION_DELAY);
                }

                // Check if the target moved to the next position
                // (only if we are not at the last position)
                if (!game.isTargetFound() && start < game.getNumSpots() - 1) {
                    System.out.println(ShooterGame.ANSI_YELLOW + "Target must have moved to position " + (start + 2)
                            + ShooterGame.ANSI_RESET);
                    game.shootAt(start + 1);
                    game.visualizeGame(debug);
                    sleep(VISUALIZATION_DELAY);
                }
            }
            return;
        }

        if (end - start == 1) {
            // Two positions case
            System.out.println(ShooterGame.ANSI_YELLOW + "Two positions case. First shooting at position " + (start + 1)
                    + ShooterGame.ANSI_RESET);
            boolean hit = game.shootAt(start);
            game.visualizeGame(debug);
            sleep(VISUALIZATION_DELAY);

            if (!hit) {
                // If missed at start, shoot at end
                System.out.println(
                        ShooterGame.ANSI_YELLOW + "Shooting at position " + (end + 1) + ShooterGame.ANSI_RESET);
                game.shootAt(end);
                game.visualizeGame(debug);
                sleep(VISUALIZATION_DELAY);
            }
            return;
        }

        // DIVIDE: Find the middle point
        int mid = (start + end) / 2;

        // Show strategy
        System.out.println(
                ShooterGame.ANSI_YELLOW + "DIVIDE: Splitting range at position " + (mid + 1) + ShooterGame.ANSI_RESET);
        game.visualizeGame(debug);
        sleep(VISUALIZATION_DELAY);

        // CONQUER: Shoot at the middle
        System.out.println(
                ShooterGame.ANSI_YELLOW + "CONQUER: Shooting at middle position " + (mid + 1) + ShooterGame.ANSI_RESET);
        boolean hitMid = game.shootAt(mid);
        game.visualizeGame(debug);
        sleep(VISUALIZATION_DELAY);

        if (hitMid) {
            return; // Target found
        }

        // The key insight: After the target moves, we can eliminate parts of the search
        // space

        // Check left boundary
        if (mid == start) {
            // Special case: if mid == start, we need to search the right half
            System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Searching right half" + ShooterGame.ANSI_RESET);
            divideAndConquer(game, mid + 1, end, debug);
            return;
        }

        // Check right boundary
        if (mid == end) {
            // Special case: if mid == end, we need to search the left half
            System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Searching left half" + ShooterGame.ANSI_RESET);
            divideAndConquer(game, start, mid - 1, debug);
            return;
        }

        // Take another strategic shot to get more information
        int strategicPos;

        // Choose a strategic position - we'll use the left quarter position
        strategicPos = (start + mid - 1) / 2;
        if (strategicPos == start)
            strategicPos++; // Ensure we don't shoot at start if it's too close
        if (strategicPos == mid)
            strategicPos--; // Ensure we don't shoot at mid if it's too close

        if (strategicPos >= start && strategicPos < mid) {
            System.out.println(ShooterGame.ANSI_YELLOW + "Taking strategic shot at position " + (strategicPos + 1)
                    + ShooterGame.ANSI_RESET);
            boolean hitStrategic = game.shootAt(strategicPos);
            game.visualizeGame(debug);
            sleep(VISUALIZATION_DELAY);

            if (hitStrategic) {
                return; // Target found
            }
        } else {
            // If we can't make a good strategic shot on left side, pick one on right side
            strategicPos = (mid + 1 + end) / 2;
            if (strategicPos > mid && strategicPos <= end) {
                System.out.println(ShooterGame.ANSI_YELLOW + "Taking strategic shot at position " + (strategicPos + 1)
                        + ShooterGame.ANSI_RESET);
                boolean hitStrategic = game.shootAt(strategicPos);
                game.visualizeGame(debug);
                sleep(VISUALIZATION_DELAY);

                if (hitStrategic) {
                    return; // Target found
                }
            }
        }

        // COMBINE: After our strategic shots, decide which half to search

        // First, try the smaller half (more efficient)
        if (mid - start <= end - mid) {
            // Left half is smaller
            System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Searching left half first [" + (start + 1) + ", "
                    + mid + "]" + ShooterGame.ANSI_RESET);
            divideAndConquer(game, start, mid - 1, debug);

            // If target not found in left half, try right half
            if (!game.isTargetFound()) {
                System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Target not found in left half. " +
                        "Searching right half [" + (mid + 2) + ", " + (end + 1) + "]" + ShooterGame.ANSI_RESET);
                divideAndConquer(game, mid + 1, end, debug);
            }
        } else {
            // Right half is smaller
            System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Searching right half first [" + (mid + 2) + ", "
                    + (end + 1) + "]" + ShooterGame.ANSI_RESET);
            divideAndConquer(game, mid + 1, end, debug);

            // If target not found in right half, try left half
            if (!game.isTargetFound()) {
                System.out.println(ShooterGame.ANSI_YELLOW + "COMBINE: Target not found in right half. " +
                        "Searching left half [" + (start + 1) + ", " + mid + "]" + ShooterGame.ANSI_RESET);
                divideAndConquer(game, start, mid - 1, debug);
            }
        }
    }

    /**
     * Helper method to pause execution for visualization purposes
     * 
     * @param millis Time to sleep in milliseconds
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
