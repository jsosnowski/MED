package pl.edu.pw.elka.med.core;

import java.util.Set;

/**
 * Interfejs transakcyjnej bazy danych.
 */
public interface Database {

    /**
     * Zwraca zbiór transakcji.
     *
     * @return zbiór transakcji
     */
    Set<Transaction> getAllTransactions();

    /**
     * Zwraca zbiór itemów.
     *
     * @return zbiór itemów
     */
    Set<Item> getAllItems();

    /**
     * Zwraca wszystkie itemy występujące w wybranej transakcji.
     *
     * @param transaction transakcja
     * @return wszystkie itemy występujące w transakcji
     */
    Set<Item> getItems(Transaction transaction);

    /**
     * Zwraca wszystkie transakcje, w których występuje wybrany item.
     *
     * @param item item
     * @return wszystkie transakcje, w których występuje item
     */
    Set<Transaction> getTransactions(Item item);
}
