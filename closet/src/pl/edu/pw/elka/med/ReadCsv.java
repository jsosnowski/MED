package pl.edu.pw.elka.med;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import pl.edu.pw.elka.med.db.TDB;
import pl.edu.pw.elka.med.db.Tid;

public class ReadCsv {

	public static TDB read(final String fileName) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		TDB tdb = new TDB();

		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				String[] items = line.split(cvsSplitBy);
//				tdb.add(new Tid(Arrays.asList(items)));
				tdb.addTid(items); //zamienia elementy na sztuczne identyfikatory
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return tdb;
	}
}
