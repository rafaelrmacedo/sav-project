package algorithms;

import model.Algorithms;

public class SelectionSort<T extends Comparable<T>> implements Algorithms<T> {

    @Override
    public void sort(T[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[minIdx]) < 0) {
                    minIdx = j;
                }
            }

            T temp = array[minIdx];
            array[minIdx] = array[i];
            array[i] = temp;
        }
    }
}
