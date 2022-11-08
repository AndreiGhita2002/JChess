package jchess.util;

public record Pair<T1, T2>(T1 left, T2 right) {
    @Override
    public String toString() {
        return "Pair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
