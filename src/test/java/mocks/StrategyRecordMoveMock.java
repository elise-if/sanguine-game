package mocks;

import java.io.IOException;
import sanguine.controller.PlayablePlayer;
import sanguine.controller.SanguineController;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.strategies.Move;
import sanguine.strategies.PublisherPlayerAction;

/**
 * Appends to a log when a move is chosen.
 */
public class StrategyRecordMoveMock implements PublisherPlayerAction, PlayablePlayer {
  Appendable log;

  /**
   * Creates a new mock.
   */
  public StrategyRecordMoveMock(Appendable log) {
    this.log = log;
  }

  @Override
  public void addListener(SanguineController controller) {

  }

  @Override
  public Move chooseMove(ReadOnlySanguineModel model, Player player) {
    try {
      log.append("move chosen");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new Move(0, 0, 0);
  }
}
