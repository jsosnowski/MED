package pl.edu.pw.elka.med.closet;

import java.util.HashSet;
import java.util.List;

public class FCI {

	private HashSet<String> items;
	private Integer sup;
	
	public FCI(HashSet<String> items, Integer sup) {
		super();
		this.items = items;
		this.sup = sup;
	}
	
	public boolean contains(String s) {
		return items.contains(s);
	}
	
	public boolean contaisAll(List<String> candidate) {
		return items.containsAll(candidate);
	}

	public HashSet<String> getItems() {
		return items;
	}

	public void setItems(HashSet<String> items) {
		this.items = items;
	}

	public Integer getSup() {
		return sup;
	}

	public void setSup(Integer sup) {
		this.sup = sup;
	}
	
	@Override
	public String toString() {
		return items + " : " + sup;
	}
}
