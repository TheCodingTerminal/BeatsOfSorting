package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class QuickSortLomuto extends Sort {

    public QuickSortLomuto(SortArray array) {
        name = "Quick Sort Lomuto";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new QuickSortLomuto(array);
    }

    private void quickSortLomuto(SortArray array, int low, int high) {

        if (low + 1 < high) {
            int pivotPoint = partitionLomuto(array, low, high);
            quickSortLomuto(array, low, pivotPoint);
            quickSortLomuto(array, pivotPoint + 1, high);

            if (isCancelled()) {
                return;
            }
            checkStatus();
        }
    }

    private int partitionLomuto(SortArray array, int low, int high) {
        if (isCancelled()) {
            return high;
        }

        int pivotValue = array.getValue(high - 1, this);
        int i = low;
        array.mark(high - 1);

        checkStatus();

        for (int j = low; j < high - 1; j++) {
            if (isCancelled()) {
                return j;
            }
            checkStatus();
            if (array.getValue(j, this) <= pivotValue) {

                array.swap(i, j);
                i++;
            }
            soundCompare(j, high);
        }

        array.swap(i, high - 1);
        array.unmark(high - 1);
        return i;
    }

    @Override
    public Void call() {
        quickSortLomuto(array, 0, array.getSize());
        unmarkall_finish();
        return null;
    }
}
