package sanguine.view;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguineModel;

/**
 * Textual representation of a sanguine.model.Sanguine board.
 */
public class SanguineTextualView implements SanguineView {

  private List<List<Cell>> grid;
  private List<Integer> blueRowScores;
  private List<Integer> redRowScores;
  private final int height;
  private final int width;

  SanguineModel model;

  /**
   * Makes a textual representation of a sanguine.model.Sanguine game.
   *
   * @param grid the grid to be represented textually
   * @param blueRowScores the blue row scores
   * @param redRowScores the red row scores
   */
  public SanguineTextualView(List<List<Cell>> grid,
                             List<Integer> blueRowScores,
                             List<Integer> redRowScores) {
    if (grid == null || blueRowScores == null || redRowScores == null) {
      throw new IllegalArgumentException("Grid and scores cannot be null");
    }
    this.grid = grid;
    this.blueRowScores = blueRowScores;
    this.redRowScores = redRowScores;
    this.height = grid.size();
    this.width = height > 0 ? grid.get(0).size() : 0;
  }

  /**
   * Makes a new textual representation of the given sanguine.model.Sanguine model.
   *
   * @param model sanguine.model.Sanguine game being represented.
   */
  public SanguineTextualView(SanguineModel model) {
    this.model = model;
    this.height = model.getBoard().getHeight();
    this.width = model.getBoard().getWidth();
  }

  @Override
  public String toString() {
    if (model != null) {
      this.grid = model.getBoard().getBoardCells();
      List<Integer> blueRowScores = new ArrayList<>();
      List<Integer> redRowScores = new  ArrayList<>();
      for (int r = 0; r < this.height; r++) {
        blueRowScores.add(model.getScore(r, Player.BLUE));
        redRowScores.add(model.getScore(r, Player.RED));
      }
      this.blueRowScores = blueRowScores;
      this.redRowScores = redRowScores;
    }
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < height; r++) {
      int blueScore = blueRowScores.get(r);
      int redScore  = redRowScores.get(r);

      sb.append(blueScore).append(' ');

      List<Cell> row = grid.get(r);
      for (int c = 0; c < width; c++) {
        sb.append(row.get(c));
      }

      sb.append(' ').append(redScore);
      if (r < height - 1) {
        sb.append('\n');
      }
    }
    return sb.toString();
  }
}