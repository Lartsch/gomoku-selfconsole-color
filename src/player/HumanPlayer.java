package player;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.fusesource.jansi.AnsiConsole;

import board.Board;
import board.Pos;

/**
 * @author CanhGosuu
 * @author lartsch
 */
public class HumanPlayer extends Player {
    private static final String INTEGER_PATTERN = "\\d+";
    private static final Scanner CIN = new Scanner(System.in);

    public HumanPlayer(char marker, Ansi.Color color) {
        super(marker, color);
    }

    /*
     * @see player.Player#decide(board.Board)*
     * 
     * @return new Move if validate Move
     */
    @Override
    protected Move decide(Board board) {
        AnsiConsole.out.println(ansi().fgBright(this.color).a(this).reset() + " please input row and col");
        String row = CIN.next();
        String col = CIN.next();
        while (!row.matches(INTEGER_PATTERN) || !col.matches(INTEGER_PATTERN)) {
            System.out.println("Row and col must be integer");
            System.out.println(this + " please input row and col");
            row = CIN.next();
            col = CIN.next();
        }

        return new Move(0, new Pos(Integer.parseInt(row) - 1, Integer.parseInt(col) - 1));
    }
}
