package de.tu_darmstadt.gdi1.bomberman.testutils;

/**
 * Der Testadapter wird von den Testfällen verwendet, um mit dem Spiel zu interagieren.
 * Der Adapter kapselt alle testbaren Aktivitäten und bildet sie auf die Spielimplementierung ab.
 * Die Tests arbeiten gegen das ITestAdapter Interface.
 */
public class DefaultTestAdapter implements ITestAdapter {

	@Override
	public boolean loadLevelFromFile(String path) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean loadLevelFromString(String data) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void tick(int count) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void tick() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
