package de.tu_darmstadt.gdi1.bomberman.tests;

import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.FieldStatus;
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

	public void testSomething () {
		assertEquals("abc", "abc");
	}
}
