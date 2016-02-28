package es.inpt.tictactoe.game;


/**
 * Class to represent the field of the game.
 * 
 * @author Iván Peña
 *
 */
public class Field {
	
	/**
	 * Active position representation on the macroboard
	 */
	private static final int ACTIVE_SQUARE_MACROBOARD = -1;
	
	/**
	 * Inactive position representation on the macroboard
	 */
	private static final int INACTIVE_SQUARE_MACROBOARD = 0;
	
	/**
	 * Dimensions of a  square
	 */
	private static final int SQUARE_DIMENSION = 3;
	
	/**
	 * Dimensions of the board
	 */
	private static final int BOARD_DIMENSION = 9;
	
	/**
	 * Number of squares on the macroboard
	 */
	private static final int MACROBOARD_LENGTH = 9;
	
	/**
	 * Number of positions on the board
	 */
	private static final int BOARD_LENGTH = 81;
	
	/**
	 * Success position combinations on a 3x3 board.
	 */
	private static final int[][] THREE_IN_A_ROW = {
	  { 0, 1, 2 },
	  { 3, 4, 5 },
	  { 6, 7, 8 },
	  { 0, 3, 6 },
	  { 1, 4, 7 },
	  { 2, 5, 8 },
	  { 0, 4, 8 },
	  { 2, 4, 6 }
	};
	
	/**
	 * The board (9x9)
	 */
	private final int[] board;
	
	/**
	 * The macroboard (3x3)
	 */
	private final int[] macroboard;
	
	private int lastPos;
	
	private int move = 0;
	
	private int round = 1;
	
	public int getRound() {
        return round;
    }


    public int getMove() {
            return move;
        }


        /**
	 * Default constructor.
	 */
	public Field() {
		board = new int[BOARD_LENGTH];
		macroboard = new int[MACROBOARD_LENGTH];
		
		for (int i = 0; i < MACROBOARD_LENGTH; i++) {
			this.macroboard[i] = ACTIVE_SQUARE_MACROBOARD;
		}
	}
	
	
	/**
	 * Moves the player's piece to the indicated position.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param playerId the player who moved
	 */
	public boolean move(int x, int y, int playerId) {
		int pos = this.getPosByMove(x, y);
		int nSquare = this.getNSquareByPos(pos);
		
		boolean isPossible = this.macroboard[nSquare] == -1 && this.board[pos] == 0;
		if (isPossible) {
			board[pos] = playerId;
			this.lastPos = pos;
			this.move++;
			this.round = move/2 + move % 2;
		}
		return isPossible;
	}
	
	/**
	 * @return if the game is finished
	 */
	public boolean isFinished() {
		int newSquare = this.getNMicroSquareByPos(this.lastPos);
		int newSquareStatus = this.checkSquare(newSquare);
		
		boolean finished = true;
		int status;
		for (int i = 0; i < MACROBOARD_LENGTH; i++) {
			status = this.checkSquare(i);
			
			if (status != ACTIVE_SQUARE_MACROBOARD) {
				this.macroboard[i] = status;
			} else if (newSquareStatus == ACTIVE_SQUARE_MACROBOARD) {
				this.macroboard[i] = (newSquare == i) ? ACTIVE_SQUARE_MACROBOARD : INACTIVE_SQUARE_MACROBOARD;
			} else {
				this.macroboard[i] = ACTIVE_SQUARE_MACROBOARD;
			}
			
			finished &= this.macroboard[i] != ACTIVE_SQUARE_MACROBOARD;
		}
		
		return finished || this.isFinished(this.macroboard, 1) ||
				this.isFinished(this.macroboard, 2);
	}

	
	/**
	 * @return the board stringified
	 */
	public String printBoard() {
		return this.stringify(this.board);
	}
	
	/**
	 * @return the macroboard stringified
	 */
	public String printMacroboard() {
		return this.stringify(this.macroboard);
	}
	
	/**
	 * String representation of the field.
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer("\n");
		int pos = 0;
		for (int x = 0; x < BOARD_DIMENSION; x++) {
			for (int y = 0; y < BOARD_DIMENSION; y++) {
				pos = y*BOARD_DIMENSION + x;
				
				if (y != 0) {
					s.append(" ");
				}	
				s.append(board[pos]);
			}
			s.append("\n");
		}
		
		s.append("\n\n");
		
		for (int x = 0; x < SQUARE_DIMENSION; x++) {
			for (int y = 0; y < SQUARE_DIMENSION; y++) {
			
				pos = y*SQUARE_DIMENSION + x;
				
				if (this.macroboard[pos] >= 0) {
					s.append(" ");
				}
				
				if (y != 0) {
					s.append(" ");
				}	
				s.append(this.macroboard[pos]);
			}
			s.append("\n");
		}
		
		s.append("\n\n").append("N° round: ").append(this.round).append("\n");
		
		return s.toString();
	}
	
	/**
	 * @param board the board to stringify
	 * @return the stringified board
	 */
	private String stringify(int[] board) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < board.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(board[i]);
		}
		return sb.toString();
	}
	
	/**
	 * Disables and enables the squares of the macroboard.
	 */
	private int checkSquare(int nSquare) {
		int piece = ACTIVE_SQUARE_MACROBOARD;
		int[] square = this.getSquare(nSquare);
		if (this.isFinished(square, 1)) {
			piece = 1;
		} else if (this.isFinished(square, 2)) {
			piece = 2;
		} else {
			boolean found = false;
			for (int i = 0; i < square.length && !found; i++) {
				found = square[i] == 0;
			}
			if (!found) {
				piece = 0;
			}
		}
		return piece;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	private int[] getSquare(int nSquare) {
		int xIni = (nSquare % SQUARE_DIMENSION) * SQUARE_DIMENSION;
		int yIni = ((int) nSquare / SQUARE_DIMENSION) * SQUARE_DIMENSION;
		
		int counter = 0, pos;
		int[] square = new int[SQUARE_DIMENSION * SQUARE_DIMENSION];
		for (int yAux = yIni; yAux < yIni + SQUARE_DIMENSION; yAux++) {
			for (int xAux = xIni; xAux < xIni + SQUARE_DIMENSION; xAux++) {
				pos = (yAux * BOARD_DIMENSION) + xAux;
				square[counter++] = board[pos];
			}
		}
		return square;
	}
	
	private int getNMicroSquareByPos(int pos) {
		return (pos % SQUARE_DIMENSION) + ((((int)pos / BOARD_DIMENSION) % SQUARE_DIMENSION) * SQUARE_DIMENSION);
	}
	
	private int getNSquareByPos(int pos) {
		int x = getX(pos);
		int y = getY(pos);
		return ((int) x / SQUARE_DIMENSION) + ((int) y / SQUARE_DIMENSION) * SQUARE_DIMENSION;
	}
	
	private int getPosByMove(int x, int y) {
		return y*BOARD_DIMENSION + x;
	}
	
	private int getX(int pos) {
		return pos % BOARD_DIMENSION;
	}

	private int getY(int pos) {
		return (int) pos / BOARD_DIMENSION;
	}
	
	/**
	 * @param board the board to analyze
	 * @return board finished?
	 */
	private boolean isFinished(int[] board, int playerId) {
		int counter;
		int piece;
		boolean finished = false;
		for (int i = 0; i < 8 && !finished; i++)  {
			counter = 0;
		    for (int j = 0; j < 3; j++)  {
		      piece = board[THREE_IN_A_ROW[i][j]];
		      if (piece == playerId) {
		    	  counter++;
		      }
		    }
		    finished = (counter == 3);
		}
		return finished;
	}
	
}
