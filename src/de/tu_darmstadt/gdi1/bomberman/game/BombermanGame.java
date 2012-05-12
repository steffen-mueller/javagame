package de.tu_darmstadt.gdi1.bomberman.game;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.gui.UIEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;

/**
 * Das eigentliche Bomberman Spiel. Diese Klasse enthält die Spiellogik. Unsere Implementierung des
 * IBombermanGame Interfaces. Das Interface normiert die Game Klasse gegenüber dem Testadapter, so
 * dass andere Implementierungen potentiell unseren Test Adapter verwenden könnten.
 *
 * WICHTIG WICHTIG: diese Klasse hat keinen State! Sie weiß nichts über das Spiel selbst. Für alles
 * muss sie die gameData Instanz fragen.
 */
public class BombermanGame implements IBombermanGame {
	protected BombermanGameData gameData;
	protected BombermanController controller;

	public BombermanGame (BombermanGameData data, BombermanController ctr) {
		gameData = data;
		controller = ctr;

		//say hello to the gui, show the loaded level:
		sendEventToUi(UIEvent.type.NEW_GAME);
	}

	// Event Management ////////////////////////////////////////////////////////////////////////////

	/**
	 * Versendet ein Event an das User Interface. Der Controller wird als Zwischenhändler verwendet,
	 * da sich Game Object und GUI besser nicht direkt kennen dürfen.
	 * @param type
	 */
	private void sendEventToUi(UIEvent.type type) {
		UIEvent event = new UIEvent(type);
		event.setBoard(getBoard());
		//event.setInformationMap(infoMapManager.getInfoMap());
		controller.sendEventToUI(event);
	}

	/**
	 * {@inheritDoc}
	 */
	public IBoard<GameElement> getBoard() {
		return this.gameData.getStepManager().getCurrentBoard();
	}

}
