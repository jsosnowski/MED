package pl.edu.pw.elka.med.charm;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
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
        // preprocessing
        List<CharmItemSet> nodes = preprocessItems(database, minSupport);

        // odpalenie algorytmu
        charmExtend(nodes, frequentItemSets, minSupport);

        return frequentItemSets;
    }

    /**
     * Główna metoda algorytmu CHARM.
     *
     * @param nodes uporządkowany zbiór wierzchołków drzewa do przeszukiwania
     * @param frequentItemSets znalezione zbiory częste
     */
    private void charmExtend(List<CharmItemSet> nodes, Set<ItemSet> frequentItemSets, long minSupport) {
        ListIterator<CharmItemSet> iterator = nodes.listIterator();

        while (iterator.hasNext()) {
            CharmItemSet currentItemSet = iterator.next();
            if (currentItemSet.isShouldBeRemoved()) {
                // usuwanie węzłów, które powinny zostać usunięte
//                FIXME
//                iterator.remove();
                continue;
            }

            List<CharmItemSet> newNodes = newList();
            ListIterator<CharmItemSet> secIterator = nodes.listIterator(iterator.previousIndex());
            secIterator.next();
            while (secIterator.hasNext()) {
                CharmItemSet nextItemSet = secIterator.next();
                if (nextItemSet.isShouldBeRemoved()) {
                    // węzeł powinien być usunięty, ale jeszcze nie zdążył
                    continue;
                }

                // nowy kandydat
                CharmItemSet newCandidate = new CharmItemSet(Sets.union(currentItemSet.getItems(),
                                                                        nextItemSet.getItems()),
                                                             Sets.intersection(currentItemSet.getTransactions(),
                                                                               nextItemSet.getTransactions()));

                if (newCandidate.getSupport() < minSupport) {
                    continue;
                }

                if (Sets.symmetricDifference(currentItemSet.getTransactions(),
                                             nextItemSet.getTransactions()).isEmpty()) {
                    // X_i i X_j są równe
                    // węzeł do usunięcia
                    nextItemSet.setShouldBeRemoved();
                    // podmieniamy X_i
//                    iterator.set(newCandidate);
//                    frequentItemSets.add(currentItemSet);
//                    currentItemSet = newCandidate;
                    currentItemSet.updateItemSet(newCandidate.getItems());
                    replaceAllSubsets(newCandidate, newNodes);
                } else if (Sets.difference(currentItemSet.getTransactions(),
                                           nextItemSet.getTransactions()).isEmpty()
                        && !Sets.difference(currentItemSet.getTransactions(),
                                           nextItemSet.getTransactions()).isEmpty()) {
                    // X_i zawiera się w X_j
                    // podmieniamy X_i
                    currentItemSet.updateItemSet(newCandidate.getItems());
                    replaceAllSubsets(newCandidate, newNodes);
                } else if (!Sets.difference(currentItemSet.getTransactions(),
                                           nextItemSet.getTransactions()).isEmpty()
                        && Sets.difference(currentItemSet.getTransactions(),
                                            nextItemSet.getTransactions()).isEmpty()) {
                    // X_j zawiera się w X_i
                    // węzeł do usunięcia
                    nextItemSet.setShouldBeRemoved();
                    // dodajemy nowy węzeł
                    newNodes.add(newCandidate);
                } else {
                    // X_i oraz X_j nie zawierają się w sobie
                    newNodes.add(newCandidate);
                }
            }

//            FIXME
//            iterator.remove();
            frequentItemSets.add(currentItemSet);
            currentItemSet.setShouldBeRemoved();

            List<CharmItemSet> listForNewNodes = newList();
            listForNewNodes.addAll(0, newNodes);
            charmExtend(listForNewNodes, frequentItemSets, minSupport);
        }
    }

    /**
     * Uaktualnia {@param newNodes} w przypadku podmiany rodzica - {@param updatedItemset}.
     *
     * @param updatedItemset uaktualniony rodzic
     * @param newNodes nowe węzły do uaktualnienia
     */
    private void replaceAllSubsets(CharmItemSet updatedItemset, Collection<CharmItemSet> newNodes) {
        for (CharmItemSet charmItemSet : newNodes) {
            charmItemSet.updateItemSet(updatedItemset.getItems());
        }
    }

    /**
     * Przygotowuje itemy na potrzeby algorytmu - przepakowuje je w odpowiednie
     * struktury oraz odfiltrowuje te itemy, które zbyt rzadko występują.
     *
     * @param database baza danych
     * @param minSupport minimalne wsparcie bezwzględne
     * @return dane przygotowane do algorytmu
     */
    private List<CharmItemSet> preprocessItems(Database database, long minSupport) {
        return  database.getAllItems()
                .parallelStream()
                .filter(item -> database.getTransactions(item).size() >= minSupport)
                .map(item -> new CharmItemSet(ImmutableSet.of(item),
                                              database.getTransactions(item)))
                .sorted(getListComparator())
                .collect(Collectors.toCollection(getListSupplier()));
    }

    /**
     * Zwraca suppliera tworzący {@link SortedSet} z odpowiednim komparatorem.
     *
     * @return supplier tworzący {@link SortedSet} z odpowiednim komparatorem
     */
    private static Supplier<List<CharmItemSet>> getListSupplier() {
        return CharmAlgorithm::newList;
    }

    /**
     * Tworzy nowy {@link SortedSet}.
     *
     * @return nowy {@link SortedSet}
     */
    private static List<CharmItemSet> newList() {
        return new LinkedList<>();
    }

    /**
     * Zwraca komparator, wg którego sortowane są ItemSety.
     *
     * @return komparator, wg którego sortowane są ItemSety
     */
    private static Comparator<CharmItemSet> getListComparator() {
        return Comparator.comparingLong(CharmItemSet::getSupport);
    }
}
