package edu.ainshams.cse332s.task5;

import java.util.Scanner;
import java.util.Random;

public class ShooterGame {
    private int numSpots;           // Total number of hiding spots
    private int targetPosition;     // Current position of the target (0-indexed)
    private int numShots;           // Count of shots fired
    private boolean targetFound;    // Flag indicating if the target is found
    private Random random;          // For random target movement
    private int[] shotHistory;      // To keep track of where shots were fired
    
    // Identifiers for target status and ANSI colors
    public static final char TARGET = 'T';      // Represents target
    public static final char HIT = 'X';         // Represents a hit
    public static final char MISS = 'O';        // Represents a miss
    public static final char EMPTY = '-';       // Represents an empty spot
    
    // ANSI color codes for terminal output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    
    /**
     * Constructor for the ShooterGame
     * @param numSpots Number of hiding spots for the target
     */
    public ShooterGame(int numSpots) {
        if (numSpots <= 1) {
            throw new IllegalArgumentException("Number of spots must be greater than 1");
        }
        
        this.numSpots = numSpots;
        this.random = new Random();
        this.numShots = 0;
        this.targetFound = false;
        this.shotHistory = new int[numSpots];
        
        // Place the target randomly
        this.targetPosition = random.nextInt(numSpots);
    }
    
    /**
     * Visualizes the current game state with enhanced ASCII art
     * @param showTarget Whether to show the actual target position (for debugging)
     */
    public void visualizeGame(boolean showTarget) {
        // Create a border based on the number of spots
        String border = "+" + "-".repeat(numSpots * 3 + 3) + "+";
        System.out.println("\n" + border);
        
        // Print position labels (1-based indexing for display)
        System.out.print("| ");
        for (int i = 0; i < numSpots; i++) {
            System.out.printf("%2d ", i+1); // Changed to 1-based indexing for display
        }
        System.out.println("|");
        
        // Print line separator
        System.out.println(border);
        
        // Print game state with ASCII art
        System.out.print("| ");
        for (int i = 0; i < numSpots; i++) {
            if (showTarget && i == targetPosition) {
                System.out.print(ANSI_PURPLE + " " + TARGET + " " + ANSI_RESET);
            } else if (shotHistory[i] > 0) {
                if (targetFound && i == targetPosition) {
                    System.out.print(ANSI_GREEN + " " + HIT + " " + ANSI_RESET);
                } else {
                    System.out.print(ANSI_RED + " " + MISS + " " + ANSI_RESET);
                }
            } else {
                System.out.print(ANSI_BLUE + " " + EMPTY + " " + ANSI_RESET);
            }
        }
        System.out.println(" |");
        
        // Print line separator
        System.out.println(border);
        
        // Print shot history
        System.out.print("| ");
        for (int i = 0; i < numSpots; i++) {
            System.out.printf("%2d ", shotHistory[i]);
        }
        System.out.println("| <- Shot count");
        
        // Print bottom border
        System.out.println(border);
        System.out.println("Shots fired: " + numShots);
        
        // Debug information
        if (showTarget) {
            System.out.println(ANSI_YELLOW + "Target position (internal): " + targetPosition + 
                               " (display: " + (targetPosition+1) + ")" + ANSI_RESET);
        }
    }
    
    /**
     * Shoots at a specific hiding spot (using 0-based indexing internally)
     * @param position Position to shoot at (0-indexed internally)
     * @return true if hit, false if miss
     */
    public boolean shootAt(int position) {
        if (position < 0 || position >= numSpots) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        
        numShots++;
        System.out.println(ANSI_YELLOW + "Shot #" + numShots + " at position " + (position+1) + ANSI_RESET); // Display as 1-based
        
        // Record this shot in history
        shotHistory[position]++;
        
        // Check if the shot hit the target
        boolean hit = (position == targetPosition);
        
        if (hit) {
            System.out.println(ANSI_GREEN + "HIT! You found the target at position " + (position+1) + ANSI_RESET); // Display as 1-based
            targetFound = true;
            return true;
        } else {
            System.out.println(ANSI_RED + "MISS! Target not at position " + (position+1) + ANSI_RESET); // Display as 1-based
            
            // Move target to an adjacent spot if game isn't over
            moveTarget();
            return false;
        }
    }
    
