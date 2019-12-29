package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class InsertionSort extends Sort {

    public InsertionSort(SortArray array) {
        name = "Insertion Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new InsertionSort(array);
    }

    private void insertionSort(SortArray array) {
        for (int i = 1; i < array.getSize(); i++) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            int key = array.getValue(i, this);
            array.mark(i);

            int j = i - 1;

            while (j >= 0 && array.getValue(j, this) > key) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();
                soundCompare(j, i);

                array.swap(j, j + 1);
                j--;
            }
            soundCompare(j, i);
            array.unmark(i);
        }

    }

    @Override
    protected Void call() {
        insertionSort(array);
        unmarkall_finish();
        return null;
    }
}