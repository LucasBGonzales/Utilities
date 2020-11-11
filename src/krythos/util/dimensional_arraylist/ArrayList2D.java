package krythos.util.dimensional_arraylist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Just a wrapper for a 2-dimensional ArrayList to make working with such things
 * easier.
 * 
 * @author Lucas "Krythos" Gonzales
 *
 * @param <E> Type of object for these lists to contain.
 */
public class ArrayList2D<E> {
	private List<List<E>> m_array;
	private int m_initCapX, m_initCapY;

	/**
	 * Constructs an empty 2-dimensional list with initial capacity of 10 rows and
	 * 10 columns.
	 */
	public ArrayList2D() {
		this(10, 10);
	}


	/**
	 * Constructs an empty 2-dimensional list with initial capacity of
	 * {@code initialCapacity} rows and 0 columns.
	 * 
	 * @param initialCapacity
	 */
	public ArrayList2D(int initialCapacity) {
		m_initCapX = initialCapacity;
		m_initCapY = initialCapacity;
		m_array = new ArrayList<List<E>>(initialCapacity);
	}


	/*
	 * Constructs an empty 2-dimensional list with initial capacity of {@code
	 * initCapX} rows and {@code initCapY} columns.
	 */
	public ArrayList2D(int initCapX, int initCapY) {
		this(initCapX);
		m_initCapY = initCapY;
		for (List<E> list : m_array)
			list = new ArrayList<E>(initCapY);
	}


	public ArrayList2D(Collection<Collection<E>> c) {
		m_array = new ArrayList<List<E>>(0);

		for (Collection<E> obj : c)
			m_array.add(new ArrayList<E>(obj));
	}


	/**
	 * Add a new row to the array.
	 */
	public void addRow() {
		m_array.add(new ArrayList<E>(m_initCapY));
	}


	/**
	 * Add a new column to each row with a {@code null} object.
	 */
	public void addColumn() {
		addColumn(null);
	}


	/**
	 * Add a new column to each row with the given object.
	 * 
	 * @param obj
	 */
	public void addColumn(E obj) {
		for (List<E> list : m_array)
			list.add(obj);
	}


	/**
	 * Sets the specified object to the specified location.
	 * 
	 * @param r   Row
	 * @param c   Column
	 * @param obj Object to set
	 */
	public void set(int r, int c, E obj) {
		m_array.get(r).set(c, obj);
	}


	public E get(int r, int c) {
		return m_array.get(r).get(c);
	}


	public List<E> get(int r) {
		return m_array.get(r);
	}


	public void add(int r, E obj) {
		m_array.get(r).add(obj);
	}


	public void add(int r, int c, E obj) {
		m_array.get(r).add(c, obj);
	}


	public void remove(int r, int c) {
		m_array.get(r).remove(c);
	}


	public void remove(int r) {
		m_array.remove(r);
	}


	public int size() {
		return m_array.size();
	}


	public int size(int r) {
		return m_array.get(r).size();
	}


	/**
	 * @return
	 */
	public List<List<E>> getList() {
		return m_array;
	}
}
