package edu.ainshams.cse332s;

import edu.ainshams.cse332s.task1.TrominoTiling;
import edu.ainshams.cse332s.task2.KnightTourSolver;
import edu.ainshams.cse332s.task3.TowerOfHanoiSolver;
import edu.ainshams.cse332s.task5.ShooterGameSolver;
import edu.ainshams.cse332s.task6.LatticeCoverSolver;

import java.util.Scanner;

public class Main {
    // ANSI color codes for terminal
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_BLINK = "\u001B[5m";
    
    // Emoji icons for each task
    public static final String EMOJI_TROMINO = "🧩"; // Puzzle piece
    public static final String EMOJI_KNIGHT = "♞"; // Chess knight
    public static final String EMOJI_TOWER = "🗼"; // Tower
    public static final String EMOJI_KNIGHT_EXCHANGE = "⚔️"; // Crossed swords
    public static final String EMOJI_SHOOTER = "🎯"; // Target
    public static final String EMOJI_LATTICE = "📏"; // Ruler
    public static final String EMOJI_EXIT = "👋"; // Wave
    public static final String EMOJI_ERROR = "❌"; // X mark
    public static final String EMOJI_SUCCESS = "✅"; // Check mark
    public static final String EMOJI_WAIT = "⏳"; // Hourglass
    public static final String EMOJI_MENU = "📋"; // Clipboard
    
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        displayStartupAnimation();
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            
            int choice = getChoice(0, 6);
            
