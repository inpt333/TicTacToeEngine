package es.inpt.tictactoe.exception;

public class GameException extends Exception {

	private static final long serialVersionUID = -5230940812562299033L;

	private String playerId;
	
	private String message;

	public GameException(String playerId, String message) {
		super();
		this.playerId = playerId;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer(this.message)
			.append(" -> Player ")
			.append(this.playerId)
			.append(" loses the game");
		return sb.toString();
	}
	
}
