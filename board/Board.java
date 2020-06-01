package board;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import setup.GameSetup;
import setup.Main;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.*;

import player.*;


/**
 * @author CanhGosuu
 * @author lartsch
 */
public class Board {
	private static final int N_ROW = 15;
	private static final int N_COL = 15;
	private static final char EMPTY_CHAR = '-';
	private static final int AVAILABLE_DISTANCE = 2;
	private static final List<Pos> ALL_POS = buildAllPos();//
	private static final List<List<Pos>> BANDS = buildBands();
	private static final Map<Player, Set<Set<Pos>>> GROUPS_CACHE = new HashMap<>();
	private static final int[][] SCORE_TABLE = { { 1, 1, 1 }, { 5, 10, 20 }, { 10, 500, 1000 }, { 25, 5000, 10000 },
			{ 1000000, 1000000, 1000000 } };

	public static int getnRow() {
		return N_ROW;
	}

	public static int getnCol() {
		return N_COL;
	}
	
	private int turnCounter;
	private final GameStatus status;
	private final char[][] grid;
	public final Player player1;
	public final Player player2;

	public Board(Player player1, Player player2) {
		this.grid = buildGrid();
		this.player1 = player1;
		this.player2 = player2;
		this.status = new GameStatus(Status.ONGOING, null, Collections.emptySet());
		this.turnCounter = 1;
	}
	
	public Board(Board other) {
		this.player1 = other.player1;
		this.player2 = other.player2;
		this.grid = copyOf(other.grid);
		this.status = new GameStatus(other.status.getStatus(), other.status.getWinner(), other.status.getWinningSet());
		this.turnCounter = other.turnCounter;
	}

	/**
	 * @param src
	 * @return má»™t ma tráº­n copy tá»« src
	 */
	private static char[][] copyOf(char[][] src) {
		int length = src.length;
		char[][] target = new char[length][src[0].length];
		for (int i = 0; i < length; i++) {
			System.arraycopy(src[i], 0, target[i], 0, src[i].length);
		}
		return target;
	}
	
