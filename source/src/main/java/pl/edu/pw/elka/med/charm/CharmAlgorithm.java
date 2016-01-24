package pl.edu.pw.elka.med.charm;

import com.google.common.collect.ImmutableSet;
import pl.edu.pw.elka.med.core.Algorithm;
import pl.edu.pw.elka.med.core.Database;
import pl.edu.pw.elka.med.core.ItemSet;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementacja algorytmu wyszukiwania zbiorów częstych CHARM.
 */
public class CharmAlgorithm implements Algorithm {

    @Override
    public Set<ItemSet> run(Database database, long minSupport) {
        Set<ItemSet> frequentItemSets = new HashSet<>();
        SortedSet<CharmItemSet> nodes = preprocessItems(database, minSupport);

        return frequentItemSets;
    }

    /**
     * Główna metoda algorytmu CHARM.
     *
     * @param nodes uporządkowany zbiór wierzchołków drzewa do przeszukiwania
     * @param frequentItemSets znalezione zbiory częste
     */
    private void charmExtend(SortedSet<CharmItemSet> nodes, Set<ItemSet> frequentItemSets) {
//        FIXME zaimplementować
    }

    /**
     * Przygotowuje itemy na potrzeby algorytmu - przepakowuje je w odpowiednie
     * struktury oraz odfiltrowuje te itemy, które zbyt rzadko występują.
     *
     * @param database baza danych
     * @param minSupport minimalne wsparcie bezwzględne
     * @return dane przygotowane do algorytmu
     */
    private SortedSet<CharmItemSet> preprocessItems(Database database, long minSupport) {
        return  database.getAllItems()
                .parallelStream()
                .filter(item -> database.getTransactions(item).size() >= minSupport)
                .map(item -> new CharmItemSet(ImmutableSet.of(item),
                                              database.getTransactions(item)))
                .collect(Collectors.toCollection(getSortedSetSupplier()));
    }

    /**
     * Zwraca suppliera tworzący {@link SortedSet} z odpowiednim komparatorem.
     *
     * @return supplier tworzący {@link SortedSet} z odpowiednim komparatorem
     */
    static Supplier<SortedSet<CharmItemSet>> getSortedSetSupplier() {
        return () -> new TreeSet<>(getSortedSetComparator());
    }

    /**
     * Zwraca komparator, wg którego sortowane są ItemSety.
     *
     * @return komparator, wg którego sortowane są ItemSety
     */
    static Comparator<CharmItemSet> getSortedSetComparator() {
        return Comparator.comparingLong(CharmItemSet::getSupport);
    }
}
