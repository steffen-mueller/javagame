package de.tu_darmstadt.gdi1.bomberman.testutils;

/**
 * Statische Factory, die eine Implementierung von ITestAdapter für die Testcases liefert.
 * Diese Klasse muss von den implementierenden Erstsemestern "gehackt" werden.
 */
public class TestAdapterFactory {

	/**
	 * Diese Methode muss einen funktionsfähigen TestAdapter liefern.
	 * @return 
	 */
	public static ITestAdapter createTestAdapter ()
	{
		return new DefaultTestAdapter();
	}
}
