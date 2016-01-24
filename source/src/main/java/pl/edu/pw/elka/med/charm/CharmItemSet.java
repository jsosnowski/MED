package pl.edu.pw.elka.med.charm;

import pl.edu.pw.elka.med.core.Item;
import pl.edu.pw.elka.med.core.ItemSet;
import pl.edu.pw.elka.med.core.Transaction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementacja zbioru itemów dla algorytmu CHARM.
 */
class CharmItemSet implements ItemSet {

    private final Set<Item> items;
    private final Set<Transaction> transactions;

    CharmItemSet(Set<Item> items, Set<Transaction> transactions) {
        this.items = new HashSet<>();
        this.items.addAll(items);
        this.transactions = new HashSet<>();
        this.transactions.addAll(transactions);
    }

    @Override
    public long getSupport() {
        return transactions.size();
    }

    @Override
    public Set<Item> getItems() {
        return Collections.unmodifiableSet(items);
    }

    /**
     * Zwraca wszystkie transakcje, w których występuje zbiór.
     *
     * @return wszystkie transakcje, w których występuje zbiór
     */
    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }
}
