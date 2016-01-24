package pl.edu.pw.elka.med.core;

/**
 * Transakcja z bazy danych.
 */
public class Transaction {

    private final long id;

    Transaction(long id) {
        this.id = id;
    }

    /**
     * Zwraca identyfikator transakcji.
     *
     * @return identyfikator transakcji
     */
    public long getId() {
        return id;
    }
}
