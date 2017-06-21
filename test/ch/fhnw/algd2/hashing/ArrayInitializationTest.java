package ch.fhnw.algd2.hashing;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayInitializationTest {

	@Test(expected = IllegalArgumentException.class)
	public void testZeroSizeInitialization() {
		MyHashSetFactory.create(0);
		fail("Should throw exception");
	}

	@Test(timeout = 3000)
	public void testSmallInitializationSizes() {
		int[] arraySizes = new int[] { 7, 8, 9 };
		for (int size : arraySizes) {
			testInitializationSize(size);
		}
	}
	
	@Test(timeout = 3000)
	public void testLargeInitializationSizes() {
		int[] arraySizes = new int[] { 256, 1000, 2048};
		for (int size : arraySizes) {
			testInitializationSize(size);
		}
	}
	

	private void testInitializationSize(int capacity) {
			Object[] array = MyHashSetFactory.create(capacity).copyOfArray();
			if (!(isPrime(array.length)) && !(isPowerOf2(array.length))) {
				fail(String
						.format("Initialized Array size is not a prime / power of 2: Given size: %d, Array size: ",
								capacity, array.length));
			}
			
			assertTrue("Minimal required array size: "+capacity, array.length >= capacity);
			assertTrue("Max allowed array size is: max(1000, 3*initialSize)", array.length <= Math.max(1000, 3*capacity));
	}
	

	private boolean isPrime(int length) {
		if (length <= 2 || length % 2 == 0) {
			return false;
		}
		int t = 3;
		while (t * t <= length && length % t != 0) {
			t = t + 2;
		}
		return t * t > length;
	}

	private boolean isPowerOf2(int length) {
		return ((length != 0) && (length & (length - 1)) == 0);
	}

}
