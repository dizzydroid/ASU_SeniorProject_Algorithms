package edu.ainshams.cse332s;

import edu.ainshams.cse332s.task1.TrominoTiling;
import edu.ainshams.cse332s.task2.KnightTourSolver;
import edu.ainshams.cse332s.task3.TowerOfHanoiSolver;
import edu.ainshams.cse332s.task5.ShooterGameSolver;
import edu.ainshams.cse332s.task6.LatticeCoverSolver;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
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
                    System.out.println("Exiting program. Goodbye!");
                    break;
            }
        }
        
        scanner.close();
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== Algorithms Course Project =====");
        System.out.println("1. Task 1: Tromino Tiling Problem");
        System.out.println("2. Task 2: Knight's Tour Problem");
        System.out.println("3. Task 3: Tower of Hanoi (Four-Peg)");
        System.out.println("4. Task 4: Knights Exchange");
        System.out.println("5. Task 5: Shooter Game");
        System.out.println("6. Task 6: Lattice Cover");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
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
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
        
        return choice;
    }
    
    private static void runTask1() {
        System.out.println("\n===== Task 1: Tromino Tiling Problem =====");
        
        try {
            TrominoTiling trominoTiling = new TrominoTiling();
            trominoTiling.run();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void runTask2() {
        System.out.println("\n===== Task 2: Knight's Tour Problem =====");
        
        try {
            KnightTourSolver.run(scanner);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void runTask3() {
        System.out.println("\n===== Task 3: Tower of Hanoi (Four-Peg) =====");
        
        try {
            TowerOfHanoiSolver.run(scanner);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void runTask4() {
        System.out.println("\n===== Task 4: Knights Exchange =====");
        
        try {
            //TODO
            System.out.println("This task is not yet implemented.");
            
            // KnightsExchangeSolver.run(scanner);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void runTask5() {
        System.out.println("\n===== Task 5: Shooter Game =====");
        
        try {
            ShooterGameSolver.run(scanner);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void runTask6() {
        System.out.println("\n===== Task 6: Lattice Cover =====");
        
        try {
            LatticeCoverSolver.run(scanner);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        
        waitForEnter();
    }
    
    private static void waitForEnter() {
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
}