            switch (choice) {
                case 1:
                    runTask1();
                    break;
                case 2:
                    runTask2();
                    break;
                case 3:
                    runTask3();
                    break;
                case 4:
                    runTask4();
                    break;
                case 5:
                    runTask5();
                    break;
                case 6:
                    runTask6();
                    break;
                case 0:
                    running = false;
                    System.out.println(ANSI_GREEN + EMOJI_EXIT + " Exiting program. Goodbye!" + ANSI_RESET);
                    break;
            }
        }
        
        scanner.close();
    }
    
    /**
     * Clears the console screen
     */
    private static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix/Linux/MacOS
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing screen fails, print some newlines instead
            System.out.println("\n\n\n\n\n\n\n\n\n\n");
        }
    }
    
    /**
     * Displays a startup animation
     */
    private static void displayStartupAnimation() {
        clearScreen();
        
        String[] frames = {
            "   ╔════════════════════════════════════════╗\n" +
            "   ║   " + ANSI_CYAN + "Algorithms" + ANSI_RESET + " Course Project            ║\n" +
            "   ╚════════════════════════════════════════╝",
            
            "   ╔════════════════════════════════════════╗\n" +
            "   ║   " + ANSI_CYAN + "Algorithms Course" + ANSI_RESET + " Project            ║\n" +
            "   ╚════════════════════════════════════════╝",
            
            "   ╔════════════════════════════════════════╗\n" +
            "   ║   " + ANSI_CYAN + "Algorithms Course Project" + ANSI_RESET + "            ║\n" +
            "   ╚════════════════════════════════════════╝",
        };
        
        String[] loading = {
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■" + ANSI_YELLOW + "······]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■" + ANSI_YELLOW + "·····]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■■" + ANSI_YELLOW + "····]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■■■" + ANSI_YELLOW + "···]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■■■■" + ANSI_YELLOW + "··]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■■■■■" + ANSI_YELLOW + "·]" + ANSI_RESET,
            "   Loading " + ANSI_YELLOW + "[" + ANSI_GREEN + "■■■■■■■" + ANSI_YELLOW + "]" + ANSI_RESET,
            "   " + ANSI_GREEN + EMOJI_SUCCESS + " Launching application.." + ANSI_RESET
        };
        
        try {
            // Display title animation
            for (int i = 0; i < frames.length; i++) {
                clearScreen();
                System.out.println("\n\n\n");
                System.out.println(frames[i]);
                Thread.sleep(300);
            }
            
            System.out.println("\n\n");
            
            // Display loading bar
            for (String load : loading) {
                clearScreen();
                System.out.println("\n\n\n");
                System.out.println(frames[frames.length - 1]);
                System.out.println("\n\n");
                System.out.println(load);
                Thread.sleep(200);
            }
            
            Thread.sleep(500);
            
        } catch (InterruptedException e) {
            // Do nothing on interrupt
        }
    }
    
    private static void displayMainMenu() {
        clearScreen();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "\n╔═══════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_CYAN + "║  " + ANSI_YELLOW + EMOJI_MENU + " ALGORITHMS COURSE PROJECT " + EMOJI_MENU + ANSI_CYAN + "          ║" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_CYAN + "╚═══════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "\n┌───┬───────────────────────────────────────┐" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "1" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_TROMINO + " " + ANSI_WHITE + "Tromino Tiling Problem" + ANSI_GREEN + "             │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "2" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_KNIGHT + " " + ANSI_WHITE + "Knight's Tour Problem" + ANSI_GREEN + "               │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "3" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_TOWER + " " + ANSI_WHITE + "Tower of Hanoi (Four-Peg)" + ANSI_GREEN + "          │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "4" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_KNIGHT_EXCHANGE + " " + ANSI_WHITE + "Knights Exchange" + ANSI_GREEN + "                   │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "5" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_SHOOTER + " " + ANSI_WHITE + "Shooter Game" + ANSI_GREEN + "                       │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "6" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_LATTICE + " " + ANSI_WHITE + "Lattice Cover" + ANSI_GREEN + "                      │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "│ " + ANSI_BOLD + "0" + ANSI_RESET + ANSI_GREEN + " │ " + ANSI_YELLOW + EMOJI_EXIT + " " + ANSI_WHITE + "Exit" + ANSI_GREEN + "                               │" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "└───┴───────────────────────────────────────┘" + ANSI_RESET);
        System.out.print(ANSI_BOLD + ANSI_PURPLE + "\nEnter your choice: " + ANSI_RESET);
    }
    
    private static int getChoice(int min, int max) {
        int choice = -1;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);
                
                if (choice >= min && choice <= max) {
                    validInput = true;
                } else {
                    System.out.printf(ANSI_YELLOW + "Please enter a number between %d and %d: " + ANSI_RESET, min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print(ANSI_RED + "Invalid input. Please enter a number: " + ANSI_RESET);
            }
        }
        
        return choice;
    }
    
    private static void runTask1() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_TROMINO + " Task 1: Tromino Tiling Problem " + EMOJI_TROMINO + " =====" + ANSI_RESET);
        
        try {
            System.out.println(ANSI_CYAN + "Loading Tromino Tiling Solver..." + ANSI_RESET);
            TrominoTiling trominoTiling = new TrominoTiling();
            trominoTiling.run();
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void runTask2() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_KNIGHT + " Task 2: Knight's Tour Problem " + EMOJI_KNIGHT + " =====" + ANSI_RESET);
        
        try {
            System.out.println(ANSI_CYAN + "Loading Knight's Tour Solver..." + ANSI_RESET);
            KnightTourSolver.run(scanner);
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void runTask3() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_TOWER + " Task 3: Tower of Hanoi (Four-Peg) " + EMOJI_TOWER + " =====" + ANSI_RESET);
        
        try {
            System.out.println(ANSI_CYAN + "Loading Tower of Hanoi Solver..." + ANSI_RESET);
            TowerOfHanoiSolver.run(scanner);
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void runTask4() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_KNIGHT_EXCHANGE + " Task 4: Knights Exchange " + EMOJI_KNIGHT_EXCHANGE + " =====" + ANSI_RESET);
        
        try {
            //TODO
            System.out.println(ANSI_PURPLE + "This task is not yet implemented." + ANSI_RESET);
            
            // KnightsExchangeSolver.run(scanner);
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void runTask5() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_SHOOTER + " Task 5: Shooter Game " + EMOJI_SHOOTER + " =====" + ANSI_RESET);
        
        try {
            System.out.println(ANSI_CYAN + "Loading Shooter Game Solver..." + ANSI_RESET);
            ShooterGameSolver.run(scanner);
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void runTask6() {
        System.out.println(ANSI_BOLD + ANSI_YELLOW + "\n===== " + EMOJI_LATTICE + " Task 6: Lattice Cover " + EMOJI_LATTICE + " =====" + ANSI_RESET);
        
        try {
            System.out.println(ANSI_CYAN + "Loading Lattice Cover Solver..." + ANSI_RESET);
            LatticeCoverSolver.run(scanner);
        } catch (Exception e) {
            System.out.println(ANSI_RED + EMOJI_ERROR + " An error occurred: " + e.getMessage() + ANSI_RESET);
        }
        
        waitForEnter();
    }
    
    private static void waitForEnter() {
        System.out.println("\n" + ANSI_BLUE + EMOJI_WAIT + " Press Enter to return to the main menu..." + ANSI_RESET);
        scanner.nextLine();
    }
}
