/****************************************************************
 * Mancala.java
 * Do not modify this file!
 * This file has the main method.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Awari {

    public static GUI gui;

    public static final boolean VERBOSE = true;

    public static void main(String[] args) throws Exception {
        String p1, p2;
        BoardState initial;
    
        if (args.length >= 3) {
        
            p1 = args[0];
            p2 = args[1];
            initial = loadFile(args[2]);
        } else {
            p1 = "HumanPlayer";
            p2 = "HumanPlayer";
            initial = null;
        }


        Player player1 = (Player) Class.forName(p1).newInstance();
        Player player2 = (Player) Class.forName(p2).newInstance();

        if (p1.endsWith("AI"))  ((studentAI)player1).setMaxDepth(Integer.parseInt(args[3]));
        if (p2.endsWith("AI"))  ((studentAI)player2).setMaxDepth(Integer.parseInt(args[3]));

        gui = new GUI("Player 1", "Player 2", initial);

        Match match = new Match(player1, player2, initial);
        int winner = match.play();
        if (winner == 0) {
            gui.textArea.append("Game is a draw");
        } else {
            gui.textArea.append("Winner is " + winner);
        }
    }

    private static BoardState loadFile(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int[] score = new int[2];
            int[] house = new int[12];
            score[0] = sc.nextInt();
            score[1] = sc.nextInt();
            for (int i = 0; i < 6; i++) {
                house[11-i] = sc.nextInt();
            }
            for (int i = 0; i < 6; i++) {
                house[i] = sc.nextInt();
            }
            sc.close();
            return new BoardState(house, score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
