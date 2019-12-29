package beatsOfSorting.algorithms;

import beatsOfSorting.SortArray;
import beatsOfSorting.sound.SoundPlayer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

public abstract class Sort extends Task<Void> {

    protected SortArray array;
    protected String name;
    protected SimpleLongProperty delay = new SimpleLongProperty(2);
    protected SimpleBooleanProperty isPaused = new SimpleBooleanProperty(false);

    public String getName() {
        return name;
    }

    public abstract Sort createNewSort();

    public void setArray(SortArray array) {
        this.array = array;
    }

    public void setDelay(double delay) {
        this.delay.setValue(delay);
    }

    public void swapN(int firstIndex, int secondIndex) {
        array.swap(firstIndex, secondIndex);
    }

    public void soundCompare(int val1, int val2) {
        if (val1 < array.getSize() && val2 < array.getSize() && val1 >= 0 && val2 >= 0) {
            SoundPlayer.addAccessList(array.getValueDirect(val1));
            SoundPlayer.addAccessList(array.getValueDirect(val2));

            array.comp++;
        }
    }

    public void doDelay() {
        try {
            Thread.sleep(delay.get());
        } catch (InterruptedException ex) {
            if (isCancelled()) {
                return;
            }
        }
    }

    public synchronized void checkStatus() {
        while (isPaused.get() && !Thread.currentThread().isInterrupted()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void pause() {
        isPaused.set(true);
    }

    public final Boolean isPaused() {
        return isPaused.getValue();
    }

    public SimpleBooleanProperty isPausedProperty() {
        return isPaused;
    }

    synchronized public void resume() {
        isPaused.set(false);
        notify();
    }

    public void unmarkall_finish() {
        for (int i = 0; i < array.getSize(); i++) {

            array.mark(i, Color.RED);
            if (i + 1 < array.getSize())
                soundCompare(i, i + 1);
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
            }
            array.unmark(i);
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
