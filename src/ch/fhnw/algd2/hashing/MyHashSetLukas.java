package ch.fhnw.algd2.hashing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Hashset mit Open Addressing Kollisionsbehandlung --> Double Hashing
 */
public class MyHashSetLukas<E> implements Set<E> {

	private static final Object SENTINEL = new Object();

	private Object[] table;
	private int size = 0;

	public MyHashSetLukas(int minCapacity) {
		if (minCapacity < 4) {
			throw new IllegalArgumentException("At least table size 4 required");
		}
		// TODO: Aufgabe 1
		int capacity = 1;
		while (capacity < minCapacity) {
			// bitshift --> from 1 to 2, 2 to 4, 4 to 8, ...
			capacity <<= 1;
		}
		table = new Object[capacity];
	}

	@Override
	public boolean contains(Object o) {
		// TODO: Aufgabe 3
		if (o == null) {
			throw new NullPointerException("Null is not allowed");
		}

		int counter = 0;
		int step = calcStep(o);
		int index = startIndex(o);
		while(table[index] != null && !table[index].equals(o)
				&& counter != table.length) {
			index = (index + step) % table.length;
			++counter;
		}

		return o.equals(table[index]);
	}

	private int startIndex(Object o) {
		return (o.hashCode() & table.length - 1);
	}

	private int calcStep(Object o) {
		int stepSize = 1 + (o.hashCode() & table.length - 1)
				% table.length - 2;
		return stepSize % 2 == 0 ? stepSize - 1 : stepSize;
	}

	private int index(Object o) {
		int index = startIndex(o);
		int step = calcStep(o);
		while (table[index] != null) {
			index = index + step & (table.length - 1);
		}
		return index;
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public Object[] copyOfArray() {
		return Arrays.copyOf(table, table.length);
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(E e) {
		if (e == null) {
			throw new NullPointerException(
					"Null are not allowed in this collection.");
		}

		// TODO: Aufgabe 2
		if (contains(e)) {
			return false;
		}

		if (size == table.length) {
			throw new IllegalStateException("Can not add, table is full");
		}
		int i = startIndex(e);
		int step = calcStep(e);
		while (table[i] != null && table[i] != SENTINEL) {
			i = (i + step) % table.length;
		}
		table[i] = e;
		++size;

		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean hasChanged = false;
		for (E elem : c) {
			if (add(elem)) {
				hasChanged = true;
			}
		}
		return hasChanged;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			if (remove(o)) {
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null)
			throw new NullPointerException("Null not allowed");
		// TODO: Aufgabe 4
		int index = index(o);
		if (!contains(o)) {
			return false;
		}
		table[index] = SENTINEL;
		--size;
		return true;
	}

	@Override
	public void clear() {
		table = new Object[table.length];
		size = 0;
	}

}
