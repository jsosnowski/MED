package pl.edu.pw.elka.med.core;

/**
 * Implementacja itemu z bazy danych.
 */
public class Item {

    private final long id;
    private final String type;
    private final Object value;

    Item(long id, String type, Object value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    /**
     * Zwraca identyfikator itemu.
     *
     * @return identyfikator itemu
     */
    public long getId() {
        return id;
    }

    /**
     * Zwraca typ itemu.
     *
     * @return typ itemu
     */
    public String getType() {
        return type;
    }

    /**
     * Zwraca wartość itemu.
     *
     * @return wartość itemu
     */
    public Object getValue() {
        return value;
    }
}
