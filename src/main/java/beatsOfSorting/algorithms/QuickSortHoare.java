package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class QuickSortHoare extends Sort {

    public QuickSortHoare(SortArray array) {
        name = "Quick Sort Hoare";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new QuickSortHoare(array);
    }

    private int partitionHoare(SortArray array, int low, int high) {
        if (isCancelled()) {
            return low;
        }
        int pivot = array.getValue(low, this);

        int i = low - 1, j = high + 1;

        array.mark(low);

        checkStatus();

        while (true) {
            if (isCancelled()) {
                return low;
            }

            checkStatus();
            // Find leftmost element greater
            // than or equal to pivot
            do {
                if (isCancelled()) {
                    return low;
                }
                soundCompare(i, low);

                checkStatus();
                i++;

            } while (array.getValue(i, this) < pivot);

            // Find rightmost element smaller
            // than or equal to pivot
            do {
                if (isCancelled()) {
                    return low;
                }
                soundCompare(j, low);

                checkStatus();
                j--;
            } while (array.getValue(j, this) > pivot);

            // If two pointers met.
            if (i >= j) {
                if (isCancelled()) {
                    return j;
                }
                checkStatus();

                array.unmark(low);
                return j;
            }

            array.swap(i, j);
            if (low == i)
                low = j;
            else if (low == j)
                low = i;

            array.mark(low);
        }
    }

    private void quickSortHoare(SortArray array, int low, int high) {
        if (low < high) {
            int pivotPoint = partitionHoare(array, low, high);
            quickSortHoare(array, low, pivotPoint);
            quickSortHoare(array, pivotPoint + 1, high);

            if (isCancelled()) {
                return;
            }
        }
    }

    @Override
    public Void call() {
        quickSortHoare(array, 0, array.getSize() - 1);
        unmarkall_finish();

        return null;
    }
}
