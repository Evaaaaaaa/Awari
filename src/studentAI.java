public class studentAI extends Player {
	private int maxDepth;

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * This is a wrapper function for alpha-beta search. It should use
	 * alpha-beta search to update the data member move (which will be returned
	 * by the getMove() method to the Match class that is controlling the game
	 * environment). Since the whole search space for Awari is extremely large,
	 * you need to cut off the search at some fixed depth limit, which is
	 * specified by the maxDepth class member. There is a 10-second time limit
	 * to calculate each move. You can assume maxDepth will not be greater than
	 * 15.
	 **/
	public void move(BoardState state) {
		move = alphabetaSearch(state, maxDepth);
	}

	/**
	 * This function will start the alpha-beta search (see Figure 5.7 in the
	 * textbook for reference). The detailed descriptions of input and output
	 * are given below:
	 * 
	 * @param state
	 *            The board state for the current player (the MAX player). You
	 *            can assume the pits for the current player are always in the
	 *            lower row, and that the lower row is player 1.
	 * @param maxDepth
	 *            The maximum search depth allowed.
	 * @return Return the best move that leads to the state that gives the
	 *         maximum SBE value for the current player; returns the move with
	 *         the smallest index in the case of ties. The value of the move
	 *         should be in the range [0, 5], with 0 representing the leftmost
	 *         pit.
	 **/
	public int alphabetaSearch(BoardState state, int maxDepth) {
		BoardState temp = null;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int v = Integer.MIN_VALUE;
		int moves = 0;
        
		for (int j = 0; j < 6; ++j) {
			if (state.isLegalMove(1, j)) {
				temp = state.applyMove(1, j);
				//since v has to be a max move, at max and decrease to 0
                v = Math.max(v,minValue(temp, maxDepth, maxDepth-1,alpha,beta));
                if (v > alpha){
                	 //notice that v is the score, not the step
                     moves = j;
                	 alpha = Math.max(alpha, v);
                }              
			}
        }
        
		
			return moves;
	}
		

	/**
	 * This function will search for the minimax value associated with the best
	 * move for the MAX player. The search should be cut off when the current
	 * depth equals to the maximum allowed depth. It is important to note that
	 * we will also call the SBE function to evaluate the game state when the
	 * game is over, i.e., when someone has won the game. The only condition for
	 * determining a leaf node (besides having reached maximin depth) is that
	 * there are no legal moves for the player to make, effectively ending the
	 * game at that state. The detailed descriptions of input and output are:
	 * 
	 * @param state
	 *            The game state that the MAX player is currently searching from
	 * @param maxDepth
	 *            The maximum search depth allowed @ currentDepth The current
	 *            depth in the search tree
	 * @param alpha
	 *            The α value
	 * @param beta
	 *            The β value
	 * @return The minimax value corresponding to the best move for the MAX
	 *         player
	 **/
	public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {	   
		// if game is over
		if (currentDepth == 0 || noLegalMoves(state, 1)) {
			return sbe(state);
		} else {
			int v = Integer.MIN_VALUE;
			BoardState temp = null;
			for (int i = 0; i < 6; i++) {
				if (state.isLegalMove(1, i)) {
					temp = state.applyMove(1, i);
					v = Math.max(v, minValue(temp,  maxDepth, currentDepth - 1, alpha, beta));
				
					if (v >= beta) {
						return v;
					}
					alpha = Math.max(alpha, v);
				}
			}

			return v;
		}
	}

	/** @return returns the best value for the MIN player. **/
	public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
	            // if game is over
		if (currentDepth == 0 || noLegalMoves(state, 2)) {
			return sbe(state);
		} else {
			int v = Integer.MAX_VALUE;
			BoardState temp = null;
			for (int i = 0; i < 6; i++) {
     			if (state.isLegalMove(2, i)) {		
					temp = state.applyMove(2, i);
					v = Math.min(v, maxValue(temp, maxDepth, currentDepth - 1, alpha, beta));
					if (v <= alpha) {
						return v;
	 				}
					beta = Math.min(beta, v);
			}
		}
      		return v;
		}
	}

	/**
	 * This function takes a board state as input and returns its SBE value. Use
	 * the following method: Return the number of stones in the storehouse of
	 * the current player minus the number of stones in the opponent’s
	 * storehouse. Always assume the current player is player 1.
	 **/
	public int sbe(BoardState state) {
		//get my score becomes 10 at the last call
		int score = state.getMyScore(1) - state.getMyScore(2);
		return score;
	}

	private boolean noLegalMoves(BoardState state,int player) {
		int legalMoves = 0;
		int p = player;
		if( p == 1){
		for (int i = 0; i < 6; i++) {
			if (state.isLegalMove(1, i)) {
				legalMoves++;		
			}
		}
		}
		else {
			for (int i = 0; i < 6; i++) {				
				if (state.isLegalMove(2, i)) {					
					legalMoves++;
				}
			}
		}
		
		if (legalMoves == 0) {
			return true;
		}
		return false;
	}
}
