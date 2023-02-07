package kytheros.util.misc;

import java.util.Arrays;
import java.util.Objects;

public class KArrays {

	/**
	 * Creates a copy of array with the object at the given index removed. The
	 * length of the new Object[] will be array.length-1;
	 * 
	 * @param array Source array to copy from.
	 * @param index Index to remove in array.
	 * @return Object[] with element at index removed.
	 */
	public static Object[] remove(Object[] array, int index) {
		Object[] new_array = new Object[array.length - 1];
		boolean f_removed = false;
		for (int i = 0; i < array.length; i++) {
			if (i == index) {
				f_removed = true;
				continue;
			}
			new_array[i - (f_removed ? 1 : 0)] = array[i];
		}
		return new_array;
	}


	/**
	 * Creates a copy of array with the first instance of the specified element
	 * removed. The length of the new Object[] will be array.length-1 if the element
	 * exists.
	 * 
	 * @param array   Source array to copy from.
	 * @param element Element to remove in array.
	 * @return new Object[] with first instance of element removed if it exists, or
	 *         a direct copy of array if it doesn't exist.
	 */
	public static Object[] remove(Object[] array, Object element) {
		// If the element doesn't exist in the array, return a copy of the array.
		if (KArrays.indexOf(array, element) < 0)
			return Arrays.copyOf(array, array.length);

		// Remove the first instance of the element in the array.
		Object[] new_array = new Object[array.length - 1];
		boolean f_removed = false;
		for (int i = 0; i < array.length; i++) {
			if (!f_removed && array[i].equals(element)) {
				f_removed = true;
				continue;
			}
			new_array[i - (f_removed ? 1 : 0)] = array[i];
		}
		return new_array;
	}


	/**
	 * Creates a copy of array with all instances of the specified element
	 * removed. The length of the new Object[] will be {@code array.length-instances}.
	 * 
	 * @param array   Source array to copy from.
	 * @param element Element to remove in array.
	 * @return new Object[] with all instances of element removed if it exists, or
	 *         a direct copy of array if it doesn't exist.
	 */
	public static Object[] removeAll(Object[] array, Object element) {
		// If the element doesn't exist in the array, return a copy of the array.
		if (KArrays.indexOf(array, element) < 0)
			return Arrays.copyOf(array, array.length);
		
		int instances = countMatches(array, element);
		
		Object[] new_array = new Object[array.length - instances];
		
		instances=0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(element)) {
				instances++;
				continue;
			}
			new_array[i - instances] = array[i];
		}
		
		return new_array;
	}


	/**
	 * Counts the number of instances of the element in the array.
	 * 
	 * @param array   Array to perform upon.
	 * @param element Element to count.
	 * @return Number of instances of {@code element} in the array.
	 */
	public static int countMatches(Object[] array, Object element) {
		int count = 0;
		for (Object e : array)
			if (e.equals(element))
				count++;
		return count;
	}


	/**
	 * Returns the index of the first occurrence of the specified element in the
	 * array, or -1 if the array does not contain the element.
	 * <p>
	 * More formally, returns the lowest index i such that Objects.equals(element,
	 * array[i]), or -1 if there is no such index.
	 * 
	 * @param array   Array to search.
	 * @param element Element to search for.
	 * @return the index of the first occurrence of the specified element in the
	 *         array, or -1 if the array does not contain the element.
	 */
	public static int indexOf(Object[] array, Object element) {
		return KArrays.indexOf(array, element, 0);
	}


	/**
	 * Returns the index of the first occurrence of the specified element in the
	 * array following {@code fromIndex}, or -1 if the array does not contain the
	 * element following {@code fromIndex}.
	 * <p>
	 * More formally, returns the lowest index i such that Objects.equals(element,
	 * array[i]), or -1 if there is no such index.
	 * 
	 * @param array     Array to search.
	 * @param element   Element to search for.
	 * @param fromIndex Index to start searching from.
	 * @return the index of the first occurrence of the specified element in the
	 *         array following {@code fromIndex}, or -1 if the array does not
	 *         contain the element following {@code fromIndex}.
	 */
	public static int indexOf(Object[] array, Object element, int fromIndex) {
		for (int i = fromIndex; i < array.length; i++)
			if (Objects.equals(array[i], element))
				return i;
		return -1;
	}


	/**
	 * Returns the index of the last occurrence of the specified element in the
	 * array, or -1 if the array does not contain the element.
	 * <p>
	 * More formally, returns the highest index i such that Objects.equals(element,
	 * array[i]), or -1 if there is no such index.
	 * 
	 * @param array   Array to search.
	 * @param element Element to search for.
	 * @return the index of the last occurrence of the specified element in the
	 *         array, or -1 if the array does not contain the element.
	 */
	public static int lastIndexOf(Object[] array, Object element) {
		for (int i = array.length - 1; i > 0; i--)
			if (Objects.equals(array[i], element))
				return i;
		return -1;
	}
}
