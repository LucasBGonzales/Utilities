package krythos.util.misc;


public class KArrays {
	// TODO Expand with more indexOf operations.

	public static int indexOf(Object[] array, Object element) {
		return KArrays.indexOf(array, element, 0);
	}


	public static int indexOf(Object[] array, Object element, int fromIndex) {
		for (int i = fromIndex; i < array.length; i++)
			if (array[i].equals(element))
				return i;
		return -1;
	}
}
