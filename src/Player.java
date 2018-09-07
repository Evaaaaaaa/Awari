public abstract class Player {
    protected int move;  //stores the current best move for the player

    Player() {}

    /**
     * Callback method to tell the player that it is its turn to move.
     * It can always assume that the player will have at least one valid move.
     *
     * @param context the current position in the game
     *
     * @return the index of the bin to move from.
     */
    public abstract void move(BoardState context);

    public int getMove() {
        return move;
    }
}
