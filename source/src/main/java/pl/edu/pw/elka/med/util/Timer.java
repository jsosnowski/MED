package pl.edu.pw.elka.med.util;

import com.google.common.base.Preconditions;

/**
 * Prosty timer na potrzeby logowania i testów.
 */
public class Timer {

    private Long startMoment;
    private Long stopMoment;

    public Timer() {
        start();
    }

    /**
     * Zaczyna odmierzanie czasu.
     */
    public void start() {
        this.startMoment = getNanoTime();
        this.stopMoment = null;
    }

    /**
     * Zatrzymuje odmierzanie czasu. Musi być wywołana przed {@link #prettyElapsed()} i {@link #nanoDifference()}.
     */
    public void stop() {
        this.stopMoment = getNanoTime();
    }

    private long getNanoTime() {
        return System.nanoTime();
    }

    /**
     * Zwraca ładnie sformatowany tekst informujący o długości zmierzonego czasu.
     * Format: godziny:minuty:sekundy.milisekundy
     *
     * @return pretty-formatted elapsed time
     */
    public String prettyElapsed() {
        Preconditions.checkNotNull(stopMoment,
                                   "Method stop() must be called before printing output!");

        long difference = stopMoment - startMoment;
        int milliseconds = (int) (difference / 1000000L) % 1000;
        int seconds = (int) (difference / 1000000000L) % 60;
        int minutes = (int) (difference / 60000000000L) % 60;
        int hours = (int) (difference / 3600000000000L);

        return String.format("%02d:%02d:%02d.%03d",
                             hours,
                             minutes,
                             seconds,
                             milliseconds);
    }

    /**
     * Zwraca liczbę milisekund, która upłynęła od początku do końca pomiaru.
     *
     * @return liczba milisekund, która upłynęła od początku do końca pomiaru
     */
    public long nanoDifference() {
        Preconditions.checkNotNull(stopMoment,
                                   "Method stop() must be called before printing output!");

        return stopMoment - startMoment;
    }
}