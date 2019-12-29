package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class BubbleSort extends Sort {

    public BubbleSort(SortArray array) {
        name = "Bubble Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new BubbleSort(array);
    }

    private void bubbleSort(SortArray array) {
        int len = array.getSize();

        for (int i = 0; i < len - 1; i++) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            for (int j = 0; j < len - i - 1; j++) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();

                if (array.getValue(j, this) > array.getValue(j + 1, this)) {
                    array.swap(j, j + 1);
                }

                soundCompare(j, j + 1);
            }
        }
    }

    @Override
    public Void call() {
        bubbleSort(array);
        unmarkall_finish();

        return null;
    }
}
