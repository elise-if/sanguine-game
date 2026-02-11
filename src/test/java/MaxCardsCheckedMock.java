import java.io.IOException;
import java.util.List;
import sanguine.model.BasicSanguineCard;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguineBoard;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;

/**
 * Used for the transcripts, this mock only allows the red player to place a card
 * in the bottom right corner.
 */
public class MaxCardsCheckedMock implements SanguineModel {
  private Appendable log;

  /**
   * Creates a new mock.
   */
  public MaxCardsCheckedMock(Appendable log) {
    this.log = log;
  }

  @Override
  public void playCard(int cardIndex, int row, int col) {

  }

  @Override
  public void pass() {

  }

  @Override
  public SanguineBoard getBoard() {
    return null;
  }

  @Override
  public int getScore(int row, Player player) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getBoardHeight() {
    return 3;
  }

  @Override
  public int getBoardWidth() {
    return 5;
  }

  @Override
  public Cell getCell(int row, int col) {
    return null;
  }

  @Override
  public List<SanguineCard> getHand(Player player) {
    return List.of(new BasicSanguineCard("mock", 1, 1,
            new boolean[][]{{false, false, false, false, false},
                            {false, false, true, false, false},
                            {false, true, false, true, false},
                            {false, false, true, false, false},
                            {false, false, false, false, false}}));
  }

  @Override
  public Player getOwner(int row, int col) {
    return null;
  }

  @Override
  public boolean canPlayCard(int cardIdx, int row, int col) {
    try {
      log.append("Checked card at row: " + row + ", col: " + col + "\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return row == 2 && col == 4;
  }

  @Override
  public int getTotalScore(Player player) {
    return 0;
  }

  @Override
  public Player getWinner() {
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return null;
  }
}
