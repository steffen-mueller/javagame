package de.tu_darmstadt.gdi1.bomberman.tests;


import org.junit.Test;
import static org.junit.Assert.*;

import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.Element;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.FieldStatus;
import de.tu_darmstadt.gdi1.bomberman.testutils.TestAdapterFactory;


public class BombermanTestExtended1 {
	String simpleLevel = 	"###################\n"+
							"#1 ***  *  * *** 2#\n"+
							"# ###*#*# #*#*### #\n"+
							"# #* *  ***  * *#*#\n"+
							"#*#*# #*#*#*# #*#*#\n"+
							"#* ************  *#\n"+
							"#*#*# #*#*#*# #*#*#\n"+
							"#*#* *  ***  * *#*#\n"+
							"# ###*#*# #*#*### #\n"+
							"#3 ***  *  * *** 4#\n"+
							"###################\n";

String testLevel =  "#####\n"+
					"#1 2#\n"+
					"#3 4#\n"+
					"#####\n";

/**
 * Testet ob die Bombe einen Besitzer hat
 */
@Test
public void testExtendedBombs(){
	ITestAdapter a = TestAdapterFactory.createTestAdapter();
	if (!a.loadLevelFromString(testLevel))
		fail("Could not load level");
	
	a.attemptMovePlayer(1, ITestAdapter.Direction.BOMB);
	assertEquals(a.getPlayer(1, 1).getDescription(), a.getBomb(1, 1).getPlayer().getDescription());
	
	}
	
	


}
