package krythos.util.csv_parser;

import java.util.ArrayList;

public class CSVDatabase {
	ArrayList<ArrayList<String>> m_database;

	public CSVDatabase() {
		m_database = new ArrayList<ArrayList<String>>();
	}

	/**
	 * Returns the item at the provided row and index in the csv database. The
	 * indices provided should be greater than or equal to zero; if they are nor,
	 * then an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param row   row of csv. Should be >= 0.
	 * @param index item index on row. Should be >= 0.
	 * @return {@code String} for item on the specified row. If the index or row
	 *         does not exist, returns {@code null}.
	 * @throws IndexOutOfBoundsException - if the index is out of range(index < 0 ||
	 *                                   index >= size())
	 */
	public String get(int row, int index) {
		// If in range, get item.
		if (row < m_database.size() && index < m_database.get(row).size())
			return m_database.get(row).get(index);

		// If not, return null
		return null;
	}

	/**
	 * Sets the item at the given row and index to the given {@code String} The
	 * indices provided should be greater than or equal to zero; if they are nor,
	 * then an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param row   row of csv. Should be >= 0.
	 * @param index item index on row. Should be >= 0.
	 * @param item  {@code String} item to add at specified row and index.
	 * @throws IndexOutOfBoundsException - if the index is out of range(index < 0)
	 */
	public void set(int row, int index, String item) {

		// Expand Database to fit.
		while (m_database.size() <= row)
			m_database.add(new ArrayList<String>());

		while (m_database.get(row).size() <= index)
			m_database.get(row).add("");

		// Add item
		m_database.get(row).set(index, item);

	}

	/**
	 * Returns the number of rows in the database.
	 * 
	 * @return {@code Integer}
	 */
	public int getRows() {
		return m_database.size();
	}

	/**
	 * Returns the number of items in a given row.
	 * 
	 * @param row
	 * @return {@code Integer}
	 */
	public int getRowLength(int row) {
		return m_database.get(row).size();
	}

	@Override
	public String toString()
	{
		String str_return = "";
		
		// Build String
		for(int row=0; row < getRows(); row++)
		{
			// Construct line
			for(int i=0; get(row,i) != null; i++)
				str_return += "\"" + get(row,i) + "\",";
			
			// Remove last comma, add newline
			str_return = str_return.substring(0, str_return.length()-1) + "\n";
		}
		
		return str_return;
	}
}
