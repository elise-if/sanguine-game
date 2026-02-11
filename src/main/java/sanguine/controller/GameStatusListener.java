package sanguine.controller;

import sanguine.model.Player;

/**
 * Listener interface for game state changes made by the model.
 */
public interface GameStatusListener {
  /**
   * Called when the active player changes.
   *
   * @param currentPlayer the player whose turn it now is (RED or BLUE)
   */
  void turnChanged(Player currentPlayer);

  /**
   * Called when the game ends because two consecutive passes occurred.
   *
   * @param winner    the winning player, or null if the game ended in a tie
   * @param redScore  final total score for Red
   * @param blueScore final total score for Blue
   */
  void gameOver(Player winner, int redScore, int blueScore);
}