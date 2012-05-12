package de.tu_darmstadt.gdi1.bomberman.testutils;

/**
 * Der Testadapter wird von den Testfällen verwendet, um mit dem Spiel zu interagieren.
 * Der Adapter kapselt alle testbaren Aktivitäten und bildet sie auf die Spielimplementierung ab.
 * Die Tests arbeiten gegen die Implementierung dieses Interface.
 */
public interface ITestAdapter {

	public boolean loadLevelFromFile (String path);
	public boolean loadLevelFromString (String data);

	

	public void tick (int count);
	public void tick ();
}
