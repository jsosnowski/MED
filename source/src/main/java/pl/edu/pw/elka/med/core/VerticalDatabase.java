package pl.edu.pw.elka.med.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pionowa implementacja bazy danych.
 *
 * Ta implementacja bazuje na założeniu, że bardzo tanio można znaleźć wszystkie
 * transakcje, w których występuje podany item, ale drogie stają się operacje
 * znalezienia wszystkich itemów z konkretnej transakcji.
 */
public class VerticalDatabase extends AbstractCSVDatabase {

    private final Map<Item, Set<Transaction>> itemsToTransactionsMap;

    public VerticalDatabase(InputStream dataInputStream) throws IOException {
        this.itemsToTransactionsMap = new HashMap<>();
        initDataMap(dataInputStream);
    }

    private void initDataMap(InputStream dataInputStream) throws IOException {
        loadData(dataInputStream)
                .forEach(((transaction, items) ->
                        items.forEach(item -> {
                            Set<Transaction> transactions = itemsToTransactionsMap.get(item);
                            if (transactions == null) {
                                transactions = new HashSet<>();
                                itemsToTransactionsMap.put(item, transactions);
                            }
                            transactions.add(transaction);
                        })));
    }

    @Override
    public Set<Transaction> getAllTransactions() {
        return Collections.unmodifiableSet(
                itemsToTransactionsMap
                        .values()
                        .parallelStream()
                        .reduce(ImmutableSet.of(),
                                Sets::union));
    }

    @Override
    public Set<Item> getAllItems() {
        return Collections.unmodifiableSet(itemsToTransactionsMap.keySet());
    }

    @Override
    public Set<Item> getItems(Transaction transaction) {
        return Collections.unmodifiableSet(
                itemsToTransactionsMap
                        .entrySet()
                        .parallelStream()
                        .filter(entry -> entry.getValue().contains(transaction))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet()));
    }

    @Override
    public Set<Transaction> getTransactions(Item item) {
        return Collections.unmodifiableSet(itemsToTransactionsMap.get(item));
    }
}