    /**
     * Moves the target to an adjacent spot randomly
     */
    private void moveTarget() {
        // Save the previous position for debugging
        int previousPosition = targetPosition;
        
        // If target is at leftmost position, it can only move right
        if (targetPosition == 0) {
            targetPosition = 1;
        }
        // If target is at rightmost position, it can only move left
        else if (targetPosition == numSpots - 1) {
            targetPosition = numSpots - 2;
        }
        // Otherwise, it can move either left or right
        else {
            // Random boolean to decide direction (true = right, false = left)
            boolean moveRight = random.nextBoolean();
            targetPosition += moveRight ? 1 : -1;
        }
        
        System.out.println(ANSI_CYAN + "Target has moved from position " + (previousPosition+1) + 
                           " to position " + (targetPosition+1) + ANSI_RESET);
    }
    
    /**
     * Gets the number of spots
     * @return Number of hiding spots
     */
    public int getNumSpots() {
        return numSpots;
    }
    
    /**
     * Checks if the target has been found
     * @return true if target is found, false otherwise
     */
    public boolean isTargetFound() {
        return targetFound;
    }
    
    /**
     * Gets the number of shots fired
     * @return Number of shots
     */
    public int getNumShots() {
        return numShots;
    }
    
    /**
     * Gets the target's position (for testing)
     * @return Current position of the target
     */
    protected int getTargetPosition() {
        return targetPosition;
    }
    
    /**
     * Sets the target position (for testing and debugging)
     * @param position Position to set the target to
     */
    protected void setTargetPosition(int position) {
        if (position >= 0 && position < numSpots) {
            this.targetPosition = position;
        }
    }
    
    /**
     * Main method to run the game interactively
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(ANSI_PURPLE + "===========================================");
        System.out.println("  SHOOTER GAME - DIVIDE AND CONQUER EDITION  ");
        System.out.println("==========================================" + ANSI_RESET);
        System.out.println("\nA target is hiding in one of many spots in a line.");
        System.out.println("After each shot, the target moves to an adjacent spot.");
        System.out.println("Your goal is to find and hit the target.");
        System.out.println("\nLegend:");
        System.out.println(ANSI_BLUE + " - " + ANSI_RESET + ": Empty spot");
        System.out.println(ANSI_RED + " O " + ANSI_RESET + ": Miss");
        System.out.println(ANSI_GREEN + " X " + ANSI_RESET + ": Hit");
        System.out.println(ANSI_PURPLE + " T " + ANSI_RESET + ": Target (shown in debug mode)\n");
        
        System.out.print("Enter the number of hiding spots (n > 1): ");
        int numSpots = scanner.nextInt();
        
        ShooterGame game = new ShooterGame(numSpots);
        ShooterGameSolver solver = new ShooterGameSolver();
        
        System.out.println("\n" + ANSI_YELLOW + "Game initialized with " + numSpots + " hiding spots." + ANSI_RESET);
        System.out.println("1. Play manually");
        System.out.println("2. Let the algorithm play");
        System.out.println("3. Show a step-by-step solution with target visible (debug)");
        System.out.print("Your choice: ");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            // Manual play
            while (!game.isTargetFound()) {
                game.visualizeGame(false);
                System.out.print("Enter position to shoot (1-" + numSpots + "): "); // Changed to 1-based indexing
                int position = scanner.nextInt() - 1; // Convert from 1-based input to 0-based
                if (position >= 0 && position < numSpots) {
                    game.shootAt(position);
                } else {
                    System.out.println(ANSI_RED + "Invalid position! Please enter a number between 1 and " + numSpots + ANSI_RESET);
                }
            }
        } else if (choice == 2) {
            // Algorithm play
            solver.solveGame(game);
        } else {
            // Debug mode
            solver.solveGameWithDebug(game);
        }
        
        System.out.println("\n" + ANSI_GREEN + "Success! Target hit in " + game.getNumShots() + " shots." + ANSI_RESET);
        // Show final game state
        game.visualizeGame(false);
        scanner.close();
    }
}
