package beatsOfSorting.sound;

import java.util.ArrayList;

public class Oscillator {
    /// frequency of generated wave
    private double m_freq;
    /// start and end of wave in sample time
    private long m_tstart, m_tend;
    /// duration of oscillation note
    private long m_duration = 44100 / 8;
    private int s_samplerate = 44100; // 44100 sample points per 1 second

    /// construct new oscillator
    public Oscillator(double freq, long tstart, long duration) {
        m_freq = freq;
        m_tstart = tstart;
        m_tend = m_tstart + duration;
        m_duration = duration;
    }

    /// return start time
    long tstart() {
        return m_tstart;
    }

    /// true if the oscillator is silent at time p
    boolean is_done(long p) {
        return (p >= m_tend);
    }

    /// global timestamp of callback in sound sample units
    static long s_pos = 0;

    /// mix in the output of this oscillator on the wave output
    ArrayList<Double> mix(ArrayList<Double> data, int size, long p) {
        for (int i = 0; i < size; ++i) {
            if (p + i < m_tstart)
                continue;
            if (p + i >= m_tend) {
                break;
            }

            long trel = (p + i - m_tstart);

            data.set(i, data.get(i) + (envelope(trel) * wave((double) trel / s_samplerate * m_freq)));
        }
        return data;
    }

    private double wave(double x) {
        x = x % 1.0;

        if (x <= 0.25)
            return 4.0 * x;
        if (x <= 0.75)
            return 2.0 - 4.0 * x;
        return 4.0 * x - 4.0;
    }

    /// envelope applied to wave (uses ADSR)
    double envelope(long i) {
        double x = (double) i / m_duration;
        if (x > 1.0)
            x = 1.0;

        // *** ADSR envelope

        final double attack = 0.025; // percentage of duration
        final double decay = 0.1; // percentage of duration
        final double sustain = 0.9; // percentage of amplitude
        final double release = 0.3; // percentage of duration

        if (x < attack)
            return 1.0 / attack * x;

        if (x < attack + decay)
            return 1.0 - (x - attack) / decay * (1.0 - sustain);

        if (x < 1.0 - release)
            return sustain;

        return sustain / release * (1.0 - x);
    }
}