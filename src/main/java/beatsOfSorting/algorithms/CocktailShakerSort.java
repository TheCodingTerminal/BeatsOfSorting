package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;

public class CocktailShakerSort extends Sort {

    public CocktailShakerSort(SortArray array) {
        name = "Cocktail Shaker Sort";
        this.array = array;
        isPaused.set(false);
    }

    @Override
    public Sort createNewSort() {
        return new CocktailShakerSort(array);
    }

    private void cocktailShakerSort(SortArray array) {
        int lo = 0, hi = array.getSize() - 1, mov = lo;

        while (lo < hi) {
            if (isCancelled()) {
                return;
            }
            checkStatus();
            for (int i = hi; i > lo; --i) {
                if (isCancelled()) {
                    return;
                }

                if (array.getValue(i - 1, this) > array.getValue(i, this)) {
                    array.swap(i - 1, i);
                    mov = i;
                }
                soundCompare(i - 1, i);
                checkStatus();
            }

            lo = mov;

            for (int i = lo; i < hi; ++i) {
                if (isCancelled()) {
                    return;
                }

                if (array.getValue(i, this) > array.getValue(i + 1, this)) {
                    array.swap(i, i + 1);
                    mov = i;
                }
                soundCompare(i, i + 1);

                checkStatus();
            }

            hi = mov;
        }
    }

    @Override
    public Void call() {
        cocktailShakerSort(array);
        unmarkall_finish();

        return null;
    }
}
