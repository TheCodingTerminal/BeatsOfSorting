package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import javafx.scene.paint.Color;

public class SelectionSort extends Sort {

    public SelectionSort(SortArray array) {
        name = "Selection Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new SelectionSort(array);
    }

    private void selectionSort(SortArray array) {
        int len = array.getSize();
        int minIndex;

        for (int i = 0; i < len - 1; i++) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            minIndex = i;
            array.mark(minIndex, Color.BLUE);

            for (int j = i + 1; j < len; j++) {
                if (isCancelled()) {
                    return;
                }

                array.mark(j);
                checkStatus();

                if (array.getValue(j, this) < array.getValue(minIndex, this)) {
                    array.markSwap(j, minIndex);
                    minIndex = j;
                }
                soundCompare(j, minIndex);

                // follow the minIndex if it's swapped
                if (minIndex != j) {
                    array.unmark(j);
                }
            }

            array.swap(i, minIndex);
            array.unmark(minIndex);

            if (i > 0) {
                array.unmark(i - 1);
            }
            array.mark(i, Color.BLUE);
        }
    }

    @Override
    protected Void call() {
        selectionSort(array);
        unmarkall_finish();

        return null;
    }
}
