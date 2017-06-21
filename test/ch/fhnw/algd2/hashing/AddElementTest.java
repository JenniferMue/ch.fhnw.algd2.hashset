package ch.fhnw.algd2.hashing;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class AddElementTest extends AbstractHashTest {
	@Test(expected = NullPointerException.class)
	public void checkAddingNull_expectException() {
		Set<String> set = MyHashSetFactory.create(8);
		set.add(null);
		fail("Should throw exception");
	}

	@Test(timeout = 5000)
	public void testAddElements_WithoutCollision() {
		String[] letters = getNonLetterStringsWithDifferentHash(5);
		MyHashSet<String> set = MyHashSetFactory.create(8);
		addAndCheckElements(letters, set);
	}

	@Test(timeout = 5000)
	public void testAddElements_WithHashCodeMinusOne() {
		MyHashSet<Object> set = new MyHashSet<Object>(8);
		for (int i = 0; i < set.copyOfArray().length; i++) {
			set.add(new MockObject(-1));
		}
	}

	@Test(timeout = 5000)
	public void testAddElements_WithHashCodeMinus2() {
		MyHashSet<Object> set = new MyHashSet<Object>(8);
		for (int i = 0; i < set.copyOfArray().length; i++) {
			set.add(new MockObject(-2));
		}
	}

	private void addAndCheckElements(String[] letters, MyHashSet<String> set) {
		for (String s : letters) {
			assertTrue(set.add(s));
		}
		assertEquals(letters.length, set.size());
		Object[] objects = set.copyOfArray();
		for (Object o : objects) {
			if (o != null) assertTrue(contains(letters, o));
		}
	}

	@Test(timeout = 5000)
	public void testAddElements_WithCollision() {
		String[] words = getStringsWithEqualHash(7);
		MyHashSet<String> set = MyHashSetFactory.create(16);
		addAndCheckElements(words, set);
	}

	@Test(timeout = 5000)
	public void testAddMaxElements_WithCollision() {
		// Prime: 257, powerOf2: 256
		MyHashSet<String> set = MyHashSetFactory.create(256);
		int capacity = set.copyOfArray().length;
		String[] stringsHavingSameHash = getStringsWithEqualHash(capacity);
		addAndCheckElements(stringsHavingSameHash, set);
	}

	@Test(timeout = 5000)
	public void testAddingDuplicates() {
		Set<String> set = MyHashSetFactory.create(8);
		String[] strings = new String[] { "A", "B", "C" };
		for (String s : strings) {
			assertTrue(set.add(s));
		}
		assertEquals(strings.length, set.size());
		for (String s : strings) {
			assertFalse(set.add(s));
			assertEquals(strings.length, set.size());
		}
	}

	@Test(timeout = 5000, expected = IllegalStateException.class)
	public void testAddElementToFullSet_ExpectException() {
		MyHashSet<String> set = MyHashSetFactory.create(7);
		int capacity = set.copyOfArray().length;
		for (int i = 0; i < capacity; i++) {
			assertTrue(set.add(String.valueOf(i + 70)));
		}
		set.add("A");
		fail("Should throw an IllegalStateException");
	}

	@Test(timeout = 5000)
	public void testAddDuplicateToFullSet_ExpectResultFalse() {
		MyHashSet<String> set = MyHashSetFactory.create(8);
		int capacity = set.copyOfArray().length;
		String[] stringsHavingSameHash = getStringsWithEqualHash(capacity);
		addAndCheckElements(stringsHavingSameHash, set);
		assertFalse("Already existing element can always be added",
				set.add(stringsHavingSameHash[capacity - 1]));
	}

	private boolean contains(String[] elements, Object o) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].equals(o)) {
				return true;
			}
		}
		return false;
	}

	private static final class MockObject {
		private final int hashCode;

		public MockObject(int hashCode) {
			this.hashCode = hashCode;
		}

		@Override
		public int hashCode() {
			return hashCode;
		}
	}
}
