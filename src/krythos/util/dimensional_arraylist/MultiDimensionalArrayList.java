package krythos.util.dimensional_arraylist;

import java.util.ArrayList;
import java.util.List;

class MultiDimensionalArrayList<E> {
	private DataPoint m_primaryDataPoint;

	public static void main(String[] args) {
		new MultiDimensionalArrayList<Object>(0).testDriver();
	}


	public void testDriver() {
		/*
		 * TODO I have this half-working. You can add as many items as you wish to the
		 * final dimension of the MultiDimensionalArrayList, however I still need to
		 * <--allow the intervening dimensions to expand an contract-->. This is more
		 * complicated than I originally thought it would be.
		 */
		MultiDimensionalArrayList<String> md_arr = new MultiDimensionalArrayList<String>(2, 2, 2);

		for (int i0 = 0; i0 < 2; i0++)
			for (int i1 = 0; i1 < 2; i1++)
				for (int i2 = 0; i2 < 5; i2++)
					md_arr.add(String.format("(%d,%d,%d)", i0, i1, i2), i0, i1, i2);

		for (int i0 = 0; i0 < 2; i0++)
			for (int i1 = 0; i1 < 2; i1++)
				for (int i2 = 0; i2 < 5; i2++)
					System.out.println(md_arr.get(i0, i1, i2));
	}


	public MultiDimensionalArrayList(MultiDimensionalArrayList<? extends E> c) {
		// TODO Create Copies of other MultiDemensionalArrayLists
	}


	public MultiDimensionalArrayList(int... dim) {
		m_primaryDataPoint = new DataPoint(getIndicesArray(dim));
	}


	/**
	 * 
	 * @param indices The location of the object you want to collect.
	 * @return {@code Object} at the given location.
	 * @throws RuntimeException if the indices given are out-of-bounds.
	 */
	@SuppressWarnings("unchecked")
	public E get(int... indices) {
		return (E) m_primaryDataPoint.get(getIndicesArray(indices));
	}


	public void add(E obj, int... indices) {
		m_primaryDataPoint.add(obj, getIndicesArray(indices));
	}


	private List<Integer> getIndicesArray(int... indices) {
		List<Integer> arr = new ArrayList<Integer>(0);
		for (int i : indices)
			arr.add(i);
		return arr;
	}


	public class DataPoint {
		private List<Object> m_data;

		public DataPoint(List<Integer> dim) {
			List<Integer> newDim = new ArrayList<Integer>(dim);
			if (dim.size() > 1) {
				m_data = new ArrayList<Object>(0);
				int this_index = newDim.get(0);
				newDim.remove(0);
				for (int i = 0; i < this_index; i++)
					m_data.add(new DataPoint(newDim));
			} else
				m_data = new ArrayList<Object>(newDim.get(0));
		}


		@SuppressWarnings("unchecked")
		private void add(Object obj, List<Integer> indices) {
			RuntimeException e_invalidLocation = new RuntimeException("Invalid Location: " + indices.toString());

			// If nothing here, Invalid Location
			if (indices.size() < 1)
				throw e_invalidLocation;

			// Get the index for this Object, remove from indices
			int this_index = indices.get(0);
			indices.remove(0);

			// If nothing left in indices, return this object. Else, continue recursion.
			if (indices.size() == 0) {
				m_data.add(obj);
			} else {
				// If object doesn't exist, Invalid Location
				if (m_data.size() <= this_index)
					throw e_invalidLocation;

				// Try to get DataPoint. If not a DataPoint, Invalid Location
				DataPoint p;
				try {
					p = (DataPoint) m_data.get(this_index);
					p.add(obj, indices);
				} catch (Exception e) {
					throw e_invalidLocation;
				}

			}
		}


		/**
		 * Recursive get function.
		 * 
		 * @param indices The location of the object you want to collect from this
		 *                DataPoint.
		 * @return {@code Object} at the given location.
		 * @throws RuntimeException if the indices given are out-of-bounds.
		 */
		@SuppressWarnings("unchecked")
		private Object get(List<Integer> indices) throws RuntimeException {
			RuntimeException e_invalidLocation = new RuntimeException("Invalid Location: " + indices.toString());

			// If nothing here, Invalid Location
			if (indices.size() < 1)
				throw e_invalidLocation;

			// Get the index for this Object, remove from indices
			int this_index = indices.get(0);
			indices.remove(0);

			// If nothing left in indices, return this object. Else, continue recursion.
			if (indices.size() == 0)
				if (this_index >= m_data.size())
					throw e_invalidLocation;
				else
					return m_data.get(this_index);
			else {
				// If object doesn't exist, Invalid Location
				if (m_data.size() <= this_index)
					throw e_invalidLocation;

				// Try to get DataPoint. If not a DataPoint, Invalid Location
				DataPoint p;
				try {
					p = (DataPoint) m_data.get(this_index);
					return p.get(indices);
				} catch (Exception e) {
					throw e_invalidLocation;
				}

			}
		}


		/**
		 * 
		 * @return
		 */
		public int size() {
			return m_data.size();
		}
	}
}