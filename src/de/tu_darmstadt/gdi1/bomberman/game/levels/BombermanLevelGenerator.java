/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tu_darmstadt.gdi1.bomberman.game.levels;

/**
 *
 * @author JUehV
 */
public class BombermanLevelGenerator {

    private static char generateBlock(boolean withSolid) {
        int mod = 2;
        int value = (int) Math.abs(Math.random()*10000);
        if (withSolid) {
            mod = 3;
        }
        value = value % mod;

        switch (value) {
            case 0:
                return ' ';
            case 1:
                return '*';
            case 2:
                return '#';
            default:
                // will never occure
                return ' ';
        }
    }

    public static String generateGrid(int size, int player) {
        StringBuilder mapString = new StringBuilder();

        // automatic size correction
        if (size < 7) {
            size = 6;
        }

        // automatic player correchtion
        // not needed because of architecture
//        if (player > 4){
//            player = 4;
//        } else if (player < 2){
//            player = 2;
//        }

        // Print top boarder
        for (int i = 0; i < size; i++) {
            mapString.append('#');
        }
        mapString.append('\n');

        // Print top player line
        mapString.append('#').append('1').append(' ');
        for (int i = 0; i < size - 6; i++) {
            mapString.append(generateBlock(false));
        }
        mapString.append(' ');
        if (player > 2) {
            mapString.append('3');
        } else {
            mapString.append(' ');
        }
        mapString.append('#').append('\n');
        // Print 2nd top player line
        mapString.append('#').append(' ');
        for (int i = 0; i < size - 4; i++) {
            mapString.append(generateBlock(true));
        }
        mapString.append(' ').append('#').append('\n');

        // Print middle field
        for (int j = 0; j < size - 6; j++) {
            mapString.append('#').append(generateBlock(false));
            for (int i = 0; i < size - 4; i++) {
                mapString.append(generateBlock(true));
            }
            mapString.append(generateBlock(false)).append('#').append('\n');
        }
        // Print bottom player line
        mapString.append('#').append(' ');
        for (int i = 0; i < size - 4; i++) {
            mapString.append(generateBlock(true));
        }
        mapString.append(' ').append('#').append('\n');

        mapString.append('#');
        if (player > 3) {
            mapString.append('4');
        } else {
            mapString.append(' ');
        }
        mapString.append(' ');
        for (int i = 0; i < size - 6; i++) {
            mapString.append(generateBlock(false));
        }
        mapString.append(' ').append('2').append('#').append('\n');

        // Print bottom boarder
        for (int i = 0; i < size; i++) {
            mapString.append('#');
        }

        return mapString.toString();
    }
}
