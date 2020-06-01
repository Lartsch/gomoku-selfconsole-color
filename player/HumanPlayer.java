package player;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.Color.*;
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

    public HumanPlayer(char marker, Ansi.Color color, String name) {
        super(marker, color, name);
    }

    /*
     * @see player.Player#decide(board.Board)*
     * 
     * @return new Move if validate Move
     */
    @Override
    protected Move decide(Board board) {
        AnsiConsole.out.println(" "+ansi().fgBright(this.color).a(this.name).reset() + ", please input row and col:");
        System.out.print(" ");
        String row = CIN.next();
        System.out.print(" ");
        String col = CIN.next();
        while (!row.matches(INTEGER_PATTERN) || !col.matches(INTEGER_PATTERN)) {
            AnsiConsole.out.println(ansi().fg(YELLOW).a(" Row and col must be integer").reset());
            AnsiConsole.out.println(" "+ansi().fgBright(this.color).a(this.name).reset() + ", please input row and col:");
            System.out.print(" ");
            row = CIN.next();
            System.out.print(" ");
            col = CIN.next();
        }

        return new Move(0, new Pos(Integer.parseInt(row) - 1, Integer.parseInt(col) - 1));
    }
}
