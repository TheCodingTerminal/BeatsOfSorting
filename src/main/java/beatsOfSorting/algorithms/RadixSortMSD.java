package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class RadixSortMSD extends Sort {

    public RadixSortMSD(SortArray array) {
        name = "Radix Sort MSD";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new RadixSortMSD(array);
    }

    private int[] fillPrefixSum(int[] arr, int indexStart, int indexEnd, int[] prefixSum, int prefixBegin) {
        int[] result = prefixSum;
        int i = indexStart;
        int j = prefixBegin;
        if (indexStart != indexEnd) {
            int val = arr[indexStart];
            prefixSum[j++] = val;
            while (++i != indexEnd) {
                if (isCancelled()) {
                    result = null;
                    break;
                }
                checkStatus();
                val = val + arr[i];
                prefixSum[j++] = val;
            }
        }
        return result;
    }

    private void radixSortMSD(SortArray array, int low, int high, int depth) {
        if (isCancelled()) {
            return;
        }
        checkStatus();

        array.mark(low);
        array.mark(high - 1);

        int RADIX = 10;
        int[] count = new int[RADIX];
        Arrays.fill(count, 0);

        int pmax = (int) Math.floor(Math.log(array.getMax() + 1) / Math.log(RADIX));

        int base = (int) Math.pow(RADIX, pmax - depth);

        // Store count of occurrences in count[]
        for (int i = low; i < high; i++) {
            if (isCancelled()) {
                return;
            }

            array.mark(i);
            count[((array.getValueSound(i, this)) / base) % RADIX]++;
            checkStatus();
            array.unmark(i);
        }

        int[] prefixSum = new int[RADIX];
        Arrays.fill(prefixSum, 0);

        prefixSum = fillPrefixSum(count, 0, count.length, prefixSum, 0);

        for (int value : prefixSum) {
            if (isCancelled()) {
                return;
            }

            if (value == 0)
                continue;

            array.mark(low + value - 1, Color.MEDIUMTURQUOISE);
            checkStatus();
        }

        // reorder items in-place by walking cycles
        for (int i = 0, j; i < (high - low); ) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            while ((j = --prefixSum[array.getValueSound(low + i, this) / base % RADIX]) > i) {
                if (isCancelled()) {
                    return;
                }
                checkStatus();

                array.swap(low + i, low + j);
            }
            i += count[array.getValueSound(low + i, this) / base % RADIX];
        }

        array.unmarkall();

        // no more depth to sort?
        if (depth + 1 > pmax)
            return;

        // recurse on buckets
        int sum = low;
        for (int i = 0; i < RADIX; i++) {
            if (isCancelled()) {
                return;
            }
            checkStatus();

            if (count[i] > 1)
                radixSortMSD(array, sum, sum + count[i], depth + 1);
            sum += count[i];
        }
    }

    @Override
    public Void call() {
        radixSortMSD(array, 0, array.getSize(), 0);
        unmarkall_finish();

        return null;
    }
}
