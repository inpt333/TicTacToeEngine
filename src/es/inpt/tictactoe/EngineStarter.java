package es.inpt.tictactoe;

import java.io.IOException;

import es.inpt.tictactoe.exception.GameException;
import es.inpt.tictactoe.game.Field;
import es.inpt.tictactoe.io.Parser;

public class EngineStarter {
	
	public static void main(String[] args) throws IOException {
//		Parser parser1 = new Parser(field, "tictactoebotSquare", 1);
//		Parser parser2 = new Parser(field, "tictactoebotPosition", 2);
		
		if (args.length != 2) {
			System.out.println("Incorrect number of parameters. You should specify two different names that matches with the file name of your bots.");
			return;
		}
		
		Field field = new Field();
		Parser parser1 = new Parser(field, args[0], 1);
		Parser parser2 = new Parser(field, args[1], 2);

		try {
			int player = 0;
			do {
				if (player == 0) {
					parser1.run();
				} else {
					parser2.run();
				}
				player = ++player % 2;
			} while (!field.isFinsihed());
			
			System.out.println(field);
			
		} catch (GameException ge) {
			System.out.println(ge.getMessage());
		}
	}

}