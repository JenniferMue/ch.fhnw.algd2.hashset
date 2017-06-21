package ch.fhnw.algd2.hashing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Hashset mit Open Addressing Kollisionsbehandlung
 */
public class MyHashSet<E> implements Set<E> {

  private Object[] table;
  private int size = 0;
  private static final Object SENTINEL = new Object();

  public MyHashSet(int minCapacity) {
    if (minCapacity < 4) {
      throw new IllegalArgumentException("At least table size 4 required");
    }
    // TODO: Aufgabe 1
    int capacity = 1;
    while (capacity < minCapacity) {
      // bitshift --> from 1 to 2, 2 to 4, 4 to 8 and 8 to 16
      capacity <<= 1;
    }
    table = new Object[minCapacity];
  }

  public int startIndex(Object o) {
    // TODO: Aufgabe 2
    return (o.hashCode() & table.length - 1);
  }

  public int step(Object o) {
    int stepSize = 1 + (o.hashCode() & table.length - 1)
        % (table.length - 2);
    return stepSize = stepSize % 2 == 0 ? stepSize - 1 : stepSize;
  }

  public int index(Object o) {
    int index = startIndex(o);
    int step = step(o);
    while (table[index] != null) {
      index = index + step & (table.length - 1);
    }
    return index;
  }

  @Override
  public boolean contains(Object o) {
    // TODO: Aufgabe 3
    if (o == null) {
     throw new NullPointerException("Null is not allowed");
    }
    int step = step(o);
    int counter = 0;
    int index = startIndex(o);
    while (table[index] != null && !table[index].equals(o) && counter != table.length) {
      index = (index + step) % table.length;
      ++counter;
    }
    return o.equals(table[index]);
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

    if (contains(e)) {
      return false;
    }

    if (table.length == size) {
      throw new IllegalStateException("table is full");
    }
    int index = startIndex(e);
    int step = step(e);
    // TODO: Aufgabe 2
    while (table[index] != null && table[index] != SENTINEL) {
      index = (index + step) % table.length;
    }
    table[index] = e;
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
    return false;
  }

  @Override
  public void clear() {
    table = new Object[table.length];
    size = 0;
  }
}
