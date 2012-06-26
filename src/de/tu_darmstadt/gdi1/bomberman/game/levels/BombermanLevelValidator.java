/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_darmstadt.gdi1.bomberman.game.levels;

import de.tu_darmstadt.gdi1.bomberman.BombermanController;
import de.tu_darmstadt.gdi1.bomberman.game.elements.GameElement;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Player;
import de.tu_darmstadt.gdi1.bomberman.game.elements.Wall;
import de.tu_darmstadt.gdi1.framework.interfaces.IGameBoard;
import de.tu_darmstadt.gdi1.framework.utils.Point;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Validiert das Gameboard nach folgenden Aspekten: 1. Ist das Feld von Wänden
 * umgeben und somit auch Quadratisch? 2. Sind mehr als 1 und weniger als 5
 * Spieler vorhanden? 3. Sind Player nur einfach vertreten? 4. Können sich die
 * Spieler untereinander erreichen?
 *
 * @author JUehV
 */
public class BombermanLevelValidator {

    private IGameBoard<GameElement> gameBoard;
    private static final Logger logger = Logger.getLogger(BombermanController.class.getName());
    private StringBuilder errorStack = new StringBuilder();
    private List<String> playerStack = new ArrayList<String>();

    private class GameBoardNode {

        private List<Integer> children = new ArrayList<Integer>();
        private boolean isPlayerStation;
        private Point coordinate;
        private int id;

        public GameBoardNode(int id, boolean isPlayerStation, Point coordinate) {
            this.isPlayerStation = isPlayerStation;
            this.coordinate = coordinate;
            this.id = id;
        }

        public List<Integer> getChildren() {
            return children;
        }

        public void addChild(int child) {
            this.children.add(child);
        }

        public Point getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(Point coordinate) {
            this.coordinate = coordinate;
        }

        public boolean isIsPlayerStation() {
            return isPlayerStation;
        }

        public void setIsPlayerStation(boolean isPlayerStation) {
            this.isPlayerStation = isPlayerStation;
        }

        public int getId() {
            return id;
        }
    }

    /**
     *
     * @param gameBoard
     */
    public BombermanLevelValidator(IGameBoard<GameElement> gameBoard) {
        this.gameBoard = gameBoard;
    }

    public String getErrors() {
        return errorStack.toString();
    }

    private void logNotSolidAtPoint(int x, int y) {
        StringBuilder msg = new StringBuilder("Frame is not solid at point:");
        msg.append(x).append(",").append(y);
        // log msg
        logger.log(Level.SEVERE, msg.toString());
        // add msg to stack
        errorStack.append(msg).append("\n");
    }

