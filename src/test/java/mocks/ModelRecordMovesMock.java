package mocks;

import java.io.IOException;
import java.util.List;
import sanguine.controller.GameStatusListener;
import sanguine.controller.PublisherSanguineModel;
import sanguine.model.Cell;
import sanguine.model.Player;
import sanguine.model.SanguineBoard;
import sanguine.model.SanguineCard;

/**
 * Appends to a log when a move is made.
 */
public class ModelRecordMovesMock implements PublisherSanguineModel {
  Appendable log;

  /**
   * Creates a new mock.
   */
  public ModelRecordMovesMock(Appendable log) {
    this.log = log;
  }

  @Override
  public void addGameStatusListener(GameStatusListener listener) {

  }

  @Override
  public void startGame() {

  }

  @Override
  public void playCard(int cardIndex, int row, int col) {

  }

  @Override
  public void pass() {
    try {
      log.append("pass\n");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
    return 1;
  }

  @Override
  public int getBoardWidth() {
    return 2;
  }

  @Override
  public Cell getCell(int row, int col) {
    return null;
  }

  @Override
  public List<SanguineCard> getHand(Player player) {
    return List.of();
  }

  @Override
  public Player getOwner(int row, int col) {
    return null;
  }

  @Override
  public boolean canPlayCard(int cardIdx, int row, int col) {
    return true;
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
