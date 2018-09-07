/****************************************************************
 * HumanPlayer.java
 * Do not modify this file!
 * This file is for humanPlayer.
 */


public class HumanPlayer extends Player {
    public static final int SLEEP_TIME = 10;
    public static int choice = -1;

    public void move(BoardState context) {
        //reset the choice from board
        choice = -1;
        //select the first legal move to be safe
        for (int i = 0; i < 6; i++) {
            if (context.isLegalMove(1, i)) {
                move = i;
                break;
            }
        }

        //try to get the choice from GUI
        while (choice == -1) {
            Utility.tSleep(SLEEP_TIME);
        }
        //transform from the index of the bins to the choice
        if (choice < 6)
            move = choice;
        else
            move = choice - 6;
        choice = -1;
    }
}
