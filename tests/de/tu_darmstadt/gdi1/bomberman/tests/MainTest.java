package de.tu_darmstadt.gdi1.bomberman.tests;

import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.TestAdapterFactory;

/**
 * Beispieltest.
 */
public class MainTest extends junit.framework.TestCase {
	public void testSome () {
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		assertEquals("abc", "abc");
	}
}
