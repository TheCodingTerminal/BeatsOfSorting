package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class GnomeSort extends Sort {

    public GnomeSort(SortArray array) {
        name = "Gnome Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new GnomeSort(array);
    }

    private void gnomeSort(SortArray array) {
        int len = array.getSize();

        for (int i = 1; i < len; ) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            if (array.getValue(i, this) >= array.getValue(i - 1, this)) {
                i++;
            } else {
                array.swap(i, i - 1);
                if (i > 1) {
                    i--;
                }
            }

            soundCompare(i, i - 1);
        }
    }

    @Override
    public Void call() {
        gnomeSort(array);
        unmarkall_finish();

        return null;
    }
}
