import java.util.*;

public class QuebraCabecaDeslizanteSolver {
    public static void main(String[] args) {
        int[][] initialBoard = {
                {7, 2, 4},
                {5, 0, 6},
                {8, 3, 1}
        };

        int[][] goalBoard = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8}
        };

        Node initialNode = new Node(initialBoard);
        Node goalNode = new Node(goalBoard);

        List<Node> path = breadthFirstSearch(initialNode, goalNode);

        if (path == null) {
            System.out.println("No path found");
            return;
        }

        for (Node node : path) {
            for (int[] row : node.value) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
        }

        System.out.println("Movements: " + (path.size()-1));
    }


    public static List<Node> breadthFirstSearch(Node from, Node to) {
        Map<Node, List<Node>> shortestPathByNode = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(from);
        shortestPathByNode.put(from, new ArrayList<>());

        var operations = 0;

        while (!queue.isEmpty()){
            var currentNode = queue.remove();

            operations++;

            if (Arrays.deepEquals(currentNode.value, to.value)) {
                System.out.println("Iterations: " + operations);
                shortestPathByNode.get(to).add(currentNode);
                return shortestPathByNode.get(to);
            }

            for (var neighbor: retrieveNeighborhood(currentNode)){
                if (!shortestPathByNode.containsKey(neighbor)) {
                    currentNode.connect(neighbor);
                    queue.add(neighbor);
                    shortestPathByNode.put(neighbor, new ArrayList<>(shortestPathByNode.get(currentNode)));
                    shortestPathByNode.get(neighbor).add(currentNode);
                }
            }
        }

        return null;
    }

    private static List<Node> retrieveNeighborhood(Node currentNode) {
        List<Node> neighborhood = new ArrayList<>();

        Point zeroPoint = getZeroPoint(currentNode.value);

        if (zeroPoint == null)
            throw new RuntimeException("Zero point not found");

//        Swap to left
        if (zeroPoint.x > 0){
            var newNode = new Node(
                    swapValues(
                            currentNode.value,
                            zeroPoint,
                            new Point(zeroPoint.x - 1, zeroPoint.y)
                    )
            );

            newNode.connect(currentNode);

            neighborhood.add(newNode);
        }

//        Swap to right
        if (zeroPoint.x < currentNode.value[0].length - 1){
            var newNode = new Node(
                    swapValues(
                            currentNode.value,
                            zeroPoint,
                            new Point(zeroPoint.x + 1, zeroPoint.y)
                    )
            );
            newNode.connect(currentNode);

            neighborhood.add(newNode);
        }

//        Swap to up
        if (zeroPoint.y > 0){
            var newNode = new Node(
                    swapValues(
                            currentNode.value,
                            zeroPoint,
                            new Point(zeroPoint.x, zeroPoint.y - 1)
                    )
            );
            newNode.connect(currentNode);

            neighborhood.add(newNode);
        }

//        Swap to down
        if (zeroPoint.y < currentNode.value.length - 1){
            var newNode = new Node(
                    swapValues(
                            currentNode.value,
                            zeroPoint,
                            new Point(zeroPoint.x, zeroPoint.y + 1)
                    )
            );
            newNode.connect(currentNode);

            neighborhood.add(newNode);
        }

        return neighborhood;
    }

    private static Point getZeroPoint(int[][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    private static int[][] swapValues(int[][] value, Point zeroPoint, Point goalPoint) {
        int[][] copyMatrix = new int[][]{
                {value[0][0], value[0][1], value[0][2]},
                {value[1][0], value[1][1], value[1][2]},
                {value[2][0], value[2][1], value[2][2]}
        };

        int goalPointElement = copyMatrix[goalPoint.y][goalPoint.x];

        copyMatrix[goalPoint.y][goalPoint.x] = 0;
        copyMatrix[zeroPoint.y][zeroPoint.x] = goalPointElement;

        return copyMatrix;
    }

    public static record Point (int x, int y){}

    public static class Node {
        public final int[][] value;
        private final List<Node> connections = new ArrayList<>();

        public Node(int[][] value) {
            this.value = value;
        }

        public void connect(Node node){
            this.connections.add(node);
        }

        public List<Node> getConnections() {
            return connections;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            Node node = (Node) object;

            return Arrays.deepEquals(node.value, this.value);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(value);
        }
    }
}
