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
        Map<Integer, List<CharmItemSet>> frequentItemSets = new HashMap<>();
        // preprocessing
        List<CharmItemSet> nodes = preprocessItems(database, minSupport);

        // odpalenie algorytmu
        charmExtend(nodes, frequentItemSets, minSupport);

        // przepakowanie do zbioru
        Set<ItemSet> resultSet = new HashSet<>();
        frequentItemSets.values().forEach(resultSet::addAll);

        return resultSet;
    }

    /**
     * Główna metoda algorytmu CHARM.
     *
     * @param nodes uporządkowany zbiór wierzchołków drzewa do przeszukiwania
     * @param frequentItemSets znalezione zbiory częste
     */
    private void charmExtend(List<CharmItemSet> nodes,
                             Map<Integer, List<CharmItemSet>> frequentItemSets,
                             long minSupport) {
        ListIterator<CharmItemSet> iterator = nodes.listIterator();

        while (iterator.hasNext()) {
            CharmItemSet currentItemSet = iterator.next();
            if (currentItemSet.isShouldBeRemoved()) {
                // nie usuwamy węzłów ze względu na problemy z ConcurrentModificationException
                continue;
            }

            List<CharmItemSet> newNodes = newList();
            ListIterator<CharmItemSet> secIterator = nodes.listIterator(iterator.previousIndex());
            secIterator.next();
            while (secIterator.hasNext()) {
                CharmItemSet nextItemSet = secIterator.next();
                if (nextItemSet.isShouldBeRemoved()) {
                    // usuniętych węzłów nie bierzemy pod uwagę
                    continue;
                }

                // nowy kandydat
                CharmItemSet newCandidate = new CharmItemSet(Sets.union(currentItemSet.getItems(),
                                                                        nextItemSet.getItems()),
                                                             Sets.intersection(currentItemSet.getTransactions(),
                                                                               nextItemSet.getTransactions()));

                if (newCandidate.getSupport() < minSupport) {
                    // odrzucamy kandydata, ponieważ ma za małe wsparcie
                    continue;
                }

                if (Sets.symmetricDifference(currentItemSet.getTransactions(),
                                             nextItemSet.getTransactions()).isEmpty()) {
                    // X_i i X_j są równe
                    // węzeł do usunięcia
                    nextItemSet.setShouldBeRemoved();
                    // podmieniamy X_i
                    currentItemSet.updateItemSet(newCandidate.getItems());
                    replaceAllSubsets(newCandidate, newNodes);
                } else if (Sets.difference(currentItemSet.getTransactions(),
                                           nextItemSet.getTransactions()).isEmpty()
                        && !Sets.difference(nextItemSet.getTransactions(),
                                            currentItemSet.getTransactions()).isEmpty()) {
                    // X_i zawiera się w X_j
                    // podmieniamy X_i
                    currentItemSet.updateItemSet(newCandidate.getItems());
                    replaceAllSubsets(newCandidate, newNodes);
                } else if (!Sets.difference(currentItemSet.getTransactions(),
                                           nextItemSet.getTransactions()).isEmpty()
                        && Sets.difference(nextItemSet.getTransactions(),
                                           currentItemSet.getTransactions()).isEmpty()) {
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

            Collections.sort(newNodes, getListComparator());
            if (!newNodes.isEmpty()) {
                charmExtend(newNodes, frequentItemSets, minSupport);
            }

            tryAddToFrequentItemSets(currentItemSet, frequentItemSets);
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

    private int calculateCharmItemSetPseudoHashCode(CharmItemSet newCharmItemSet) {
        return newCharmItemSet
                .getTransactions()
                .parallelStream()
                .mapToInt(transaction -> (int) transaction.getId())
                .sum();
    }

    /**
     * Podczas dodawania nowego zbioru częstego do zbiorów wynikowych musimy się upewnić, że nie istnieje
     * tam już zbiór zamknięty o takim samym wsparciu, którego nowy zbiór byłby podzbiorem.
     *
     * W tym celu korzystamy z czegoś w rodzaju ręcznie zarządzanej hash-mapy.
     *
     * @param newFrequentItemSet nowo dodawany zbiór częsty
     * @param frequentItemSets wynikowa kolekcja zbiorów częstych
     */
    private void tryAddToFrequentItemSets(CharmItemSet newFrequentItemSet,
                                          Map<Integer, List<CharmItemSet>> frequentItemSets) {
        int hashCode = calculateCharmItemSetPseudoHashCode(newFrequentItemSet);
        List<CharmItemSet> list = frequentItemSets.get(hashCode);
        if (list == null) {
            list = newList();
            frequentItemSets.put(hashCode, list);
        }

        boolean isCanAdd = true;
        ListIterator<CharmItemSet> iterator = list.listIterator();
        while (iterator.hasNext()) {
            CharmItemSet charmItemSet = iterator.next();

            if (charmItemSet.getSupport() == newFrequentItemSet.getSupport()) {
                if (Sets.symmetricDifference(charmItemSet.getTransactions(),
                                             newFrequentItemSet.getTransactions()).isEmpty()) {
                    // zbiory są identyczne, nie powinno się zdarzyć
                    isCanAdd = false;
                    break;
                } else if (Sets.difference(charmItemSet.getTransactions(),
                                    newFrequentItemSet.getTransactions()).isEmpty()
                        && !Sets.difference(newFrequentItemSet.getTransactions(),
                                            charmItemSet.getTransactions()).isEmpty()) {
                    // nowy zbiór zawiera się w starym, nie dodajemy
                    isCanAdd = false;
                    break;
                } else if (!Sets.difference(charmItemSet.getTransactions(),
                                           newFrequentItemSet.getTransactions()).isEmpty()
                        && Sets.difference(newFrequentItemSet.getTransactions(),
                                            charmItemSet.getTransactions()).isEmpty()) {
                    // stary zbiór zawiera się w nowym - tak nie powinno być
                    if (newFrequentItemSet.getItems().size() > charmItemSet.getItems().size()) {
                        iterator.set(newFrequentItemSet);
                    }
                    isCanAdd = false;
                }
            }
        }

        if (isCanAdd) {
            list.add(newFrequentItemSet);
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
