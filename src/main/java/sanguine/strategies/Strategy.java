package sanguine.strategies;

import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineModel;

/**
 * Represents a computer player strategy for Sanguine.
 * Pure AI strategies do NOT have a visual view — only human players do.
 */
public interface Strategy {

  /**
   * Chooses the next move according to this strategy.
   *
   * @param model  the current (read-only) state of the game
   * @param player the player to move (RED or BLUE)
   * @return a Move representing the chosen card + position, or a pass move
   */
  Move chooseMove(ReadOnlySanguineModel model, Player player);

  /**
   * Executes one full turn using this strategy (play card or pass).
   *
   * @param model  the mutable model to act on
   * @param player the player whose turn it is
   * @return true if a move was made (or pass), false only on error
   */
  default boolean makeMove(SanguineModel model, Player player) {
    Move m = chooseMove(model, player);
    if (m.pass()) {
      model.pass();
      return true;
    }
    model.playCard(m.cardIndex(), m.row(), m.col());
    return true;
  }
}