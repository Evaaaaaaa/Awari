/****************************************************************
 * BoardState.java
 * Do not modify this file!
 * The GameState class is used to represent the Awari Board during play. The
 * current player is always assumed to be on the bottom row. You are recommended to read and understand this file.
 */

import java.util.Arrays;

public final class BoardState {
    /** returned by status() when the game is not over **/
    public static final int GAME_NOT_OVER  = Integer.MIN_VALUE;

    /** returned by status() when the player on the lower row has won **/
    public static final int GAME_OVER_P1_WIN  =  1;

    /** returned by status() when game is over but the players have tied **/
    public static final int GAME_OVER_TIE  =  0;

    /** returned by status() when the player on the upper row has won **/
    public static final int GAME_OVER_P2_WIN = 2;

    public int[] house;
	public int[] score = {0,0};
    private String top = "Player 2";
    private String bottom = "Player 1";

    /**
     * This default constructor will create an initial board for the beginning
     * of a game. Each house has 4 stones initially.
     */
    public BoardState() {
    	house = new int[12];
    	Arrays.fill(house, 4);
    	score = new int[2];
    	Arrays.fill(score, 0);
    }

    /**
     * This constructor assigns a different board state
     * @param house the number of stones in each house
     * @param score the scores for both players
     */
    public BoardState(int[] house, int[] score) {
    	this.house = house;
    	this.score = score;
    }

    /**
     * A copy constructor
     * @param source the object out of which to construct a copy
     */
    public BoardState(BoardState source) {
    	house = new int[12];
    	score = new int[2];
		System.arraycopy(source.house, 0, house, 0, source.house.length);
		System.arraycopy(source.score, 0, score, 0, source.score.length);
    }
    
    /**
     * return board sate for player 1 - same as current state
     * @param player is the player requesting to make that move - either 1 or 2
     */
    public BoardState getPlayerBoard(int player) {
    	if(player == 1)
    		return this;
    	else
    		return this.rotate();
    }

    /**
     * returns a class constant indicating the current status of the game
     * checks if endgame state is met
     * @param nextPlayer - the player who is set to go next
     * @return the current game status as defined by the class constants
     */
    public int status(int nextPlayer) {
        if(score[0] >= 25)
        	return GAME_OVER_P1_WIN;
        else if(score[1] >= 25)
        	return GAME_OVER_P2_WIN;
        else if(score[0]==24 && score[1]==24)
        	return GAME_OVER_TIE;
        
    	// If all of nextPlayer's moves are illegal, then game is over
        boolean noLegalMoves = true;
        for(int i = 0; i < 6; i++) {
        	if(isLegalMove(nextPlayer, 5-i)) {
        		noLegalMoves = false;
        		break;
        	}
        }
        
        if(noLegalMoves) {
        	if(score[0] > score[1])
        		return GAME_OVER_P1_WIN;
        	else if(score[1] > score[0])
        		return GAME_OVER_P2_WIN;
        	else
        		return GAME_OVER_TIE;
        }
        
        return GAME_NOT_OVER;
    }
    
    /**
     * Forces the end of the game, for example when states are repeated
     * @return the endgame state for whichever player has the higher number of stones in his storehouse
     */
    public int endGame() {
        if(score[0] > score[1])
        	return GAME_OVER_P1_WIN;
        else if(score[1] > score[0])
        	return GAME_OVER_P2_WIN;
        else
        	return GAME_OVER_TIE;
    }

    /**
     * returns a boolean based upon the state and the house chosen.
     *
     * @param move the proposed bin from which to advance the stones
     * must be between 0 and 5 inclusive
     * @param player is the player requesting to make that move - either 1 or 2
     * @return true; the move from this state is legal<br>
     * false; otherwise
     */
    public boolean isLegalMove(int player, int move) {
    	if(move < 0 || move > 5) {
    		return false;
    	}
    	
    	int loc = move;
    	if(player == 2) 
    		loc = move + 6;
    	if(house[loc] == 0) {
    		return false;
    	}
    	BoardState afterMove = applyMove(player, move);
    	
    	// if other player is left without stones after the move, it is illegal
		return !isWithoutStones(otherPlayer(player), afterMove);
	}
    