    private boolean isWall(List<GameElement> elements) {
        for (GameElement element : elements) {
            if (element.getDescription().equalsIgnoreCase(Wall.DESCRITION)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds out if border is solid. Checks implicitly if all lines have the
     * same number of characters.
     *
     * @return true if the border is solid. false else.
     */
    private boolean checkBorder() {
        // Hilfsvariablen
        final int bottomLine = gameBoard.getHeight() - 1;
        final int rightLine = gameBoard.getWidth() - 1;
        boolean retval = true;

        // check top and bottom
        for (int i = 0; i < gameBoard.getWidth(); i++) {
            // get top elements
            List<GameElement> topElements = gameBoard.getElements(i, 0);
            // get bottom elements
            List<GameElement> bottomElements = gameBoard.getElements(i, bottomLine);
            // check element stacks
            if (!isWall(topElements)) {
                logNotSolidAtPoint(i, 0);
                retval = false;
            }
            if (!isWall(bottomElements)) {
                logNotSolidAtPoint(i, bottomLine);
                retval = false;
            }
        }

        // check left and right border
        for (int i = 1; i < gameBoard.getHeight() - 1; i++) {
            // get left elements
            List<GameElement> leftElements = gameBoard.getElements(0, i);
            // get right elements
            List<GameElement> rightElements = gameBoard.getElements(rightLine, i);
            // check element stacks
            if (!isWall(leftElements)) {
                logNotSolidAtPoint(0, i);
                retval = false;
            }
            if (!isWall(rightElements)) {
                logNotSolidAtPoint(rightLine, i);
                retval = false;
            }
        }

        if (retval) {
            logger.log(Level.INFO, "Border is ok.");
        }
        return retval;
    }

    private boolean isPlayer(List<GameElement> elements,boolean addToStack) {
        for (GameElement element : elements) {
            // Substring kann hier nicht eingesetzt werden, da es Descriptions gibt
            // die kürzer sind als "Player". Daher wird Patternmachting verwende.
            if (Pattern.matches(Player.PATTERN, element.getDescription())) {
                if (addToStack){
                    playerStack.add(element.getDescription());
                }
                return true;
            }

        }
        return false;
    }

    /**
     * Checks if there are enough player present. It will also fail if there are
     * more then 4 players. Notice that you should check the Border first. The
     * Method builds the player stack which is needed by checkPlayerIdentity.
     *
     * @return true for 1 < player < 5. false else.
     */
    private boolean checkPlayerCount() {
        int numOfPlayer = 0;

        for (int i = 1; i < gameBoard.getHeight() - 1; i++) {
            for (int j = 1; j < gameBoard.getWidth() - 1; j++) {
                if (isPlayer(gameBoard.getElements(j, i),true)) {
                    numOfPlayer++;
                }
            }
        }

        if (numOfPlayer < 5 && numOfPlayer > 1) {
            logger.log(Level.INFO, "Found {0} Player.", numOfPlayer);
            return true;
        } else {
            StringBuilder msg = new StringBuilder("No valid Player count: ");
            msg.append(numOfPlayer);
            // log msg
            logger.log(Level.SEVERE, msg.toString());
            // add msg to stack
            errorStack.append(msg).append("\n");
            return false;
        }
    }

    /**
     * Checks if every player is only one time on the gameboard. Notice: you
     * MUST NOT run this bevor checkPlayerCount().
     *
     * @return
     *
     * @see #checkPlayerCount()
     */
    private boolean checkPlayerIdentity() {
        boolean retval = true;
        for (int i = 0; i < playerStack.size(); i++) {
            String item = playerStack.get(i);
            for (int j = i + 1; j < playerStack.size(); j++) {
                if (item.equalsIgnoreCase(playerStack.get(j))) {
                    StringBuilder msg = new StringBuilder("Player is multiple times present: ");
                    msg.append(item);
                    // log msg
                    logger.log(Level.SEVERE, msg.toString());
                    // add msg to stack
                    errorStack.append(msg).append("\n");
                    retval = false;
                }
            }
        }
        return retval;
    }

    private Map<Integer, GameBoardNode> convertGameBoardToGraph() {
        Map<Integer, GameBoardNode> graph = new HashMap<Integer, GameBoardNode>();
        Point pos = new Point(1, 1);


        // Spielfeld in betretbare Nodes konvertiern
        for (int i = 1; i < gameBoard.getHeight() - 1; i++) {
            for (int j = 1; j < gameBoard.getWidth() - 1; j++) {
                if (!isWall(gameBoard.getElements(j, i))) {
                    graph.put(graph.size(), new GameBoardNode(graph.size(),
                            isPlayer(gameBoard.getElements(j, i),false),
                            new Point(j, i)));
                }
            }
        }

        // linking nodes
        for (GameBoardNode item : graph.values()) {
            int right = item.getCoordinate().getX() + 1;
            int left = item.getCoordinate().getX() - 1;
            int stationX = item.getCoordinate().getX();
            int down = item.getCoordinate().getY() + 1;
            int up = item.getCoordinate().getY() - 1;
            int stationY = item.getCoordinate().getY();

            for (GameBoardNode linkItem : graph.values()) {
                if (item.equals(linkItem)) {
                    continue;
                }
                if (right == linkItem.getCoordinate().getX()
                        || left == linkItem.getCoordinate().getX()
                        || stationX == linkItem.getCoordinate().getX()) {
                    if (up == linkItem.getCoordinate().getY()
                            || down == linkItem.getCoordinate().getY()
                            || stationY == linkItem.getCoordinate().getY()) {
                        // item is direct neighbour 
                        item.addChild(linkItem.getId());
                    }
                }
            }
        }
        return graph;
    }

    private boolean checkPlayerConnected() {
        // Hilfsvariablen
        Queue<Integer> searchQueue = new LinkedList<Integer>();
        List<Integer> checkedList = new ArrayList<Integer>();
        int foundPlayers = 0;
        // breadth-first search
        Map<Integer, GameBoardNode> gameBoardAsGraph = convertGameBoardToGraph();

        GameBoardNode startNode = gameBoardAsGraph.get(0);
        // build the queue
        for (int childId : startNode.getChildren()) {
            searchQueue.add(childId);
        }
        // search in the queue
        while (!searchQueue.isEmpty()) {
            Integer id = searchQueue.poll();
            if (id != null && !checkedList.contains(id)) {
                // add to checklist 
                checkedList.add(id);
                // check if node is a player station and add children to queue
                GameBoardNode node = gameBoardAsGraph.get(id);
                if (node != null) {
                    for (int childId : node.getChildren()) {
                        searchQueue.add(childId);
                    }

                    if (node.isPlayerStation) {
                        foundPlayers++;

                        StringBuilder msg = new StringBuilder("Player found at: ");
                        msg.append(node.getCoordinate().getX()).append(",");
                        msg.append(node.getCoordinate().getY());
                        // log msg
                        logger.log(Level.INFO, msg.toString());

                        if (foundPlayers >= playerStack.size()) {
                            return true;
                        }
                    }
                }
            }
        }

        String msg = "Not all Players are connected!";
        // log msg
        logger.log(Level.SEVERE, msg);
        // add msg to stack
        errorStack.append(msg).append("\n");

        return false;
    }

    public boolean isGameBoardValid() {
        boolean isValid = true;
        // check if boarder is solid
        isValid = isValid && checkBorder();
        isValid = isValid && checkPlayerCount();
        isValid = isValid && checkPlayerIdentity();
        isValid = isValid && checkPlayerConnected();

        return isValid;
    }
}
