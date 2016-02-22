package es.inpt.tictactoe.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Wrapper for the PrintStream used to write the output.
 * 
 * @author Iván Peña
 *
 */
public class Printer {

	/**
	 * The PrintStream used to write the output.
	 */
	private PrintStream printer;

	/**
	 * Constructor.
	 * 
	 * @param printer the printer to initialize
	 */
	public Printer(OutputStream os) {
		this.printer = new PrintStream(os);
	}
	
	/**
	 * New line not included at the end.
	 * 
	 * @param text the text to write
	 */
	public void print(String text) {
		this.printer.print(text);
		this.printer.flush();
	}
	
	/**
	 * New line included at the end.
	 * 
	 * @param text the text to write
	 */
	public void println(String text) {
		this.printer.println(text);
		this.printer.flush();
	}
	
}
