package pl.edu.pw.elka.med.core;

import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.elka.med.util.Timer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstrakcyjna klasa bazowa baz danych.
 */
abstract class AbstractCSVDatabase implements Database {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCSVDatabase.class);

    /**
     * Wczytuje wszystkie dane z pliku CSV do poziomej bazy danych w postaci mapy (transakcja -> zbiór itemów).
     *
     * @param dataInputStream strumień danych z pliku CSV
     * @return mapa (transakcja -> zbiór itemów)
     * @throws IOException
     */
    protected Map<Transaction, Set<Item>> loadData(InputStream dataInputStream) throws IOException {
        logger.debug("Loading data");
        Timer timer = new Timer();
        Map<Transaction, Set<Item>> dataMap = new LinkedHashMap<>();

        CSVReader csvReader = new CSVReader(new InputStreamReader(dataInputStream));
        String[] header = null;
        String [] nextLine;
        int i = 0;
        while ((nextLine = csvReader.readNext()) != null) {
            if (header == null) {
                // odczytujemy header
                header = nextLine;
                continue;
            }

            Transaction currentTransaction = new Transaction(i);
            int itemCount = nextLine.length;
            Set<Item> currentItems = new LinkedHashSet<>(itemCount);
            for (int j = 0; j < itemCount; j++) {
                currentItems.add(new Item(header[j], nextLine[j]));
            }

            dataMap.put(currentTransaction, currentItems);
            i++;
        }

        dataInputStream.close();
        timer.stop();
        logger.debug("Finished loading data. It took: {}", timer.prettyElapsed());
        return dataMap;
    }
}
