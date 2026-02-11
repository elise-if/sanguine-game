import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import sanguine.model.CardCell;
import sanguine.model.Cell;
import sanguine.model.EmptyCell;
import sanguine.model.PawnsCell;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.view.SanguineTextualView;

/**
 * Tests for SanguineTextualView.
 */
public class TextualViewTests {
  private final SanguineCard mockRedCard = new MockCard("RedCard", 1, 5);
  private final SanguineCard mockBlueCard = new MockCard("BlueCard", 1, 3);

  /**
   * Helper to make a grid of cells.
   *
   * @param rows the number of rows
   * @param cols the number of columns
   * @param cells the number of cells
   * @return the grid of cells
   */
  private List<List<Cell>> grid(int rows, int cols, Cell[][] cells) {
    List<List<Cell>> grid = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
      List<Cell> row = new ArrayList<>();
      for (int c = 0; c < cols; c++) {
        row.add(cells[r][c]);
      }
      grid.add(row);
    }
    return grid;
  }

  /**
   * Makes of list of the scores.
   *
   * @param values the scores
   * @return the list of scores
   */
  private List<Integer> scores(Integer... values) {
    return new ArrayList<>(Arrays.asList(values));
  }

  @Test
  public void testEmptyBoard3x5() {
    Cell[][] cells = {
        {emptyCell(), emptyCell(), emptyCell(), emptyCell(), emptyCell()},
        {emptyCell(), emptyCell(), emptyCell(), emptyCell(), emptyCell()},
        {emptyCell(), emptyCell(), emptyCell(), emptyCell(), emptyCell()}
    };
    List<List<Cell>> grid = grid(3, 5, cells);
    List<Integer> blueScores = scores(0, 0, 0);
    List<Integer> redScores = scores(0, 0, 0);

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected =
        "0 _____ 0\n"
            + "0 _____ 0\n"
            + "0 _____ 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testInitialBoardWithPawns() {
    Cell[][] cells = {
        {pawnCell(Player.RED, 1), emptyCell(), emptyCell(), emptyCell(),
            pawnCell(Player.BLUE, 1)},
        {pawnCell(Player.RED, 1), emptyCell(), emptyCell(), emptyCell(),
            pawnCell(Player.BLUE, 1)},
        {pawnCell(Player.RED, 1), emptyCell(), emptyCell(), emptyCell(),
            pawnCell(Player.BLUE, 1)}
    };
    List<List<Cell>> grid = grid(3, 5, cells);
    List<Integer> blueScores = scores(0, 0, 0);
    List<Integer> redScores = scores(0, 0, 0);

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected =
        "0 1___1 0\n"
            + "0 1___1 0\n"
            + "0 1___1 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testCardsAndPawnsMixed() {
    Cell[][] cells = {
        {cardCell(mockRedCard, Player.RED), pawnCell(Player.RED, 2), emptyCell(),
            pawnCell(Player.BLUE, 1), cardCell(mockBlueCard, Player.BLUE)},
        {emptyCell(), cardCell(mockRedCard, Player.RED), pawnCell(Player.RED, 3), emptyCell(),
            pawnCell(Player.BLUE, 2)},
        {pawnCell(Player.RED, 1), emptyCell(), emptyCell(), emptyCell(),
            pawnCell(Player.BLUE, 1)}
    };
    List<List<Cell>> grid = grid(3, 5, cells);
    List<Integer> blueScores = scores(3, 0, 0);  // only blue card in row 0
    List<Integer> redScores = scores(10, 5, 0);  // red cards: 5+5 in row 0, 5 in row 1

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected =
        "3 R2_1B 10\n"
            + "0 _R3_2 5\n"
            + "0 1___1 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testMaxPawnsAndCards() {
    Cell[][] cells = {
        {pawnCell(Player.RED, 3), cardCell(mockRedCard, Player.RED),
            pawnCell(Player.BLUE, 3),
            cardCell(mockBlueCard, Player.BLUE), pawnCell(Player.RED, 1)},
        {emptyCell(), emptyCell(), emptyCell(), emptyCell(), emptyCell()}
    };
    List<List<Cell>> grid = grid(2, 5, cells);
    List<Integer> blueScores = scores(3, 0);
    List<Integer> redScores = scores(5, 0);

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected =
        "3 3R3B1 5\n"
            + "0 _____ 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void test1x3Board() {
    Cell[][] cells = {{pawnCell(Player.RED, 1), emptyCell(), pawnCell(Player.BLUE,
        1)}};
    List<List<Cell>> grid = grid(1, 3, cells);
    List<Integer> blueScores = scores(0);
    List<Integer> redScores = scores(0);

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected = "0 1_1 0";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testHighScores() {
    Cell[][] cells = {
        {cardCell(mockRedCard, Player.RED), cardCell(mockRedCard, Player.RED),
            cardCell(mockRedCard, Player.RED)},
        {cardCell(mockBlueCard, Player.BLUE), cardCell(mockBlueCard, Player.BLUE),
            cardCell(mockBlueCard, Player.BLUE)}
    };
    List<List<Cell>> grid = grid(2, 3, cells);
    List<Integer> blueScores = scores(0, 9);   // 3+3+3
    List<Integer> redScores = scores(15, 0);   // 5+5+5

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    String expected =
        "0 RRR 15\n"
            + "9 BBB 0";
    assertEquals(expected, view.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullGrid() {
    new SanguineTextualView(null, scores(0),
        scores(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBlueScores() {
    Cell[][] cells = {{emptyCell()}};
    new SanguineTextualView(grid(1, 1, cells), null, scores(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRedScores() {
    Cell[][] cells = {{emptyCell()}};
    new SanguineTextualView(grid(1, 1, cells), scores(0), null);
  }

  @Test
  public void testEmptyBoard0x0() {
    List<List<Cell>> grid = new ArrayList<>();
    List<Integer> blueScores = new ArrayList<>();
    List<Integer> redScores = new ArrayList<>();

    SanguineTextualView view = new SanguineTextualView(grid, blueScores, redScores);
    assertEquals("", view.toString());
  }

  /**
   * Returns an empty cell.
   *
   * @return an empty cell
   */
  private Cell emptyCell() {
    return new EmptyCell();
  }

  /**
   * returns a pawncell.
   *
   * @param player the player who owns the cell
   * @param count the number of pawns on the cell
   * @return the pawn cell
   */
  private Cell pawnCell(Player player, int count) {
    return new PawnsCell(player, count);
  }

  /**
   * helper which returns a cardcell.
   *
   * @param card the card to be in the cell
   * @param player the player who played the card
   * @return the card cell
   */
  private Cell cardCell(SanguineCard card, Player player) {
    return new CardCell(card, player);
  }
}
