package ch.fhnw.algd2.hashing;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class RemoveElementTest extends AbstractHashTest {
	@Test
	public void testRemoveNonExistingElement() {
		Set<String> set = MyHashSetFactory.create(8);
		set.add("A");
		set.add("B");
		assertEquals(2, set.size());
		assertFalse(set.remove("C"));
		assertFalse(set.remove("D"));
		assertEquals(2, set.size());
	}

	@Test(expected = NullPointerException.class)
	public void testRemoveNull_ExpectException() {
		MyHashSetFactory.create(8).remove(null);
		fail("Should throw an exception");
	}

	@Test
	public void testRemoveElement() {
		Set<String> set = MyHashSetFactory.create(8);
		set.add("A");
		set.add("B");
		assertEquals(2, set.size());
		assertTrue(set.contains("A"));
		assertTrue(set.remove("A"));
		assertFalse(set.contains("A"));
		assertEquals(1, set.size());
	}

	@Test
	public void testRemoveAndAdd() {
		MyHashSet<String> set = MyHashSetFactory.create(8);
		String toRemove = "aoffckzdaoffckzdddnqavbj";
		String toRemove2 = "aoffckzdaoffckzdbhlijevx";
		String[] words = new String[] { "aoffckzdaoffckzdaoffckzd",
				"aoffckzdaoffckzdatafwjsh", toRemove2, "aoffckzdaoffckzdbrbjscia",
				"aoffckzdaoffckzdcyxowxpb", toRemove, "aoffckzdaoffckzddiiqutzn" };
		String toAdd = "aoffckzdaoffckzddndrjssr";
		String toAdd2 = "aoffckzdaoffckzddwyssqez";
		for (String word : words) {
			assertTrue(set.add(word));
		}
		// Remove first element
		assertEquals(words.length, set.size());
		assertTrue(set.remove(toRemove));
		assertEquals(words.length - 1, set.size());
		assertEquals(1, numSentinels(words, set));
		// Remove second element
		assertTrue(set.remove(toRemove2));
		assertEquals(words.length - 2, set.size());
		assertEquals(2, numSentinels(words, set));
		String[] allWords = new String[words.length + 2];
		System.arraycopy(words, 0, allWords, 0, words.length);
		allWords[words.length] = toAdd;
		allWords[words.length + 1] = toAdd2;
		// Add element
		assertTrue(set.add(toAdd));
		assertEquals(words.length - 1, set.size());
		assertEquals(1, numSentinels(allWords, set));
		// Add element
		assertTrue(set.add(toAdd2));
		assertEquals(words.length, set.size());
		assertEquals(0, numSentinels(allWords, set));
	}

	@Test(timeout = 5000)
	public void testRemoveInFullTable() {
		MyHashSet<String> set = MyHashSetFactory.create(7);
		int capacity = set.copyOfArray().length;
		String[] toAdd = getStringsWithEqualHash(capacity);
		for (String add : toAdd) {
			set.add(add);
		}
		assertEquals(capacity, set.size());
		assertFalse(set.remove("A"));
	}

	@Test(timeout = 5000)
	public void testRemoveInFullTableWithSentinels() {
		MyHashSet<String> set = MyHashSetFactory.create(7);
		int capacity = set.copyOfArray().length;
		String[] toAdd = getStringsWithEqualHash(capacity);
		for (String add : toAdd) {
			set.add(add);
		}
		assertEquals(capacity, set.size());
		for (int i = 0; i < capacity / 2; i++) {
			assertTrue(set.remove(toAdd[i]));
		}
		assertFalse(set.remove("A"));
	}

	@Test(timeout = 5000)
	public void testAddToFullTableWithWithSentinels() {
		MyHashSet<String> set = MyHashSetFactory.create(7);
		int capacity = set.copyOfArray().length;
		String[] toAdd = getStringsWithEqualHash(capacity);
		for (String add : toAdd) {
			set.add(add);
		}
		assertEquals(capacity, set.size());
		for (int i = 0; i < capacity / 2; i++) {
			assertTrue(set.remove(toAdd[i]));
		}
		assertFalse(set.add(toAdd[toAdd.length-1]));
		assertTrue(set.add(toAdd[0]));
	}

	@Test(timeout = 5000)
	public void testAddDuplicateToProbingChainWithSentinel_ExpectResultFalse() {
		MyHashSet<String> set = MyHashSetFactory.create(8);
		String[] stringsHavingSameHash = getStringsWithEqualHash(2);
		assertTrue(set.add(stringsHavingSameHash[0]));
		assertTrue(set.add(stringsHavingSameHash[1]));
		assertTrue(set.remove(stringsHavingSameHash[0]));
		assertFalse(set.add(stringsHavingSameHash[1]));
		assertEquals(1, set.size());
	}

	private int numSentinels(String[] words, MyHashSet<String> set) {
		int nSentinels = 0;
		Object sentinel = null;
		Object[] elements = set.copyOfArray();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				continue;
			}
			if (!contains(words, elements[i])) {
				++nSentinels;
				if (sentinel == null) {
					sentinel = elements[i];
				} else {
					// Only one sentinel-object allowed! (memory efficient)
					assertSame("Always use the same object as sentinel", sentinel,
							elements[i]);
				}
			}
		}
		return nSentinels;
	}

	private boolean contains(String[] words, Object object) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].equals(object)) return true;
		}
		return false;
	}
}
