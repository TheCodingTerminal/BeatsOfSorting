package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class CombSort extends Sort {

    public CombSort(SortArray array) {
        name = "Comb Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new CombSort(array);
    }

    private void combSort(SortArray array) {
        int len = array.getSize();
        double shrink = 1.3;
        boolean swapped = false;
        int gap = len;

        while (gap > 1 || swapped) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            if (gap > 1) {
                gap = (int) ((float) gap / shrink);
            }
            swapped = false;

            for (int i = 0; gap + i < len; i++) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();

                if (array.getValue(i, this) > array.getValue(i + gap, this)) {
                    array.swap(i, i + gap);
                    swapped = true;
                }
                soundCompare(i, i + gap);
            }
        }
    }

    @Override
    public Void call() {
        combSort(array);
        unmarkall_finish();

        return null;
    }
}
