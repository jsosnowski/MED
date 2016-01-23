package pl.edu.pw.elka.med.core;

/**
 * Interfejs pojedynczego itemu.
 */
public interface Item {

    /**
     * Zwraca identyfikator itemu.
     *s
     * @return identyfikator itemu
     */
    long getId();

    /**
     * Zwraca typ itemu.
     *
     * @return typ itemu
     */
    String getType();

    /**
     * Zwraca wartość itemu.
     *
     * @return wartość itemu
     */
    Object getValue();
}
