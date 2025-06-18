import java.util.Scanner;

public class FloydWarshallExtended {
    final static int INF = 99999; 

    // Function to implement Floyd Warshall Algorithm
    static void floydWarshall(int graph[][], int V) {
        int[][] dist = new int[V][V]; // Stores shortest distances
        int[][] next = new int[V][V]; // Stores path reconstruction info

        // Initialize the solution matrix same as the input graph matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];

                // If there is a direct edge, store the next vertex in the path
                if (graph[i][j] != INF && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1; // No path exists initially
                }
            }
        }

        // Compute shortest paths using Floyd-Warshall Algorithm
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k]; // Update path
                    }
                }
            }
        }

        // Check for negative weight cycle
        for (int i = 0; i < V; i++) {
            if (dist[i][i] < 0) {
                System.out.println("Graph contains a negative weight cycle!");
                return;
            }
        }

        // Print the final shortest distances
        printSolution(dist, V);

        // Allow user to retrieve the shortest path between two nodes
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDo you want to find the shortest path between two vertices? (yes/no)");
        String response = scanner.next();

        while (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter source vertex (0 to " + (V - 1) + "): ");
            int src = scanner.nextInt();
            System.out.print("Enter destination vertex (0 to " + (V - 1) + "): ");
            int dest = scanner.nextInt();

            if (src < 0 || src >= V || dest < 0 || dest >= V) {
                System.out.println("Invalid vertices! Please enter valid values.");
            } else {
                printPath(next, src, dest);
            }

            System.out.println("\nDo you want to check another path? (yes/no)");
            response = scanner.next();
        }
        scanner.close();
    }

    // Function to print the shortest distance matrix
    static void printSolution(int dist[][], int V) {
        System.out.println("The shortest distances between every pair of vertices:");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Function to print the shortest path between two vertices
    static void printPath(int[][] next, int src, int dest) {
        if (next[src][dest] == -1) {
            System.out.println("No path exists between " + src + " and " + dest);
            return;
        }

        System.out.print("Shortest path from " + src + " to " + dest + ": " + src);
        while (src != dest) {
            src = next[src][dest];
            System.out.print(" -> " + src);
        }
        System.out.println();
    }

    // Main function to take input and execute Floyd-Warshall
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input number of vertices
        System.out.print("Enter the number of vertices: ");
        int V = scanner.nextInt();

        int[][] graph = new int[V][V];

        // Taking input for the adjacency matrix
        System.out.println("Enter the adjacency matrix (use " + INF + " for infinity):");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = scanner.nextInt();
            }
        }

        // Run Floyd-Warshall Algorithm
        floydWarshall(graph, V);

        scanner.close();
    }
}
