package sanguine.controller;

import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.strategies.Move;

/**
 * Represents the controller for a human player in Sanguine. The human player's moves are chosen
 * by the person playing.
 */
public class HumanPlayer implements PlayablePlayer, PlayerActionListener {
  @Override
  public Move chooseMove(ReadOnlySanguineModel model, Player player) {
    return null;
  }

  @Override public void cardSelected(int cardIndex, Player player) {}

  @Override public void cellSelected(int row, int col) {}

  @Override public void confirmMove() {}

  @Override public void passTurn() {}
}
