package de.tu_darmstadt.gdi1.bomberman.game.elements;

import java.util.List;
import javax.swing.ImageIcon;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.framework.utils.Point;


/**
 * javadoc: Not yet commented
 *
 * @version $Id$
 */
public class Delegate extends GameElement {

    public Delegate() {
    }

    @Override
    public GameElement clone() {
        return new Delegate();
    }

    @Override
    public ImageIcon getImageIcon() {
		return null;
    }

    @Override
    public String getDescription() {
        return "(Delegate)";
    }

    @Override
    public char getParsingSymbol() {
        return ' ';
    }

    public boolean isSolid() {
        return false;
    }

    public boolean isDirty() {
        return false;
    }

    @Override
    public void destroy() {
		return;
    }

	// Delegate runner /////////////////////////////////////////////////////////////////////////////

	public void manageSuddenDeath (BombermanController controller) {
		for (int i = 1; i < gameBoard.getWidth()-1; i++) {
			for (int j = 1; j < gameBoard.getHeight()-1; j++) {
				GameElement gameElement = gameBoard.getElements(i, j).get(gameBoard.getElements(i, j).size() - 1);
				if (!(gameElement instanceof Wall)) {
					System.out.println("Sudden Death: Stoning " + i + ", " + j);

					if (gameElement instanceof Player) {
						System.out.println(gameElement.getDescription() + " died");
						gameElement.destroy();
					}

					// TODO - Place Wall
					List<GameElement> gE = gameBoard.getElements(i,j);
					Wall wall = new Wall();
					wall.setCoordinates(i, j);
					wall.setGameBoard(gameBoard);
					wall.setGameData(gameData);
					gE.add(wall);
					gameBoard.setElements(i, j, gE);


					controller.addDirtyPoint(new Point(i,j));
					controller.redrawDirtyPoints();
					return;
				}
			}
		}
	}
}
