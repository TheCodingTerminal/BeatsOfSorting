package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class HeapSort extends Sort {

    public HeapSort(SortArray array) {
        name = "Heap Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new HeapSort(array);
    }

    int prevPowerOfTwo(int x) {
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x - (x >> 1);
    }

    private void heapSort(SortArray array) {
        int n = array.getSize();
        for (int i = n / 2 - 1; i < n; i++) {
            if (isCancelled()) {
                return;
            }

            checkStatus();

            array.mark(i, (int) Math.floor(Math.log(prevPowerOfTwo(i + 1)) / Math.log(2)) + 4);
        }
        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            if (isCancelled()) {
                return;
            }

            checkStatus();

            heapify(array, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i >= 0; i--) {
            if (isCancelled()) {
                return;
            }

            checkStatus();

            // Move current root to end
            swapN(0, i);
            array.mark(i);
            if (i + 1 < n) {
                array.unmark(i + 1);
            }

            // call max heapify on the reduced heap
            heapify(array, i, 0);
        }
    }

    private void heapify(SortArray array, int n, int i) {
        if (isCancelled()) {
            return;
        }
        checkStatus();
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && array.getValue(l, this) > array.getValue(largest, this)) {
            largest = l;
        }
        soundCompare(l, largest);

        // If right child is larger than largest so far
        if (r < n && array.getValue(r, this) > array.getValue(largest, this)) {
            largest = r;
        }
        soundCompare(r, largest);

        // If largest is not root
        if (largest != i) {
            swapN(i, largest);

            // Recursively heapify the affected sub-tree
            heapify(array, n, largest);
        }
        array.mark(i, (int) Math.floor(Math.log(prevPowerOfTwo(i + 1)) / Math.log(2)) + 4);
    }

    @Override
    public Void call() {
        heapSort(array);
        unmarkall_finish();

        return null;
    }
}
