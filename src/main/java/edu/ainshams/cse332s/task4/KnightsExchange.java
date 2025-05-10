package edu.ainshams.cse332s.task4;
import java.util.*;

public class KnightsExchange {
    // Board dimensions
    private static final int ROWS = 4;
    private static final int COLS = 3;
    
    // Knight moves
    private static final int[][] KNIGHT_MOVES = {
        {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
        {1, -2}, {1, 2}, {2, -1}, {2, 1}
    };
    
    // Precomputed distances embedded directly
    private static final Map<String, Integer> distances = initializeDistances();
    
    // Initialize the distances map with precomputed values
    private static Map<String, Integer> initializeDistances() {
        Map<String, Integer> map = new HashMap<>();
        // Embedding the JSON values directly
        map.put("0202", 0); map.put("0210", 1); map.put("0222", 2); map.put("0230", 3);
        map.put("0221", 1); map.put("0231", 2); map.put("0201", 3); map.put("0211", 4);
        map.put("0200", 2); map.put("0212", 3); map.put("0220", 4); map.put("0232", 5);
        map.put("1002", 1); map.put("1010", 0); map.put("1022", 1); map.put("1030", 2);
        map.put("1021", 2); map.put("1031", 1); map.put("1001", 2); map.put("1011", 3);
        map.put("1000", 3); map.put("1012", 2); map.put("1020", 3); map.put("1032", 4);
        map.put("2202", 2); map.put("2210", 1); map.put("2222", 0); map.put("2230", 1);
        map.put("2221", 3); map.put("2231", 2); map.put("2201", 1); map.put("2211", 2);
        map.put("2200", 4); map.put("2212", 3); map.put("2220", 2); map.put("2232", 3);
        map.put("3002", 3); map.put("3010", 2); map.put("3022", 1); map.put("3030", 0);
        map.put("3021", 4); map.put("3031", 3); map.put("3001", 2); map.put("3011", 1);
        map.put("3000", 5); map.put("3012", 4); map.put("3020", 3); map.put("3032", 2);
        map.put("2102", 1); map.put("2110", 2); map.put("2122", 3); map.put("2130", 4);
        map.put("2121", 0); map.put("2131", 3); map.put("2101", 4); map.put("2111", 5);
        map.put("2100", 1); map.put("2112", 2); map.put("2120", 3); map.put("2132", 4);
        map.put("3102", 2); map.put("3110", 1); map.put("3122", 2); map.put("3130", 3);
        map.put("3121", 3); map.put("3131", 0); map.put("3101", 3); map.put("3111", 4);
        map.put("3100", 2); map.put("3112", 1); map.put("3120", 2); map.put("3132", 3);
        map.put("0102", 3); map.put("0110", 2); map.put("0122", 1); map.put("0130", 2);
        map.put("0121", 4); map.put("0131", 3); map.put("0101", 0); map.put("0111", 3);
        map.put("0100", 3); map.put("0112", 2); map.put("0120", 1); map.put("0132", 2);
        map.put("1102", 4); map.put("1110", 3); map.put("1122", 2); map.put("1130", 1);
        map.put("1121", 5); map.put("1131", 4); map.put("1101", 3); map.put("1111", 0);
        map.put("1100", 4); map.put("1112", 3); map.put("1120", 2); map.put("1132", 1);
        map.put("0002", 2); map.put("0010", 3); map.put("0022", 4); map.put("0030", 5);
        map.put("0021", 1); map.put("0031", 2); map.put("0001", 3); map.put("0011", 4);
        map.put("0000", 0); map.put("0012", 1); map.put("0020", 2); map.put("0032", 3);
        map.put("1202", 3); map.put("1210", 2); map.put("1222", 3); map.put("1230", 4);
        map.put("1221", 2); map.put("1231", 1); map.put("1201", 2); map.put("1211", 3);
        map.put("1200", 1); map.put("1212", 0); map.put("1220", 1); map.put("1232", 2);
        map.put("2002", 4); map.put("2010", 3); map.put("2022", 2); map.put("2030", 3);
        map.put("2021", 3); map.put("2031", 2); map.put("2001", 1); map.put("2011", 2);
        map.put("2000", 2); map.put("2012", 1); map.put("2020", 0); map.put("2032", 1);
        map.put("3202", 5); map.put("3210", 4); map.put("3222", 3); map.put("3230", 2);
        map.put("3221", 4); map.put("3231", 3); map.put("3201", 2); map.put("3211", 1);
        map.put("3200", 3); map.put("3212", 2); map.put("3220", 1); map.put("3232", 0);
        return map;
    }
    
    static class Knight {
        private final String type; // "white" or "black"
        private int row;
        private int col;
        
        public Knight(String type, int row, int col) {
            this.type = type;
            this.row = row;
            this.col = col;
        }
        
        public Knight copy() {
            return new Knight(this.type, this.row, this.col);
        }
        
        @Override
        public String toString() {
            return type + " at (" + row + "," + col + ")";
        }
    }
    
    // Optimized State class with efficient copying
    static class State {
        private final Knight[] knights;
        
        public State(Knight[] knights) {
            this.knights = knights;
        }
        
        public State copy() {
            Knight[] newKnights = new Knight[knights.length];
            for (int i = 0; i < knights.length; i++) {
                newKnights[i] = knights[i].copy();
            }
            return new State(newKnights);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof State)) return false;
            
            State state = (State) obj;
            if (knights.length != state.knights.length) return false;
            
            for (int i = 0; i < knights.length; i++) {
                Knight k1 = knights[i];
                Knight k2 = state.knights[i];
                if (!k1.type.equals(k2.type) || k1.row != k2.row || k1.col != k2.col) {
                    return false;
                }
            }
            return true;
        }
        
    }
    
    // Move class to record knight moves
    static class Move {
        private final Knight knight;
        private final int fromRow, fromCol;
        private final int toRow, toCol;
        
        public Move(Knight knight, int fromRow, int fromCol, int toRow, int toCol) {
            this.knight = knight;
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
        
        @Override
        public String toString() {
            return knight.type + " knight moved from (" + fromRow + "," + fromCol + 
                   ") to (" + toRow + "," + toCol + ")";
        }
    }
    
    // Node class for the priority queue (optimized)
    static class Node implements Comparable<Node> {
        private final State state;
        private final int evaluation;
        private final Node parent;
        private final Move move;
        private final int depth;
        
        public Node(State state, Node parent, Move move) {
            this.state = state;
            this.parent = parent;
            this.move = move;
            this.depth = (parent == null) ? 0 : parent.depth + 1;
            
            // Evaluate with depth consideration built in
            int stateScore = evaluate(state);
            this.evaluation = stateScore + depth;
        }
        
        @Override
        public int compareTo(Node other) {
            int evalCompare = Integer.compare(this.evaluation, other.evaluation);
            return evalCompare != 0 ? evalCompare : Integer.compare(this.depth, other.depth);
        }
    }
    
    // Check if a position is valid on the board
    private static boolean isValid(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }
    
    // Get the distance from current position to target position
    private static int getDistance(int srcRow, int srcCol, int destRow, int destCol) {
        String key = "" + srcRow + srcCol + destRow + destCol;
        return distances.getOrDefault(key, Integer.MAX_VALUE);
    }
    
    // Check if a position is occupied by any knight
    private static boolean isOccupied(State state, int row, int col) {
        for (Knight knight : state.knights) {
            if (knight.row == row && knight.col == col) {
                return true;
            }
        }
        return false;
    }
    
    // Optimized evaluation function for finding 16-move solution
    private static int evaluate(State state) {
        int total = 0;
        
        // For white knights (need to reach row 0)
        for (int i = 0; i < 3; i++) {
            Knight knight = state.knights[i];  // White knights are indices 0, 1, 2
            // Distance from current position to any position in row 0 (desired)
            int minDist = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                int dist = getDistance(knight.row, knight.col, 0, col);
                minDist = Math.min(minDist, dist);
            }
            
            total += minDist * minDist;
        }
        
        // For black knights (need to reach row 3)
        for (int i = 3; i < 6; i++) {
            Knight knight = state.knights[i];  // Black knights are indices 3, 4, 5
            // Distance from current position to any position in row 3 (desired)
            int minDist = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                int dist = getDistance(knight.row, knight.col, 3, col);
                minDist = Math.min(minDist, dist);
            }
            
            total += minDist * minDist;
        }
        return total;
    }
    
    // Function to get valid neighbors of the current state (optimized)
    private static List<State> getNeighbors(State state) {
        List<State> neighbors = new ArrayList<>();
        
        for (int k = 0; k < state.knights.length; k++) {
            Knight knight = state.knights[k];
            
            // Skip knights already at their goal row
            if ((knight.type.equals("white") && knight.row == 0) ||
                (knight.type.equals("black") && knight.row == 3)) {
                continue;
            }
            
            // Try all possible knight moves
            for (int[] move : KNIGHT_MOVES) {
                int newRow = knight.row + move[0];
                int newCol = knight.col + move[1];
                
                // Check if move is valid and destination is not occupied
                if (isValid(newRow, newCol) && !isOccupied(state, newRow, newCol)) {
                    State newState = state.copy();
                    newState.knights[k].row = newRow;
                    newState.knights[k].col = newCol;
                    neighbors.add(newState);
                }
            }
        }
        
        return neighbors;
    }
    
    // Optimized goal state check
    private static boolean isGoalState(State state) {
        // Check white knights (first 3) in row 0 and black knights (last 3) in row 3
        for (int i = 0; i < 3; i++) {
            if (state.knights[i].row != 0 || state.knights[i+3].row != 3) 
                return false;
        }
        return true;
    }
    
    
    private static Move findMove(State prevState, State nextState) {
        for (int k = 0; k < prevState.knights.length; k++) {
            Knight pk = prevState.knights[k];
            Knight nk = nextState.knights[k];
            
            if (pk.row != nk.row || pk.col != nk.col) {
                return new Move(pk.copy(), pk.row, pk.col, nk.row, nk.col);
            }
        }
        return null;
    }
    
   
    public static List<Move> findOptimalSolution() {
        // Initialize starting knights positions
        Knight[] initialKnights = {
            new Knight("white", 3, 0), new Knight("white", 3, 1), new Knight("white", 3, 2),
            new Knight("black", 0, 0), new Knight("black", 0, 1), new Knight("black", 0, 2)
        };
        
        int targetDepth = 16;
        System.out.println("Targeting optimal " + targetDepth + "-move solution");
        
        State initialState = new State(initialKnights);
        PriorityQueue<Node> queue = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();
        
        // Start search with initial state
        queue.add(new Node(initialState, null, null));
        int nodesExpanded = 0, maxQueueSize = 0;
        
        while (!queue.isEmpty()) {
            maxQueueSize = Math.max(maxQueueSize, queue.size());
            Node current = queue.poll();
            nodesExpanded++;
            
            // Skip if beyond target or already visited at better depth
            if (current.depth > targetDepth) continue;
            
            String stateKey = getStateKey(current.state);
            if (visited.containsKey(stateKey) && visited.get(stateKey) <= current.depth) 
                continue;
            
            // Mark as visited and check for goal
            visited.put(stateKey, current.depth);
            
            if (isGoalState(current.state)) {
                System.out.printf("Solution found! Depth: %d, Nodes: %d, Max queue: %d%n", 
                                 current.depth, nodesExpanded, maxQueueSize);
                
                // Reconstruct path
                List<Move> moves = new ArrayList<>();
                Node node = current;
                while (node.parent != null) {
                    moves.add(0, node.move);
                    node = node.parent;
                }
                
                if (moves.size() == targetDepth) {
                    System.out.println("Found optimal " + targetDepth + "-move solution!");
                    return moves;
                } else if (moves.size() < targetDepth) {
                    System.out.println("Found even better " + moves.size() + "-move solution!");
                    return moves;
                }
                
                // Continue search for better solution
                System.out.println("Found " + moves.size() + "-move solution, searching for better...");
                continue;
            }
            
            // Generate neighbors and add to queue
            for (State nextState : getNeighbors(current.state)) {
                String nextKey = getStateKey(nextState);
                if (visited.containsKey(nextKey) && visited.get(nextKey) <= current.depth + 1)
                    continue;
                
                queue.add(new Node(nextState, current, findMove(current.state, nextState)));
            }
            
            // Progress reporting
            if (nodesExpanded % 50000 == 0) {
                System.out.println("Progress: " + nodesExpanded + " nodes, queue: " + queue.size());
            }
        }
        
        System.out.println("Search completed. Nodes expanded: " + nodesExpanded);
        return new ArrayList<>();
    }
    
    
    private static String getStateKey(State state) {
        StringBuilder key = new StringBuilder(24); // Pre-sized for efficiency
        for (Knight knight : state.knights) {
            key.append(knight.type.charAt(0)) 
               .append(knight.row)
               .append(knight.col);
        }
        return key.toString();
    }
    
    // Method to print the board state
    public static void printBoard(State state) {
        System.out.println("\nCurrent Board State:");
        System.out.println("------------------");
        
        // Create a 2D representation of the board
        String[][] board = new String[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = ".";  // Empty cell
            }
        }
        
        // Place knights on the board
        for (Knight knight : state.knights) {
            if (knight.type.equals("white")) {
                board[knight.row][knight.col] = "W";  // White knight
            } else {
                board[knight.row][knight.col] = "B";  // Black knight
            }
        }
        
        // Print the board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("------------------");
        System.out.println("W = White Knight, B = Black Knight, . = Empty Cell");
    }

    public static void main(String[] args) {
        System.out.println("Knight Exchange using embedded distance data");
        System.out.println("Loaded " + distances.size() + " distance values");
        
        System.out.println("Starting optimal solution search...");
        long startTime = System.currentTimeMillis();
        List<Move> solution = findOptimalSolution();
        long endTime = System.currentTimeMillis();
        System.out.println("Search completed in " + (endTime - startTime) + " ms");
        
        if (solution.isEmpty()) {
            System.out.println("Could not find optimal solution");
        } else {            
            System.out.println("Solution found in " + solution.size() + " moves:");
            for (Move move : solution) {
                System.out.println(move);
            }
            
            // Print the initial board state
            Knight[] initialKnights = {
                new Knight("white", 3, 0),
                new Knight("white", 3, 1),
                new Knight("white", 3, 2),
                new Knight("black", 0, 0),
                new Knight("black", 0, 1),
                new Knight("black", 0, 2)
            };
            State initialState = new State(initialKnights);
            System.out.println("\nInitial board state:");
            printBoard(initialState);
            
            // Apply all moves to get final state
            State finalState = initialState.copy();
            for (Move move : solution) {
                // Find the knight in the state
                for (Knight knight : finalState.knights) {
                    if (knight.type.equals(move.knight.type) && 
                        knight.row == move.fromRow && 
                        knight.col == move.fromCol) {
                        // Move the knight
                        knight.row = move.toRow;
                        knight.col = move.toCol;
                        break;
                    }
                }
            }
            
            // Print the final board state
            System.out.println("\nFinal board state after all moves:");
            printBoard(finalState);
            
            // Verify the goal state
            boolean isGoal = isGoalState(finalState);
            System.out.println("\nIs the final state a goal state? " + (isGoal ? "Yes" : "No"));
        }
    }
}

