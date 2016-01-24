package pl.edu.pw.elka.med.closet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.pw.elka.med.db.TDB;
import pl.edu.pw.elka.med.db.Tid;

public class ClosetAlgorithm {
	public int min_sup = 2;

	public FciList start(TDB tdb, int min_sup) {
		this.min_sup = min_sup;
		FreqList f_list = new FreqList();
		f_list = f_list.createFList(tdb, min_sup);
		FciList fciList = new FciList();
		closest(new ArrayList<>(), tdb, f_list, fciList);
		
		return fciList;
	}

	public void closest(List<String> fitem, TDB db, FreqList f_list, FciList fcis) {
		int nrow = db.size();
//		System.out.println("ClosetRun: " + fitem + "; f_list = " + f_list + "; fcis = " + fcis + "; db.size = " + nrow);
		
		FreqList inEveryTx = f_list.inEveryTx(nrow);
//		System.out.println("InEvery: " + inEveryTx);
		fcis.addFciCandidate(fitem, inEveryTx, nrow);
		f_list.removeAll(inEveryTx);
		fitem.addAll(inEveryTx.getList().stream().map(e->e.getItem()).collect(Collectors.toList()));

		HashSet<String> prev = new HashSet<>();
		for (FItem f : f_list.getList()) {
			prev.add(f.getItem());
			FreqList nfList = new FreqList();
			TDB createDB = createDB(f.getItem(), db, prev, f_list, nfList);
			
			List<String> candidate = new ArrayList<>(fitem);
			candidate.add(f.getItem());
			
			int support = createDB.size();
			if (!fcis.containsAlreadyCandidate(candidate, support) && support >= min_sup) {
//				System.out.println(candidate + " > " + createDB);
//				System.out.println("GO: " + candidate + " : " + createDB.size());
				closest(candidate, createDB, nfList, fcis);
			}
		}
	}

	public TDB createDB(String i, TDB db, HashSet<String> prev, FreqList f_list, FreqList newF_list) {
		TDB newDB = new TDB();
		HashMap<String, Integer> freq = new HashMap<>();
		for (Tid tid : db) {
			if (tid.contains(i)) {
				Tid newTid = new Tid();
				for (String t : tid) {
					if (!prev.contains(t) && f_list.contains(t)) {
						newTid.add(t);
						Integer v = freq.get(t);
						freq.put(t, (v == null ? 1 : (v + 1)));
					}
				}
				
				// empty tid also should go to get correct db size
				newDB.add(newTid);
			}
		}

		newF_list.setMap(freq, min_sup);
		return newDB;
	}
}
