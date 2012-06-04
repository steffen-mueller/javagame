package de.tu_darmstadt.gdi1.framework.view;


import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.tu_darmstadt.gdi1.framework.exceptions.FrameworkError;
import de.tu_darmstadt.gdi1.framework.exceptions.ParameterOutOfRangeException;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoard;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardElement;
import de.tu_darmstadt.gdi1.framework.interfaces.IBoardPanel;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameView;
import de.tu_darmstadt.gdi1.framework.interfaces.IStepManager;
import de.tu_darmstadt.gdi1.framework.interfaces.IUserInterfaceEvent;
import de.tu_darmstadt.gdi1.framework.utils.Point;



/**
 * Class representing the basic game panel with include pause screen.
 *
 * Using the BoardPanel(IGameView) constructor, you'll get an BoardPanel
 * with an empty JPanel as pause screen. To let the framework use your own
 * panel, use the BoardPanel(IGameView, JPanel) constructor.
 *
 * @author f_m
 */
public class BoardPanel<E extends IBoardElement> extends JPanel implements IBoardPanel<E> {
	private static final long serialVersionUID = 1L;
	private IGameView<E> gameView = null;
	private int columns = -1;
	private int rows = -1;
	private JPanel pausePanel = null;
	private JPanel boardPanel = null;
	private CardLayout layout = null;
	private IBoard<E> lastBoard;
	private Dimension lastD;
	private boolean pause = false;
	private boolean emptyPanel = false;

	/**
	 * Constructor for an new BoardPanel with default pause screen.
	 * @param view class implementing IGameView
	 */
	public BoardPanel(final IGameView<E> view) {
		this(view, null);
	}

	/**
	 * Constructor for an new BoardPanel with customized pause screen.
	 * @param view class implementing IGameView
	 * @param pausePanel the pause screen
	 */
	public BoardPanel(final IGameView<E> view, JPanel pausePanel) {

		if(pausePanel == null) {
			pausePanel = new JPanel();
			emptyPanel = true;
		}

		gameView = view;
		this.pausePanel = pausePanel;
		boardPanel = new JPanel();

		layout = new CardLayout(0,0);
		setLayout(layout);
		JPanel inBetweenPanel = new JPanel();
		((FlowLayout) inBetweenPanel.getLayout()).setHgap(0);
		((FlowLayout) inBetweenPanel.getLayout()).setVgap(0);
		add(boardPanel, "game");
		add(this.pausePanel, "pause");
	}

	/**
	 * Paint the given board. This method can be used if you want to
	 * force a paint of the complete board, which means every single picture.<br><br>
	 * Notice: to force a repaint of every element will take much more time
	 * than the ordinary {@link de.tu_darmstadt.gdi1.framework.view.BoardPanel#paintBoard(IBoard)} method, but for certain
	 * cases we need to offer this possibility to the framework user, who
	 * can set a flag in any {@link IUserInterfaceEvent} event to make us use
	 * this method instead of the default one below.
	 * @param board the board to paint.
	 * @param forceNewPaint true if you want to paint everything.
	 */
	public void paintBoard(IBoard<E> board, final boolean forceNewPaint) {
		Dimension d = null, d2 = null;

		if (IStepManager.class.isInstance(board)) {
			board = ((IStepManager<E>)board).getCurrentBoard();
		}

		// the GridLayout gets filled from left to right from
		// top to bottom
		if ((lastBoard != null) && (lastBoard.getHeight() == board.getHeight())
				&& (lastBoard.getWidth() == board.getWidth())) {
				if (!forceNewPaint) {
					// there is a copy of the previous board and it has the same size, therefore only changed Elements are
					// updated
					for (int y = 0; y < board.getHeight(); y++) {
						for (int x = 0; x < board.getWidth(); x++) {
							if (gameView.shouldPaintElementsAtCoordinate(lastBoard, board, new Point(x, y))) {
								//if shouldPaintElementsAtCoordinate answers true we should repaint:
								d = placeElementGroup(x, y, board, true);
							} else {
								// label's icon stays the same
								d = lastD;

							}
						}
					}
				} else {
					// everything shall be painted without regarding only changed elements
					for (int y = 0; y < board.getHeight(); y++) {
						for (int x = 0; x < board.getWidth(); x++) {
							d = placeElementGroup(x, y, board, true);
						}
					}
				}
		} else {
			// remove existing components and create a new board
			updateLayout(board.getWidth(), board.getHeight());
			for (int y = 0; y < board.getHeight(); y++) {
				for (int x = 0; x < board.getWidth(); x++) {
					d = placeElementGroup(x, y, board, false);
				}
			}
		}

		if (d2 != null && !d2.equals(d)) {
			throw new FrameworkError("The images for all components need to have the same size!");
		} else {
			d2 = d;
		}
		lastBoard = board.clone();
		lastD = d;

		Dimension size = new Dimension(board.getWidth() * d.width, board.getHeight() * d.height);

		/*
		 * Dealing with some stupid swing stuff. We've to lock the panels
		 * size into all directions so that the LayoutManager shows it
		 * as we want him to.
		 */

		boardPanel.setPreferredSize(size);
		boardPanel.setMinimumSize(size);
		boardPanel.setMaximumSize(size);

		if (!pause) {
			setMinimumSize(size);
			setMaximumSize(size);
			setPreferredSize(size);
		}
	}

