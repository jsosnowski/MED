package pl.edu.pw.elka.med.closet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.pw.elka.med.db.TDB;
import pl.edu.pw.elka.med.db.Tid;

public class FreqList {
	private List<FItem> list = new ArrayList<>();
	HashMap<String, Integer> listHM = new HashMap<>();

	public FreqList() {
	}

	public FreqList(final FreqList obj, int min_sup) {
		setMap(new HashMap<>(obj.listHM), min_sup);
	}

	public FreqList(HashMap<String, Integer> map, int min_sup) {
		setMap(map, min_sup);
	}

	public void setMap(HashMap<String, Integer> map, int min_sup) {
		listHM = map;
		removeNotFrequentAndSort(min_sup);
	}

	public List<FItem> getList() {
		return list;
	}

	public boolean contains(String s) {
		return listHM.containsKey(s);
	}

/*	public void removeAll(List<FItem> toRemoveByValue) {
		list.removeAll(toRemoveByValue);
		toRemoveByValue.stream().map(f -> f.getItem())
				.forEach(i -> listHM.remove(i));
		// list.forEach(i -> listHM.put(i.getItem(), i.getFreq()));
	}
*/
	public void removeAll(FreqList flist) {
//		System.out.println("remv: " + flist);
		list.removeAll(flist.getList());
		flist.getList().stream().map(f -> f.getItem())
				.forEach(i -> listHM.remove(i));
	}

	public FreqList createFList(TDB tdb, int min_sup) {
		HashMap<String, Integer> f_list = new HashMap<>(tdb.size());
		for (Tid tid : tdb) {
			for (String item : tid) {
				Integer v = f_list.get(item);
				if (v == null)
					f_list.put(item, 1);
				else
					f_list.put(item, v + 1);
			}
		}

		listHM = new HashMap<>(f_list);
		removeNotFrequentAndSort(min_sup);
		return this;
	}

	private void removeNotFrequentAndSort(int min_sup) {
		// remove not frequent items
		listHM.entrySet().removeIf(e -> e.getValue() < min_sup);

		List<String> fsorted = Arrays.asList(listHM.keySet().toArray(
				new String[0]));
		fsorted.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Integer i1 = listHM.get(o1);
				Integer i2 = listHM.get(o2);
				return i1 < i2 ? -1 : i1 == i2 ? 0 : 1;
			}
		});

		List<FItem> fList = new ArrayList<>();
		for (String s : fsorted) {
			fList.add(new FItem(new String(s), listHM.get(s)));
		}

		list = fList;
	}
	
	private void replaceListHM(List<FItem> fitems) {
		listHM = new HashMap<>(); 
		fitems.forEach(i -> listHM.put(i.getItem(), i.getFreq())); 
	}

	public FreqList inEveryTx(final int nrow) {
		List<FItem> inEveryTx = new ArrayList<>();
		for (FItem f : list) {
			if (f.getFreq() == nrow)
				inEveryTx.add(f);
		}

		FreqList flist = new FreqList(); 
		flist.list = inEveryTx;
		flist.replaceListHM(inEveryTx);
		
		// flist.removeAll(inEveryTx);

		return flist;
	}

	@Override
	public String toString() {
		return list.stream().map(i -> i.getItem() + " : " + i.getFreq())
				.collect(Collectors.joining(", "));
	}
}
