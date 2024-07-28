package model;

public interface Algorithms<T extends Comparable<T>> {
    void sort(T[] array);

    default void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}