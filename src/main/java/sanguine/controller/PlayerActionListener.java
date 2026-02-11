package sanguine.controller;

import sanguine.model.Player;

/**
 * Listener interface for player input actions originating from the GUI.
 */
public interface PlayerActionListener {
  /**
   * Notifies that a card has been selected (or deselected if cardIndex = -1).
   *
   * @param cardIndex index of the selected card in the current player's hand
   * @param player    the player who performed the selection (always the current player)
   */
  void cardSelected(int cardIndex, Player player);

  /**
   * Notifies that a board cell has been selected (or deselected if row/col = -1).
   *
   * @param row row index of the selected cell
   * @param col column index of the selected cell
   */
  void cellSelected(int row, int col);

  /** Notifies that the player confirms and plays the currently selected card/cell. */
  void confirmMove();

  /** Notifies that the player passes their turn. */
  void passTurn();
}