	public static void clearScreen() {
		try {
			if(Main.systemName.contains("windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else if(Main.systemName.contains("mac") || Main.systemName.contains("linux")) {
				// MAC UNTESTED BUT SHOULD WORK
				new ProcessBuilder("tput", "reset").inheritIO().start().waitFor();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @return List lÆ°u trá»¯ khá»Ÿi táº¡o vá»‹ trÃ­
	 */
	private static List<Pos> buildAllPos() {
		List<Pos> poses = new ArrayList<>();
		for (int i = 0; i < N_ROW; i++) {
			for (int j = 0; j < N_COL; j++) {
				poses.add(new Pos(i, j));
			}
		}
		return poses;
	}

	/**
	 * @return List pos theo 4 hÆ°á»›ng ( lÆ°u vÃ o BANDS)
	 */
	private static List<List<Pos>> buildBands() {
		Map<Integer, List<Pos>> _hashMap = new HashMap<>();
		int offset = 2 * (N_ROW + N_COL); // 15 row, 15col, 15 topleft-botright,15 topright-botleft =60
		for (int i = 0; i < N_ROW; i++) {
			for (int j = 0; j < N_COL; j++) {
				// row
				load(_hashMap, i, new Pos(i, j));// key=1->14 --

				// col
				load(_hashMap, j + offset, new Pos(i, j));// key=60->74 |

				// diagonal
				load(_hashMap, i + j + 2 * offset, new Pos(i, j)); // key=120->148 \
				load(_hashMap, i - j + 3 * offset, new Pos(i, j));// key= 166->194 /
			}
		}
		return _hashMap.values().stream().filter(val -> val.size() > 1).collect(Collectors.toList());
	}

	/**
	 * @param _map lÆ°u trá»¯ giÃ¡ trá»‹ <key, pos>
	 * @param pos  khÃ´ng thá»ƒ ghi Ä‘Ã¨ lÃªn cÃ¡c vá»‹ trÃ­ => Collections.singletonList(pos)
	 * @param key
	 */
	private static void load(Map<Integer, List<Pos>> _map, int key, Pos pos) {
		List<Pos> _pos = _map.get(key);
		if (_pos == null) { //
			_map.put(key, new ArrayList<>(Collections.singletonList(pos)));
		} else {
			_pos.add(pos);
		}
	}

	/**
	 * @return khá»Ÿi táº¡o ma tráº­n. Start game
	 */
	private char[][] buildGrid() {
		char[][] grid = new char[N_ROW][N_COL];
		for (int i = 0; i < N_ROW; i++) {
			for (int j = 0; j < N_COL; j++) {
				grid[i][j] = EMPTY_CHAR;
			}
		}
		return grid;
	}

	/**
	 * @param pos
	 * @param player
	 * @return Ä‘Ã¡nh dáº¥u nhá»¯ng vá»‹ trÃ­ Ä‘Ã£ Ä‘Æ°á»£c Ä‘Ã¡nh vÃ  pust vÃ o GROUP_CACHE
	 */
	public boolean mark(Pos pos, Player player) {
		if ((pos.getRow() < 0 || pos.getRow() > N_ROW - 1) || (pos.getCol() < 0 || pos.getCol() > N_COL - 1)) {
			AnsiConsole.out.println(ansi().fg(YELLOW).a("Row must between 1 and " + N_ROW + ", Col must between 1 and " + N_COL).reset());
			return false;
		}
		if (this.grid[pos.getRow()][pos.getCol()] != EMPTY_CHAR) {
			AnsiConsole.out.println(ansi().fg(YELLOW).a(pos + "=" + this.grid[pos.getRow()][pos.getCol()] + " is not empty").reset());
			return false;
		}
		this.grid[pos.getRow()][pos.getCol()] = player.marker;
		scanGroups();
		selfCheck();
		return true;
	}

	/**
	 * check tráº¡ng trÃ¡i sau má»—i láº§n Ä‘Ã¡nh
	 */
	private void selfCheck() {
		Set<Set<Pos>> groupsOfP1 = GROUPS_CACHE.get(this.player1);
		Set<Set<Pos>> groupsOfP2 = GROUPS_CACHE.get(this.player2);
		if (groupsOfP1.stream().anyMatch(g -> g.size() >= 5)) {
			this.status.status = Status.P1_WIN;
			this.status.winner = this.player1;
			this.status.winningSet = groupsOfP1.stream().filter(g -> g.size() >= 5).findFirst()
					.orElse(Collections.emptySet());
		} else if (groupsOfP2.stream().anyMatch(g -> g.size() >= 5)) {
			this.status.status = Status.P2_WIN;
			this.status.winner = this.player2;
			this.status.winningSet = groupsOfP2.stream().filter(g -> g.size() >= 5).findFirst()
					.orElse(Collections.emptySet());
		} else if (isDraw()) {
			this.status.status = Status.DRAW;
		}
	}

	/**
	 * scan board vÃ  put vÃ o 2 GROUPS_CACHE chá»©a cÃ¡c group con cá»§a 2 ngÆ°á»�i chÆ¡i
	 */
	private void scanGroups() {
		Set<Set<Pos>> groupsOfP1 = new HashSet<>();
		Set<Set<Pos>> groupsOfP2 = new HashSet<>();
		for (List<Pos> band : BANDS) {
			Set<Pos> group1 = new HashSet<>();
			Set<Pos> group2 = new HashSet<>();
			for (int i = 0; i < band.size(); i++) {
				Pos pos = band.get(i);
				if (this.grid[pos.getRow()][pos.getCol()] == this.player1.marker) {
					group1.add(pos);
					// last one trigger
					if (i == band.size() - 1) {
						groupsOfP1.add(group1);
					}
				} else {
					if (!group1.isEmpty()) {
						groupsOfP1.add(group1);
						group1 = new HashSet<>();
					}
				}
				if (this.grid[pos.getRow()][pos.getCol()] == this.player2.marker) {
					group2.add(pos);
					// last one trigger
					if (i == band.size() - 1) {
						groupsOfP2.add(group2);
					}
				} else {
					if (!group2.isEmpty()) {
						groupsOfP2.add(group2);
						group2 = new HashSet<>();
					}
				}
			}
		}
		GROUPS_CACHE.put(this.player1, groupsOfP1);
		GROUPS_CACHE.put(this.player2, groupsOfP2);
	}

	/**
	 * @return Ä‘Ã£ háº¿t Ä‘áº¥t cho 2 tháº±ng mÃºa?
	 */
	private boolean isDraw() {
		return ALL_POS.stream().noneMatch(p -> this.grid[p.getRow()][p.getCol()] == EMPTY_CHAR);
	}

	public GameStatus status() {
		return this.status;
	}

	/**
	 * @return list cÃ¡c á»©ng cá»­ viÃªn cho quÃ¢n cá»� tiáº¿p theo
	 */
	public Set<Pos> getChildPos() {
		return ALL_POS.stream().filter(p -> this.grid[p.getRow()][p.getCol()] == EMPTY_CHAR && hasPlayerAdjacent(p))
				.collect(Collectors.toSet());
	}

	/**
	 * @param pos lÃ  má»™t vá»‹ trÃ­ trá»‘ng Ä‘ang cáº§n xÃ©t
	 * @return kiá»ƒm tra xem cÃ³ quÃ¢n nÃ o liá»�n ká»� khÃ´ng
	 */
	private boolean hasPlayerAdjacent(Pos pos) {
		int rowL = pos.getRow() - AVAILABLE_DISTANCE < 0 ? 0 : pos.getRow() - AVAILABLE_DISTANCE;
		int colL = pos.getCol() - AVAILABLE_DISTANCE < 0 ? 0 : pos.getCol() - AVAILABLE_DISTANCE;
		int rowH = pos.getRow() + AVAILABLE_DISTANCE > N_ROW ? N_ROW : pos.getRow() + AVAILABLE_DISTANCE;
		int colH = pos.getCol() + AVAILABLE_DISTANCE > N_COL ? N_COL : pos.getCol() + AVAILABLE_DISTANCE;

		for (int i = rowL; i < rowH; i++) {
			for (int j = colL; j < colH; j++) {
				if (this.grid[i][j] != EMPTY_CHAR) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param player
	 * @param        _depth: Ä‘á»™ sÃ¢u cao nháº¥t trá»« Ä‘á»™ sÃ¢u cá»§a Ä‘iá»ƒm Ä‘ang xÃ©t
	 * @return Ä‘iá»ƒm Ä‘Ã¡nh giÃ¡ cá»§a cÃ¡c á»©ng cá»­ viÃªn
	 */
	public int evaluate(Player player, int _depth) {
		if (this.status.isWinning()) {
			return (player == this.status.getWinner()) ? (Integer.MAX_VALUE - 1 - _depth)
					: (Integer.MIN_VALUE + 1 + _depth);
		} else if (this.status.isDraw()) {
			return 0;
		} else {
			int selfScore = GROUPS_CACHE.get(player).stream().mapToInt(g -> score(g, false)).sum();
			int enemyScore = GROUPS_CACHE.get(getEnemy(player)).stream().mapToInt(g -> score(g, true)).sum();
			return selfScore - enemyScore;
		}
	}

	/**
	 * @param group: táº­p cÃ¡c xxx liá»�n ká»�
	 * @param isEnemy: true/false
	 * @return sá»‘ Ä‘iá»ƒm cá»§a group hiá»‡n táº¡i
	 */
	private int score(Set<Pos> group, boolean isEnemy) {
		int size = group.size(); // kÃ­ch thÆ°á»›c hiá»‡n táº¡i
		int open = countOfOpen(group);// sá»‘ Ä‘áº§u tá»± do
		int ratio = isEnemy && size > 2 ? 2 : 1;
		return ratio * SCORE_TABLE[size - 1][open];
	}

	/**
	 * @param player
	 * @return chuyá»ƒn ngÆ°á»�i chÆ¡i tiáº¿p theo
	 */
	public Player getEnemy(Player player) {
		return player == this.player1 ? this.player2 : this.player1;
	}

	/**
	 * @param group
	 * @return sá»‘ kháº£ nÄƒng má»Ÿ rá»™ng vá»� 2 phÃ­a cá»§a 1 group: cháº·n 2 Ä‘áº§u:0, cháº·n 1 Ä‘áº§u:
	 *         1, khÃ´ng cháº·n: 2
	 */
	private int countOfOpen(Set<Pos> group) {
		List<Pos> poses = new ArrayList<>(group);
		poses.sort(Comparator.comparing(Pos::getIndex));
		Pos min = poses.get(0);
		Pos max = poses.get(poses.size() - 1);
		if (min.getRow() == max.getRow()) { // cÃ¹ng hÃ ng: check 2 Ä‘áº§u cá»™t Ä‘á»ƒ má»Ÿ rá»™ng
			return (min.getCol() > 0 && this.grid[min.getRow()][min.getCol() - 1] == EMPTY_CHAR ? 1 : 0)
					+ (max.getCol() < N_COL - 1 && this.grid[min.getRow()][max.getCol() + 1] == EMPTY_CHAR ? 1 : 0);
		} else if (min.getCol() == max.getCol()) {// cÃ¹ng cá»™t: check 2 Ä‘áº§u cá»™t Ä‘á»ƒ má»Ÿ rá»™ng
			return (min.getRow() > 0 && this.grid[min.getRow() - 1][min.getCol()] == EMPTY_CHAR ? 1 : 0)
					+ (max.getRow() < N_ROW - 1 && this.grid[max.getRow() + 1][min.getCol()] == EMPTY_CHAR ? 1 : 0);
		} else {
			if (min.getCol() < max.getCol()) { // Ä‘Æ°á»�ng chÃ©o 1: check 2 Ä‘áº§u cá»™t Ä‘á»ƒ má»Ÿ rá»™ng
				return (min.getRow() > 0 && min.getCol() > 0
						&& this.grid[min.getRow() - 1][min.getCol() - 1] == EMPTY_CHAR ? 1 : 0)
						+ (max.getRow() < N_ROW - 1 && max.getCol() < N_COL - 1
								&& this.grid[max.getRow() + 1][max.getCol() + 1] == EMPTY_CHAR ? 1 : 0);
			} else {// Ä‘Æ°á»�ng chÃ©o 2: check 2 Ä‘áº§u cá»™t Ä‘á»ƒ má»Ÿ rá»™ng
				return (min.getRow() > 0 && min.getCol() < N_COL - 1
						&& this.grid[min.getRow() - 1][min.getCol() + 1] == EMPTY_CHAR ? 1 : 0)
						+ (max.getRow() < N_ROW - 1 && max.getCol() > 0
								&& this.grid[max.getRow() + 1][max.getCol() - 1] == EMPTY_CHAR ? 1 : 0);
			}
		}
	}

	/**
	 * @done
	 */
	public void start() {
		print();
		while (!this.status.isGameOver()) {
			this.player1.next(this);
			print();
			if (this.status.isGameOver()) {
				break;
			}
			if (this.player1 instanceof MinimaxPlayer && this.player2 instanceof MinimaxPlayer) {
				// pause();
			}
			this.player2.next(this);
			print();
			if (this.player1 instanceof MinimaxPlayer && this.player2 instanceof MinimaxPlayer) {
				// pause();
			}
		}
	}

	/**
	 * @done
	 */
	public void print() {
		if(GameSetup.clearScreenString.equalsIgnoreCase("y")) {
			clearScreen();
		}	
		System.out.println();
		System.out.println();
		System.out.println();
		AnsiConsole.out.println(ansi().fgBright(WHITE).bold().a(" --- Turn #" + this.turnCounter++ +" ---").reset());
		System.out.println();
		AnsiConsole.out.println((this.player1.step() == this.player2.step() ? "  * " : "    ") + ansi().fgBright(this.player1.color).a(buildPlayerInfo(this.player1)).reset());
		AnsiConsole.out.println((this.player1.step() == this.player2.step() ? "    " : "  * ") + ansi().fgBright(this.player2.color).a(buildPlayerInfo(this.player2)).reset());
		System.out.println();
		System.out.print("     ");
		for (int i = 0; i < 9; i++) {
			System.out.print((i + 1) + "   ");
		}
		for (int i = 9; i < N_COL; i++) {
			System.out.print((i + 1) + "  ");
		}
		System.out.println();
		for (int i = 0; i < N_ROW; i++) {
			System.out.print(" "+(i + 1) + (i >= 9 ? "  " : "   "));
			for (int j = 0; j < N_COL; j++) {
				if(this.grid[i][j] == EMPTY_CHAR) {
					System.out.print(this.grid[i][j] + "   ");
				}
				if(this.grid[i][j] == this.player1.marker) {
					AnsiConsole.out.print(ansi().fgBright(this.player1.color).a(this.grid[i][j] + "   ").reset());
				}
				if(this.grid[i][j] == this.player2.marker) {
					AnsiConsole.out.print(ansi().fgBright(this.player2.color).a(this.grid[i][j] + "   ").reset());
				}
			}
			System.out.println();
			System.out.println();
		}
		System.out.println();
		if (this.status.isGameOver()) {
			System.out.println();
			System.out.println();
			if (this.status.isWinning()) {
				AnsiConsole.out.println(ansi().fgBright(RED).bold().a(" "+this.status.getWinner() + " is the WINNER, congratulations!").reset());
			} else {
				AnsiConsole.out.println(ansi().fgBright(RED).bold().a(" You both are so good, but game is draw!").reset());
			}
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(WHITE).bold().a(" SUMMARY:").reset());
			double timesOfP1 = this.player1.time() * 1.0 / 1E9;
			double timesOfP2 = this.player2.time() * 1.0 / 1E9;
			AnsiConsole.out.print(ansi().fgBright(this.player1.color));
			System.out.printf(" %s   Step: %d   Total Time: %3.1fs   Avg Time: %.1fs\n", " " + this.player1 + " ("+this.player1.name+")",
					this.player1.step(), timesOfP1, timesOfP1 / this.player1.step());
			AnsiConsole.out.print(ansi().fgBright(this.player2.color));
			System.out.printf(" %s   Step: %d   Total Time: %3.1fs   Avg Time: %.1fs\n", " " + this.player2 + " ("+this.player2.name+")",
					this.player2.step(), timesOfP2, timesOfP2 / this.player2.step());
			AnsiConsole.out.print(ansi().reset());
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			AnsiConsole.out.println(ansi().fgBright(RED).bold().a(" Do you want to play another round? (y/n)").reset());
			System.out.println(" 'y' will clear screen and start a new round, 'n' will close the game");
			System.out.print(" ");
			Scanner answer = new Scanner(System.in);
			String newRound = answer.next();
			while (!newRound.equalsIgnoreCase("y") && !newRound.equalsIgnoreCase("n")) {
				AnsiConsole.out.println(ansi().fg(YELLOW).a(" Input 'y' or 'n' no").reset());
				System.out.print(" ");
				newRound = answer.next();
			}
			if(newRound.equalsIgnoreCase("y")) {
				clearScreen();
				GameSetup.runGame();
			} else {
				System.out.println();
				AnsiConsole.out.println(ansi().fgBright(RED).a("Goodbye!").reset());
				System.exit(0);
			}
			answer.close();
		}
	}

	/**
	 * @param player
	 * @return string: vá»‹ trÃ­ Ä‘Ã¡nh
	 */
	private String buildPlayerInfo(Player player) {
		double seconds = (double) player.getLastTime() / 1_000_000_000.0;
		return player + " (" + player.name + ")" + "  Step: " + player.step() + "  Last Pos: " + player.getLastPos() + "  Depth: "
				+ player.getBestDepth() + "  Score: " + player.getScore() + "  Time: " + seconds + " seconds.";
	}

	/**
	 * @done
	 */
	// private void pause() {
	// System.out.println("[Print any key to continue]");
	// final Scanner cin = new Scanner(System.in);
	// cin.useDelimiter("\n");
	// cin.nextLine();
	// // cin.close();
	// }
}