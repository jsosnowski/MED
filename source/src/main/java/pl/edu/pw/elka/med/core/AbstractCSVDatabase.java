package pl.edu.pw.elka.med.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstrakcyjna klasa bazowa baz danych.
 */
public abstract class AbstractCSVDatabase implements Database {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCSVDatabase.class);
}
