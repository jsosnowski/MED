package pl.edu.pw.elka.med.core;

import java.util.Set;

/**
 * Interfejs algorytmu wyszukiwania zbiorów częstych.
 */
public interface Algorithm {

    /**
     * Wyszukuje wszystkie zbiory częste o wsparciu bezwzględnym wyższym lub równym temu przekazanemu.
     *
     * @param database baza danych
     * @param minSupport wsparcie bezwględne
     * @return wszystkie znalezione zbiory częste
     */
    Set<ItemSet> run(Database database, long minSupport);
}
