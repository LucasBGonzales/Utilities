package kytheros.util.csv_parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVParser {
	private CSVDatabase m_database;
	private char m_commentDelimiter;
	private char m_itemDelimiter;
	private char m_stringDelimiter;
	private File m_csvFile;


	public CSVParser() {
		m_csvFile = null;
		m_commentDelimiter = '#';
		m_itemDelimiter = ',';
		m_stringDelimiter = '\"';
	}


	/**
	 * Creates the CSVParser object and imports the data from the provided
	 * csv file.
	 * 
	 * @param csv_file File that references a csv file.
	 */
	public CSVParser(File csv_file) {
		this();
		loadCSV(csv_file);
	}


	/**
	 * Returns the {@code CSVDatabase} object for this parser.
	 * 
	 * @return {@code CSVDatabase}
	 */
	public CSVDatabase getDatabase() {
		return m_database;
	}


	/**
	 * Will attempt to load the specified csv file. If there is a file
	 * already loaded, it will be overwritten.
	 * 
	 * @param csv_file
	 */
	public void loadCSV(File csv_file) {
		m_csvFile = csv_file;
		loadCSV();
	}


	private void loadCSV() {
		// Initialize Database
		m_database = new CSVDatabase();

		// Set up scanner to read data.
		Scanner s = null;
		try {
			s = new Scanner(m_csvFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		// Load Data
		int row = 0;
		while (s.hasNextLine()) {
			ArrayList<String> arr_items = new ArrayList<String>();

			// Get Line
			String line = s.nextLine();

			// Get Items
			arr_items.addAll(getItems(line));

			// Parse any remaining content into database
			// Ignore empty row
			if ((arr_items.size() == 1 && arr_items.get(0).equals("")) == false) {
				for (int i = 0; i < arr_items.size(); i++)
					this.m_database.set(row, i, arr_items.get(i).trim());
				row++;
			}
		}

	}


	private ArrayList<String> getItems(String line) {
		// List to Return
		ArrayList<String> arr_items = new ArrayList<String>();

		if (line.length() < 1) {
			return arr_items;
		}

		// Formatting :: Add itemDelimiter to end to signal stop.
		line = line + m_itemDelimiter;

		boolean in_string = false;
		for (int i = 0; i < line.length(); i++) {
			// Get Character
			char c = line.charAt(i);

			if(i==139)
				System.out.print("");
			
			// Test for String
			if (c == m_stringDelimiter) {
				in_string = !in_string;
			}
			// Test for comment, can't be in-string.
			else if (c == m_commentDelimiter && !in_string) {
				// Cut out comment
				line = line.substring(0, i);
				line += " ";
				i--;
			}
			// Test for item, can't be in-string.
			else if ((c == m_itemDelimiter || (i == line.length() - 1 && line.length() > 0)) && !in_string) {
				// Get Item
				String item = line.substring(0, i);

				/** Do this first to have proper length **/
				// Cut it out of line
				line = line.substring(i + 1);

				// Adjust loop index
				i -= item.length() + 1;
				/**  **/

				// Remove double quotes, trim excess spaces.
				item = item.replaceAll((m_stringDelimiter + "" + m_stringDelimiter), m_stringDelimiter + "").trim();

				// Check for surrounding quotes, remove them if they exist
				if (item.length() > 0 && item.charAt(0) == m_stringDelimiter
						&& item.charAt(item.length() - 1) == m_stringDelimiter)
					item = item.substring(1, item.length() - 1);

				// Add item to arr_items
				arr_items.add(item);
			}
		}

		return arr_items;
	}
}
