package krythos.util.dimensional_arraylist;

import java.util.ArrayList;
import java.util.List;

class MultiDimensionalArrayList<E> {
	private DataPoint m_primaryDataPoint;

	public static void main(String[] args) {
		new MultiDimensionalArrayList<Object>().testDriver();
	}


	public void testDriver() {
		// TODO how do I iterate through everything if I don't know the dimensions?
		// Perhaps a method to return the dimensions... then, utilizing class uses a
		// loop that iterates an increasing array for indices up to that dimension?
		// Perhaps not even. Just use get() for increasing size until there is no more
		// valid sizes?
		MultiDimensionalArrayList<String> md_arr = new MultiDimensionalArrayList<String>();

		md_arr.add("testAdd(0)", 0);
		md_arr.add("testAdd(1)", 1);
		md_arr.add("testAdd()", null);
		System.out.println(md_arr.get(0));
		md_arr.set("testSet(0,0,0,0)", 0, 0, 0, 0);
		System.out.println(md_arr.get(0, 0, 0, 0));

		for (int i0 = 0; i0 < 2; i0++)
			for (int i1 = 0; i1 < 2; i1++)
				md_arr.add(String.format("testAdd(%d,%d)", i0, i1), i0, i1);

		for (int i0 = 0; i0 < 2; i0++)
			for (int i1 = 0; i1 < 2; i1++)
				System.out.println(md_arr.get(i0, i1, 0));

		System.out.println(md_arr.sizeList(0, 0, 0, 0));
		System.out.println(md_arr.sizeObjects(0));
		System.out.println("toString():\n" + md_arr.toString());
		md_arr.remove(0);
		md_arr.remove(1, 1, 0);
		System.out.println("toString():\n" + md_arr.toString());

		List<Integer> indices = new ArrayList<Integer>();

		System.out.println("Test Iteration");


		// How to iterate through all objects sequentially??
		boolean stop = false;
		while (!stop) {
			indices.add(0);

			// Iterate through each dimension
			for (int dim_iterator = indices.size() - 2; dim_iterator >= 0 || indices.size() == 1;) {

				// Iterate through each object in dimension
				for (int index_iterator = 0;; index_iterator++) {
					indices.set(indices.size() - 1, index_iterator);
					try {
						System.out.println(md_arr.get(indices));
					} catch (Exception e) {
						if (index_iterator == 0)
							stop = true;
						indices.set(indices.size() - 1, 0);
						break;
					}
				}
				
				if(indices.size() == 1)
				{
					stop = false;
					break;
				}
				
				indices.set(dim_iterator, indices.get(dim_iterator) + 1);
				if (md_arr.sizeList(indices) <= 0)
				{
					indices.set(dim_iterator, 0);
					dim_iterator--;
					continue;
				}
				else
					stop = false;

			}
		}
	}


	public MultiDimensionalArrayList(MultiDimensionalArrayList<? extends E> c) {
		// TODO Create Copies of other MultiDemensionalArrayLists
	}


	public MultiDimensionalArrayList() {
		m_primaryDataPoint = new DataPoint();
	}


	@Override
	public String toString() {
		return m_primaryDataPoint.toString("");
	}


	public int sizeList(int... indices) {
		return m_primaryDataPoint.sizeList(getIndicesArray(indices));
	}


	public int sizeList(List<Integer> indices) {
		return m_primaryDataPoint.sizeList(new ArrayList<Integer>(indices));
	}


	public int sizeObjects(int... indices) {
		return m_primaryDataPoint.sizeObjects(getIndicesArray(indices));
	}


	public int sizeObjects(List<Integer> indices) {
		return m_primaryDataPoint.sizeObjects(new ArrayList<Integer>(indices));
	}


	/**
	 * Returns the object at the given index.
	 * 
	 * @param indices The location of the object you want to collect from this
	 *                DataPoint.
	 * @return {@code Object} at the given location.
	 * @throws RuntimeException               if the indices List is null.
	 * @throws ArrayIndexOutOfBoundsException if the indices given are
	 *                                        out-of-bounds.
	 */
	public E get(int... indices) throws RuntimeException, ArrayIndexOutOfBoundsException {
		return m_primaryDataPoint.get(getIndicesArray(indices));
	}


