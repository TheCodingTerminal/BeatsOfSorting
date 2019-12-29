package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class ShellSort extends Sort {

    public ShellSort(SortArray array) {
        name = "Shell Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new ShellSort(array);
    }

    private void shellSort(SortArray array) {
        int len = array.getSize();

        for (int gap = len / 2; gap > 0; gap /= 2) {
            if (isCancelled()) {
                return;
            }

            checkStatus();

            for (int i = gap; i < len; i++) {
                if (isCancelled()) {
                    return;
                }

                checkStatus();

                int temp = array.getValue(i, this);

                int j;

                for (j = i; j >= gap && array.getValue(j - gap, this) > temp; j -= gap) {
                    if (isCancelled()) {
                        return;
                    }

                    checkStatus();
                    array.setValue(j, array.getValue(j - gap, this), this);
                    soundCompare(j, j - gap);
                }

                array.setValue(j, temp, this);
                soundCompare(j, temp);
            }
        }
    }

    @Override
    protected Void call() {
        shellSort(array);
        unmarkall_finish();

        return null;
    }
}
