package edu.ainshams.cse332s.task1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class TrominoTilingSolver {
    private int nextTileId = 1;
    
    public void solve(TrominoTiling tiling) {
        nextTileId = 1; // Reset the tile ID counter
        tileBoard(tiling, 0, 0, tiling.getSize(), tiling.getMissingRow(), tiling.getMissingCol());
        guaranteedColoring(tiling);
    }
    
    private void tileBoard(TrominoTiling tiling, int row, int col, int size, int missingRow, int missingCol) {
        if (size == 2) {
            int currentTrominoId = nextTileId++;
            TrominoTiling.Tromino tromino = new TrominoTiling.Tromino(currentTrominoId);
            for (int r = row; r < row + 2; r++) {
                for (int c = col; c < col + 2; c++) {
                    if (r != missingRow || c != missingCol) {
                        tiling.getTrominoId()[r][c] = currentTrominoId;
                        tromino.addCell(r, c);
                    }
                }
            }
            tiling.addTromino(tromino);
            return;
        }

        int s = size / 2;
        int currentTrominoId = nextTileId++;
        TrominoTiling.Tromino tromino = new TrominoTiling.Tromino(currentTrominoId);
        
        // Positions for center tromino
        int centerR = row + s - 1;
        int centerC = col + s - 1;

        // Top-left quadrant
        if (missingRow < row + s && missingCol < col + s) {
            tileBoard(tiling, row, col, s, missingRow, missingCol);
        } else {
            tiling.getTrominoId()[centerR][centerC] = currentTrominoId;
            tromino.addCell(centerR, centerC);
            tileBoard(tiling, row, col, s, centerR, centerC);
        }

        // Top-right quadrant
        if (missingRow < row + s && missingCol >= col + s) {
            tileBoard(tiling, row, col + s, s, missingRow, missingCol);
        } else {
            tiling.getTrominoId()[centerR][centerC + 1] = currentTrominoId;
            tromino.addCell(centerR, centerC + 1);
            tileBoard(tiling, row, col + s, s, centerR, centerC + 1);
        }

        // Bottom-left quadrant
        if (missingRow >= row + s && missingCol < col + s) {
            tileBoard(tiling, row + s, col, s, missingRow, missingCol);
        } else {
            tiling.getTrominoId()[centerR + 1][centerC] = currentTrominoId;
            tromino.addCell(centerR + 1, centerC);
            tileBoard(tiling, row + s, col, s, centerR + 1, centerC);
        }

        // Bottom-right quadrant
        if (missingRow >= row + s && missingCol >= col + s) {
            tileBoard(tiling, row + s, col + s, s, missingRow, missingCol);
        } else {
            tiling.getTrominoId()[centerR + 1][centerC + 1] = currentTrominoId;
            tromino.addCell(centerR + 1, centerC + 1);
            tileBoard(tiling, row + s, col + s, s, centerR + 1, centerC + 1);
        }
    
        tiling.addTromino(tromino);
    }
    
    /**
     * Build a detailed undirected graph representation of tromino adjacencies
     */
    private Map<Integer, List<Integer>> buildAdjacencyGraph(TrominoTiling tiling) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[][] board = tiling.getTrominoId();
        int size = tiling.getSize();
        
        // Initialize adjacency lists for each tromino
        for (TrominoTiling.Tromino tromino : tiling.getTrominoes()) {
            graph.put(tromino.getId(), new ArrayList<>());
        }
        
        // Check horizontal adjacencies
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size-1; c++) {
                int id1 = board[r][c];
                int id2 = board[r][c+1];
                
                if (id1 != 0 && id2 != 0 && id1 != id2) {
                    if (!graph.get(id1).contains(id2)) {
                        graph.get(id1).add(id2);
                    }
                    if (!graph.get(id2).contains(id1)) {
                        graph.get(id2).add(id1);
                    }
                }
            }
        }
        
        // Check vertical adjacencies
        for (int r = 0; r < size-1; r++) {
            for (int c = 0; c < size; c++) {
                int id1 = board[r][c];
                int id2 = board[r+1][c];
                
                if (id1 != 0 && id2 != 0 && id1 != id2) {
                    if (!graph.get(id1).contains(id2)) {
                        graph.get(id1).add(id2);
                    }
                    if (!graph.get(id2).contains(id1)) {
                        graph.get(id2).add(id1);
                    }
                }
            }
        }
        
        return graph;
    }
    
    // 
    private void guaranteedColoring(TrominoTiling tiling) {
        Map<Integer, List<Integer>> graph = buildAdjacencyGraph(tiling);
        Map<Integer, Integer> colors = new HashMap<>();
        
        // A list of tromino IDs to color
        List<Integer> trominoIds = new ArrayList<>();
        for (TrominoTiling.Tromino tromino : tiling.getTrominoes()) {
            trominoIds.add(tromino.getId());
        }
        
        welshPowellColoring(graph, colors, trominoIds);
        applyColorsToBoard(tiling, colors);
    }
    
    /**
     * Welsh-Powell graph coloring algorithm
     * 1. Sort vertices by degree (number of neighbors) in descending order
     * 2. Assign the first available color to each vertex that doesn't conflict with neighbors
     */
    private void welshPowellColoring(Map<Integer, List<Integer>> graph, Map<Integer, Integer> colors, List<Integer> trominoIds) {
        // Sort vertices by degree (descending)
        trominoIds.sort((id1, id2) -> graph.get(id2).size() - graph.get(id1).size());
        
        // Color each vertex
        for (Integer id : trominoIds) {
            // Find the first available color that doesn't conflict with neighbors
            boolean[] usedColors = new boolean[4]; // We use 1-3, index 0 ignored
            
            for (Integer neighbor : graph.get(id)) {
                if (colors.containsKey(neighbor)) {
                    int neighborColor = colors.get(neighbor);
                    if (neighborColor > 0 && neighborColor < 4) {
                        usedColors[neighborColor] = true;
                    }
                }
            }
            
            // Find the first available color
            int color = 1;
            while (color <= 3 && usedColors[color]) {
                color++;
            }
            colors.put(id, color);
        }
    }
    
    private void applyColorsToBoard(TrominoTiling tiling, Map<Integer, Integer> colors) {
        for (TrominoTiling.Tromino tromino : tiling.getTrominoes()) {
            int id = tromino.getId();
            int color = colors.getOrDefault(id, 1);
            for (int[] cell : tromino.getCells()) {
                tiling.getBoard()[cell[0]][cell[1]] = color;
            }
        }
    }
    
    /**
     * Verify that the coloring is valid (no adjacent trominoes have same color)
     * Used for testing purposes
     */
    public boolean verifyColoring(TrominoTiling tiling) {
        int[][] board = tiling.getBoard();
        int[][] idBoard = tiling.getTrominoId();
        int size = tiling.getSize();
        
        // Check horizontal adjacencies
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size-1; c++) {
                int id1 = idBoard[r][c];
                int id2 = idBoard[r][c+1];
                
                if (id1 != 0 && id2 != 0 && id1 != id2) {
                    if (board[r][c] == board[r][c+1]) {
                        return false; // Found adjacent trominoes with same color
                    }
                }
            }
        }
        
        // Check vertical adjacencies
        for (int r = 0; r < size-1; r++) {
            for (int c = 0; c < size; c++) {
                int id1 = idBoard[r][c];
                int id2 = idBoard[r+1][c];
                
                if (id1 != 0 && id2 != 0 && id1 != id2) {
                    if (board[r][c] == board[r+1][c]) {
                        return false; // Found adjacent trominoes with same color
                    }
                }
            }
        }
        return true;
    }
}