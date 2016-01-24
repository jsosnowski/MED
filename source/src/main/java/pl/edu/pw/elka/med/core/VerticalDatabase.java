package pl.edu.pw.elka.med.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        loadData(dataInputStream).forEach(((transaction, items) ->
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
        Set<Transaction> allTransactionsSet = new HashSet<>();
        itemsToTransactionsMap.values().forEach(allTransactionsSet::addAll);

        return Collections.unmodifiableSet(allTransactionsSet);
    }

    @Override
    public Set<Item> getAllItems() {
        return Collections.unmodifiableSet(itemsToTransactionsMap.keySet());
    }

    @Override
    public Set<Item> getItems(Transaction transaction) {
        Set<Item> items = new HashSet<>();
        itemsToTransactionsMap.forEach((item, transactions) -> {
            if (transactions.contains(transaction)) {
                items.add(item);
            }
        });

        return items;
    }

    @Override
    public Set<Transaction> getTransactions(Item item) {
        return Collections.unmodifiableSet(itemsToTransactionsMap.get(item));
    }
}