	/**
	 * Returns the object at the given index.
	 * 
	 * @param indices The location of the object you want to collect from this
	 *                DataPoint.
	 * @return {@code Object} at the given location.
	 * @throws RuntimeException               if the indices List is null.
	 * @throws ArrayIndexOutOfBoundsException if the indices given are
	 *                                        out-of-bounds.
	 */
	public E get(List<Integer> indices) throws RuntimeException, ArrayIndexOutOfBoundsException {
		return m_primaryDataPoint.get(new ArrayList<Integer>(indices));
	}


	/**
	 * 
	 * @param The index of the item to be removed.
	 * @return Returns the object that was removed from the list.
	 * @param The index of the item to be removed.
	 * @return Returns the object that was removed from the list.
	 * @throws RuntimeException               if the indices List is null.
	 * @throws ArrayIndexOutOfBoundsException if the indices given are
	 *                                        out-of-bounds.
	 */
	public E remove(int... indices) throws RuntimeException, ArrayIndexOutOfBoundsException {
		return m_primaryDataPoint.remove(getIndicesArray(indices));
	}


	/**
	 * Adds the given object to the DataPoint at the given index.
	 * 
	 * @param obj     Object to add to DataPoint.
	 * @param indices Index of the DataPoint to add obj to.
	 * @throws RuntimeException if the indices List is null.
	 */
	public void add(E obj, int... indices) throws RuntimeException {
		if (indices == null)
			m_primaryDataPoint.add(obj, new ArrayList<Integer>(0));
		else
			m_primaryDataPoint.add(obj, getIndicesArray(indices));
	}


	/**
	 * Adds the given object to the DataPoint at the given index.
	 * 
	 * @param obj     Object to add to DataPoint.
	 * @param indices Index of the DataPoint to set obj to.
	 * @throws RuntimeException if the indices List is null.
	 */
	public void set(E obj, int... indices) throws RuntimeException {
		m_primaryDataPoint.set(getIndicesArray(indices), obj);
	}


	/**
	 * Returns a {@code List<Integer>} of indices for the given indefinite list of
	 * indices.
	 * 
	 * @param indices
	 * @return {@code List<Integer>} of indices for the given indefinite list of
	 *         indices.
	 */
	private List<Integer> getIndicesArray(int... indices) {
		List<Integer> arr = new ArrayList<Integer>(0);
		for (int i : indices)
			arr.add(i);
		return arr;
	}


	private class DataPoint {
		private List<E> m_dataObjects;
		private List<DataPoint> m_dataList;


		public DataPoint() {
			m_dataObjects = new ArrayList<E>(0);
			m_dataList = new ArrayList<DataPoint>(0);
		}


		/**
		 * Adds the given object to the DataPoint at the given index.
		 * 
		 * @param obj     Object to add to DataPoint.
		 * @param indices Index of the DataPoint to set obj to.
		 * @throws RuntimeException if the indices List is null.
		 */
		private void set(List<Integer> indices, E obj) {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If no indices, this is the DataPoint to add the Object to.
			if (indices.size() == 1) {
				while (m_dataObjects.size() <= indices.get(0))
					m_dataObjects.add(null);
				m_dataObjects.set(indices.get(0), obj);
			} else {
				// Else, get the next index, remove it from indices array, then add the object
				// to that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Make Sure DataPoint Exists.
				while (m_dataList.size() <= this_index)
					m_dataList.add(new DataPoint());

				// Add to DataPoint at this_index.
				m_dataList.get(this_index).set(indices, obj);
			}
		}


		/**
		 * Adds the given object to the DataPoint at the given index.
		 * 
		 * @param obj     Object to add to DataPoint.
		 * @param indices Index of the DataPoint to add obj to.
		 * @throws RuntimeException if the indices List is null.
		 */
		private void add(E obj, List<Integer> indices) throws RuntimeException {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If no indices, this is the DataPoint to add the Object to.
			if (indices.size() == 0)
				m_dataObjects.add(obj);
			else {
				// Else, get the next index, remove it from indices array, then add the object
				// to that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Make Sure DataPoint Exists.
				while (m_dataList.size() <= this_index)
					m_dataList.add(new DataPoint());

				// Add to DataPoint at this_index.
				m_dataList.get(this_index).add(obj, indices);
			}
		}


