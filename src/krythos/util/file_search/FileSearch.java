package krythos.util.file_search;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import krythos.util.logger.Log;

public class FileSearch {

	/**
	 * Return a {@code List} of files inside of the given directory.
	 * Parameters are used to refine search criteria.
	 * 
	 * <p>
	 * nested_depth defines how many sub-directories to search. A
	 * negative value specifies to search all sub-directories. A value of
	 * zero will only search the given directory.
	 * 
	 * <p>
	 * extensions defines which file extensions to include. An empty
	 * list will include all file extensions, else only the specified
	 * extensions will be included.
	 * <p>
	 * Example: {"xml","txt"} will only include XML and Text Documents
	 * in the returned {@code List).
	 * 
	 * @param directory the parent directory to be searched.
	 * 
	 * @param nested_depth    the depth of nesting to search. A negative
	 *                        number means no limit.
	 * @param extensions      the extensions to filter by. An empty list
	 *                        will include all files.
	 * @param include_folders whether to include all nested folders.
	 * @return
	 */
	public static List<File> getNestedFiles(File directory, int nested_depth, String[] extensions,
			boolean include_folders) {

		// Array to return.
		List<File> lstReturn = new ArrayList<File>(0);

		// Files to parse
		List<File> openList = new ArrayList<File>(0);
		openList.addAll(Arrays.asList(directory.listFiles()));

		// Loop while openList has content and we are within our depth
		// parameters.
		for (int depth = 0; openList.size() > 0 && (depth <= nested_depth || nested_depth < 0); depth++) {
			List<File> next_list = new ArrayList<File>(0);

			for (File file : openList) {
				// Handle folders.
				if (file.isFile() == false) {
					// If folders are included, add this to return list.
					if (include_folders)
						lstReturn.add(file);
					// Get all nested files.
					next_list.addAll(Arrays.asList(file.listFiles()));
				} else {
					if (extensions == null || extensions.length == 0)
						lstReturn.add(file);
					else
						for (String e : extensions) {
							int file_length = file.getName().length();
							String file_ext = file.getName().substring(file_length - (e.length()), file_length);
							if ((file_length > e.length() + 1) && file_ext.equals(e)) {
								lstReturn.add(file);
								break;
							}
						}
				}
			}
			openList = next_list;
		}

		return lstReturn;
	}


	public static void main(String[] a) {
		Log.println(getNestedFiles(new File("D:\\My Stuff\\Documents\\Sourcetree\\LanguageConstructionKit"), -1,
				new String[] { "xml" }, false).toString());
	}
}
