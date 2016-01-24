package pl.edu.pw.elka.med;

import pl.edu.pw.elka.med.closet.ClosetAlgorithm;
import pl.edu.pw.elka.med.closet.FciList;
import pl.edu.pw.elka.med.db.TDB;

public class ClosetMain {

	public static void main(String[] args) {
		ClosetAlgorithm alg = new ClosetAlgorithm();

		if (args.length != 2) {
			System.out.println("Podaj dwa parametry:");
			System.out.println("\t<nazwa_pliku> <wsparcie_w_ulamku>");
			System.out.println("Na przykład: ");
			System.out.println("\tres/mush.data 0.4");
			return;
		}

		TDB tdb = ReadCsv.read(args[0]);
		Double min_sup = Double.parseDouble(args[1]) * tdb.size();

		FciList foundFcis = alg.start(tdb, min_sup.intValue());
		System.out.println("Result FCIs: ");
		System.out.println(foundFcis);
		System.out.println("DONE");
	}
}
