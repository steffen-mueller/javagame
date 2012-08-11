package de.tu_darmstadt.gdi1.bomberman.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import org.tritonus.share.TDebug.AssertException;

import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter;
import de.tu_darmstadt.gdi1.bomberman.testutils.ITestAdapter.FieldStatus;
import de.tu_darmstadt.gdi1.bomberman.testutils.TestAdapterFactory;

/**
 * Beispieltest.
 */
public class BombermanTestAdapterMinimal  {
	String simpleLevel = "###################\n"+
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
	
	String falseLevel = "###################\n"+
						"#1 ***  *  * *** 2#\n"+
						"# ###*#*# #*#*### #\n"+
						"# #* *  ***  * *#*#\n"+
						"#*#*# #*#*#*# #*#*#\n"+
						"#* ************  * \n"+
						"#*#*# #*#*#*# #*#*#\n"+
						"#*#* *  ***  * *#*#\n"+
						"# ###*#*# #*#*### #\n"+
						"#3 ***  *  * ***  #\n"+
						"###################\n";

	/**
	 *Pr�ft ob ein invalides Level geladen werden kann
	 */
	
		@Test(expected=Exception.class) public void loadFalseLevel() {
			ITestAdapter a = TestAdapterFactory.createTestAdapter();
			a.loadLevelFromString(falseLevel);
	    }
		
			
	
	
	/**
	 * Prüft, ob der Spieler tatsächlich ein Feld nach unten wandert, wenn die Taste "runter"
	 * gedrückt wird.
	 *
		@Test
	public void testSimpleMovement () {
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		if (!a.loadLevelFromString(simpleLevel))
			fail("Could not load level");

		// Sicherstellen, dass der Spieler 1 am Anfang auch auf 1,1 steht.
		assertTrue(a.getLevelStatus()[1][1].contains(ITestAdapter.Element.PLAYER1));

		// Spieler 1 nach unten bewegen und dann einen Tick vergehen lassen.
		a.attemptMovePlayer(1, ITestAdapter.Direction.DOWN);
		
		//auch die anderen Spieler werden bewegt
		a.attemptMovePlayer(2, ITestAdapter.Direction.DOWN);
		a.attemptMovePlayer(3, ITestAdapter.Direction.UP);
		a.attemptMovePlayer(4, ITestAdapter.Direction.UP);
		a.tick(1);

		// Jetzt müsste der Spieler auf 1,2 stehen...
		assertTrue(a.getLevelStatus()[1][2].contains(ITestAdapter.Element.PLAYER1));
		
		//und auch die anderen Spieler m�ssten sich bewegt haben
		assertTrue(a.getLevelStatus()[17][2].contains(ITestAdapter.Element.PLAYER2));
		assertTrue(a.getLevelStatus()[1][8].contains(ITestAdapter.Element.PLAYER3));
		assertTrue(a.getLevelStatus()[17][8].contains(ITestAdapter.Element.PLAYER4));
		
		// ... aber nicht mehr auf 1,1 !
		assertFalse(a.getLevelStatus()[1][1].contains(ITestAdapter.Element.PLAYER1));

		// Jetzt ist die Frage: gehts im nächsten Tick weiter? HOffentlich nicht.
		a.tick(1);
		assertTrue(a.getLevelStatus()[1][2].contains(ITestAdapter.Element.PLAYER1));

		// Und dann noch warten, bis die restlichen Ticks abgelaufen sind.
		a.tick(a.getIntParameter(ITestAdapter.Parameter.PLAYER_SPEED)-2);

		// Jetzt müsste der Spieler auf 1,3 stehen...
		assertTrue(a.getLevelStatus()[1][3].contains(ITestAdapter.Element.PLAYER1));
		// ... aber nicht mehr auf 1,2 !
		assertFalse(a.getLevelStatus()[1][2].contains(ITestAdapter.Element.PLAYER1));
	}

	/**
	 * Prüft, dass der Spieler hängen bleibt.
	 *
		@Test
	public void testBlockingMovement () {
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		if (!a.loadLevelFromString(simpleLevel))
			fail("Could not load level");

		// Sicherstellen, dass der Spieler 1 am Anfang auch auf 1,1 steht.
		assertTrue(a.getLevelStatus()[1][1].contains(ITestAdapter.Element.PLAYER1));

		// Spieler 1 nach unten bewegen und dann einen Tick vergehen lassen.
		a.attemptMovePlayer(1, ITestAdapter.Direction.RIGHT);
		a.tick(1);

		assertTrue(a.getLevelStatus()[2][1].contains(ITestAdapter.Element.PLAYER1));
		// ... aber nicht mehr auf 1,1 !
		assertFalse(a.getLevelStatus()[1][1].contains(ITestAdapter.Element.PLAYER1));

		// Wartenwartenwarten
		a.tick(a.getIntParameter(ITestAdapter.Parameter.PLAYER_SPEED));

		// Immer noch!
		assertTrue(a.getLevelStatus()[2][1].contains(ITestAdapter.Element.PLAYER1));
		assertFalse(a.getLevelStatus()[3][1].contains(ITestAdapter.Element.PLAYER1));
	}
	
	/**
	 * Testet ob ein Spieler gestorben ist
	 *
		@Test
	public void testDeath(){
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		if (!a.loadLevelFromString(simpleLevel))
			fail("Could not load level");
		
		a.attemptMovePlayer(1, ITestAdapter.Direction.RIGHT);
		a.tick(1);
		a.attemptMovePlayer(1, ITestAdapter.Direction.BOMB);
		a.tick(61);
		assertFalse(a.getLevelStatus()[2][1].contains(ITestAdapter.Element.PLAYER1));
	}
	
	
	/**
	 * testet ob eine Explosion statt gefunden hat und ob der Spieler und der Stein entfernt wurden
	 *
		@Test
	public void testExplosion(){
		ITestAdapter a = TestAdapterFactory.createTestAdapter();
		if (!a.loadLevelFromString(simpleLevel))
			fail("Could not load level");
		
		a.attemptMovePlayer(1, ITestAdapter.Direction.RIGHT);
		a.tick(1);
		a.attemptMovePlayer(1, ITestAdapter.Direction.BOMB);
		a.tick(61);
		assertFalse(a.getLevelStatus()[2][1].contains(ITestAdapter.Element.PLAYER1));
		assertFalse(a.getLevelStatus()[3][1].contains(ITestAdapter.Element.STONE));
	}
	*/
}
