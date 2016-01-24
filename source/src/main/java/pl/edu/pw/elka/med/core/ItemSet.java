package pl.edu.pw.elka.med.core;

import java.util.Set;

/**
 * Zbiór itemów.
 */
public interface ItemSet {

    /**
     * Zwraca wsparcie zbioru itemów.
     *
     * @return wsparcie zbioru itemów
     */
    long getSupport();

    /**
     * Zwraca itemy ze zbioru.
     * @return itemy ze zbioru
     */
    Set<Item> getItems();
}
