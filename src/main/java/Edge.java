public class Edge {
    private final GraphObj target;
    private int weight;

    public Edge(GraphObj target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    public Edge(GraphObj target) {
        this.target = target;
        this.weight = 0;
    }

    public GraphObj getTarget() { return target; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }
}