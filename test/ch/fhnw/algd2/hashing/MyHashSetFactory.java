package ch.fhnw.algd2.hashing;

public class MyHashSetFactory {

	public static <E> MyHashSet<E> create(int minCapacity) {
		return new MyHashSet<E>(minCapacity);
	}

}
