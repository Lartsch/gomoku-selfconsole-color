package setup;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;

import java.util.Scanner;

import org.fusesource.jansi.AnsiConsole;

import board.*;
import player.*;

/**
 * 
 * @author CanhGosuu
 * @author lartsch
 *
 */
public class GameSetup {
	private static final Scanner CIN = new Scanner(System.in);
	public static String clearScreenString;
	public static void runGame() {
		System.out.println();
		AnsiConsole.out.println(ansi().fgBright(RED).a("Do you want the screen cleared after every turn? (y/n - default y)").reset());
		clearScreenString = CIN.next();
		Board.clearScreen();
		System.out.println();
		AnsiConsole.out.println(ansi().fgBright(RED).a("Hi, I'm Gosu, please choose the game mode.").reset());
		System.out.println("1: Computer vs Human   2: Computer vs Computer   3: Minimax vs AlphaBeta");
		String mode = CIN.next();
		while (!mode.equals("1") && !mode.equals("2") && !mode.equals("3")) {
			AnsiConsole.out.println(ansi().fg(YELLOW).a("Game mode must be 1, 2 or 3").reset());
			mode = CIN.next();
		}
		if (mode.equals("1")) {
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Great! Please choose the game level (1~5)").reset());
			String level = CIN.next();
			while (!level.equals("1") && !level.equals("2") && !level.equals("3") && !level.equals("4") && !level.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Level must be between 1 and 5").reset());
				level = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Do you want to have the first turn? (y/n)").reset());
			String first = CIN.next();
			while (!first.equalsIgnoreCase("y") && !first.equalsIgnoreCase("n")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Input 'y' or 'n' no").reset());
				first = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Perfect! Remember player marker: ").fgBright(GREEN).a("You:'X'").fgBright(RED).a(" | ").fgBright(BLUE).a("Computer:'O'").fgBright(RED).a(" - Ready? (y/n)").reset());
			while (!CIN.next().equalsIgnoreCase("y")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Input 'y' to start game").reset());
			}

			Player computer = new AlphaBetaPlayer('O', Integer.parseInt(level), BLUE);
			Player human = new HumanPlayer('X', GREEN);
			Board board;
			if (first.equalsIgnoreCase("y")) {
				board = new Board(human, computer);
			} else {
				board = new Board(computer, human);
			}
			board.start();
		} else if (mode.equals("2")) {
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Great! Please choose the game level for Computer 1 (1~5)").reset());
			String level1 = CIN.next();
			while (!level1.equals("1") && !level1.equals("2") && !level1.equals("3") && !level1.equals("4") && !level1.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Level must be between 1 and 5").reset());
				level1 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Great! Please choose the game level for Computer 2 (1~5)").reset());
			String level2 = CIN.next();
			while (!level2.equals("1") && !level2.equals("2") && !level2.equals("3") && !level2.equals("4") && !level2.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Level must be between 1 and 5").reset());
				level2 = CIN.next();
			}
			Player computer1 = new AlphaBetaPlayer('X', Integer.parseInt(level1), BLUE);
			Player computer2 = new AlphaBetaPlayer('O', Integer.parseInt(level2), GREEN);
			Board board = new Board(computer1, computer2);
			board.start();
		} else {
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Great! Please choose the game level for Minimax (1~5)").reset());
			String level1 = CIN.next();
			while (!level1.equals("1") && !level1.equals("2") && !level1.equals("3") && !level1.equals("4") && !level1.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Level must be between 1 and 5").reset());
				level1 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Great! Please choose the game level for AlphaBeta (1~5)").reset());
			String level2 = CIN.next();
			while (!level2.equals("1") && !level2.equals("2") && !level2.equals("3") && !level2.equals("4") && !level2.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Level must be between 1 and 5").reset());
				level2 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Do you want Minimax to have the first turn? (y/n)").reset());
			String first = CIN.next();
			while (!first.equalsIgnoreCase("y") && !first.equalsIgnoreCase("n")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Input 'y' or 'n' no").reset());
				first = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a("Perfect! Remember player marker: ").fgBright(BLUE).a("Minimax:'X'").fgBright(RED).a(" | ").fgBright(GREEN).a("AlphaBeta:'O'").fgBright(RED).a(" - Ready? (y/n)").reset());
			while (!CIN.next().equalsIgnoreCase("y")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a("Input 'y' to start game").reset());
			}
			Player computer1 = new MinimaxPlayer('X', Integer.parseInt(level1), BLUE);
			Player computer2 = new AlphaBetaPlayer('O', Integer.parseInt(level2), GREEN);
			Board board;
			if (first.equalsIgnoreCase("y")) {
				board = new Board(computer1, computer2);
			} else {
				board = new Board(computer2, computer1);
			}
			board.start();
		}
	}
}
