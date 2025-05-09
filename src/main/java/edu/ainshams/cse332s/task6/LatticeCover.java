package edu.ainshams.cse332s.task6;

import java.util.*;

public class LatticeSolver {

    record Point(int x, int y) {}
    record Segment(Point start, Point end) {}

    private int n;
    private Set<Point> allLatticePoints;
    private final int TARGET_LINES;
    private Map<Integer, Point> pointIndexMap; // Optional: Map index to Point for bitmask state
    private Map<Point, Integer> indexPointMap; // Optional: Map Point to index for bitmask state


    public LatticeSolver(int n) {
        if (n <= 2) {
            throw new IllegalArgumentException("n must be greater than 2");
        }
        this.n = n;
        this.TARGET_LINES = 2 * n - 2;
        this.allLatticePoints = generateLattice(n);
        // Optional: Initialize maps for bitmask approach
        initializePointIndexMaps();
    }

    private Set<Point> generateLattice(int size) {
        Set<Point> points = new HashSet<>();
        for (int i = 1; i <= size; i++) {
            for (int j = 1; j <= size; j++) {
                points.add(new Point(i, j));
            }
        }
        return points;
    }

     private void initializePointIndexMaps() {
        pointIndexMap = new HashMap<>();
        indexPointMap = new HashMap<>();
        int index = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                Point p = new Point(i, j);
                pointIndexMap.put(index, p);
                indexPointMap.put(p, index);
                index++;
            }
        }
    }


    public List<Segment> solve() {
        List<Point> potentialStartPoints = generatePotentialStartPoints(); // Includes lattice + relevant external points
        List<Point> potentialEndPoints = generatePotentialEndPoints(); // Includes lattice + relevant external points

        for (Point p0 : potentialStartPoints) {
            for (Point p1 : potentialEndPoints) {
                if (p0.equals(p1)) continue;

                Segment startSegment = new Segment(p0, p1);
                Set<Point> initialCovered = getPointsOnSegment(startSegment);

                if (!initialCovered.isEmpty()) {
                    List<Segment> path = new ArrayList<>();
                    path.add(startSegment);
                    Set<Point> covered = new HashSet<>(initialCovered);

                    // // --- Using Set for covered points ---
                    // if (backtrackSet(p1, path, covered, 1)) {
                    //     return path; // Solution found
                    // }

                    // --- Using Bitmask for covered points ---
                    int initialState = 0;
                    for (Point p : initialCovered) {
                         if (indexPointMap.containsKey(p)) { // Only consider lattice points for mask
                            initialState |= (1 << indexPointMap.get(p));
                         }
                    }
                    Map<Pair<Integer, Point>, Boolean> memo = new HashMap<>(); // Memoization for bitmask approach
                    if (backtrackMask(p1, path, initialState, 1, memo)) {
                         return path; // Solution found
                    }
                }
            }
        }
        return null; // No solution found
    }

    // Backtracking using Set (simpler state, potentially slower)
    private boolean backtrackSet(Point currentPoint, List<Segment> path, Set<Point> coveredPoints, int linesUsed) {
        if (coveredPoints.size() == n * n && linesUsed == TARGET_LINES) {
            return true; // Success
        }
        if (linesUsed >= TARGET_LINES) {
            return false; // Too many lines
        }

        List<Point> candidateNextPoints = generateCandidatePoints(currentPoint, path);

        for (Point nextPoint : candidateNextPoints) {
             if (nextPoint.equals(currentPoint)) continue; // Avoid zero-length segments
            Segment newSegment = new Segment(currentPoint, nextPoint);

            // Avoid immediate retracing
            if (linesUsed > 0) {
                Segment lastSegment = path.get(path.size() - 1);
                if (nextPoint.equals(lastSegment.start()) && currentPoint.equals(lastSegment.end())) {
                    continue;
                }
            }

            Set<Point> newlyCoveredOnSegment = getPointsOnSegment(newSegment);
            Set<Point> newlyAddedToCoverage = new HashSet<>(); // Track points newly covered by this step

            for(Point p : newlyCoveredOnSegment) {
                 // Only consider actual lattice points for coverage goal
                if (allLatticePoints.contains(p)) {
                    if (coveredPoints.add(p)) { // add returns true if the element was not already present
                        newlyAddedToCoverage.add(p);
                    }
                }
            }

            path.add(newSegment); // Add segment to path

            if (backtrackSet(nextPoint, path, coveredPoints, linesUsed + 1)) {
                return true; // Solution found down this branch
            }

            // Backtrack: Remove segment and undo coverage changes
            path.remove(path.size() - 1);
            coveredPoints.removeAll(newlyAddedToCoverage); // Efficiently remove only the points added at this step
        }

        return false; // No solution found from this state
    }


     // Backtracking using Bitmask (more complex state, potentially faster with memoization)
    private record Pair<K, V>(K key, V value) {} // Simple Pair class for memoization key

    private boolean backtrackMask(Point currentPoint, List<Segment> path, int currentState, int linesUsed, Map<Pair<Integer, Point>, Boolean> memo) {
        int targetState = (1 << (n * n)) - 1;
        if (currentState == targetState && linesUsed == TARGET_LINES) {
            return true; // Success
        }
        if (linesUsed >= TARGET_LINES) {
            return false; // Too many lines
        }

        Pair<Integer, Point> memoKey = new Pair<>(currentState, currentPoint);
        if (memo.containsKey(memoKey)) {
            // TODO: Need careful handling of memoization result.
            // Returning memo.get(memoKey) might be wrong if the path matters.
            // For feasibility (can we reach the end?), it might work.
            // For finding *the* path, memoization is trickier.
            // Let's skip memoization for path finding for now.
            // return memo.get(memoKey);
        }


        List<Point> candidateNextPoints = generateCandidatePoints(currentPoint, path);

        for (Point nextPoint : candidateNextPoints) {
             if (nextPoint.equals(currentPoint)) continue;
            Segment newSegment = new Segment(currentPoint, nextPoint);

            // Avoid immediate retracing
            if (linesUsed > 0) {
                Segment lastSegment = path.get(path.size() - 1);
                if (nextPoint.equals(lastSegment.start()) && currentPoint.equals(lastSegment.end())) {
                    continue;
                }
            }

            Set<Point> newlyCoveredOnSegment = getPointsOnSegment(newSegment);
            int nextState = currentState;
            int bitsAdded = 0; // Track which bits were flipped for rollback

            for(Point p : newlyCoveredOnSegment) {
                if (indexPointMap.containsKey(p)) { // Is it a lattice point?
                    int pointIndex = indexPointMap.get(p);
                    int bit = (1 << pointIndex);
                    if ((currentState & bit) == 0) { // If not already covered
                        nextState |= bit;
                        bitsAdded |= bit; // Record the bit that was flipped on
                    }
                }
            }

            path.add(newSegment); // Add segment to path

            if (backtrackMask(nextPoint, path, nextState, linesUsed + 1, memo)) {
                // memo.put(memoKey, true); // Store success if found
                return true; // Solution found down this branch
            }

            // Backtrack: Remove segment and undo state changes
            path.remove(path.size() - 1);
            // currentState remains unchanged as nextState was a local var.
            // No explicit state rollback needed for currentState itself.
        }

        // memo.put(memoKey, false); // Store failure if no path found from here
        return false; // No solution found from this state
    }


    // --- Helper Methods ---

    private Set<Point> getPointsOnSegment(Segment segment) {
        // Crucial geometric calculation: Find all lattice points on the segment.
        Set<Point> points = new HashSet<>();
        Point p1 = segment.start();
        Point p2 = segment.end();

        // Handle vertical lines
        if (p1.x() == p2.x()) {
            int startY = Math.min(p1.y(), p2.y());
            int endY = Math.max(p1.y(), p2.y());
            for (Point p : this.allLatticePoints) {
                if (p.x() == p1.x() && p.y() >= startY && p.y() <= endY) {
                    points.add(p);
                }
            }
            return points;
        }

        // Handle horizontal lines
        if (p1.y() == p2.y()) {
            int startX = Math.min(p1.x(), p2.x());
            int endX = Math.max(p1.x(), p2.x());
            for (Point p : this.allLatticePoints) {
                if (p.y() == p1.y() && p.x() >= startX && p.x() <= endX) {
                    points.add(p);
                }
            }
            return points;
        }

        // Handle diagonal/other lines using cross-product for collinearity
        long dx = p2.x() - p1.x();
        long dy = p2.y() - p1.y();

        for (Point p : this.allLatticePoints) {
            // Check collinearity: (p.y - p1.y) * dx == (p.x - p1.x) * dy
            long cross_product_check = (long)(p.y() - p1.y()) * dx - (long)(p.x() - p1.x()) * dy;

            if (cross_product_check == 0) {
                // Check if p lies between p1 and p2 (inclusive)
                boolean in_x_bounds = (dx > 0)? (p.x() >= p1.x() && p.x() <= p2.x()) : (p.x() >= p2.x() && p.x() <= p1.x());
                boolean in_y_bounds = (dy > 0)? (p.y() >= p1.y() && p.y() <= p2.y()) : (p.y() >= p2.y() && p.y() <= p1.y());

                if (in_x_bounds && in_y_bounds) {
                    points.add(p);
                }
            }
        }
        return points;
    }


    private List<Point> generateCandidatePoints(Point currentPoint, List<Segment> path) {
        // *** This is the most critical part for performance and finding solutions ***
        // Strategy: Consider points that align with currentPoint and other lattice points?
        // Consider predefined external points? (e.g., (0,0), (n+1,0), (0,n+1), (n+1,n+1), etc.)
        // Needs careful design based on known solution patterns or extensive search space definition.
        List<Point> candidates = new ArrayList<>();
        // Example strategy: Try connecting to all other lattice points + a few key external points
        // Add all points in allLatticePoints (excluding currentPoint)
        for(Point p : allLatticePoints) {
            if (!p.equals(currentPoint)) candidates.add(p);
        }
        // Add strategic external points (example: one unit outside corners/edges)
        for(int i = 0; i <= n + 1; i++) {
             // Add points along extended grid lines
            candidates.add(new Point(i, 0)); candidates.add(new Point(i, n + 1));
            candidates.add(new Point(0, i)); candidates.add(new Point(n + 1, i));
             // Add points potentially further out for diagonal sweeps? (e.g., needed for n=3 solution)
            candidates.add(new Point(-1, i)); candidates.add(new Point(n + 2, i));
            candidates.add(new Point(i, -1)); candidates.add(new Point(i, n + 2));
        }
         // Add corner points further out
        candidates.add(new Point(0,0)); candidates.add(new Point(n+1, 0));
        candidates.add(new Point(0, n+1)); candidates.add(new Point(n+1, n+1));
        candidates.add(new Point(-1,-1)); candidates.add(new Point(n+2, -1));
        candidates.add(new Point(-1, n+2)); candidates.add(new Point(n+2, n+2));


        // Remove duplicates and the current point itself
        Set<Point> uniqueCandidates = new HashSet<>(candidates);
        uniqueCandidates.remove(currentPoint);

        return new ArrayList<>(uniqueCandidates); // Return unique candidates
    }

    private List<Point> generatePotentialStartPoints() {
        // Similar to candidate generation, define where the path can start.
        // Could be all lattice points + strategic external points.
        // Let's use the same generator logic for simplicity, excluding a dummy point.
        return generateCandidatePoints(new Point(-100,-100), Collections.emptyList());
    }
     private List<Point> generatePotentialEndPoints() {
        // Similar logic for the second point of the first segment.
        return generateCandidatePoints(new Point(-100,-100), Collections.emptyList());
    }

    // Main method for testing
    public static void main(String args) {
        int n = 3; // Example: 3x3 grid
        if (args.length > 0) {
            try {
                n = Integer.parseInt(args);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input for n, using default n=3.");
            }
        }

        System.out.println("Attempting to solve for n=" + n);
        LatticeSolver solver = new LatticeSolver(n);
        long startTime = System.currentTimeMillis();
        List<Segment> solution = solver.solve();
        long endTime = System.currentTimeMillis();


        if (solution!= null) {
            System.out.println("Solution found for n=" + n + " with " + solution.size() + " lines:");
            for (Segment s : solution) {
                System.out.println("  Segment from (" + s.start().x() + "," + s.start().y() +
                                   ") to (" + s.end().x() + "," + s.end().y() + ")");
            }
             // Verification (optional but recommended)
            Set<Point> coveredBySolution = new HashSet<>();
            for(Segment s : solution) {
                coveredBySolution.addAll(solver.getPointsOnSegment(s));
            }
            boolean allCovered = true;
            for(Point p : solver.allLatticePoints) {
                if (!coveredBySolution.contains(p)) {
                    allCovered = false;
                    System.out.println("Error: Point " + p + " not covered!");
                }
            }
            if (allCovered && coveredBySolution.size() >= solver.allLatticePoints.size()) {
                 System.out.println("Verification: All " + solver.allLatticePoints.size() + " lattice points covered.");
            } else {
                 System.out.println("Verification Error: Not all points covered or issue with coverage check.");
                 System.out.println("Target: " + solver.allLatticePoints.size() + ", Actually Covered: " + coveredBySolution.size());

            }

        } else {
            System.out.println("No solution found for n=" + n);
        }
         System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }}
