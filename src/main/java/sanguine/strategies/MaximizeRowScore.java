package sanguine.strategies;

import java.util.ArrayList;
import sanguine.controller.SanguineController;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Maximize Row-Score strategy:
 * For each row from top to bottom:
 *   If the current player has lower or equal score in that row,
 *   find the first card+position that would make their score strictly greater.
 * If no such move exists in any row → pass.
 */
public class MaximizeRowScore implements PublisherPlayerAction {
  private ArrayList<SanguineController> listeners;

  /**
   * Constructs a new MaximizeRowScore AI strategy.
   * No parameters are required because this is a pure AI strategy that operates on the
   * read-only model provided at move-selection time.
   */
  public MaximizeRowScore() {
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