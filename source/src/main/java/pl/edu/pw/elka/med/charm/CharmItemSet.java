package pl.edu.pw.elka.med.charm;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
    private boolean shouldBeRemoved;

    CharmItemSet(Set<Item> items, Set<Transaction> transactions) {
        this.items = new HashSet<>();
        this.items.addAll(items);
        this.transactions = new HashSet<>();
        this.transactions.addAll(transactions);
        this.shouldBeRemoved = false;
    }

    /**
     * Uaktualnia zbiór itemów w przypadku uaktualnienia rodzica.
     *
     * @param parentItems itemy rodzica
     */
    void updateItemSet(Set<Item> parentItems) {
        Set<Item> itemDiff = new HashSet<>(Sets.difference(parentItems, items));
        this.items.addAll(itemDiff);
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

    /**
     * Słuzy do oznakowania zbioru jako zbiór do usunięcia.
     */
    void setShouldBeRemoved() {
        this.shouldBeRemoved = true;
    }

    public boolean isShouldBeRemoved() {
        return shouldBeRemoved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CharmItemSet)) return false;

        CharmItemSet that = (CharmItemSet) o;

        return new EqualsBuilder()
                .append(shouldBeRemoved, that.shouldBeRemoved)
                .append(items, that.items)
                .append(transactions, that.transactions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(items)
                .append(transactions)
                .append(shouldBeRemoved)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("items", items)
                .append("support", getSupport())
                .toString();
    }
}
