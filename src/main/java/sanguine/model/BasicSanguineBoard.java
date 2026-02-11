package sanguine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete board implementation using EmptyCell, PawnsCell, and CardCell.
 */
public class BasicSanguineBoard implements SanguineBoard {

  private final int height;
  private final int width;
  private final List<List<Cell>> grid;
  private final List<Integer> redRowScores;
  private final List<Integer> blueRowScores;

  /**
   * Initializes an empty basic sanguine.model.Sanguine board.
   *
   * @param rows the number of rows for the board
   * @param cols the number of columns for the board
   */
  public BasicSanguineBoard(int rows, int cols) {
    if (rows <= 0) {
      throw new IllegalArgumentException("rows must be positive");
    }
    if (cols <= 1 || cols % 2 == 0) {
      throw new IllegalArgumentException("cols must be odd and >1");
    }

    this.height = rows;
    this.width = cols;
    this.grid = new ArrayList<>(rows);
    this.redRowScores = new ArrayList<>(rows);
    this.blueRowScores = new ArrayList<>(rows);

    for (int r = 0; r < rows; r++) {
      List<Cell> row = new ArrayList<>(cols);
      for (int c = 0; c < cols; c++) {
        row.add(new EmptyCell());
      }
      grid.add(row);
      redRowScores.add(0);
      blueRowScores.add(0);
    }

    for (int r = 0; r < rows; r++) {
      grid.get(r).set(0, new PawnsCell(Player.RED, 1));
      grid.get(r).set(cols - 1, new PawnsCell(Player.BLUE, 1));
    }
  }

  /**
   * Creates a copy of the given board.
   *
   * @param board the board to create a copy of.
   */
  public BasicSanguineBoard(SanguineBoard board) {
    this.height = board.getHeight();
    this.width = board.getWidth();
    this.grid = board.getBoardCells();
    this.redRowScores = new ArrayList<>();
    this.blueRowScores = new ArrayList<>();
    board.recalcAllScores();
  }

  @Override
  public void playCard(SanguineCard card, Player player, int row, int col) {
    if (!isValidPos(row, col)) {
      throw new IllegalArgumentException("invalid position");
    }

    Cell target = grid.get(row).get(col);
    if (!(target instanceof PawnsCell)) {
      throw new IllegalStateException("cell must contain pawns");
    }
    PawnsCell pawnCell = (PawnsCell) target;
    if (pawnCell.getOwner() != player) {
      throw new IllegalStateException("cell not owned by player");
    }
    if (pawnCell.getPawnCount() < card.getCost()) {
      throw new IllegalStateException("not enough pawns");
    }

    grid.get(row).set(col, new CardCell(card, player));

    boolean[][] influence = player == Player.RED
        ? card.getInfluence() : mirror(card.getInfluence());
    for (int dr = -2; dr <= 2; dr++) {
      for (int dc = -2; dc <= 2; dc++) {
        if (influence[dr + 2][dc + 2]) {
          int tr = row + dr;
          int tc = col + dc;
          if (isValidPos(tr, tc)) {
            Cell old = grid.get(tr).get(tc);
            if (!(old instanceof CardCell)) {
              grid.get(tr).set(tc, old.addPawns(player));
            }
          }
        }
      }
    }

    recalcAllScores();
  }

  /**
   * Returns if the position is valid within the range of the board grid size.
   *
   * @param r the row number
   * @param c the column number
   * @return true if the position is valid within the bounds of the board or false if not
   */
  private boolean isValidPos(int r, int c) {
    return r >= 0 && r < height && c >= 0 && c < width;
  }

  /**
   * Recalculates the score for each row for each player.
   *
   * @param row the row whose score is being calculated
   */
  private void recalcRowScore(int row) {
    int red = 0;
    int blue = 0;
    for (Cell cell : grid.get(row)) {
      if (cell instanceof CardCell cc) {
        if (cc.getOwner() == Player.RED) {
          red += cc.getCard().getValue();
        } else {
          blue += cc.getCard().getValue();
        }
      }
    }
    redRowScores.set(row, red);
    blueRowScores.set(row, blue);
  }

  @Override
  public void recalcAllScores() {
    for (int r = 0; r < height; r++) {
      recalcRowScore(r);
    }
  }

  @Override
  public int getScore(int row, Player player) {
    if (row < 0 || row >= height) {
      throw new IllegalArgumentException("invalid row");
    }
    return player == Player.RED ? redRowScores.get(row) : blueRowScores.get(row);
  }

  @Override public int getWidth() {
    return width;
  }

  @Override public int getHeight() {
    return height;
  }

  @Override
  public List<List<Cell>> getBoardCells() {
    return this.grid;
  }

  /**
   * Mirrors the red grid across the vertical axis for blue.
   *
   * @param orig the red player's grid.
   * @return the mirrored version which is the blue player's grid.
   */
  private static boolean[][] mirror(boolean[][] orig) {
    boolean[][] m = new boolean[5][5];
    for (int r = 0; r < 5; r++) {
      for (int c = 0; c < 5; c++) {
        m[r][c] = orig[r][4 - c];
      }
    }
    return m;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < height; r++) {
      sb.append(redRowScores.get(r)).append(' ');
      for (int c = 0; c < width; c++) {
        sb.append(grid.get(r).get(c));
      }
      sb.append(' ').append(blueRowScores.get(r));
      if (r < height - 1) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }
}