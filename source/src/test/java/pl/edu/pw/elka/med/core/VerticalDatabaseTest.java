package pl.edu.pw.elka.med.core;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Testy pionowej bazy danych.
 */
public class VerticalDatabaseTest {

    private VerticalDatabase verticalDatabase;

    @Before
    public void setUp() throws Exception {
        @SuppressWarnings("ConstantConditions")
        String fileName = this.getClass().getClassLoader().getResource("agaricus-lepiota.data").getFile();
        verticalDatabase = new VerticalDatabase(new FileInputStream(fileName));
    }

    @After
    public void tearDown() throws Exception {
        verticalDatabase = null;
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        Assertions.assertThat(verticalDatabase.getAllTransactions().size()).isEqualTo(8124);
    }

    @Test
    public void testGetAllItems() throws Exception {
        Assertions.assertThat(verticalDatabase.getAllItems().size()).isEqualTo(119);
    }
}