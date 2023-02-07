package kytheros.util.csv_parser;

import java.io.File;

/**
 * This just runs an example. 
 * @author Lucas "kytheros" Gonzales
 *
 */
public class Runner {

	public static void main(String[] args) {
		File f = new File(System.getProperty("user.dir") + "\\CSV Test Files\\ancient_language_roots.csv");
		System.out.println("test:\n\tpath:\t" + f.getAbsolutePath());
		System.out.println("test:\n\texists:\t" + f.exists());

		CSVParser parser = new CSVParser(f);
		System.out.println("test:\n\tDatabase:\n" + parser.getDatabase().toString());
	}

}
