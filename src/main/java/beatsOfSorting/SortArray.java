package beatsOfSorting;

import beatsOfSorting.algorithms.Sort;
import beatsOfSorting.sound.SoundPlayer;
import javafx.beans.NamedArg;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SortArray {

    private int[] array;
    public int arrayAccess;
    public int swap;
    public int comp;
    private Color[] barColors;
    private int lastIndex;
    private Color[] colors = {Color.RED, Color.BLUE, Color.LIME, Color.YELLOW, Color.ORANGE, Color.ALICEBLUE,
            Color.PINK, Color.INDIGO, Color.MAGENTA, Color.BROWN, Color.GOLD, Color.MEDIUMSLATEBLUE, Color.BEIGE,
            Color.TEAL, Color.LAVENDERBLUSH, Color.ROSYBROWN};

    public SortArray(@NamedArg("size") int size) {
        array = new int[size];
        barColors = new Color[size];
        arrayAccess = 0;
        lastIndex = -1;
        swap = 0;
        comp = 0;
    }

    public SortArray(int[] array) {
        this.array = array;
        barColors = new Color[array.length];
        arrayAccess = 0;
        lastIndex = -1;
        swap = 0;
        comp = 0;
    }

    public SortArray(SortArray sortArray) {
        array = new int[sortArray.array.length];
        System.arraycopy(sortArray.array, 0, array, 0, sortArray.array.length);
        arrayAccess = sortArray.arrayAccess;
        swap = sortArray.swap;
        comp = sortArray.comp;
        barColors = sortArray.barColors;
        lastIndex = -1;
    }

    public void initArray() {
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
            //barColors[i] = Color.hsb((float) i / array.length * 360.0, 1.0F, 1);
            barColors[i] = Color.GREEN;
        }
        arrayAccess = 0;
        lastIndex = -1;
        swap = 0;
        comp = 0;
    }

    public void setDescendingArray() {
        for (int i = array.length - 1; i >= 0; i--) {
            array[(array.length - 1) - i] = i;
            barColors[i] = Color.GREEN;
        }
        arrayAccess = 0;
        lastIndex = -1;
        swap = 0;
        comp = 0;
    }

    public void resetBarColors() {
        for (int i = 0; i < array.length; i++) {
            barColors[i] = Color.GREEN;
        }
    }

    public int getSize() {
        return array.length;
    }

    public int getValueSound(int i, Sort al) {
        al.doDelay();
        arrayAccess++;
        SoundPlayer.addAccessList(array[i]);
        return array[i];
    }

    public int getValue(int i, Sort al) {
        al.doDelay();
        arrayAccess++;

        return array[i];
    }

    public int getValueDirect(int i) {

        return array[i];
    }

    public void setValue(int i, int val, Sort al) {
        al.doDelay();
        isLastIndex(i);
        array[i] = val;
        barColors[i] = Color.BLUE;
        arrayAccess++;
        SoundPlayer.addAccessList(array[i]);
        lastIndex = i;
    }

    private void isLastIndex(int index) {
        if (lastIndex != -1) {
            if (lastIndex != index) {
                unmark(lastIndex);
                lastIndex = -1;
            }
        }
    }

    public void shuffle() {
        if ((array.length - 1) >= 1) {
            Random rng = new Random();
            for (int i = 0; i < array.length; i++) {
                int swapWithIndex = rng.nextInt(array.length - 1);
                swapIndexNoAccess(i, swapWithIndex);
                barColors[i] = Color.GREEN;
            }
        }
    }

    public void swap(int firstIndex, int secondIndex) {
        isLastIndex(firstIndex);
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;

        if (barColors[secondIndex] != Color.RED) {
            Color tempCol = barColors[firstIndex];
            if (barColors[secondIndex] == Color.GREEN) {
                barColors[secondIndex] = Color.BLUE;
            }
            barColors[firstIndex] = barColors[secondIndex];
            barColors[secondIndex] = tempCol;
        }
        arrayAccess++;
        arrayAccess++;
        swap++;
        lastIndex = firstIndex;
    }

    public void markSwap(int firstIndex, int secondIndex) {
        barColors[firstIndex] = Color.RED;
        barColors[secondIndex] = Color.GREEN;
    }

    public void swapIndexNoAccess(int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    public void mark(int position) {
        barColors[position] = Color.RED;
    }

    public void mark(int position, Color color) {
        barColors[position] = color;
    }

    public void unmark(int position) {
        barColors[position] = Color.GREEN;
    }

    public void unmarkall() {
        for (int i = 0; i < array.length; i++) {
            barColors[i] = Color.GREEN;
        }
    }


    public Color getBarColor(int position) {
        return barColors[position];
    }

    public void mark(int position, int col) {
        barColors[position] = colors[col];
    }

    public int getMax() {
        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
        return Collections.max(list);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int value : array) {
            s.append(value).append(" ");
        }
        System.out.println();
        return s.toString();
    }
}