package sanguine.strategies;

import java.util.ArrayList;
import sanguine.controller.SanguineController;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Fill First strategy:
 * - Scans rows top to bottom, columns left-to-right (Red) or right-to-left (Blue),
 *   then tries every card in hand order.
 * - This exact order is required to match the transcript tests
 * (they expect the last cell checked twice).
 */
public class FillFirst implements PublisherPlayerAction {
  private final ArrayList<SanguineController> listeners;

  /**
   * Creates new Fill First AI strategy.
   */
  public FillFirst() {
    this.listeners = new ArrayList<>();
  }

  @Override
  public Move chooseMove(ReadOnlySanguineModel model, Player player) {
    int handSize = model.getHand(player).size();

    for (int row = 0; row < model.getBoardHeight(); row++) {
      int startCol = player == Player.RED ? 0 : model.getBoardWidth() - 1;
      int endCol = player == Player.RED ? model.getBoardWidth() : -1;
      int step = player == Player.RED ? 1 : -1;

      for (int col = startCol; col != endCol; col += step) {
        for (int card = 0; card < handSize; card++) {
          if (model.canPlayCard(card, row, col)) {
            return new Move(row, col, card);
          }
        }
      }
    }
    return new Move(true);
  }

  @Override
  public void addListener(SanguineController controller) {
    this.listeners.add(controller);
  }
}