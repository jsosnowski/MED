package pl.edu.pw.elka.med.charm;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.pw.elka.med.core.Database;
import pl.edu.pw.elka.med.core.ItemSet;
import pl.edu.pw.elka.med.core.VerticalDatabase;

import java.io.FileInputStream;
import java.util.Set;

/**
 * Testy klasy {@link CharmAlgorithm}.
 */
public class CharmAlgorithmTest {

    private CharmAlgorithm charmAlgorithm;
    private Database database;

    @Before
    public void setUp() throws Exception {
        @SuppressWarnings("ConstantConditions")
        String fileName = this.getClass().getClassLoader().getResource("agaricus-lepiota.data").getFile();
        database = new VerticalDatabase(new FileInputStream(fileName));
        charmAlgorithm = new CharmAlgorithm();
    }

    @After
    public void tearDown() throws Exception {
        charmAlgorithm = null;
    }

    @Test
    public void testRun() throws Exception {
        // given
        long minSup = (long) (database.getAllTransactions().size() * 0.50);

        // when
        Set<ItemSet> frequentItemSets = charmAlgorithm.run(database, minSup);

        // then
        Assertions.assertThat(frequentItemSets.size() > 0).isTrue();

        System.out.println("minsup=" + minSup + ", found=" + frequentItemSets.size());

        frequentItemSets
                .stream()
                .sorted((o1, o2) -> Long.compare(o1.getSupport(), o2.getSupport()) * -1)
                .limit(5)
                .forEach(itemSet -> System.out.println(itemSet.toString()));
        System.out.println("---");

        frequentItemSets
                .stream()
                .sorted((o1, o2) -> Long.compare(o1.getSupport(), o2.getSupport()))
                .limit(5)
                .forEach(itemSet -> System.out.println(itemSet.toString()));
    }
}