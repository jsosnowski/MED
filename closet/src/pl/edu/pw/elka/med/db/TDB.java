package pl.edu.pw.elka.med.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TDB extends ArrayList<Tid> {
	public Map<String, String> mapping = new HashMap<>();
	private int counter = 0;

	private static final long serialVersionUID = 1719711755231271729L;

	public void addTid(String[] tid) {
		Tid nTid = new Tid();
		for (String i : tid) {
			String inserted = mapping.putIfAbsent(i, "" + counter);
			nTid.add(mapping.get(i));
			counter += (inserted == null ? 1 : 0);
		}
		add(nTid);
	}
}
