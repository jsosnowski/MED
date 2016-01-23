package pl.edu.pw.elka.med.core;

import java.util.Set;

/**
 * Interfejs pojedynczej transakcji.
 */
public interface Transaction {

    /**
     * Zwraca identyfikator transakcji.
     *
     * @return identyfikator transakcji
     */
    long getId();

    /**
     * Zwraca zbiór itemów transakcji.
     *
     * @return zbiór itemów transakcji
     */
    Set<Item> getItems();
}
