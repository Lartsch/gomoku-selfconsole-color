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
		Board.clearScreen();
		System.out.println();
		AnsiConsole.out.println(ansi().fgBright(RED).a(" Do you want the screen cleared after every turn? (y/n)").reset());
		System.out.print(" ");
		clearScreenString = CIN.next();
		while (!clearScreenString.equalsIgnoreCase("y") && !clearScreenString.equalsIgnoreCase("n")) {
			AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' or 'n' no").reset());
			System.out.print(" ");
			clearScreenString = CIN.next();
		}
		Board.clearScreen();
		System.out.println();
		AnsiConsole.out.println(ansi().fgBright(RED).a(" Hi, I'm Gosu, please choose the game mode.").reset());
		System.out.println(" 1: Computer vs Human   2: Computer vs Computer   3: Minimax vs AlphaBeta");
		System.out.print(" ");
		String mode = CIN.next();
		while (!mode.equals("1") && !mode.equals("2") && !mode.equals("3")) {
			AnsiConsole.out.println(ansi().fg(YELLOW).a(" Game mode must be 1, 2 or 3").reset());
			System.out.print(" ");
			mode = CIN.next();
		}
		if (mode.equals("1")) {
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" What's your name? (min. 3 characters, alphanumeric)").reset());
			System.out.print(" ");
			String name = CIN.next();
			while(name == null || name.trim().isEmpty() || name.length() < 3 || name.matches("[0-9]+")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Please input a valid name (min. 3 characters, alphanumeric)").reset());
				System.out.print(" ");
				name = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Great, "+name+"! Please choose the game level (1~5)").reset());
			System.out.print(" ");
			String level = CIN.next();
			while (!level.equals("1") && !level.equals("2") && !level.equals("3") && !level.equals("4") && !level.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Level must be between 1 and 5").reset());
				System.out.print(" ");
				level = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Do you want to have the first turn, "+name+"? (y/n)").reset());
			System.out.print(" ");
			String first = CIN.next();
			while (!first.equalsIgnoreCase("y") && !first.equalsIgnoreCase("n")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' or 'n' no").reset());
				System.out.print(" ");
				first = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Perfect! Remember player marker: ").fgBright(GREEN).a(name+":'X'").fgBright(RED).a(" | ").fgBright(BLUE).a("Computer:'O'").fgBright(RED).a(" - Ready? (y/n)").reset());
			System.out.print(" ");
			while (!CIN.next().equalsIgnoreCase("y")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' to start game").reset());
				System.out.print(" ");
			}

			Player computer = new AlphaBetaPlayer('O', Integer.parseInt(level), BLUE, "CPU");
			Player human = new HumanPlayer('X', GREEN, name);
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
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Great! Please choose the game level for Computer 1 (1~5)").reset());
			System.out.print(" ");
			String level1 = CIN.next();
			while (!level1.equals("1") && !level1.equals("2") && !level1.equals("3") && !level1.equals("4") && !level1.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Level must be between 1 and 5").reset());
				System.out.print(" ");
				level1 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Great! Please choose the game level for Computer 2 (1~5)").reset());
			System.out.print(" ");
			String level2 = CIN.next();
			while (!level2.equals("1") && !level2.equals("2") && !level2.equals("3") && !level2.equals("4") && !level2.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Level must be between 1 and 5").reset());
				System.out.print(" ");
				level2 = CIN.next();
			}
			Player computer1 = new AlphaBetaPlayer('X', Integer.parseInt(level1), BLUE, "AlphaBeta-1");
			Player computer2 = new AlphaBetaPlayer('O', Integer.parseInt(level2), GREEN, "AlphaBeta-2");
			Board board = new Board(computer1, computer2);
			board.start();
		} else {
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Great! Please choose the game level for Minimax (1~5)").reset());
			System.out.print(" ");
			String level1 = CIN.next();
			while (!level1.equals("1") && !level1.equals("2") && !level1.equals("3") && !level1.equals("4") && !level1.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Level must be between 1 and 5").reset());
				System.out.print(" ");
				level1 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Great! Please choose the game level for AlphaBeta (1~5)").reset());
			System.out.print(" ");
			String level2 = CIN.next();
			while (!level2.equals("1") && !level2.equals("2") && !level2.equals("3") && !level2.equals("4") && !level2.equals("5")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Level must be between 1 and 5").reset());
				System.out.print(" ");
				level2 = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Do you want Minimax to have the first turn? (y/n)").reset());
			System.out.print(" ");
			String first = CIN.next();
			while (!first.equalsIgnoreCase("y") && !first.equalsIgnoreCase("n")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' or 'n' no").reset());
				System.out.print(" ");
				first = CIN.next();
			}
			Board.clearScreen();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).a(" Perfect! Remember player marker: ").fgBright(BLUE).a("Minimax:'X'").fgBright(RED).a(" | ").fgBright(GREEN).a("AlphaBeta:'O'").fgBright(RED).a(" - Ready? (y/n)").reset());
			System.out.print(" ");
			while (!CIN.next().equalsIgnoreCase("y")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' to start game").reset());
				System.out.print(" ");
			}
			Player computer1 = new MinimaxPlayer('X', Integer.parseInt(level1), BLUE, "Minimax");
			Player computer2 = new AlphaBetaPlayer('O', Integer.parseInt(level2), GREEN, "AlphaBeta");
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
