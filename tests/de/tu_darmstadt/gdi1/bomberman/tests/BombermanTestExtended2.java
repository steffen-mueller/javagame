
package de.tu_darmstadt.gdi1.bomberman.tests;


import org.junit.Test;
import static org.junit.Assert.*;

import org.tritonus.share.TDebug.AssertException;

import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.Element;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.FieldStatus;
import de.tu_darmstadt.gdi1.bomberman.testutils.TestAdapterFactory;

public class BombermanTestExtended2 {
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

	String testLevel =  "#######\n"+
						"#1 2  #\n"+
						"#3 4  #\n"+
						"#######\n";
			/**
			 * Testet ob die Zeitmessung korrekt lauft
			 */
			
			@Test
			public void testTime(){
				ITestAdapter a = TestAdapterFactory.createTestAdapter();
				if (!a.loadLevelFromString(testLevel))
					fail("Could not load level");
				
				assertEquals(a.GetTime(), 0);
				a.tick(160);
				assertEquals(a.GetTime(), 8);
			}
			
			
}
