package pl.edu.pw.elka.med.closet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FciList {
	private List<FCI> fcis = new ArrayList<>();

	public boolean addFciCandidate(List<String> fitem, FreqList inEveryTx,
			int candSupport) {
		List<String> newFciCandidate = inEveryTx.getList().stream()
				.map(i -> i.getItem()).collect(Collectors.toList());
		// new FCI consist of previous frequency items (fitem's) also
		newFciCandidate.addAll(fitem);

		if (!newFciCandidate.isEmpty()
				&& isGoodCandidate(newFciCandidate, candSupport)) {
			// create new Frequent Closed Itemset
			FCI newFci = new FCI(new HashSet<String>(newFciCandidate),
					candSupport);
			fcis.add(newFci);
//			System.out.println("FCI added: " + newFci);
			return true;
		}
		return false;
	}

	/**
	 * Check if the candidate is already a subset of any other FCI with the same
	 * support. If any FCI contains this candidate and the supports are equal,
	 * then the candidate already has frequent closed itemset (so the candidate
	 * is WRONG).
	 * 
	 * @param candidate
	 * @param candSup
	 *            candidate support
	 * @return true if there exist a FCI which subsumed the candidate
	 */
	public boolean containsAlreadyCandidate(List<String> candidate, int candSup) {
		return !isGoodCandidate(candidate, candSup);
	}

	/**
	 * Check if candidate is a subset of any FCI with the same support. If any
	 * FCI contains this candidate and the supports are equal, then this
	 * candidate is WRONG. Otherwise candidate is good and can be added to
	 * Frequent Closed Itemset.
	 * 
	 * @param candidate
	 * @param candSupport
	 *            candidate support
	 * @return true if no FCI contains this candidate (with the same support).
	 */
	private boolean isGoodCandidate(List<String> candidate, int candSupport) {
		boolean result = true;
		for (FCI fci : fcis) {
			boolean contains = fci.contaisAll(candidate);
			if (contains && fci.getSup() == candSupport) {
				result = false;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return fcis.stream().map(f -> f.toString())
				.collect(Collectors.joining(", "));
	}
}
