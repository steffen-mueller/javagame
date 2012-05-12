package de.tu_darmstadt.gdi1.bomberman.gui;

import de.tu_darmstadt.gdi1.bomberman.framework.ControllerEvent;
import de.tu_darmstadt.gdi1.bomberman.framework.AbstractBombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import de.tu_darmstadt.gdi1.framework.view.UserInterface;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.ImageIcon;


/**
 * Das User Interface zum Benutzer.
 */
public class Gui extends UserInterface<GameElement> {

	protected AbstractBombermanController controller;
	public Gui (AbstractBombermanController ctr)
	{
		super();
		controller = ctr;

		setTitle("The amazing TU Darmstadt Bomberman!");
	}

	@Override
	protected void handleNonFrameworkEvents(IUserInterfaceEvent<GameElement> event) {
		if (!(event instanceof BombermanUIEvent))
		{
			// TODO: mourn around
			return;
		}

		BombermanUIEvent bmevent = (BombermanUIEvent) event;
		switch (bmevent.getType())
		{
			case QUIT_GAME:
				this.dispose();
				break;
		}
	}

	@Override
	protected void mouseClicked(int x, int y) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyTyped(KeyEvent evt) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyPressed(KeyEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void keyboardKeyReleased(KeyEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void menuItemClicked(String itemName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected void closingRequested() {
		ControllerEvent evt = ControllerEvent.create(ControllerEvent.type.USER_QUIT);
		controller.handleEventImmediately(evt);
	}

	@Override
	public ImageIcon getComponentForList(List<GameElement> aList, Point coordinates) {
		// TODO
		return new ImageIcon("resource/images/test.png");
	}

}
