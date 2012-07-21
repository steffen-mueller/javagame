package de.tu_darmstadt.gdi1.bomberman.tests;

import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.TestAdapterFactory;

/**
 * Beispieltest.
 */
public class MainTest extends junit.framework.TestCase {
	String simpleLevel = "###################\n"+
						 "#1 ***  *  * *** 2#\n"+
						 "# ###*#*# #*#*### #\n"+
						 "#*#* *  ***  * *#*#\n"+
						 "#*#*# #*#*#*# #*#*#\n"+
						 "#* ************  *#\n"+
						 "#*#*# #*#*#*# #*#*#\n"+
						 "#*#* *  ***  * *#*#\n"+
						 "# ###*#*# #*#*### #\n"+
						 "#3 ***  *  * *** 4#\n"+
						 "###################\n";

	public void testSome () {
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		if (!a.loadLevelFromString(simpleLevel))
			fail("Could not load level");

		a.tick(1);
		assertEquals("abc", "abc");
	}
}
