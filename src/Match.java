/****************************************************************
 * Match.java
 * Do not modify this file!
 * This file actually plays the game, you can adjust parameters like THINK_TIME here.
 */

import java.util.HashSet;
import java.util.Set;

public class Match {
    /** time limit each player has to decide on a move (default: 10 secs) **/
    private final static int THINK_TIME = 10;

    private Player player1, player2;
    private int player1score, player2score;
    private BoardState board;
    public static boolean isPlayer1turn;
    private Set<BoardState> player1oldState, player2oldState;

    /**
     * Creates a new match object.
     * Both player classes should extend the abstract Player class.
     * The classes should be on the classpath at run time
     *
     * @param player1 the name of the class representing player1
     * @param player2 the name of the class representing player2
     * @param state the initial board state
     */

    Match(Player player1, Player player2, BoardState state) {
        this.player1 = player1;
        this.player2 = player2;
        if (state != null) {
            board = new BoardState(state);
            Awari.gui.setPots(board);
        } else {
            board = new BoardState();
        }
        player1score = player2score = 0;
        player1oldState = new HashSet<>();
        player2oldState = new HashSet<>();
    }

    /**
     * Executes the actual match playing. Starts with player 1 then alternates
     * until the game ends.
     *
     * @return the number corresponding to the winning player; either 1 or 2.
     * returns 0 in case of a tie.
     */
    public int play() {
        isPlayer1turn = true;
        int move = -1;

        player1oldState.add(board);
        int status = board.status(1);

        while (status == BoardState.GAME_NOT_OVER) {


            Object mutex = new Object();
            long timeout = THINK_TIME * 1000;

            try {
                synchronized (mutex) {
                    MovingThread movingThread;
                    if (isPlayer1turn) {
                        Awari.gui.textArea.setText("Player 1's turn to move\n");
                        movingThread = new MovingThread(player1, new BoardState(board), mutex, timeout);
                    } else {
                        Awari.gui.textArea.setText("Player 2's turn to move\n");
                        movingThread = new MovingThread(player2, new BoardState(board.rotate()), mutex, timeout);
                    }
                    movingThread.start();  //schedule the moving thread
                    mutex.wait(timeout);  //go to sleep for the timeout or until the move returns

                    Thread.sleep(500);  //sleep for half a second to allow for cleanup
                    if (movingThread.isAlive()) {
                        movingThread.stop();  //kill the moving thread if it is still going
                        Awari.gui.textArea.append("Time limit expired\n");
                        //System.out.println("Thread timeout expired");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();  //nasty, we shouldn't get here
            }

            if (isPlayer1turn) {


                move = player1.getMove();
                if (!board.isLegalMove(1, move)) {
                    Awari.gui.textArea.append("An illegal move is performed\n");
                    return 2;
                }
                Awari.gui.applyMove(move, isPlayer1turn, new BoardState(board));
                board = board.applyMove(1, move);
                if (player2oldState.contains(board)) {
                    Awari.gui.textArea.append("Duplicate states\n");
                    return board.endGame();
                }
                else
                    player2oldState.add(board);
                status = board.status(2);
            } else {
                move = player2.getMove();
                if (!board.isLegalMove(2, move)) {
                    Awari.gui.textArea.append("An illegal move is performed\n");
                    return 1;
                }
                Awari.gui.applyMove(move, isPlayer1turn, new BoardState(board));
                board = board.applyMove(2, move);
                if (player1oldState.contains(board)) {
                    Awari.gui.textArea.append("Duplicate states\n");
                    return board.endGame();
                }
                else
                    player1oldState.add(board);
                status = board.status(1);
            }

            if (Awari.VERBOSE) {
                if (isPlayer1turn)  System.out.println("\nPlayer 1's move: " + move + "\n");
                else    System.out.println("\nPlayer 2's move: " + (move+6) + "\n");
                System.out.println(board);
            }
            isPlayer1turn = !isPlayer1turn;
        }

        return status;
    }


    /**
     * @returns the final score of player1.
     */
    public int getPlayer1Score() {
        return player1score;
    }

    /**
     * @returns the final score of player2.
     */
    public int getPlayer2Score() {
        return player2score;
    }

    private static class MovingThread extends Thread {
        private Player player;
        private BoardState context;
        private Object mutex;
        private long timeout;

        private int moveResult;

        public MovingThread(Player player, BoardState context, Object mutex, long timeout) {
            this.player = player;
            this.context = context;
            this.mutex = mutex;
            this.timeout = timeout;
        }

        public void run() {
            moveResult = 0;
            try {
                synchronized (mutex) {  //this is just to ensure that the player doesn't
                    //move much before the timer is started
                }
                player.move(context);
                synchronized (mutex) {
                    mutex.notify();  //wake up the match if we finished before the time
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        public int getMove() {
            return player.getMove();
        }

    }
}
