import java.util.*;

public class RotaPirenopolisSolver {
    public static void main(String[] args) {
        var apGoianiaNode = new Node<>(new City(0, "Ap. Goiania"));
        var goianiaNode = new Node<>(new City(1, "Goiania"));
        var trindadeNode = new Node<>(new City(2, "Trindade"));
        var senCanedoNode = new Node<>(new City(3, "Sen. Canedo"));
        var neropolisNode = new Node<>(new City(4, "Nerópolis"));
        var anapolisNode = new Node<>(new City(5, "Anápolis"));
        var pirenopolisNode = new Node<>(new City(6, "Pirenópolis"));

        apGoianiaNode.connect(goianiaNode);
        apGoianiaNode.connect(neropolisNode);
        apGoianiaNode.connect(senCanedoNode);

        goianiaNode.connect(apGoianiaNode);
        goianiaNode.connect(trindadeNode);
        goianiaNode.connect(neropolisNode);

        trindadeNode.connect(goianiaNode);

        senCanedoNode.connect(apGoianiaNode);
        senCanedoNode.connect(anapolisNode);

        neropolisNode.connect(apGoianiaNode);
        neropolisNode.connect(goianiaNode);
        neropolisNode.connect(anapolisNode);

        anapolisNode.connect(neropolisNode);
        anapolisNode.connect(senCanedoNode);
        anapolisNode.connect(pirenopolisNode);

        pirenopolisNode.connect(anapolisNode);

        var from = apGoianiaNode;
        var to = pirenopolisNode;

        List<Node<City>> path = getShortestPath(from, to);

        if (path == null) System.out.println("No path found");

        else {
            System.out.print("Shortest Path: ");
            for (var node : path) {
                System.out.printf("[%d] %s -> ", node.value.id(), node.value.name());
            }
            System.out.printf("[%d] %s\n", to.value.id(), to.value.name());
            System.out.println("Jumps: " + path.size());
        }
    }

    private static List<Node<City>> getShortestPath(Node<City> from, Node<City> to) {
        Queue<Node<City>> queue = new LinkedList<>();
        Map<Node<City>, List<Node<City>>> shortestPathByNode = new HashMap<>();

        queue.add(from);
        shortestPathByNode.put(from, new ArrayList<>());

        while (!queue.isEmpty()){
            var currentNode = queue.remove();

            System.out.printf("Current Node: [%d] %s\n", currentNode.value.id(), currentNode.value.name());

            if (currentNode.equals(to)) return shortestPathByNode.get(to);

            for (var connection: currentNode.getConnections()){
                if (!shortestPathByNode.containsKey(connection)) {
                    queue.add(connection);
                    shortestPathByNode.put(connection, new ArrayList<>(shortestPathByNode.get(currentNode)));
                    shortestPathByNode.get(connection).add(currentNode);
                }
            }

            System.out.print("Current Queue: ");
            for (var node : queue) {
                System.out.printf("[%d] %s | ", node.value.id(), node.value.name());
            }
            System.out.println();
            System.out.println();
        }

        return null;
    }


    public static record City(int id, String name) {}


    public static class Node<T> {
        public final T value;
        private final List<Node<T>> connections = new ArrayList<>();

        public Node(T value) {
            this.value = value;
        }

        public void connectBidirectional(Node<T> node){
            this.connections.add(node);
            node.connections.add(this);
        }

        public void connect(Node<T> node){
            this.connections.add(node);
        }

        public List<Node<T>> getConnections() {
            return connections;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) return false;
            Node<?> node = (Node<?>) object;

            return node.value.equals(this.value);
        }
    }
}
