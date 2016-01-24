package pl.edu.pw.elka.med;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.pw.elka.med.charm.CharmAlgorithm;
import pl.edu.pw.elka.med.core.*;
import pl.edu.pw.elka.med.util.Timer;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * Aplikacja wyszukująca zbiory częste w zbiorze danych. Entry-point aplikacji.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        if (args.length > 3 || args.length < 2) {
            logger.info("CHARM algorithm - Application usage: <csv file path> <min support> [output file path]");
            System.exit(255);
        }

        String filePath = args[0];
        Database database = new VerticalDatabase(new FileInputStream(filePath));
        int allTransactionsSize = database.getAllTransactions().size();
        logger.info("Loaded database with {} transactions and {} items.",
                    allTransactionsSize,
                    database.getAllItems().size());
        Algorithm algorithm = new CharmAlgorithm();

        double minSupport = Double.parseDouble(args[1]);
        long absoluteMinSupport = (long) (minSupport * allTransactionsSize);
        logger.info("Running CHARM algorithm with relative min support = {} (absolute min support = {}). " +
                            "Starting timer.",
                    minSupport,
                    absoluteMinSupport);
        Timer timer = new Timer();

        Set<ItemSet> frequentItemSets = algorithm.run(database, absoluteMinSupport);

        timer.stop();
        logger.info("Finished algorithm. It took: {}", timer.prettyElapsed());
        logger.info("Found {} frequent item sets.", frequentItemSets.size());

        if (args.length == 3) {
            writeOutputFile(args[2], timer);
        }

        printTop10ItemSets(frequentItemSets, allTransactionsSize);
    }

    private static void printTop10ItemSets(Set<ItemSet> frequentItemSets, int allTransactionsSize) {
        logger.info("Top 10 frequent ItemSets with highest support:");
        frequentItemSets
                .stream()
                .sorted(Comparator.comparingLong(ItemSet::getSupport).reversed())
                .limit(10)
                .forEach(itemSet -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("{");
                    for (Iterator<Item> iterator = itemSet.getItems().iterator(); iterator.hasNext(); ) {
                        Item item = iterator.next();
                        stringBuilder.append(item.getType());
                        stringBuilder.append(": ");
                        stringBuilder.append(item.getValue());
                        if (iterator.hasNext()) {
                            stringBuilder.append(", ");
                        }

                    }
                    stringBuilder.append("} - support: " + (double) (itemSet.getSupport()) / allTransactionsSize);


                    logger.info(stringBuilder.toString());
                });
    }

    private static void writeOutputFile(String outputFilePath, Timer timer) {
        try (Writer writer = new FileWriter(outputFilePath)) {
            writer.append(Long.toString(timer.nanoDifference()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
