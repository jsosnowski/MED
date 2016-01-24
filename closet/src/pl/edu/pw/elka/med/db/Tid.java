package pl.edu.pw.elka.med.db;

import java.util.Collection;
import java.util.HashSet;

public class Tid extends HashSet<String> {

	private static final long serialVersionUID = 743698798511907326L;

	public Tid() {
		super();
	}

	public Tid(Collection<? extends String> c) {
		super(c);
	}

	public Tid(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public Tid(int initialCapacity) {
		super(initialCapacity);
	}
}
