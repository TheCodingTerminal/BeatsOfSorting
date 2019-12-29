package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class CycleSort extends Sort {

    public CycleSort(SortArray array) {
        name = "Cycle Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new CycleSort(array);
    }

    public void cycleSort(SortArray array, int n) {
        // traverse array elements and put it to the right place
        for (int cycle_start = 0; cycle_start <= n - 2; cycle_start++) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            // initialize item as starting point
            int item = array.getValue(cycle_start, this);

            // Find position where we put the item.
            // Count all smaller elements on right side of item.
            int pos = cycle_start;
            for (int i = cycle_start + 1; i < n; i++) {
                if (isCancelled()) {
                    return;
                }

                checkStatus();

                if (array.getValue(i, this) < item) {
                    pos++;
                }

                soundCompare(i, cycle_start);
            }

            // If item is already in correct position
            if (pos == cycle_start) {
                array.mark(pos, 2);
                continue;
            }
            soundCompare(pos, cycle_start);
            // Ignore all duplicate elements
            while (item == array.getValue(pos, this)) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();
                pos++;
                soundCompare(pos, cycle_start);
            }
            soundCompare(pos, cycle_start);

            // Put the item in right position
            if (pos != cycle_start) {
                int temp = item;
                item = array.getValue(pos, this);
                array.setValue(pos, temp, this);
                array.mark(pos, 2);
                array.swap++;
            }

            // Rotate rest of the cycle
            while (pos != cycle_start) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();
                pos = cycle_start;

                // Find position where we put the element
                for (int i = cycle_start + 1; i < n; i++) {
                    if (isCancelled()) {
                        return;
                    }
                    checkStatus();
                    if (array.getValue(i, this) < item) {
                        pos++;
                    }

                    soundCompare(i, cycle_start);
                }

                // Ignore all duplicate elements
                while (item == array.getValue(pos, this)) {
                    if (isCancelled()) {
                        return;
                    }

                    checkStatus();

                    pos++;

                    soundCompare(pos, cycle_start);
                }

                soundCompare(pos, cycle_start);

                // Put the item in right position
                if (item != array.getValue(pos, this)) {
                    int temp = item;
                    item = array.getValue(pos, this);
                    array.setValue(pos, temp, this);
                    array.mark(pos, 2);
                    array.swap++;
                }

                soundCompare(pos, cycle_start);
            }
        }
    }

    @Override
    public Void call() {
        cycleSort(array, array.getSize());
        unmarkall_finish();

        return null;
    }
}
