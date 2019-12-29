package beatsOfSorting.sound;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SoundPlayer {

    private int sampleRate; // 44100 sample points per 1 second
    private AudioFormat af;
    private SourceDataLine sdl;
    private final byte[] byteBuffer = new byte[4096 * 2];
    private static double oldvol = 1.0;
    private static ArrayList<Oscillator> s_osclist = new ArrayList<>();
    private static final long s_max_oscillators = 512;
    private static long p = 0;
    private static ArrayList<Integer> s_access_list = new ArrayList<>();
    public boolean soundOn;
    protected SimpleLongProperty delay;
    private SimpleDoubleProperty soundSustain;

    public SoundPlayer() throws LineUnavailableException {
        soundOn = true;
        sampleRate = 44100;
        af = new AudioFormat((float) sampleRate, 16, 1, true, false);
        sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af, byteBuffer.length);
        sdl.start();
        soundSustain = new SimpleDoubleProperty(2);
        delay = new SimpleLongProperty(2);
    }

    public static void resetSound() {
        p = 0;
        s_osclist.clear();
    }

    public SimpleDoubleProperty soundSustainProperty() {
        return soundSustain;
    }

    public SimpleLongProperty delayProperty() {
        return delay;
    }

    public void soundSchedulerService(int arraySize) {
        int samples = 4096;

        double period = 1000d / (44100d / samples);
        Runnable runnable = () -> soundCallback(samples, arraySize);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
        service.scheduleAtFixedRate(runnable, 0, (long) (1000000 * period), TimeUnit.NANOSECONDS);
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }
    }

    private synchronized void playSound() {
        if (soundOn)
            sdl.write(byteBuffer, 0, byteBuffer.length);
    }

    public synchronized void soundCallback(int size, int arraySize) {
        double pscale = (double) size / s_access_list.size();

        for (int i = 0; i < s_access_list.size(); ++i) {
            double relindex = s_access_list.get(i) / ((double) arraySize - 1);
            double freq = arrayindex_to_frequency(relindex);

            add_oscillator(freq, p, (long) (p + i * pscale), (long) ((delay.get() / 1000.0) * soundSustain.get() * 44100));
        }
        s_access_list.clear();

        // calculate waveform
        ArrayList<Double> wave = new ArrayList<>(Collections.nCopies(size, 0.0));
        int wavecount = 0;

        for (Oscillator m : s_osclist) {
            if (!m.is_done(p)) {
                wave = m.mix(wave, size, p);
                ++wavecount;
            }
        }

        if (wavecount == 0) {
            // set zero, the function below messes up when vol = 0.0
            Arrays.fill(byteBuffer, (byte) 0);
        } else {
            // count maximum wave amplitude
            double vol = Collections.max(wave);

            if (vol < oldvol) {
                // slowly ramp up volume
                vol = 0.9 * oldvol;
            }

            // convert waveform to samples, with ramping of volume

            for (int i = 0; i < size; ++i) {
                double v = 24000.0 * wave.get(i) / (oldvol + (vol - oldvol) * (i / (double) size));

                if (v > 32200) {
                    v = 32200;
                }
                if (v < -32200) {
                    v = -32200;
                }

                double normV = 2 * ((v - -32200) / (32200 - -32200)) - 1;

                short sampleInt = (short) (normV * Short.MAX_VALUE);

                byteBuffer[i * 2] = (byte) (sampleInt << 8); // write 8bits ________WWWWWWWW out of 16
                byteBuffer[i * 2 + 1] = (byte) (sampleInt >>> 8); // write 8bits WWWWWWWW________ out of 16
            }

            playSound();
            oldvol = vol;
        }
        p += size;
    }

    /// add an oscillator to the list (reuse finished ones)
    static void add_oscillator(double freq, long p, long pstart, long duration) {
        // find free oscillator
        long oldest = 0, toldest = Long.MAX_VALUE;

        for (int i = 0; i < s_osclist.size(); ++i) {
            if (s_osclist.get(i).is_done(p)) {
                s_osclist.set(i, new Oscillator(freq, pstart, duration));
                return;
            }

            if (s_osclist.get(i).tstart() < toldest) {
                oldest = i;
                toldest = s_osclist.get(i).tstart();
            }
        }

        if (s_osclist.size() < s_max_oscillators) {
            // add new one
            s_osclist.add(new Oscillator(freq, pstart, duration));
        } else {
            // replace oldest oscillator
            s_osclist.set((int) oldest, new Oscillator(freq, pstart, duration));
        }
    }

    /// function mapping array index (normalized to [0,1]) to frequency
    static double arrayindex_to_frequency(double aindex) {
        return 120 + 1200 * (aindex * aindex);
    }

    public static void addAccessList(int val) {
        s_access_list.add(val);
    }
}