		/**
		 * 
		 * @param The index of the item to be removed.
		 * @return Returns the object that was removed from the list.
		 * @throws RuntimeException               if the indices List is null.
		 * @throws ArrayIndexOutOfBoundsException if the indices given are
		 *                                        out-of-bounds.
		 */
		private E remove(List<Integer> indices) throws RuntimeException, ArrayIndexOutOfBoundsException {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If one index, this is the DataPoint to get the Object from.
			if (indices.size() == 1)
				return m_dataObjects.remove(indices.get(0).intValue());
			else {
				// Else, get the next index, remove it from indices array, then get the object
				// of that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Check this_index exists
				if (m_dataList.size() <= this_index)
					throw new ArrayIndexOutOfBoundsException("Index From " + indices.toString() + " is Out-Of-Bounds.");
				else
					// Get DataPoint from this_index.
					return m_dataList.get(this_index).remove(indices);
			}
		}


		/**
		 * Recursive get function.
		 * 
		 * @param indices The location of the object you want to collect from this
		 *                DataPoint.
		 * @return {@code Object} at the given location.
		 * @throws RuntimeException               if the indices List is null.
		 * @throws ArrayIndexOutOfBoundsException if the indices given are
		 *                                        out-of-bounds.
		 */
		private E get(List<Integer> indices) throws RuntimeException, ArrayIndexOutOfBoundsException {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If one index, this is the DataPoint to get the Object from.
			if (indices.size() == 1)
				return m_dataObjects.get(indices.get(0));
			else {
				// Else, get the next index, remove it from indices array, then get the object
				// of that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Check this_index exists
				if (m_dataList.size() <= this_index)
					throw new ArrayIndexOutOfBoundsException("Index From " + indices.toString() + " is Out-Of-Bounds.");
				else
					// Get DataPoint from this_index.
					return m_dataList.get(this_index).get(indices);
			}
		}


		/**
		 * @return Integer size of the array of Lists in this DataPoint.
		 */
		public int sizeList(List<Integer> indices) throws RuntimeException {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If one index, this is the DataPoint to get the Object from.
			if (indices.size() == 0)
				return m_dataList.size();
			else {
				// Else, get the next index, remove it from indices array, then get the object
				// of that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Check this_index exists
				if (m_dataList.size() <= this_index)
					return -1;
				else
					// Get DataPoint from this_index.
					return m_dataList.get(this_index).sizeList(indices);
			}
		}


		/**
		 * @return Integer size of the array of objects in this DataPoint.
		 */
		public int sizeObjects(List<Integer> indices) throws RuntimeException {
			if (indices == null)
				throw new RuntimeException("Null Indices Array: Must be non-null");

			// If one index, this is the DataPoint to get the Object from.
			if (indices.size() == 0)
				return m_dataObjects.size();
			else {
				// Else, get the next index, remove it from indices array, then get the object
				// of that index's DataPoint using the reduced indices array.

				// Get Index & Remove it.
				int this_index = indices.get(0);
				indices.remove(0);

				// Check this_index exists
				if (m_dataList.size() <= this_index)
					return -1;
				else
					// Get DataPoint from this_index.
					return m_dataList.get(this_index).sizeObjects(indices);
			}
		}


		private String toString(String current_index) {
			String ret = "";

			// Add the objects for this DataPoint.
			for (int i = 0; i < m_dataObjects.size(); i++) {
				String this_point = "(";
				if (current_index.length() != 0)
					this_point += current_index + ", ";
				this_point += i + ") : ";
				if (m_dataObjects.get(i) == null)
					this_point += "null";
				else
					this_point += m_dataObjects.get(i).toString();
				ret += this_point + "\n";
			}

			// Add the objects for each DataPoint in this DataPoint's m_dataList.
			for (int i = 0; i < m_dataList.size(); i++) {
				String next_index = (current_index + "");
				if (current_index.length() != 0)
					next_index += ", ";
				next_index += i;
				ret += m_dataList.get(i).toString(next_index);
			}

			return ret;
		}
	}
}