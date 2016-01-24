package pl.edu.pw.elka.med.charm;

import pl.edu.pw.elka.med.core.Item;
import pl.edu.pw.elka.med.core.ItemSet;
import pl.edu.pw.elka.med.core.Transaction;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementacja zbioru itemów dla algorytmu CHARM.
 */
class CharmItemSet implements ItemSet {

    private final SortedSet<Item> items;
    private final SortedSet<Transaction> transactions;

    CharmItemSet(Set<Item> items, Set<Transaction> transactions) {
//        FIXME przemyśleć sprawę kopiowania zbioru items i transactions
        this.items = new TreeSet<>();
        this.items.addAll(items);
        this.transactions = new TreeSet<>();
        this.transactions.addAll(transactions);
    }

    @Override
    public double getSupport() {
        return transactions.size();
    }

    @Override
    public Set<Item> getItems() {
        return Collections.unmodifiableSortedSet(items);
    }

    /**
     * Zwraca wszystkie transakcje, w których występuje zbiór.
     *
     * @return wszystkie transakcje, w których występuje zbiór
     */
    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSortedSet(transactions);
    }
}
