package ch.fhnw.algd2.hashing;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class ContainsElementTest extends AbstractHashTest {

	@Test
	public void checkContainsMethod_WithoutCollision() {
		Set<String> set = MyHashSetFactory.create(8);
		assertTrue(set.add("A"));
		assertTrue(set.add("B"));
		assertTrue(set.add("C"));

		assertTrue(set.contains("A"));
		assertTrue(set.contains("B"));
		assertTrue(set.contains("C"));
		assertFalse(set.contains("E"));
		assertFalse(set.contains("b"));
	}

	@Test(expected = NullPointerException.class)
	public void checkContainsNull_ExpectExcption() {
		MyHashSetFactory.create(8).contains(null);
		fail("Should throw a Nullpointer Exception");
	}

	@Test(timeout = 5000)
	public void checkContainsInFullArray_WithCollition() {
		checkContainsInFullArray(true);
	}

	@Test(timeout = 5000)
	public void checkContainsInFullArray_WithoutCollition() {
		checkContainsInFullArray(false);
	}

	private void checkContainsInFullArray(boolean withCollision) {
		MyHashSet<String> set = MyHashSetFactory.create(7);
		int capacity = set.copyOfArray().length;
		String[] strings;
		if (withCollision) {
			strings = getStringsWithEqualHash(capacity);
		} else {
			strings = getNonLetterStringsWithDifferentHash(capacity);
		}

		for (int i = 0; i < capacity; i++) {
			set.add(strings[i]);
		}
		assertEquals(capacity, set.size());
		assertFalse(set.contains("A"));
	}
}
