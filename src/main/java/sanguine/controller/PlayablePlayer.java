package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.strategies.Move;

/**
 * Represents anything that can act as a player in the game, either a human
 * or an AI controlled by a strategy.
 */
public interface PlayablePlayer {
  /**
   * Returns the move this player wishes to make.
   * Human players return null.
   *
   * @param model  current read-only game state
   * @param player the player to move (RED or BLUE)
   * @return the chosen move, or null for a human waiting on GUI input
   */
  Move chooseMove(ReadOnlySanguineModel model, Player player);
}