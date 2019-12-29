package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import javafx.scene.paint.Color;

public class StoogeSort extends Sort {

    public StoogeSort(SortArray array) {
        name = "Stooge Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new StoogeSort(array);
    }

    private void stoogeSort(int low, int high) {
        if (isCancelled()) {
            return;
        }
        checkStatus();

        if (array.getValue(low, this) > array.getValue(high, this)) {
            if (isCancelled()) {
                return;
            }
            soundCompare(low, high);

            array.swap(low, high);

            checkStatus();
        }
        soundCompare(low, high);

        if (high - low + 1 >= 3) {
            if (isCancelled()) {
                return;
            }

            int t = (high - low + 1) / 3;

            array.mark(low, Color.BLUE);
            array.mark(high, Color.BLUE);

            checkStatus();

            stoogeSort(low, high - t);
            stoogeSort(low + t, high);
            stoogeSort(low, high - t);

            if (isCancelled()) {
                return;
            }

            checkStatus();

            array.unmark(low);
            array.unmark(high);
        }
    }

    @Override
    protected Void call() throws Exception {
        stoogeSort(0, array.getSize() - 1);
        unmarkall_finish();

        return null;
    }
}
