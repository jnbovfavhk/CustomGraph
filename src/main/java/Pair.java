import java.util.List;
import java.util.Objects;

public class Pair {
    private GraphObj left;
    private List<Edge> right;

    // Обязательно пустой конструктор для Gson
    public Pair() {
    }

    public Pair(GraphObj left, List<Edge> right) {
        this.left = left;
        this.right = right;
    }

    // Геттеры (обязательно для Gson)
    public GraphObj getLeft() {
        return left;
    }

    // Сеттеры (обязательно для Gson)
    public void setLeft(GraphObj left) {
        this.left = left;
    }

    public List<Edge> getRight() {
        return right;
    }

    public void setRight(List<Edge> right) {
        this.right = right;
    }

    // Для удобства - методы как в MutablePair
    public GraphObj getKey() {
        return left;
    }

    public List<Edge> getValue() {
        return right;
    }

    public void setKey(GraphObj left) {
        this.left = left;
    }

    public void setValue(List<Edge> right) {
        this.right = right;
    }

    // equals и hashCode для корректной работы в коллекциях
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(left, pair.left) &&
                Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "left=" + (left != null ? left.getName() : "null") +
                ", right=" + (right != null ? right.size() + " edges" : "null") +
                '}';
    }
}