    /**
     * returns the number of the opposite player
     * @param thisPlayer 
     * @return the number of the opposite player
     */
    private int otherPlayer(int thisPlayer) {
    	if(thisPlayer == 1)
    		return 2;
    	else
    		return 1;
    }
    
    /**
     * checks to see if a player has no stones in his pits
     * @param player who's side is being checked
     * @param board the current state of the board
     * @return true if the other player has no stones left
     */
    private boolean isWithoutStones(int player, BoardState board) {
    	for(int i = 0; i < 6; i++) {
    		int thisHouse = (player-1)*6 + i;
    		if(board.house[thisHouse] > 0)
    			return false;
    	}
    	return true;
    }
    
    /**
     * returns a boardstate based upon the state and move applied.
     *
     * @param move the bin from which to advance the stones
     * must be between 0 and 5 inclusive
     * @param player is the player whose move created the board to make that move - either 1 or 2
     * @return true; the move resulting in the board is legal<br>
     * false; otherwise
     */
    public BoardState applyMove(int player, int move) {
    	BoardState afterMove = new BoardState(this);
    	if(player == 2) 
    		move = move + 6;
    	
    	int houseIndex = move;
    	int stonesLeft = afterMove.house[move];
    	
    	while(stonesLeft > 0) {
    		houseIndex = (houseIndex+1)%12;
    		if(houseIndex != move) {
    			afterMove.house[houseIndex]++; 
    			stonesLeft--;
    			}
    	}
    	afterMove.house[move] = 0;
    	
    	// apply captures on temporary board to ensure grand slam doesn't capture
    	BoardState tempBoard = new BoardState(afterMove);
    	
    	// apply capturing
		while((player == 1 && houseIndex >= 6) || 
				(player == 2 && houseIndex < 6 && houseIndex >= 0)) {
			if(tempBoard.house[houseIndex] == 2 || tempBoard.house[houseIndex] == 3) {
				tempBoard.score[player-1] += tempBoard.house[houseIndex];
				tempBoard.house[houseIndex] = 0;
				houseIndex --;
			}else break;
		}
    	
    	// only apply capture if not a grand-slam
		if(!isWithoutStones(otherPlayer(player), tempBoard)) {
			afterMove = tempBoard;
		}
    	
    	return afterMove;
    }

    /**
     * @return the score of player1.
     * @param player is the player requesting to make that move - either 1 or 2
     */
    public int getMyScore(int player) {
        return score[player-1];
    }

    @Override
    public String toString() {
    	StringBuilder bs = new StringBuilder(" " + top + "\n ____ ____ ____ ____ ____ ____ ____ ____\n|    | ");
    	for(int i = 11; i >=6; i--) {
    		if(house[i] < 10)
    			bs.append(" ");
    		bs.append(house[i]).append(" | ");
    	}
    	bs.append("   |\n| ");
    	if(score[1] < 10)
    		bs.append(" ");
    	bs.append(score[1]).append(" |____|____|____|____|____|____|    | \n|    | ");
    	for(int i = 0; i < 6; i++) {
    		if(house[i] < 10)
    			bs.append(" ");
    		bs.append(house[i]).append(" | ");
    	}
    	if(score[0] < 10)
    		bs.append(" ");
    	bs.append(score[0]);
    	bs.append(" |\n|____|____|____|____|____|____|____|____| \n" + "                                ").append(bottom);
        return bs.toString();
    }

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoardState) {
		    BoardState other = (BoardState) obj;
		    return Arrays.equals(this.house, other.house);
        }
        return false;
	}

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.house);
    }

    /**
     * The board is rotated after a turn, so that each player always has their
     * houses in the lower row
     *
     * @return true; the move from this state is legal<br>
     * false; otherwise
     */
    public BoardState rotate() {
        BoardState rotatedBoard = new BoardState();
        for (int i = 0; i < house.length; i++) {
			rotatedBoard.house[(i+6)%12] = house[i];
		}
        rotatedBoard.score[0] = score[1];
        rotatedBoard.score[1] = score[0];
        rotatedBoard.top = bottom;
        rotatedBoard.bottom = top;
        return rotatedBoard;
    }
}