	/**
	 * Paints the given board to this panel.<br><br>
	 * Notice: for performance issues, only the positions on which elements
	 * have changed will be painted. If you want to force a complete repaint
	 * of the board no matter of any changes you should use {@link de.tu_darmstadt.gdi1.framework.view.BoardPanel#paintBoard(IBoard, boolean)}.
	 * <br><strong>This is the default method to use.</strong>
	 *
	 * @param board the board to paint.
	 * @throws ParameterOutOfRangeException if the images don't have the same sizes.
	 */
	public void paintBoard(final IBoard<E> board) {
		paintBoard(board, false);
	}

	/**
	 * Sets the pause mode to the given boolean.
	 * @param pauseMode the pause mode.
	 */
	public void setPauseMode(boolean pauseMode) {
		pause = pauseMode;
		if (pauseMode) {
			layout.show(this, "pause");
			if(!emptyPanel) {
				setMinimumSize(pausePanel.getPreferredSize());
				setMaximumSize(pausePanel.getPreferredSize());
				setPreferredSize(pausePanel.getPreferredSize());
				getParent().setSize(getPreferredSize());
			}
			//TODO the current visible Element should have the focus.
			//interesting for the keylisteners
			//(its bad if a hidden component has the focus and react to keyevents)
			//pausePanel.requestFocusInWindow();
		} else {
			layout.show(this, "game");
			setMinimumSize(boardPanel.getPreferredSize());
			setMaximumSize(boardPanel.getPreferredSize());
			setPreferredSize(boardPanel.getPreferredSize());
			//TODO the current visible Element should have the focus.
			//interesting for the keylisteners
			//its also bad if a visible component does NOT have focus and does not react to keyevents)
			//getParent().requestFocusInWindow();
			//boardPanel.getComponent(0).requestFocusInWindow();
			//TODO the boardpanel should pass the focus to the underlying gameview. Does it so by default?
			//secure an element gets the focus which is listening for keyevents
		}
	}

	/**
	 * Toggles the pause mode.
	 */
	public void togglePauseMode() {
		layout.next(this);
	}

	/**
	 * Pushes the corresponding icon on a {@link javax.swing.JLabel} with a registered MouseListener to the GridLayout.
	 *
	 * @param x
	 *            the column
	 * @param y
	 *            the row
	 * @param board
	 *            the IBoard
	 * @param update
	 *            true: existing label's icon is changed false: a new label is created
	 * @return Dimension the Dimension of the placed element
	 */
	private Dimension placeElementGroup(final int x, final int y, final IBoard<E> board, final boolean update){

		JLabel label;
		int index = y * board.getWidth() + x;

		ImageIcon icon = gameView.getComponentForBoard(board, new Point(x, y));

		if (icon == null) {
			throw new FrameworkError("Can't paint an element that has null as image!");
		}

		if (update) {
			try { // ... to update the label's icon
				label = (JLabel) boardPanel.getComponent(index);
				label.setIcon(icon);
			} catch (Exception e) {
				// if anything goes wrong, create a new label and add it; therefore the old label is removed
				boardPanel.remove(index);
				placeElementGroup(x, y, board, false);
			}
		} else {
			// create new label and add it
			label = new JLabel();
			label.setIcon(icon);
			label.addMouseListener(new MouseAdapter() {
				public void mousePressed(final MouseEvent evt) {
					((UserInterface<E>) gameView).mouseClicked(x, y);
				}
			});
			label.setBorder(null);
			label.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			boardPanel.add(label, index);
		}

		return new Dimension(icon.getIconWidth(), icon.getIconHeight());
	}

	/**
	 * This method should be called every time before the panel is redrawn, with the cols and rows of the current Board
	 * to ensure the panel resizes if the board has changed its size (new level for example.
	 *
	 * @param cols
	 *            the number of columns.
	 * @param rows
	 *            the number of rows.
	 */
	private void updateLayout(final int cols, final int rows) {
		if (columns != cols || this.rows != rows) {
			boardPanel.removeAll();

			columns = cols;
			this.rows = rows;

			// 0, 'cause GridLayout takes only one
			// parameter, the other one is ignored.
			boardPanel.setLayout(new GridLayout(0, cols));
		}
	}

	/**
	 * Call this function if you need to know the
	 * amount of columns your gameboard currently has.
	 * <br>Counting starts at 1.
	 *
	 * @return the amount of columns
	 */
	public int getGameColumns() {
		return columns;
	}

	/**
	 * Call this function if you need to know the
	 * amount of rows your gameboard currently has.
	 * <br>Counting starts at 1.
	 *
	 * @return the amount of rows
	 */
	public int getGameRows() {
		return rows;
	}

	/**
	 * HACK: hinzugefügt von Steffen Müller.
	 */
	public JLabel getLabelAt (IBoard<E> board, int x, int y) {
		if (board != null)
			return (JLabel) boardPanel.getComponent(y * board.getWidth() + x);
		else
			return null;
	}
}
