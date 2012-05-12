package de.tu_darmstadt.gdi1.bomberman.game;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.levels.BombermanGameData;
import de.tu_darmstadt.gdi1.bomberman.gui.BombermanUIEvent;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;

/**
 *
 * @author Steffen Müller
 */
public class BombermanGame implements IBombermanGame {
	protected BombermanGameData gameData;
	protected BombermanController controller;

	public BombermanGame (BombermanGameData data, BombermanController ctr) {
		gameData = data;
		controller = ctr;

		//say hello to the gui, show the loaded level:
		sendEventToUi(BombermanUIEvent.type.NEW_GAME);
	}

	/**
	 * Sends the given action {@link EnumEventForUI} over the {@link IController} to the {@link Gui}
	 * The {@link EventForUI} will be filled with the current Board and infomap.
	 * @param action
	 * 		the action to send
	 * @param pauseMode
	 * 		the pause mode which should be set, may null.
	 */
	private void sendEventToUi(BombermanUIEvent.type type) {
		BombermanUIEvent event = new BombermanUIEvent(type);
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
