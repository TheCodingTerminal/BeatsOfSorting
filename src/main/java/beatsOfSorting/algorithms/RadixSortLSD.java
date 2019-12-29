package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class RadixSortLSD extends Sort {

    public RadixSortLSD(SortArray array) {
        name = "Radix Sort LSD";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new RadixSortLSD(array);
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

    // A function to do counting sort of array according to
    // the digit represented by exp.
    private void countSort(SortArray array, int len, int exp, int radix) {
        int i;
        int[] count = new int[10];
        Arrays.fill(count, 0);
        int base = (int) Math.pow(radix, exp);

        // Store count of occurrences in count[]
        SortArray copy = new SortArray(array);
        for (i = 0; i < len; i++) {
            if (isCancelled()) {
                return;
            }

            array.mark(i);
            count[((array.getValueSound(i, this)) / base) % radix]++;
            checkStatus();
            array.unmark(i);
        }

        int[] prefixSum = new int[radix + 1];
        Arrays.fill(prefixSum, 0);

        prefixSum = fillPrefixSum(count, 0, count.length, prefixSum, 1);

        for (int i1 = 0; i1 < prefixSum.length - 1; i1++) {
            if (isCancelled()) {
                return;
            }

            if (prefixSum[i1] >= array.getSize()) {
                continue;
            }

            array.mark(prefixSum[i1], Color.MEDIUMTURQUOISE);
            checkStatus();
        }

        // Copy the output array to array, so that array now
        // contains sorted numbers according to current digit
        for (i = 0; i < len; i++) {
            if (isCancelled()) {
                return;
            }

            int r = (copy.getValueSound(i, this) / base) % radix;

            array.setValue(prefixSum[r]++, copy.getValueDirect(i), this);
            checkStatus();
        }
        array.unmarkall();
    }

    // The main function to that sorts array of size n using
    // Radix Sort
    public void radixSort(SortArray array) {
        int len = array.getSize();

        int RADIX = 10;
        int pmax = (int) Math.ceil(Math.log(array.getMax() + 1) / Math.log(RADIX));

        // Do counting sort for every digit. Note that instead
        // of passing digit number, exp is passed. exp is 10^i
        for (int exp = 0; exp < pmax; exp++) {
            if (isCancelled()) {
                return;
            }

            countSort(array, len, exp, RADIX);

            checkStatus();
        }
    }

    @Override
    protected Void call() throws Exception {
        radixSort(array);
        unmarkall_finish();

        return null;
    }
}
