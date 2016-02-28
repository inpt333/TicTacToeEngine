package es.inpt.tictactoe.io;

import java.io.IOException;
import java.util.Scanner;

import es.inpt.tictactoe.exception.GameException;
import es.inpt.tictactoe.game.Field;

/**
 * Parses the input given by the bots
 * and provides an output.
 * 
 * @author Iv�n Pe�a
 *
 */
public class Parser {

	/**
	 * The field.
	 */
	private final Field field;
	
	/**
	 * Used to get the input from the bots.
	 */
	private Scanner scan;
	
	/**
	 * Used to give the output to the bots.
	 */
	private Printer printer;
	
	/**
	 * The current player name.
	 */
	private String playerName;
	
	/**
	 * The current player ID.
	 */
	private int playerId;
	
	/**
	 * The current player is a human.
	 */
	private boolean isUser;

	/**
	 * Constructor.
	 * 
	 * @throws IOException 
	 */
	public Parser(Field field, String playerName, int playerId) throws IOException {
		this.field = field;
		this.playerId = playerId;
		this.playerName = playerName;
		this.isUser = playerName.equalsIgnoreCase("user");
		
		if (this.isUser) {
			this.scan = new Scanner(System.in);
			this.printer = new Printer(System.out);
		} else {
			Process ps = Runtime.getRuntime().exec(new String[]{"java", "-jar", playerName + ".jar"});
			this.scan = new Scanner(ps.getInputStream());
			this.printer = new Printer(ps.getOutputStream());
		}
	}

	/**
	 * Where the logic starts.
	 * 
	 * @param field the field
	 * @throws GameException 
	 */
	public void run() throws GameException {
		
		printer.println("settings your_botid " + this.playerId);
		printer.println("update game round " + this.field.getRound());
		printer.println("update game move " + this.field.getMove());
		printer.println("update game field " + this.field.printBoard());
		printer.println("update game macroboard " + this.field.printMacroboard());
		printer.println("action move 10000");
		
		if (this.isUser) {
			printer.println(this.field.toString());
		}
		
		try {
			
			boolean moved = true;
			do {
				if (scan.hasNextLine()) {
					String line = scan.nextLine();
					if (line.length() != 0) {
						String[] parts = line.split(" ");
						if (parts[0].equals("place_move")) {
							int x = Integer.parseInt(parts[1]);
							int y = Integer.parseInt(parts[2]);
							moved = this.field.move(x, y, this.playerId);
							
							if (!moved) {
								printer.println("Move not allowed");
							}
						} else { 
							throw new Exception("Unknown command");
						}
					} else {
						throw new Exception("No command added");
					}
				}
			} while (!moved);
			
		} catch (Exception e) {
			throw new GameException(this.playerName, e.getMessage());
		}
	}
	
}
