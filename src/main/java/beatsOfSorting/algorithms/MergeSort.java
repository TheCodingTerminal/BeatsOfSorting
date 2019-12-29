package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import javafx.scene.paint.Color;

public class MergeSort extends Sort {

    public MergeSort(SortArray array) {
        name = "Merge Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new MergeSort(array);
    }

    private int[] getSubArray(SortArray array, int begin, int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            if (isCancelled()) {
                return null;
            }
            arr[i] = array.getValue(begin + i, this);
        }
        return arr;
    }

    private void merge(SortArray array, int low, int middle, int high) {
        if (isCancelled()) {
            return;
        }
        checkStatus();
        array.mark(low);
        array.mark(middle, Color.BLUE);
        array.mark(high - 1, Color.YELLOW);

        int leftSize = middle - low + 1;
        int rightSize = high - middle;

        SortArray leftArray = new SortArray(getSubArray(array, low, leftSize));
        SortArray rightArray = new SortArray(getSubArray(array, middle + 1, rightSize));

        int i = 0, j = 0, k = low;

        while (i < leftSize && j < rightSize) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            int ai = leftArray.getValue(i, this), aj = rightArray.getValue(j, this);

            if (ai <= aj) {
                array.setValue(k++, ai, this);
                soundCompare(i++, j);
            } else {
                array.setValue(k++, aj, this);
                soundCompare(i, j++);
            }
            array.swap++;
        }

        while (i < leftSize) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            array.setValue(k++, leftArray.getValue(i++, this), this);
        }

        while (j < rightSize) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            array.setValue(k++, rightArray.getValue(j++, this), this);
        }
        array.unmark(middle);
        array.unmark(low);
        array.unmark(high - 1);

        if (isCancelled()) {
            return;
        }
        checkStatus();
    }

    private void mergeSort(SortArray array, int low, int high) {
        if (isCancelled()) {
            return;
        }
        checkStatus();

        if (low < high) {
            int middle = (low + high) / 2;

            mergeSort(array, low, middle);
            mergeSort(array, middle + 1, high);

            merge(array, low, middle, high);
            if (isCancelled()) {
                return;
            }
            checkStatus();
        }
    }

    @Override
    public Void call() {
        mergeSort(array, 0, array.getSize() - 1);
        unmarkall_finish();

        return null;
    }
}
